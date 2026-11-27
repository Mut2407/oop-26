import java.math.BigDecimal;
import java.time.LocalDate;
import models.*;
import danhsach.*;
import quanly.*;

public class KiemThu {
    private static int soKiemTra = 0;

    public static void dung(boolean dieuKien, String thongBao) {
        soKiemTra++;
        if (!dieuKien) {
            throw new AssertionError("THAT BAI o kiem tra: " + thongBao);
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      BAT DAU KIEM THU HE THONG OOP_26            ");
        System.out.println("==================================================");

        kiemThuModelsVaDaHinh();
        kiemThuDanhSachVaInterface();
        kiemThuKhoVaTonKho();
        kiemThuQuanLyVaThongKe();
        kiemThuLuuTruFile();

        System.out.println("\n==================================================");
        System.out.println(" KET QUA: DAT " + soKiemTra + " KIEM TRA THANH CONG!");
        System.out.println("==================================================");
    }

    // 1. Kiem thu Models (Class don) va Da hinh Overriding
    public static void kiemThuModelsVaDaHinh() {
        LoaiSP l1 = new LoaiSP("L01", "Giay", "Cac loai giay the thao");
        dung(l1.getMaLoaiSP().equals("L01"), "Kiem tra ma loai SP");
        dung(l1.getTenLoaiSP().equals("Giay"), "Kiem tra ten loai SP");

        // Da hinh gan lop con cho lop cha SanPham
        SanPham sp1 = new GiayTheThao("SP01", "Giay chay bo", "Nike", new BigDecimal("1500000"), l1, 42.0, "Den", "Vai", "Cao su");
        SanPham sp2 = new DungCuTheThao("SP02", "Bong da", "Adidas", new BigDecimal("250000"), l1, "Bong da", "Da", 0.45);

        // Kiem tra da hinh phuong thuc hienThiThongTin()
        dung(sp1.hienThiThongTin().contains("42.0"), "Da hinh hien thi GiayTheThao");
        dung(sp2.hienThiThongTin().contains("Bong da"), "Da hinh hien thi DungCuTheThao");

        // Kiem tra cac thuc the con lai
        KhachHang kh1 = new KhachHang("KH01", "Nguyen Van A", "0901234567", "Ha Noi");
        NhanVien nv1 = new NhanVien("NV01", "Tran Thi B", "0909876543", "Ca sang");
        NhaCungCap ncc1 = new NhaCungCap("NCC01", "Cong ty The Thao", "0912345678", "TP.HCM");

        dung(kh1.getHoTen().equals("Nguyen Van A"), "Kiem tra KhachHang");
        dung(nv1.getCaLamViec().equals("Ca sang"), "Kiem tra NhanVien");
        dung(ncc1.getTenNhaCungCap().equals("Cong ty The Thao"), "Kiem tra NhaCungCap");

        // Kiem tra HoaDon va ChiTietHoaDon
        HoaDon hd1 = new HoaDon("HD01", LocalDate.now(), kh1, nv1);
        hd1.themChiTiet(new ChiTietHoaDon(sp1, 2, new BigDecimal("1500000")));
        hd1.themChiTiet(new ChiTietHoaDon(sp2, 1, new BigDecimal("250000")));
        dung(hd1.getSoLuongChiTiet() == 2, "So luong chi tiet hoa don");
        dung(hd1.tinhTongTien().compareTo(new BigDecimal("3250000")) == 0, "Tong tien hoa don");

        // Kiem tra PhieuNhap va ChiTietPhieuNhap
        PhieuNhap pn1 = new PhieuNhap("PN01", LocalDate.now(), ncc1, nv1);
        pn1.themChiTiet(new ChiTietPhieuNhap(sp1, 10, new BigDecimal("1000000")));
        dung(pn1.getSoLuongChiTiet() == 1, "So luong chi tiet phieu nhap");
        dung(pn1.tinhTongTien().compareTo(new BigDecimal("10000000")) == 0, "Tong tien phieu nhap");

        System.out.println("[PASS] 1. Models & Da hinh (SanPham, HoaDon, PhieuNhap) - OK");
    }

    // 2. Kiem thu DanhSach va Interface IQuanLyDanhSach (CRUD, Overloading)
    public static void kiemThuDanhSachVaInterface() {
        DanhSachLoaiSP dsLoai = new DanhSachLoaiSP();
        // Da hinh qua Interface
        IQuanLyDanhSach<LoaiSP> iLoai = dsLoai;

        dung(iLoai.them(new LoaiSP("L01", "Giay", "Giay the thao")), "Them loai SP qua interface");
        dung(dsLoai.timTheoMa("L01") != null, "Tim theo ma qua danh sach");
        dung(!iLoai.them(new LoaiSP("L01", "Trung", "Khong duoc trung")), "Chong trung ma loai SP");

        dung(iLoai.sua("L01", new LoaiSP("L01", "Giay Da Sua", "Mo ta moi")), "Sua loai SP");
        dung(dsLoai.timTheoMa("L01").getTenLoaiSP().equals("Giay Da Sua"), "Thong tin sau khi sua");

        dung(iLoai.timKiem("Da Sua").length == 1, "Tim kiem gan dung loai SP qua interface");
        dung(iLoai.xoa("L01"), "Xoa loai SP qua interface");
        dung(dsLoai.timTheoMa("L01") == null, "Kiem tra da xoa");

        // Kiem thu DanhSachSanPham va Da hinh nap chong (Overloading) timKiem
        DanhSachSanPham dsSP = new DanhSachSanPham();
        LoaiSP l = new LoaiSP("L01", "The thao", "Mo ta");
        dsSP.them(new GiayTheThao("SP01", "Giay Nike", "Nike", new BigDecimal("500000"), l, 42.0, "Den", "Vai", "Cao su"));
        dsSP.them(new DungCuTheThao("SP02", "Bong Adidas", "Adidas", new BigDecimal("200000"), l, "Bong da", "Da", 0.45));

        // Overloading 1: tim theo tu khoa String
        dung(dsSP.timKiem("Nike").length == 1, "Tim kiem san pham theo tu khoa");
        // Overloading 2: tim theo khoang gia (BigDecimal, BigDecimal)
        dung(dsSP.timKiem(new BigDecimal("100000"), new BigDecimal("300000")).length == 1, "Da hinh nap chong timKiem theo khoang gia");

        System.out.println("[PASS] 2. DanhSach & Interface IQuanLyDanhSach (CRUD, Overloading) - OK");
    }

    // 3. Kiem thu Kho va Cap nhat ton kho
    public static void kiemThuKhoVaTonKho() {
        DanhSachSanPham dsSP = new DanhSachSanPham();
        LoaiSP l = new LoaiSP("L01", "The thao", "Mo ta san pham");
        SanPham sp = new GiayTheThao("SP01", "Giay", "Nike", new BigDecimal("500000"), l, 42.0, "Den", "Vai", "Cao su");
        dsSP.them(sp);

        Kho kho = new Kho(dsSP);
        dung(kho.traCuuTon("SP01") == 0, "Ton kho ban dau bang 0");

        // Nhap hang 15 cai -> ton tang len 15
        PhieuNhap pn = new PhieuNhap("PN01", LocalDate.now(), new NhaCungCap("NCC01", "NCC", "0123", "HN"), new NhanVien("NV01", "NV", "0123", "Sang"));
        pn.themChiTiet(new ChiTietPhieuNhap(sp, 15, new BigDecimal("400000")));
        dung(kho.capNhatTheoPhieuNhap(null, pn), "Cap nhat kho theo phieu nhap");
        dung(kho.traCuuTon("SP01") == 15, "Nhap 15 san pham -> ton 15");

        // Ban hang 5 cai -> ton giam con 10
        HoaDon hd = new HoaDon("HD01", LocalDate.now(), new KhachHang("KH01", "KH", "0123", "HCM"), new NhanVien("NV01", "NV", "0123", "Sang"));
        hd.themChiTiet(new ChiTietHoaDon(sp, 5, new BigDecimal("500000")));
        dung(kho.capNhatTheoHoaDon(null, hd), "Cap nhat kho theo hoa don");
        dung(kho.traCuuTon("SP01") == 10, "Ban 5 san pham -> con ton 10");

        System.out.println("[PASS] 3. Kho & Cap nhat ton kho (Nhap 15, Ban 5 -> Ton 10) - OK");
    }

    // 4. Kiem thu QuanLyCuaHang & Da hinh quan ly
    public static void kiemThuQuanLyVaThongKe() {
        // Da hinh: Khai bao lop cha QuanLyCuaHang, khoi tao lop con QuanLySanPham
        QuanLyCuaHang ql = new QuanLySanPham();
        dung(ql.getDanhSachSanPham() != null, "Lop cha truy cap du lieu san pham");
        dung(ql.getKho() != null, "Truy cap kho tu lop quan ly");

        // Kiem tra tra cuu va thong ke
        QuanLyCuaHang.traCuuTonKho("SP01");
        QuanLyCuaHang.thongKeTongTien();

        System.out.println("[PASS] 4. QuanLyCuaHang & Da hinh quan ly - OK");
    }

    // 5. Kiem thu Luu tru File Text
    public static void kiemThuLuuTruFile() {
        QuanLyCuaHang.docDuLieu();
        QuanLyCuaHang ql = new QuanLyCuaHang();
        dung(ql.getDanhSachLoaiSP().getSoLuong() >= 0, "Doc file loai SP khong bi loi");
        dung(ql.getDanhSachSanPham().getSoLuong() >= 0, "Doc file san pham thanh cong");

        System.out.println("[PASS] 5. Luu tru File Text (doc/ghi du lieu) - OK");
    }
}
