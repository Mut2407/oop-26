import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import models.*;
import services.*;

public class OopTest {
    public static void main(String[] args) throws Exception {
        check(Modifier.isAbstract(SanPham.class.getModifiers()));
        check(Modifier.isAbstract(SanPham.class.getMethod("hienThiThongTin").getModifiers()));
        check(GiayTheThao.class.getSuperclass() == SanPham.class);
        check(QuanAoTheThao.class.getSuperclass() == SanPham.class);
        check(DungCuTheThao.class.getSuperclass() == SanPham.class);
        check(IQuanLyDanhSach.class.isAssignableFrom(DanhSachSanPham.class));
        check(IQuanLyDanhSach.class.isAssignableFrom(DanhSachKhachHang.class));
        check(IQuanLyDanhSach.class.isAssignableFrom(DanhSachNhaCungCap.class));
        check(DanhSachNhanVien.class.getDeclaredField("danhSach").getType() == NhanVien[].class);
        check(Modifier.isStatic(DuLieu.class.getDeclaredField("DANH_MUC").getModifiers()));
        check(Modifier.isStatic(DuLieu.class.getMethod("timDanhMuc", String.class).getModifiers()));
        String main = Files.readString(Path.of("src/Main.java"));
        int start = main.indexOf("public static void main(String[] args)");
        int end = main.indexOf("\n    }", start);
        check(start >= 0 && main.substring(start, end).lines().count() + 1 <= 10);
        long classes;
        try (var paths = Files.walk(Path.of("src"))) {
            classes = paths.filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> { try { return Files.readString(p).matches("(?s).*\\bclass\\s+\\w+.*"); }
                        catch (Exception e) { throw new RuntimeException(e); } }).count();
        }
        check(classes >= 15);
        System.out.println("OopTest OK (inheritance, abstract methods, interface, array, static, class count, main <= 10 lines)");
    }
    private static void check(boolean value) { if (!value) throw new AssertionError(); }
}
