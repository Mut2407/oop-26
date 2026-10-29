import models.*;

public class HienThiThongTinTest {
    public static void main(String[] args) {
        SanPham sanPham = new SanPham("SP01", "Mau", 125000, "Hang", 8, "L01") {};
        kiemTra(sanPham, "Ma san pham:SP01", "Ten san pham:Mau", "Gia ban:125000.0",
                "Thuong hieu:Hang", "So luong ton:8", "Ma loai:L01");
        sanPham.setTenSanPham("Ten moi");
        sanPham.setSoLuongTon(0);
        kiemTra(sanPham, "Ten san pham:Ten moi", "So luong ton:0");
        SanPham dungCu = new DungCuTheThao("DC01", "Vot", 250000, "Yonex", 3, "DC", "Cau long", "Carbon", 0.085);
        kiemTra(dungCu, "Ma san pham:DC01", "Ten san pham:Vot", "Carbon", "0.085 kg");
        SanPham quanAo = new QuanAoTheThao("QA01", "Mau", 100000, "Hang", 2, "QA", 42, "Cao su");
        kiemTra(quanAo, "Ma san pham:QA01", "42.0", "Cao su");
        SanPham giay = new GiayTheThao("G01", "Giay", 500000, "Nike", 5, "GIAY", 41, "Den", "Cao su");
        kiemTra(giay, "Ma san pham:G01", "Thuong hieu:Nike", "41.0", "Den", "Cao su");
        System.out.println("PASS: 5 display scenarios");
    }

    private static void kiemTra(SanPham sp, String... noiDung) {
        String thucTe = sp.hienThiThongTin();
        for (String mongDoi : noiDung) {
            if (!thucTe.contains(mongDoi)) {
                throw new AssertionError("Missing " + mongDoi + " in " + thucTe);
            }
        }
    }
}
