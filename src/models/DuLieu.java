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
}
