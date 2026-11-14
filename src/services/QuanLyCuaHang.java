package services;

import models.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.IOException;

public class QuanLyCuaHang {
    private DanhSachSanPham danhSachSanPham = new DanhSachSanPham();
    private DanhSachLoaiSP danhSachLoaiSP = new DanhSachLoaiSP();
    private DanhSachKhachHang danhSachKhachHang = new DanhSachKhachHang();
    private DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien();
    private DanhSachNhaCungCap danhSachNhaCungCap = new DanhSachNhaCungCap();
    private DanhSachHoaDon danhSachHoaDon = new DanhSachHoaDon();
    private DanhSachPhieuNhap danhSachPhieuNhap = new DanhSachPhieuNhap();
    private Kho kho = new Kho(danhSachSanPham);
    private LuuTruFileText luuTru;
    private Scanner banPhim;

    public QuanLyCuaHang() {
        this("data");
    }

    public QuanLyCuaHang(String thuMuc) {
        luuTru = new LuuTruFileText(thuMuc);
    }

    public DanhSachSanPham getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public DanhSachLoaiSP getDanhSachLoaiSP() {
        return danhSachLoaiSP;
    }

    public DanhSachKhachHang getDanhSachKhachHang() {
        return danhSachKhachHang;
    }

    public DanhSachNhanVien getDanhSachNhanVien() {
        return danhSachNhanVien;
    }

    public DanhSachNhaCungCap getDanhSachNhaCungCap() {
        return danhSachNhaCungCap;
    }

    public DanhSachHoaDon getDanhSachHoaDon() {
        return danhSachHoaDon;
    }

    public DanhSachPhieuNhap getDanhSachPhieuNhap() {
        return danhSachPhieuNhap;
    }

    public Kho getKho() {
        return kho;
    }

    public void thayDuLieu(QuanLyCuaHang tam) {
        danhSachSanPham = tam.danhSachSanPham;
        danhSachLoaiSP = tam.danhSachLoaiSP;
        danhSachKhachHang = tam.danhSachKhachHang;
        danhSachNhanVien = tam.danhSachNhanVien;
        danhSachNhaCungCap = tam.danhSachNhaCungCap;
        danhSachHoaDon = tam.danhSachHoaDon;
        danhSachPhieuNhap = tam.danhSachPhieuNhap;
        kho = new Kho(danhSachSanPham);
    }

    public void docDuLieu() throws IOException {
        luuTru.docDuLieu(this);
    }

    public void ghiDuLieu() throws IOException {
        luuTru.ghiDuLieu(this);
    }

    public boolean themSanPham(SanPham sp) {
        if (sp == null)
            return false;
        return danhSachSanPham.them(sp);
    }

    public boolean suaSanPham(String ma, SanPham moi) {
        if (moi == null)
            return false;
        return danhSachSanPham.sua(ma, moi);
    }

    public boolean xoaSanPham(String ma) {
        return danhSachSanPham.xoa(ma);
    }

    public boolean xoaLoaiSP(String ma) {
        return danhSachLoaiSP.xoa(ma);
    }

    public boolean xoaKhachHang(String ma) {
        return danhSachKhachHang.xoa(ma);
    }

    public boolean xoaNhanVien(String ma) {
        return danhSachNhanVien.xoa(ma);
    }

    public boolean xoaNhaCungCap(String ma) {
        return danhSachNhaCungCap.xoa(ma);
    }

    public boolean themHoaDon(HoaDon phieu) {
        if (phieu == null || !phieu.kiemTraHopLe())
            return false;
        if (!danhSachHoaDon.them(phieu))
            return false;
        kho.capNhatTheoHoaDon(null, phieu);
        return true;
    }

    public boolean suaHoaDon(String ma, HoaDon moi) {
        HoaDon cu = danhSachHoaDon.timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!danhSachHoaDon.sua(ma, moi))
            return false;
        kho.capNhatTheoHoaDon(cu, moi);
        return true;
    }

    public boolean xoaHoaDon(String ma) {
        HoaDon cu = danhSachHoaDon.timTheoMa(ma);
        if (cu == null)
            return false;
        kho.capNhatTheoHoaDon(cu, null);
        return danhSachHoaDon.xoa(ma);
    }

    public boolean themPhieuNhap(PhieuNhap phieu) {
        if (phieu == null || !phieu.kiemTraHopLe())
            return false;
        if (!danhSachPhieuNhap.them(phieu))
            return false;
        kho.capNhatTheoPhieuNhap(null, phieu);
        return true;
    }

    public boolean suaPhieuNhap(String ma, PhieuNhap moi) {
        PhieuNhap cu = danhSachPhieuNhap.timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!danhSachPhieuNhap.sua(ma, moi))
            return false;
        kho.capNhatTheoPhieuNhap(cu, moi);
        return true;
    }

    public boolean xoaPhieuNhap(String ma) {
        PhieuNhap cu = danhSachPhieuNhap.timTheoMa(ma);
        if (cu == null)
            return false;
        kho.capNhatTheoPhieuNhap(cu, null);
        return danhSachPhieuNhap.xoa(ma);
    }

    public void chay() {
        banPhim = new Scanner(System.in, "UTF-8");
        if (!napLucBatDau())
            return;
        while (chayMotLuot()) {
            // Mỗi lần chỉ chọn 1
        }
        luuVaThongBao();
    }

    private boolean napLucBatDau() {
        try {
            docDuLieu();
            return true;
        } catch (IOException e) {
            System.out.println("Khong mo duoc du lieu: " + e.getMessage());
            return false;
        }
    }

    private boolean chayMotLuot() {
        try {
            hienThiMenu();
            return xuLyMenu(nhapSoNguyen("Chon: ", 0, 10));
        } catch (IllegalArgumentException | DateTimeException e) {
            System.out.println("Da dung thao tac: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    private void hienThiMenu() {
        System.out.println("\n===== QUAN LY CUA HANG DUNG CU THE THAO =====");
        System.out.println("1. Quan ly San pham");     
        System.out.println("2. Quan ly Loai san pham");
        System.out.println("3. Quan ly Khach hang");   
        System.out.println("4. Quan ly Nhan vien");   
        System.out.println("5. Quan ly Nha cung cap");
        System.out.println("6. Quan ly Hoa don");      
        System.out.println("7. Quan ly Phieu nhap");
        System.out.println("8. Tra cuu ton kho");  
        System.out.println("9. Thong ke tong tien");
        System.out.println("10. Luu file");    
        System.out.println("0. Luu va thoat");
    }

    //menu chính
    private boolean xuLyMenu(int chon) {
        switch (chon) {
            case 1: menuSanPham(); break;
            case 2: menuLoaiSP(); break;
            case 3: menuKhachHang(); break;
            case 4: menuNhanVien(); break;
            case 5: menuNhaCungCap(); break;
            case 6: menuHoaDon(); break;
            case 7: menuPhieuNhap(); break;
            case 8: traCuuTon(); break;
            case 9: menuThongKe(); break;
            case 10: luuVaThongBao(); break;
            case 0: return false;
        }
        return true;
    }

    private void luuVaThongBao() {
        try {
            ghiDuLieu();
            System.out.println("Da luu du lieu.");
        } catch (IOException e) {
            System.out.println("Chua luu duoc: " + e.getMessage());
        }
    }

    private int chonTacVu(String ten) {
        System.out.println("\n--- " + ten + " ---");
        System.out.println("1. Xem danh sach");   
        System.out.println("2. Them");   
        System.out.println("3. Sua");   
        System.out.println("4. Xoa");   
        System.out.println("5. Tim kiem");   
        System.out.println("0. Quay lai");
        return nhapSoNguyen("Chon: ", 0, 5);
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }

    private String nhapChuoi(String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = banPhim.nextLine().trim();
        if (giaTri.isEmpty() || giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Chuoi khong duoc rong, chua dau | hay xuong dong.");
        return giaTri;
    }

    private int nhapSoNguyen(String loiNhac, int min, int max) {
        int giaTri = Integer.parseInt(nhapChuoi(loiNhac));
        if (giaTri < min || giaTri > max)
            throw new IllegalArgumentException("Nhap so nguyen tu " + min + " den " + max + ".");
        return giaTri;
    }

    private double nhapSoThuc(String loiNhac) {
        double giaTri = Double.parseDouble(nhapChuoi(loiNhac));
        if (!Double.isFinite(giaTri) || giaTri <= 0)
            throw new IllegalArgumentException("Nhap so duong huu han.");
        return giaTri;
    }

    private BigDecimal nhapTien(String loiNhac) {
        BigDecimal giaTri = new BigDecimal(nhapChuoi(loiNhac));
        if (giaTri.signum() < 0)
            throw new IllegalArgumentException("So tien phai >= 0.");
        return giaTri;
    }

    private LocalDate nhapNgay(String loiNhac) {
        return LocalDate.parse(nhapChuoi(loiNhac + " (yyyy-MM-dd): "));
    }

    private LoaiSP chonLoaiSP() {
        LoaiSP x = danhSachLoaiSP.timTheoMa(nhapChuoi("Ma LoaiSP: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay LoaiSP.");
        return x;
    }

    private KhachHang chonKhachHang() {
        KhachHang x = danhSachKhachHang.timTheoMa(nhapChuoi("Ma KhachHang: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay KhachHang.");
        return x;
    }

    private NhanVien chonNhanVien() {
        NhanVien x = danhSachNhanVien.timTheoMa(nhapChuoi("Ma NhanVien: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhanVien.");
        return x;
    }

    private NhaCungCap chonNhaCungCap() {
        NhaCungCap x = danhSachNhaCungCap.timTheoMa(nhapChuoi("Ma NhaCungCap: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhaCungCap.");
        return x;
    }

    private SanPham chonSanPham() {
        SanPham x = danhSachSanPham.timTheoMa(nhapChuoi("Ma SP: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay san pham.");
        return x;
    }

    //menu sản phẩm
    private void menuSanPham() {
        int chon;
        do {
            chon = chonTacVu("SanPham");
            switch (chon) {
                case 1: danhSachSanPham.hienThiDanhSach(); break;
                case 2: themSanPhamTuBanPhim(); break;
                case 3: suaSanPhamTuBanPhim(); break;
                case 4: thongBao(xoaSanPham(nhapChuoi("Ma can xoa: "))); break;
                case 5: timSanPhamTuBanPhim(); break;
            }
        } while (chon != 0);
    }

    private void themSanPhamTuBanPhim() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            thongBao(themSanPham(nhapSanPham(ma, null)));
        }
    }

    private void suaSanPhamTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        SanPham cu = danhSachSanPham.timTheoMa(ma);
        if (cu == null)
            throw new IllegalArgumentException("Khong tim thay SP.");
        thongBao(suaSanPham(ma, nhapSanPham(cu.getMaSanPham(), cu)));
    }

    //menu loại sp
    private void menuLoaiSP() {
        int chon;
        do {
            chon = chonTacVu("LoaiSP");
            switch (chon) {
                case 1: danhSachLoaiSP.hienThiDanhSach(); break;
                case 2: themLoaiSPTuBanPhim(); break;
                case 3: suaLoaiSPTuBanPhim(); break;
                case 4: thongBao(xoaLoaiSP(nhapChuoi("Ma can xoa: "))); break;
                case 5: timLoaiSPTuBanPhim(); break;
            }
        } while (chon != 0);
    }

    private void themLoaiSPTuBanPhim() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            thongBao(danhSachLoaiSP.them(nhapLoaiSP(ma)));
        }
    }

    private void suaLoaiSPTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachLoaiSP.sua(ma, nhapLoaiSP(ma)));
    }

    private void timLoaiSPTuBanPhim() {
        LoaiSP[] kq = danhSachLoaiSP.timKiem(nhapChuoi("Ma/ten (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    //menu khách hàng
    private void menuKhachHang() {
        int chon;
        do {
            chon = chonTacVu("KhachHang");
            switch (chon) {
                case 1: danhSachKhachHang.hienThiDanhSach(); break;
                case 2: themKhachHangTuBanPhim(); break;
                case 3: suaKhachHangTuBanPhim(); break;
                case 4: thongBao(xoaKhachHang(nhapChuoi("Ma can xoa: "))); break;
                case 5: timKhachHangTuBanPhim(); break;
            }
        } while (chon != 0);
    }

    private void themKhachHangTuBanPhim() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            thongBao(danhSachKhachHang.them(nhapKhachHang(ma)));
        }
    }

    private void suaKhachHangTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachKhachHang.sua(ma, nhapKhachHang(ma)));
    }

    private void timKhachHangTuBanPhim() {
        KhachHang[] kq = danhSachKhachHang.timKiem(nhapChuoi("Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    // menu nhân viên
    private void menuNhanVien() {
        int chon;
        do {
            chon = chonTacVu("NhanVien");
            switch (chon) {
                case 1: danhSachNhanVien.hienThiDanhSach(); break;
                case 2: themNhanVienTuBanPhim(); break;
                case 3: suaNhanVienTuBanPhim(); break;
                case 4: thongBao(xoaNhanVien(nhapChuoi("Ma can xoa: "))); break;
                case 5: timNhanVienTuBanPhim(); break;
            }
        } while (chon != 0);
    }

    private void themNhanVienTuBanPhim() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            thongBao(danhSachNhanVien.them(nhapNhanVien(ma)));
        }
    }

    private void suaNhanVienTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachNhanVien.sua(ma, nhapNhanVien(ma)));
    }

    private void timNhanVienTuBanPhim() {
        NhanVien[] kq = danhSachNhanVien.timKiem(nhapChuoi("Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    //menu nhà cung cấp
    private void menuNhaCungCap() {
        int chon;
        do {
            chon = chonTacVu("NhaCungCap");
            switch (chon) {
                case 1: danhSachNhaCungCap.hienThiDanhSach(); break;
                case 2: themNhaCungCapTuBanPhim(); break;
                case 3: suaNhaCungCapTuBanPhim(); break;
                case 4: thongBao(xoaNhaCungCap(nhapChuoi("Ma can xoa: "))); break;
                case 5: timNhaCungCapTuBanPhim(); break;
            }
        } while (chon != 0);
    }

    private void themNhaCungCapTuBanPhim() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            thongBao(danhSachNhaCungCap.them(nhapNhaCungCap(ma)));
        }
    }

    private void suaNhaCungCapTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachNhaCungCap.sua(ma, nhapNhaCungCap(ma)));
    }

    private void timNhaCungCapTuBanPhim() {
        NhaCungCap[] kq = danhSachNhaCungCap.timKiem(nhapChuoi("Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private LoaiSP nhapLoaiSP(String ma) {
        return new LoaiSP(ma, nhapChuoi("Ten loai: "),
                nhapChuoi("Mo ta: "));
    }

    private KhachHang nhapKhachHang(String ma) {
        return new KhachHang(ma, nhapChuoi("Ho ten: "),
                nhapChuoi("SDT (9-11 chu so): "),
                nhapChuoi("Email: "));
    }

    private NhanVien nhapNhanVien(String ma) {
        return new NhanVien(ma, nhapChuoi("Ho ten: "),
                nhapChuoi("SDT (9-11 chu so): "),
                nhapChuoi("Ca lam viec: "));
    }

    private NhaCungCap nhapNhaCungCap(String ma) {
        return new NhaCungCap(ma, nhapChuoi("Ten NCC: "),
                nhapChuoi("SDT (9-11 chu so): "),
                nhapChuoi("Email: "));
    }

    private SanPham nhapSanPham(String ma, SanPham cu) {
        int loai = cu == null ? nhapSoNguyen("1. Giay  2. Dung cu: ", 1, 2) : loaiLop(cu);
        if (loai == 1)
            return nhapGiay(ma);
        return nhapDungCu(ma);
    }

    private int loaiLop(SanPham sp) {
        if (sp instanceof GiayTheThao)
            return 1;
        return 2;
    }

    private GiayTheThao nhapGiay(String ma) {
        return new GiayTheThao(ma, nhapChuoi("Ten SP: "), nhapChuoi("Thuong hieu: "),
                nhapTien("Gia ban: "), chonLoaiSP(), nhapSoThuc("Co giay: "),
                nhapChuoi("Mau: "), nhapChuoi("Chat lieu: "), nhapChuoi("Loai de: "));
    }

    private DungCuTheThao nhapDungCu(String ma) {
        return new DungCuTheThao(ma, nhapChuoi("Ten SP: "), nhapChuoi("Thuong hieu: "),
                nhapTien("Gia ban: "), chonLoaiSP(), nhapChuoi("Mon the thao: "),
                nhapChuoi("Vat lieu: "), nhapSoThuc("Khoi luong (kg): "));
    }

    private void timSanPhamTuBanPhim() {
        int chon = nhapSoNguyen("1. Ma chinh xac  2. Ma/ten gan dung  3. Loai  4. Khoang gia: ", 1, 4);
        SanPham[] kq = locSanPham(chon);
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private SanPham[] locSanPham(int chon) {
        if (chon == 1) {
            SanPham sp = danhSachSanPham.timTheoMa(nhapChuoi("Ma: "));
            return sp == null ? new SanPham[0] : new SanPham[] {sp};
        }
        if (chon == 2)
            return danhSachSanPham.timKiem(nhapChuoi("Ma/ten: "));
        if (chon == 3)
            return danhSachSanPham.locTheoLoaiSP(nhapChuoi("Ma loai: "));
        return danhSachSanPham.locTheoKhoangGia(nhapTien("Gia tu: "), nhapTien("Gia den: "));
    }

    //menu hóa đơn
    private void menuHoaDon() {
        int chon;
        do {
            chon = chonTacVuChungTu("HoaDon");
            switch (chon) {
                case 1: danhSachHoaDon.hienThiDanhSach(); break;
                case 2: themHoaDonTuBanPhim(); break;
                case 3: suaHoaDonTuBanPhim(); break;
                case 4: thongBao(xoaHoaDon(nhapChuoi("Ma can xoa: "))); break;
                case 5: timHoaDonTuBanPhim(); break;
                case 6: xemChiTietHoaDon(); break;
            }
        } while (chon != 0);
    }

    private HoaDon nhapHoaDon(String ma) {
        return new HoaDon(ma, nhapNgay("Ngay lap"), chonKhachHang(), chonNhanVien());
    }

    private void themHoaDonTuBanPhim() {
        HoaDon phieu = nhapHoaDon(nhapChuoi("Ma moi: "));
        soanHoaDon(phieu);
        thongBao(themHoaDon(phieu));
    }

    private HoaDon chonHoaDon() {
        HoaDon phieu = danhSachHoaDon.timTheoMa(nhapChuoi("Ma chung tu: "));
        if (phieu == null)
            throw new IllegalArgumentException("Khong tim thay chung tu.");
        return phieu;
    }

    private void suaHoaDonTuBanPhim() {
        HoaDon moi = chonHoaDon();
        moi.setNgayLap(nhapNgay("Ngay lap moi"));
        moi.setKhachHang(chonKhachHang());
        moi.setNhanVien(chonNhanVien());
        soanHoaDon(moi);
        thongBao(suaHoaDon(moi.getMaHoaDon(), moi));
    }

    // danh sách trả bản copy: hủy/sửa bản copy đã lưu mà ko làm thay đổi đã lưu
    private void soanHoaDon(HoaDon phieu) {
        int chon;
        do {
            chon = chonChiTiet();
            switch (chon) {
                case 1: hienThiChiTiet(phieu); break;
                case 2: thongBao(phieu.themChiTiet(nhapChiTietHoaDon(chonSanPham()))); break;
                case 3: suaDongHoaDon(phieu); break;
                case 4: thongBao(phieu.xoaChiTiet(nhapChuoi("Ma SP can xoa: "))); break;
            }
        } while (chon != 0);
    }

    private ChiTietHoaDon nhapChiTietHoaDon(SanPham sp) {
        return new ChiTietHoaDon(sp, nhapSoNguyen("So luong: ", 1, Integer.MAX_VALUE),
                nhapTien("Don gia ban (gia hien tai: " + sp.getGiaBan().toPlainString() + "): "));
    }

    private void suaDongHoaDon(HoaDon phieu) {
        SanPham sp = chonSanPham();
        thongBao(phieu.suaChiTiet(sp.getMaSanPham(), nhapChiTietHoaDon(sp)));
    }

    private void hienThiChiTiet(HoaDon phieu) {
        System.out.println(phieu.hienThiThongTin());
        ChiTietHoaDon[] ds = phieu.getChiTiet();
        for (int i = 0; i < ds.length; i++)
            System.out.println("  " + ds[i].hienThiThongTin());
    }

    private void xemChiTietHoaDon() {
        hienThiChiTiet(chonHoaDon());
    }

    private void timHoaDonTuBanPhim() {
        HoaDon[] kq = danhSachHoaDon.timKiem(nhapChuoi("Ma chung tu/ma doi tac: "));
        for (int i = 0; i < kq.length; i++)
            hienThiChiTiet(kq[i]);
        System.out.println("Tim thay: " + kq.length);
    }

    //menu phiếu nhập
    private void menuPhieuNhap() {
        int chon;
        do {
            chon = chonTacVuChungTu("PhieuNhap");
            switch (chon) {
                case 1: danhSachPhieuNhap.hienThiDanhSach(); break;
                case 2: themPhieuNhapTuBanPhim(); break;
                case 3: suaPhieuNhapTuBanPhim(); break;
                case 4: thongBao(xoaPhieuNhap(nhapChuoi("Ma can xoa: "))); break;
                case 5: timPhieuNhapTuBanPhim(); break;
                case 6: xemChiTietPhieuNhap(); break;
            }
        } while (chon != 0);
    }

    private PhieuNhap nhapPhieuNhap(String ma) {
        return new PhieuNhap(ma, nhapNgay("Ngay lap"), chonNhaCungCap(), chonNhanVien());
    }

    private void themPhieuNhapTuBanPhim() {
        PhieuNhap phieu = nhapPhieuNhap(nhapChuoi("Ma moi: "));
        soanPhieuNhap(phieu);
        thongBao(themPhieuNhap(phieu));
    }

    private PhieuNhap chonPhieuNhap() {
        PhieuNhap phieu = danhSachPhieuNhap.timTheoMa(nhapChuoi("Ma chung tu: "));
        if (phieu == null)
            throw new IllegalArgumentException("Khong tim thay chung tu.");
        return phieu;
    }

    private void suaPhieuNhapTuBanPhim() {
        PhieuNhap moi = chonPhieuNhap();
        moi.setNgayLap(nhapNgay("Ngay lap moi"));
        moi.setNhaCungCap(chonNhaCungCap());
        moi.setNhanVien(chonNhanVien());
        soanPhieuNhap(moi);
        thongBao(suaPhieuNhap(moi.getMaPhieuNhap(), moi));
    }

    // cập nhật kho khi đã lưu phiếu nhập
    private void soanPhieuNhap(PhieuNhap phieu) {
        int chon;
        do {
            chon = chonChiTiet();
            switch (chon) {
                case 1: hienThiChiTiet(phieu); break;
                case 2: thongBao(phieu.themChiTiet(nhapChiTietPhieuNhap(chonSanPham()))); break;
                case 3: suaDongPhieuNhap(phieu); break;
                case 4: thongBao(phieu.xoaChiTiet(nhapChuoi("Ma SP can xoa: "))); break;
            }
        } while (chon != 0);
    }

    private ChiTietPhieuNhap nhapChiTietPhieuNhap(SanPham sp) {
        return new ChiTietPhieuNhap(sp, nhapSoNguyen("So luong: ", 1, Integer.MAX_VALUE),
                nhapTien("Don gia nhap: "));
    }

    private void suaDongPhieuNhap(PhieuNhap phieu) {
        SanPham sp = chonSanPham();
        thongBao(phieu.suaChiTiet(sp.getMaSanPham(), nhapChiTietPhieuNhap(sp)));
    }

    private void hienThiChiTiet(PhieuNhap phieu) {
        System.out.println(phieu.hienThiThongTin());
        ChiTietPhieuNhap[] ds = phieu.getChiTiet();
        for (int i = 0; i < ds.length; i++)
            System.out.println("  " + ds[i].hienThiThongTin());
    }

    private void xemChiTietPhieuNhap() {
        hienThiChiTiet(chonPhieuNhap());
    }

    private void timPhieuNhapTuBanPhim() {
        PhieuNhap[] kq = danhSachPhieuNhap.timKiem(nhapChuoi("Ma chung tu/ma doi tac: "));
        for (int i = 0; i < kq.length; i++)
            hienThiChiTiet(kq[i]);
        System.out.println("Tim thay: " + kq.length);
    }

    private int chonTacVuChungTu(String ten) {
        System.out.println("\n--- " + ten + " ---");
        System.out.println("1. Xem danh sach");   
        System.out.println("2. Them");   
        System.out.println("3. Sua");   
        System.out.println("4. Xoa");   
        System.out.println("5. Tim kiem");
        System.out.println("6. Xem chi tiet");   
        System.out.println("0. Quay lai");
        return nhapSoNguyen("Chon: ", 0, 6);
    }

    private int chonChiTiet() {
        System.out.println("Chi tiet: 1. Xem   2. Them   3. Sua   4. Xoa   0. Luu ban soan");
        return nhapSoNguyen("Chon: ", 0, 4);
    }

    private void traCuuTon() {
        String ma = nhapChuoi("Ma SP: ");
        int ton = kho.traCuuTon(ma);
        System.out.println(ton < 0 ? "Khong tim thay SP." : "Ton hien tai: " + ton);
    }

    private void menuThongKe() {
        int chon = nhapSoNguyen("1. Tat ca\n2. Theo khoang ngay: ", 1, 2);
        if (chon == 1) {
            inTongTien(danhSachHoaDon.thongKeTongTien(), danhSachPhieuNhap.thongKeTongTien());
            return;
        }
        LocalDate tu = nhapNgay("Tu ngay");
        LocalDate den = nhapNgay("Den ngay");
        inTongTien(danhSachHoaDon.thongKeTongTien(tu, den),
                danhSachPhieuNhap.thongKeTongTien(tu, den));
    }

    private void inTongTien(BigDecimal tienBan, BigDecimal tienNhap) {
        System.out.println("Tong tien ban: " + tienBan.toPlainString());
        System.out.println("Tong tien nhap: " + tienNhap.toPlainString());
    }
}
