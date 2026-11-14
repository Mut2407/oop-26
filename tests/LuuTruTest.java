import java.io.*;
import java.math.BigDecimal;
import models.*;
import services.*;

public class LuuTruTest {
    public static void main(String[] args) throws Exception {
        File data = File.createTempFile("oop-test-", "");
        data.delete();
        data.mkdir();
        LuuTruFileText luu = new LuuTruFileText(data.getPath());
        TestSupport.dung(luu.docDuLieu().getSanPham().getSoLuong() == 0);
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themSanPham(new QuanAoTheThao("AO1", "Áo thể thao", "Nike", BigDecimal.TEN,
                ql.getLoaiSP().timTheoMa("L1"), "XL", "Trắng", "Cotton"));
        ql.themSanPham(new DungCuTheThao("DC1", "Vợt", "Yonex", BigDecimal.ONE,
                ql.getLoaiSP().timTheoMa("L1"), "Cầu lông", "Carbon", 0.085));
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 3));
        luu.ghiDuLieu(ql);
        QuanLyCuaHang docLai = luu.docDuLieu();
        TestSupport.dung(docLai.traCuuTon("SP1") == 7);
        TestSupport.dung(docLai.getSanPham().getSoLuong() == 4);
        TestSupport.dung(docLai.getSanPham().timTheoMa("AO1") instanceof QuanAoTheThao);
        TestSupport.dung(docLai.getSanPham().timTheoMa("DC1") instanceof DungCuTheThao);
        TestSupport.dung(docLai.getSanPham().timTheoMa("AO1").getTenSanPham().equals("Áo thể thao"));
        TestSupport.dung(docLai.getSanPham().timTheoMa("SP1") == docLai.getHoaDon().timTheoMa("HD1").timChiTiet("SP1").getSanPham());
        TestSupport.tien("300.30", docLai.thongKeTienBan());
        luu.ghiDuLieu(docLai);
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP1") == 7);
        docLai.xoaHoaDon("HD1");
        luu.ghiDuLieu(docLai);
        TestSupport.dung(luu.docDuLieu().traCuuTon("SP1") == 10);
        kiemTraLoiKhongGhiDe(luu, docLai, data);
        kiemTraDuLieuMau();
        xoaThuMuc(data);
        System.out.println("LuuTruTest OK: " + TestSupport.soKiemTra);
    }

    private static void kiemTraLoiKhongGhiDe(LuuTruFileText luu, QuanLyCuaHang ql, File data) throws Exception {
        ql.getLoaiSP().timTheoMa("L1").setMoTa("Khong|hop le");
        try {
            luu.ghiDuLieu(ql);
            TestSupport.dung(false);
        } catch (IOException e) {
            TestSupport.dung(luu.docDuLieu().getLoaiSP().timTheoMa("L1").getMoTa().equals(""));
        }
        File hoadon = new File(data, "hoadon.txt");
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(hoadon), "UTF-8"));
        out.write("HOA_DON\nHD1|2026-02-30|KH1|NV1\n");
        out.close();
        try {
            luu.docDuLieu();
            TestSupport.dung(false);
        } catch (IOException e) {
            TestSupport.dung(e.getMessage().contains("hoadon.txt - dong 2"));
        }
        hoadon.delete();
        try {
            luu.docDuLieu();
            TestSupport.dung(false);
        } catch (IOException e) {
            TestSupport.dung(e.getMessage().contains("du 9 file"));
        }
    }

    private static void kiemTraDuLieuMau() throws IOException {
        QuanLyCuaHang ql = new LuuTruFileText("data").docDuLieu();
        TestSupport.dung(ql.getSanPham().getSoLuong() == 4);
        TestSupport.dung(ql.getKhachHang().getSoLuong() == 10);
        TestSupport.dung(ql.getNhanVien().getSoLuong() == 5);
        TestSupport.dung(ql.getNhaCungCap().getSoLuong() == 5);
        TestSupport.dung(ql.traCuuTon("SP03") == 8);
        TestSupport.dung(ql.getHoaDon().getSoLuong() == 1);
        TestSupport.dung(ql.getPhieuNhap().getSoLuong() == 1);
    }

    private static void xoaThuMuc(File thuMuc) {
        File[] tep = thuMuc.listFiles();
        if (tep != null) {
            for (int i = 0; i < tep.length; i++) {
                if (tep[i].isDirectory()) {
                    xoaThuMuc(tep[i]);
                } else {
                    tep[i].delete();
                }
            }
        }
        thuMuc.delete();
    }
}
