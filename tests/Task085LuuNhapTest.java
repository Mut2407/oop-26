import java.io.IOException;
import java.nio.file.*;
import java.math.BigDecimal;
import models.*;
import services.*;
public class Task085LuuNhapTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("hd-store-");
        try {
            PhieuNhap h = Task082PhieuNhapTest.hd("PN900"); h.themChiTiet(Task082PhieuNhapTest.ct(2));
            DanhSachPhieuNhap hd = new DanhSachPhieuNhap(); hd.them(h);
            DanhSachSanPham sp = new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
            DanhSachNhaCungCap kh = new DanhSachNhaCungCap(DuLieu.taoDanhSachNhaCungCap());
            DanhSachNhanVien nv = new DanhSachNhanVien(DuLieu.taoDanhSachNhanVien(),DuLieu.taoDiaChiNhanVienCu());
            LuuTruFileText store = new LuuTruFileText(dir.toString());
            store.ghiSanPham("sanpham.txt",sp); store.ghiPhieuNhap("phieunhap.txt", hd);
            assert store.docPhieuNhap("phieunhap.txt",sp,kh,nv.getTatCa()).getTatCa().size()==1;
            String beforeSP=Files.readString(dir.resolve("sanpham.txt")), beforeHD=Files.readString(dir.resolve("phieunhap.txt"));
            LuuTruFileText faulty = new LuuTruFileText(dir.toString()) {
                @Override public void ghiPhieuNhap(String path, DanhSachPhieuNhap ds) throws IOException {
                    super.ghiPhieuNhap(path,ds); throw new IOException("simulated second-file failure");
                }
            };
            try { new GiaoDichNhapHang(dir,faulty).xacNhan("PN900",hd,sp,kh,nv); throw new AssertionError(); } catch(IOException expected) { }
            assert Files.readString(dir.resolve("sanpham.txt")).equals(beforeSP);
            assert Files.readString(dir.resolve("phieunhap.txt")).equals(beforeHD);
            assert hd.timTheoMa("PN900").coTheChinhSua();
            new GiaoDichNhapHang(dir,store).xacNhan("PN900",hd,sp,kh,nv);
            assert !hd.timTheoMa("PN900").coTheChinhSua();
            int count=sp.getDanhSach().get(0).getSoLuongTon();
            DanhSachPhieuNhap loaded=store.docPhieuNhap("phieunhap.txt",sp,kh,nv.getTatCa());
            assert !loaded.timTheoMa("PN900").coTheChinhSua();
            assert sp.getDanhSach().get(0).getSoLuongTon()==count;
            assert loaded.tongGiaTriNhap(h.getNgayNhap(),h.getNgayNhap()).compareTo(new BigDecimal("2.50"))==0;
            try { new GiaoDichNhapHang(dir,store).xacNhan("PN900",hd,sp,kh,nv); throw new AssertionError(); } catch(IllegalArgumentException expected) { }
            try { store.ghiPhieuNhap("phieunhap.txt",new DanhSachPhieuNhap()); throw new AssertionError(); } catch(IOException expected) { }
            Files.writeString(dir.resolve("bad.txt"),"PHIEU_NHAP_V1\nH\tBAD\t2026-11-01\tMISSING\tNV01\tCHO_XAC_NHAN\t0\n");
            try { store.docPhieuNhap("bad.txt",sp,kh,nv.getTatCa()); throw new AssertionError(); } catch(IOException expected) { }
            String bad=Files.readString(dir.resolve("bad.txt"));
            Files.writeString(dir.resolve("bad.txt"),bad+"WRONG\n");
            try { store.ghiPhieuNhap("bad.txt",hd); throw new AssertionError(); } catch(IOException expected) { }
            assert !Files.exists(dir.resolve("banhang.undo"));
            assert Integer.parseInt(PhieuNhap.taoMaTuDong().substring(2)) > 900;
            System.out.println("Task085LuuNhapTest OK");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
}
