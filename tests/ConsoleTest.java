import java.io.*;
import services.*;

public final class ConsoleTest {
    public static void main(String[] args) throws Exception {
        chungTuQuaMenu();
        crudDoiTacQuaMenu();
        eofVaFileLoi();
        System.out.println("ConsoleTest OK: " + TestSupport.soKiemTra + " assertions");
    }

    private static void chungTuQuaMenu() throws Exception {
        File data = thuMucTam();
        LuuTruFileText luu = new LuuTruFileText(data.getPath());
        luu.ghiDuLieu(TestSupport.cuaHang());
        String dau = String.join("\n", "7", "1", "PN1", "sai ngay", "2026-11-09", "NCC1", "NV1",
                "1", "SP1", "abc", "0", "10", "-1", "60.25", "0", "0",
                "6", "1", "HD1", "2026-11-09", "KH1", "NV1", "1", "SP1", "3", "100.10", "0",
                "5", "6", "hd1", "4", "kh1", "0", "9", "2", "2026-11-09", "2026-11-09", "0", "");
        String output = chay(data, dau, 0);
        TestSupport.dung(output.contains("Ngay khong hop le"));
        TestSupport.dung(output.contains("Tong tien ban: 300.30"));
        TestSupport.dung(output.contains("Tong tien nhap: 602.50"));
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP1") == 7);
        String sua = String.join("\n", "6", "2", "HD1", "2026-11-09", "KH1", "NV1",
                "2", "SP1", "SP1", "5", "100.10", "0", "0", "7", "3", "PN1", "0", "0", "");
        TestSupport.dung(chay(data, sua, 0).contains("Ton kho khong hop le"));
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP1") == 5);
        TestSupport.dung(luu.docDuLieu().getPhieuNhap().getSoLuong() == 1);
        chay(data, "6\n3\nHD1\n0\n7\n3\nPN1\n0\n0\n", 0);
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP1") == 0);
        TestSupport.dung(luu.docDuLieu().getHoaDon().getSoLuong() == 0);
        TestSupport.dung(luu.docDuLieu().getPhieuNhap().getSoLuong() == 0);
        // Product entry cannot enter or overwrite stock; covers all three product types.
        chay(data, String.join("\n", "1", "1", "SP3", "Áo thể thao", "Nike", "100", "L1", "2", "XL", "Đen", "Cotton",
                "1", "SP4", "Vợt", "Yonex", "200", "L1", "3", "Cầu lông", "Carbon", "0.085",
                "1", "SP5", "Giày", "Nike", "300", "L1", "1", "42", "Đen", "Vải", "Cao su", "0",
                "11", "2", "100", "200", "0", ""), 0);
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP3") == 0);
        TestSupport.dung(luu.docDuLieu().getSanPham().getSoLuong() == 5);
    }

    private static void crudDoiTacQuaMenu() throws Exception {
        File data = thuMucTam();
        LuuTruFileText luu = new LuuTruFileText(data.getPath());
        luu.ghiDuLieu(TestSupport.cuaHang());
        String input = String.join("\n", "2", "1", "L2", "Loại mới", "Mô tả",
                "2", "L2", "Loại sửa", "Mô tả sửa", "4", "loai", "3", "L2", "0",
                "3", "1", "KH2", "Khách mới", "0901234567", "HCM",
                "2", "KH2", "Khách sửa", "0901234568", "HN", "5", "3", "KH2", "0",
                "4", "1", "NV2", "Nhân viên mới", "0901234567", "Sáng",
                "2", "NV2", "Nhân viên sửa", "0901234568", "Chiều", "3", "NV2", "0",
                "5", "1", "NCC2", "Nhà cung cấp mới", "0901234567", "HCM",
                "2", "NCC2", "Nhà cung cấp sửa", "0901234568", "HN", "3", "NCC2", "0", "0", "");
        TestSupport.dung(chay(data, input, 0).contains("Da cap nhat"));
        QuanLyCuaHang ql = luu.docDuLieu();
        TestSupport.dung(ql.getLoaiSP().getSoLuong() == 1);
        TestSupport.dung(ql.getKhachHang().getSoLuong() == 1);
        TestSupport.dung(ql.getNhanVien().getSoLuong() == 1);
        TestSupport.dung(ql.getNhaCungCap().getSoLuong() == 1);
    }

    private static void eofVaFileLoi() throws Exception {
        File data = thuMucTam();
        LuuTruFileText luu = new LuuTruFileText(data.getPath());
        luu.ghiDuLieu(TestSupport.cuaHang());
        TestSupport.dung(chay(data, "|\n0\n", 0).contains("Khong dung dau |"));
        chay(data, "1\n2\nSP1\nTen moi\n", 0);
        TestSupport.dung("Giay chay bo".equals(luu.docDuLieu().getSanPham().timTheoMa("SP1").getTenSanPham()));
        chay(data, "7\n1\nPN1\n2026-11-09\nNCC1\nNV1\n1\nSP1\n10\n", 0);
        TestSupport.dung(luu.docDuLieu().getPhieuNhap().getSoLuong() == 0);
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP1") == 0);
        File tep = new File(data, "loaisp.txt");
        String goc = docFile(tep);
        File backup = new File(data, "sao_luu/loaisp.txt");
        backup.delete();
        backup.mkdir();
        ghiFile(new File(backup, "block"), "block");
        String output = chay(data, "2\n1\nL2\nMoi\n\n0\n8\n", 1);
        TestSupport.dung(output.contains("CHUA LUU DUOC"));
        TestSupport.dung(goc.equals(docFile(tep)));
        ghiFile(tep, "FILE_HONG\n");
        TestSupport.dung(chay(data, "0\n", 1).contains("Sai tieu de"));
        TestSupport.dung("FILE_HONG\n".equals(docFile(tep)));
    }

    private static File thuMucTam() throws IOException {
        File data = File.createTempFile("oop-console-", "");
        data.delete();
        data.mkdir();
        return data;
    }

    private static String docFile(File tep) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(tep), "UTF-8"));
        String ketQua = "";
        String dong = in.readLine();
        while (dong != null) {
            ketQua += dong + "\n";
            dong = in.readLine();
        }
        in.close();
        return ketQua;
    }

    private static void ghiFile(File tep, String noiDung) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tep), "UTF-8"));
        out.write(noiDung);
        out.close();
    }

    // Chay chuong trinh that de kiem tra cac thao tac qua ban phim.
    private static String chay(File data, String input, int exitCode) throws Exception {
        String javaBin = new File(System.getProperty("java.home"), "bin/java").getPath();
        File output = File.createTempFile("oop-output-", ".txt");
        Process p = new ProcessBuilder(javaBin, "-Dfile.encoding=UTF-8", "-cp",
                System.getProperty("java.class.path"), "Main", data.getPath())
                .redirectErrorStream(true).redirectOutput(output).start();
        OutputStream stdin = p.getOutputStream();
        stdin.write(input.getBytes("UTF-8"));
        stdin.close();
        int ketQua = p.waitFor();
        String text = docFile(output);
        output.delete();
        if (ketQua != exitCode) {
            throw new AssertionError("Expected exit " + exitCode + ", got " + ketQua + ": " + text);
        }
        return text;
    }
}
