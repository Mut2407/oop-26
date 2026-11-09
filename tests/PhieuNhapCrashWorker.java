import java.io.IOException;
import java.nio.file.Path;
import models.*;
import services.*;

/** Separate JVM: intentionally exits without finally/shutdown hooks. */
public class PhieuNhapCrashWorker {
    public static void main(String[] args) throws Exception {
        Path dir = Path.of(args[0]);
        DuLieu.khoiTaoDanhMuc();
        LuuTruFileText store = new LuuTruFileText(dir.toString()) {
            @Override public void ghiSanPham(String path, DanhSachSanPham ds) throws IOException {
                super.ghiSanPham(path, ds);
                if (args[1].equals("first")) Runtime.getRuntime().halt(77);
            }
            @Override public void ghiPhieuNhap(String path, DanhSachPhieuNhap ds) throws IOException {
                super.ghiPhieuNhap(path, ds);
                Runtime.getRuntime().halt(78);
            }
        };
        DanhSachSanPham sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
        DanhSachNhaCungCap kh=store.docNhaCungCap("nhacungcap.txt");
        DanhSachNhanVien nv=DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
        DanhSachPhieuNhap hd=store.docPhieuNhap("phieunhap.txt",sp,kh,nv.getTatCa());
        new GiaoDichNhapHang(dir,store).xacNhan("CRASH",hd,sp,kh,nv);
    }
}
