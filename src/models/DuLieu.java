package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.math.BigDecimal;

public class DuLieu {
    private static final Map<String, DanhMuc> DANH_MUC = new LinkedHashMap<>();

    public static void khoiTaoDanhMuc() {
        dangKyDanhMuc(new DanhMuc("DC", "Dụng cụ thể thao", ""));
        dangKyDanhMuc(new DanhMuc("GIAY", "Giày thể thao", ""));
    }

    public static void dangKyDanhMuc(DanhMuc danhMuc) {
        if (danhMuc == null) {
            throw new IllegalArgumentException("Danh muc khong duoc null.");
        }
        String ma = danhMuc.getMaDanhMuc().toUpperCase(Locale.ROOT);
        if (DANH_MUC.containsKey(ma)) {
            throw new IllegalArgumentException("Ma danh muc da ton tai.");
        }
        DANH_MUC.put(ma, danhMuc);
    }

    public static DanhMuc timDanhMuc(String ma) {
        return ma == null ? null : DANH_MUC.get(ma.trim().toUpperCase(Locale.ROOT));
    }

    public static List<DanhMuc> getDanhMuc() {
        return List.copyOf(DANH_MUC.values());
    }

    public static SanPham[] taoDanhSachSanPham() {
        DanhMuc giay = yeuCauDanhMuc("GIAY");
        DanhMuc dungCu = yeuCauDanhMuc("DC");
        return new SanPham[] {
            new GiayTheThao(
                "SP03", "Giay chay bo", "Nike", new BigDecimal("1200000"),
                8, "Den", null, giay, "42", "Cao su"
            ),
            new GiayTheThao(
                "SP04", "Giay bong da", "Adidas", new BigDecimal("950000"),
                12, "Trang", null, giay, "41", "Dinh cao su"
            ),
            new DungCuTheThao(
                "SP01", "Vot cau long", "Yonex", new BigDecimal("350000"),
                10, null, "Carbon", dungCu, 0.085
            ),
            new DungCuTheThao(
                "SP02", "Bong da", "Dong Luc", new BigDecimal("250000"),
                20, null, "Da tong hop", dungCu, 0.45
            )
        };
    }

    private static DanhMuc yeuCauDanhMuc(String ma) {
        DanhMuc dm = timDanhMuc(ma);
        if (dm == null) throw new IllegalArgumentException("Ma danh muc chua anh xa: " + ma);
        return dm;
    }

    public static KhachHang[] taoDanhSachKhachHang() {
        return new KhachHang[] {
            new KhachHang("KH01", "Nguyen Van A", "0123456789", "sky.walker.8492@gmail.com"),
            new KhachHang("KH02", "Nguyen Van B", "0123456788", "nova.drift.3920@outlook.com"),
            new KhachHang("KH03", "Nguyen Van C", "0123456787", "pixel.coder.7154@yahoo.com"),
            new KhachHang("KH04", "Nguyen Van D", "0123456786", "frost.byte.2046@gmail.com"),
            new KhachHang("KH05", "Nguyen Van E", "0123456785", "quantum.flux.5831@outlook.com"),
            new KhachHang("KH06", "Nguyen Van F", "0123456784", "echo.valley.9213@gmail.com"),
            new KhachHang("KH07", "Nguyen Van G", "0123456783", "neon.pulse.4678@yahoo.com"),
            new KhachHang("KH08", "Nguyen Van H", "0123456782", "cyber.fox.1385@gmail.com"),
            new KhachHang("KH09", "Nguyen Van I", "0123456781", "terra.storm.6294@outlook.com"),
            new KhachHang("KH10", "Nguyen Van J", "0123456780", "phoenix.wing.7502@gmail.com")
        };
    }
    public static NhanVien[] taoDanhSachNhanVien() {
    return new NhanVien[] {
        new NhanVien(
            "NV01", "Nguyen Van An", "Sang",
            "0901000001", "Quan 1, TP.HCM"
        ),
        new NhanVien(
            "NV02", "Tran Thi Binh", "Chieu",
            "0901000002", "Quan 3, TP.HCM"
        ),
        new NhanVien(
            "NV03", "Le Minh Chau", "Toi",
            "0901000003", "Quan 5, TP.HCM"
        ),
        new NhanVien(
            "NV04", "Pham Ngoc Dung", "Sang",
            "0901000004", "Binh Thanh, TP.HCM"
        ),
        new NhanVien(
            "NV05", "Vo Thanh Hai", "Chieu",
            "0901000005", "Thu Duc, TP.HCM"
        )
    };
}

    public static NhaCungCap[] taoDanhSachNhaCungCap() {
        return new NhaCungCap[] {
            new NhaCungCap(
                "NCC01", "The Thao An Phat",
                "0902000001", "Quan 1, TP.HCM"
            ),
            new NhaCungCap(
                "NCC02", "Dung Cu Binh Minh",
                "0902000002", "Quan 3, TP.HCM"
            ),
            new NhaCungCap(
                "NCC03", "Giay The Thao Minh Tam",
                "0902000003", "Quan 10, TP.HCM"
            ),
            new NhaCungCap(
                "NCC04", "The Thao Thanh Cong",
                "0902000004", "Tan Binh, TP.HCM"
            ),
            new NhaCungCap(
                "NCC05", "Dung Cu Viet Sport",
                "0902000005", "Thu Duc, TP.HCM"
            )
        };
    }
}
