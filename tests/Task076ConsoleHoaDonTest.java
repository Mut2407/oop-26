import java.nio.file.*;
import services.*;
import models.*;
public class Task076ConsoleHoaDonTest {
    private static boolean money(String output, String label, String expected) {
        return output.lines().filter(line -> line.contains(label)).anyMatch(line -> {
            String number = line.substring(line.indexOf(label) + label.length()).trim();
            return new java.math.BigDecimal(number).compareTo(new java.math.BigDecimal(expected)) == 0;
        });
    }
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("console-hd-");
        try {
            Files.createDirectory(dir.resolve("data"));
            try (var paths=Files.list(Path.of("data"))) {
                for (Path p:paths.filter(Files::isRegularFile).toList()) Files.copy(p,dir.resolve("data").resolve(p.getFileName()));
            }
            String output=ConsoleTestSupport.run(dir,ConsoleTestSupport.lines(
                "6","2","HDTEST","KH01","NV01","2026-11-01",
                "4","HDTEST","SP01","2","0.10","5","HDTEST","SP01","3","0.10",
                "8","HD","KH01","NV01","2026-11-01","2026-11-01","CHO_XAC_NHAN",
                "10","0","0"),0);
            assert output.contains("Da luu hoa don.") && money(output,"Tong tien: ","0.30") : output;
            DuLieu.khoiTaoDanhMuc();
            LuuTruFileText store=new LuuTruFileText(dir.resolve("data").toString());
            DanhSachSanPham sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            int before=sp.timTheoMa("SP01").getSoLuongTon();
            output=ConsoleTestSupport.run(dir,ConsoleTestSupport.lines("6","3","HDTEST","7","HDTEST","Y",
                "7","HDTEST","Y","4","HDTEST","9","2026-11-01","2026-11-01","11","3","HDTEST","0","0"),0);
            assert output.contains("Da xac nhan va luu ban hang.") && money(output,"Doanh thu da xac nhan: ","0.30") : output;
            sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
            assert sp.timTheoMa("SP01").getSoLuongTon()==before-3;
            output=ConsoleTestSupport.run(dir,"6\n3\nHDTEST\n0\n0\n",0);
            assert output.contains("DA_XAC_NHAN") && money(output,"Tong tien: ","0.30") : output;
            assert store.docSanPham("sanpham.txt",DuLieu.getDanhMuc()).timTheoMa("SP01").getSoLuongTon()==before-3;
            output=ConsoleTestSupport.run(dir,"3\n4\nKH01\n0\n4\n4\nNV01\n0\n1\n6\nSP01\n0\n0\n",0);
            assert output.contains("dang duoc hoa don tham chieu") : output;
            assert store.docKhachHang("khachhang.txt").timTheoMa("KH01")!=null;
            assert DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt")).getDiaChiCu("NV01")!=null;
            System.out.println("Task076ConsoleHoaDonTest OK");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
}
