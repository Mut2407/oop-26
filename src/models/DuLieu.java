package models;

public class DuLieu {
    public static SanPham[] taoDanhSachSanPham() {
        return new SanPham[] {
            new GiayTheThao(
                "SP03", "Giay chay bo", 1200000, "Nike",
                8, "GIAY", 42, "Den", "Cao su"
            ),
            new GiayTheThao(
                "SP04", "Giay bong da", 950000, "Adidas",
                12, "GIAY", 41, "Trang", "Dinh cao su"
            ),
            new DungCuTheThao(
                "SP01", "Vot cau long", 350000, "Yonex",
                10, "DC", "cau long", "Carbon", 0.085
            ),
            new DungCuTheThao(
                "SP02", "Bong da", 250000, "Dong Luc",
                20, "DC", "Bong da", "Da tong hop", 0.45
            )
        };
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
                "Quan 1, TP.HCM", "0902000001"
            ),
            new NhaCungCap(
                "NCC02", "Dung Cu Binh Minh",
                "Quan 3, TP.HCM", "0902000002"
            ),
            new NhaCungCap(
                "NCC03", "Giay The Thao Minh Tam",
                "Quan 10, TP.HCM", "0902000003"
            ),
            new NhaCungCap(
                "NCC04", "The Thao Thanh Cong",
                "Tan Binh, TP.HCM", "0902000004"
            ),
            new NhaCungCap(
                "NCC05", "Dung Cu Viet Sport",
                "Thu Duc, TP.HCM", "0902000005"
            )
        };
    }
}
