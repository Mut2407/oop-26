import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import models.KhachHang;
import models.NhanVien;

public class KhachHangNhanVienModelTest {
    private static int soCa;

    public static void main(String[] args) throws Exception {
        constructorDungThuTu();
        duLieuSaiBiTuChoi();
        capNhatKhachHang();
        capNhatNhanVien();
        hienThiThongTin();
        cauTrucUml();
        System.out.println("KhachHangNhanVienModelTest OK (" + soCa + " cases)");
    }

    private static void constructorDungThuTu() {
        KhachHang kh = new KhachHang(" KH_1 ", " Nguyễn Ánh ", " +84901234567 ", " Hà Nội ");
        check(kh.getMaKhachHang().equals("KH_1"));
        check(kh.getHoTen().equals("Nguyễn Ánh"));
        check(kh.getSoDienThoai().equals("+84901234567"));
        check(kh.getDiaChi().equals("Hà Nội"));
        NhanVien nv = new NhanVien(" NV-1 ", " Trần Bình ", " 0901234567 ", " Chiều ");
        check(nv.getMaNhanVien().equals("NV-1"));
        check(nv.getHoTen().equals("Trần Bình"));
        check(nv.getSoDienThoai().equals("0901234567"));
        check(nv.getCaLamViec().equals("Chiều"));
        // Model validation must not impose list-level uniqueness.
        new KhachHang("KH_1", "Khác", "0901234567", "Huế");
        new NhanVien("NV-1", "Khác", "0901234567", "Tối");
        soCa++;
    }

    private static void duLieuSaiBiTuChoi() {
        for (String ma : new String[]{null, "", "  ", "bad code", "A!"}) {
            loi(() -> new KhachHang(ma, "An", "0901234567", "Huế"));
            loi(() -> new NhanVien(ma, "An", "0901234567", "Sáng"));
        }
        for (String rong : new String[]{null, "", " \t\n", "\u2003"}) {
            loi(() -> new KhachHang("K1", rong, "0901234567", "Huế"));
            loi(() -> new KhachHang("K1", "An", "0901234567", rong));
            loi(() -> new NhanVien("N1", rong, "0901234567", "Sáng"));
            loi(() -> new NhanVien("N1", "An", "0901234567", rong));
        }
        for (String sdt : new String[]{null, "", "12345678", "1234567890123456", "090abc4567", "++84901234567"}) {
            loi(() -> new KhachHang("K1", "An", sdt, "Huế"));
            loi(() -> new NhanVien("N1", "An", sdt, "Sáng"));
        }
        // Old parameter order must not accidentally accept a shift as a phone number.
        loi(() -> new NhanVien("N1", "An", "Sang", "0901234567"));
        new KhachHang("K1", "An", "123456789", "Huế");
        new NhanVien("N1", "An", "+123456789012345", "Sáng");
        soCa++;
    }

    private static void capNhatKhachHang() {
        KhachHang kh = new KhachHang("Kh1", "An", "0901234567", "Huế");
        kh.capNhatThongTin(" Bình ", " 0912345678 ", " Đà Nẵng ");
        check(kh.getMaKhachHang().equals("Kh1"));
        check(kh.getHoTen().equals("Bình"));
        check(kh.getSoDienThoai().equals("0912345678"));
        check(kh.getDiaChi().equals("Đà Nẵng"));
        String truoc = kh.hienThiThongTin();
        loi(() -> kh.capNhatThongTin("", "0901234567", "Hà Nội"));
        loi(() -> kh.capNhatThongTin("Tên khác", "sai", "Hà Nội"));
        loi(() -> kh.capNhatThongTin("Tên khác", "0901234567", " "));
        check(kh.hienThiThongTin().equals(truoc));
        soCa++;
    }

    private static void capNhatNhanVien() {
        NhanVien nv = new NhanVien("Nv1", "An", "0901234567", "Sáng");
        nv.capNhatLienHe(" +84912345678 ");
        nv.doiCaLamViec(" Tối ");
        check(nv.getMaNhanVien().equals("Nv1") && nv.getHoTen().equals("An"));
        check(nv.getSoDienThoai().equals("+84912345678") && nv.getCaLamViec().equals("Tối"));
        String truoc = nv.hienThiThongTin();
        loi(() -> nv.capNhatLienHe("123"));
        loi(() -> nv.capNhatLienHe(null));
        loi(() -> nv.doiCaLamViec("\t"));
        loi(() -> nv.doiCaLamViec(null));
        check(nv.hienThiThongTin().equals(truoc));
        soCa++;
    }

    @SuppressWarnings("deprecation")
    private static void hienThiThongTin() {
        KhachHang kh = new KhachHang("K1", "Ánh", "0901234567", "Huế");
        NhanVien nv = new NhanVien("N1", "Bình", "0912345678", "Chiều");
        for (String value : new String[]{"K1", "Ánh", "0901234567", "Huế"}) {
            check(kh.hienThiThongTin().contains(value));
        }
        for (String value : new String[]{"N1", "Bình", "0912345678", "Chiều"}) {
            check(nv.hienThiThongTin().contains(value));
        }
        check(!nv.hienThiThongTin().contains("Dia chi"));
        check(kh.thongTinKhachHang().equals(kh.hienThiThongTin()));
        check(nv.thongTinNhanVien().equals(nv.hienThiThongTin()));
        soCa++;
    }

    private static void cauTrucUml() throws Exception {
        kiemTraModel(KhachHang.class, "maKhachHang", Set.of("maKhachHang", "hoTen", "soDienThoai", "diaChi"));
        kiemTraModel(NhanVien.class, "maNhanVien", Set.of("maNhanVien", "hoTen", "soDienThoai", "caLamViec"));
        for (var constructor : NhanVien.class.getConstructors()) {
            check(constructor.getParameterCount() == 4);
        }
        for (var method : NhanVien.class.getMethods()) {
            check(!method.getName().equals("getDiaChi"));
        }
        soCa++;
    }

    private static void kiemTraModel(Class<?> type, String ma, Set<String> fields) throws Exception {
        Set<String> thucTe = Arrays.stream(type.getDeclaredFields())
                .filter(f -> !f.isSynthetic()).map(Field::getName).collect(Collectors.toSet());
        check(thucTe.equals(fields));
        check(Modifier.isFinal(type.getDeclaredField(ma).getModifiers()));
        for (Field field : type.getDeclaredFields()) {
            check(field.getType() == String.class && Modifier.isPrivate(field.getModifiers()));
        }
        for (var method : type.getMethods()) {
            check(!method.getName().startsWith("set"));
        }
    }

    private static void loi(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Du lieu sai phai bi tu choi");
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
