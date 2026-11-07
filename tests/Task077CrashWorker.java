import java.io.IOException;
import java.nio.file.Path;
import models.*;
import services.*;

/** Separate JVM: intentionally exits without finally/shutdown hooks. */
public class Task077CrashWorker {
    public static void main(String[] args) throws Exception {
        Path dir = Path.of(args[0]);
        DuLieu.khoiTaoDanhMuc();
        LuuTruFileText store = new LuuTruFileText(dir.toString()) {
            @Override public void ghiSanPham(String path, DanhSachSanPham ds) throws IOException {
                super.ghiSanPham(path, ds);
                if (args[1].equals("first")) Runtime.getRuntime().halt(77);
            }
            @Override public void ghiHoaDon(String path, DanhSachHoaDon ds) throws IOException {
                super.ghiHoaDon(path, ds);
                Runtime.getRuntime().halt(78);
            }
        };
        DanhSachSanPham sp=store.docSanPham("sanpham.txt",DuLieu.getDanhMuc());
        DanhSachKhachHang kh=store.docKhachHang("khachhang.txt");
        DanhSachNhanVien nv=DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
        DanhSachHoaDon hd=store.docHoaDon("hoadon.txt",sp,kh,nv.getTatCa());
        new GiaoDichBanHang(dir,store).xacNhan("CRASH",hd,sp,kh,nv);
    }
}
