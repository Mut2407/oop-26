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
        if (sp == null || sp.getSoLuongTon() != 0 || !dungLoai(sp))
            return false;
        return danhSachSanPham.them(sp);
    }

    private boolean dungLoai(SanPham sp) {
        return sp.getLoaiSP() != null && danhSachLoaiSP.timTheoMa(
                sp.getLoaiSP().getMaLoaiSP()) == sp.getLoaiSP();
    }

    public boolean suaSanPham(String ma, SanPham moi) {
        if (moi == null || !dungLoai(moi))
            return false;
        return danhSachSanPham.sua(ma, moi);
    }

    public boolean xoaSanPham(String ma) {
        ma = ma == null ? "" : ma.trim();
        SanPham sp = danhSachSanPham.timTheoMa(ma);
        if (sp == null || sp.getSoLuongTon() != 0 || sanPhamDuocDung(ma))
            return false;
        return danhSachSanPham.xoa(ma);
    }

    private boolean sanPhamDuocDung(String ma) {
        for (HoaDon hd : danhSachHoaDon.getTatCa())
            if (hd.timChiTiet(ma) != null)
                return true;
        for (PhieuNhap pn : danhSachPhieuNhap.getTatCa())
            if (pn.timChiTiet(ma) != null)
                return true;
        return false;
    }

    public boolean xoaLoaiSP(String ma) {
        ma = ma == null ? "" : ma.trim();
        if (danhSachSanPham.locTheoLoaiSP(ma).length > 0)
            return false;
        return danhSachLoaiSP.xoa(ma);
    }

    public boolean xoaKhachHang(String ma) {
        ma = ma == null ? "" : ma.trim();
        for (HoaDon hd : danhSachHoaDon.getTatCa())
            if (hd.getKhachHang().getMaKhachHang().equalsIgnoreCase(ma))
                return false;
        return danhSachKhachHang.xoa(ma);
    }

    public boolean xoaNhanVien(String ma) {
        ma = ma == null ? "" : ma.trim();
        for (HoaDon hd : danhSachHoaDon.getTatCa())
            if (hd.getNhanVien().getMaNhanVien().equalsIgnoreCase(ma))
                return false;
        for (PhieuNhap pn : danhSachPhieuNhap.getTatCa())
            if (pn.getNhanVien().getMaNhanVien().equalsIgnoreCase(ma))
                return false;
        return danhSachNhanVien.xoa(ma);
    }

    public boolean xoaNhaCungCap(String ma) {
        ma = ma == null ? "" : ma.trim();
        for (PhieuNhap pn : danhSachPhieuNhap.getTatCa())
            if (pn.getNhaCungCap().getMaNhaCungCap().equalsIgnoreCase(ma))
                return false;
        return danhSachNhaCungCap.xoa(ma);
    }

    private boolean dungThamChieu(HoaDon phieu) {
        if (phieu == null || !phieu.kiemTraHopLe())
            return false;
        return danhSachKhachHang.timTheoMa(phieu.getKhachHang().getMaKhachHang())
                == phieu.getKhachHang() && danhSachNhanVien.timTheoMa(
                phieu.getNhanVien().getMaNhanVien()) == phieu.getNhanVien();
    }

    public boolean themHoaDon(HoaDon phieu) {
        if (!dungThamChieu(phieu) || !danhSachHoaDon.them(phieu))
            return false;
        if (kho.capNhatTheoHoaDon(null, phieu))
            return true;
        danhSachHoaDon.xoa(phieu.getMaHoaDon());
        return false;
    }

    public boolean suaHoaDon(String ma, HoaDon moi) {
        HoaDon cu = danhSachHoaDon.timTheoMa(ma);
        if (cu == null || !dungThamChieu(moi))
            return false;
        if (!danhSachHoaDon.sua(ma, moi))
            return false;
        if (kho.capNhatTheoHoaDon(cu, moi))
            return true;
        danhSachHoaDon.sua(ma, cu);
        return false;
    }

    public boolean xoaHoaDon(String ma) {
        HoaDon cu = danhSachHoaDon.timTheoMa(ma);
        if (cu == null || !kho.capNhatTheoHoaDon(cu, null))
            return false;
        return danhSachHoaDon.xoa(ma);
    }

    private boolean dungThamChieu(PhieuNhap phieu) {
        if (phieu == null || !phieu.kiemTraHopLe())
            return false;
        return danhSachNhaCungCap.timTheoMa(phieu.getNhaCungCap().getMaNhaCungCap())
                == phieu.getNhaCungCap() && danhSachNhanVien.timTheoMa(
                phieu.getNhanVien().getMaNhanVien()) == phieu.getNhanVien();
    }

    public boolean themPhieuNhap(PhieuNhap phieu) {
        if (!dungThamChieu(phieu) || !danhSachPhieuNhap.them(phieu))
            return false;
        if (kho.capNhatTheoPhieuNhap(null, phieu))
            return true;
        danhSachPhieuNhap.xoa(phieu.getMaPhieuNhap());
        return false;
    }

    public boolean suaPhieuNhap(String ma, PhieuNhap moi) {
        PhieuNhap cu = danhSachPhieuNhap.timTheoMa(ma);
        if (cu == null || !dungThamChieu(moi))
            return false;
        if (!danhSachPhieuNhap.sua(ma, moi))
            return false;
        if (kho.capNhatTheoPhieuNhap(cu, moi))
            return true;
        danhSachPhieuNhap.sua(ma, cu);
        return false;
    }

    public boolean xoaPhieuNhap(String ma) {
        PhieuNhap cu = danhSachPhieuNhap.timTheoMa(ma);
        if (cu == null || !kho.capNhatTheoPhieuNhap(cu, null))
            return false;
        return danhSachPhieuNhap.xoa(ma);
    }

    public void chay() {
        banPhim = new Scanner(System.in, "UTF-8");
        if (!napLucBatDau())
            return;
        while (chayMotLuot()) {
            // Moi luot chi thuc hien mot lua chon menu.
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

    private boolean xuLyMenu(int chon) {
        if (chon == 0)
            return false;
        if (chon <= 5)
            moMenuDuLieu(chon);
        else
            moMenuNghiepVu(chon);
        return true;
    }

    private void moMenuDuLieu(int chon) {
        if (chon == 1) menuSanPham();
        else if (chon == 2) menuLoaiSP();
        else if (chon == 3) menuKhachHang();
        else if (chon == 4) menuNhanVien();
        else if (chon == 5) menuNhaCungCap();
    }

    private void moMenuNghiepVu(int chon) {
        if (chon == 6) menuHoaDon();
        else if (chon == 7) menuPhieuNhap();
        else if (chon == 8) traCuuTon();
        else if (chon == 9) menuThongKe();
        else if (chon == 10) luuVaThongBao();
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
        System.out.println("6. Them nhieu (nhap n/k phan tu)");   
        System.out.println("0. Quay lai");
        return nhapSoNguyen("Chon: ", 0, 6);
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thanh cong."
                : "Khong thuc hien duoc: kiem tra ma trung/thieu, tham chieu, chi tiet va ton kho.");
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

    private void menuSanPham() {
        int chon = chonTacVu("SanPham");
        while (chon != 0) {
            xuLySanPham(chon);
            chon = chonTacVu("SanPham");
        }
    }

    private void xuLySanPham(int chon) {
        if (chon == 1) danhSachSanPham.hienThiDanhSach();
        else if (chon == 2) themSanPhamTuBanPhim();
        else if (chon == 3) suaSanPhamTuBanPhim();
        else if (chon == 4) thongBao(xoaSanPham(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timSanPhamTuBanPhim();
        else if (chon == 6) themNhieuSanPham();
    }

    private void themSanPhamTuBanPhim() {
        String ma = nhapChuoi("Ma moi: ");
        thongBao(themSanPham(nhapSanPham(ma, null)));
    }

    private void suaSanPhamTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        SanPham cu = danhSachSanPham.timTheoMa(ma);
        if (cu == null)
            throw new IllegalArgumentException("Khong tim thay SP.");
        thongBao(suaSanPham(ma, nhapSanPham(cu.getMaSanPham(), cu)));
    }

    private void themNhieuSanPham() {
        int n = nhapSoNguyen("So phan tu can them: ", 1, 1000);
        for (int i = 0; i < n; i++)
            themSanPhamTuBanPhim();
    }

    private void menuLoaiSP() {
        int chon = chonTacVu("LoaiSP");
        while (chon != 0) {
            xuLyLoaiSP(chon);
            chon = chonTacVu("LoaiSP");
        }
    }

    private void xuLyLoaiSP(int chon) {
        if (chon == 1) danhSachLoaiSP.hienThiDanhSach();
        else if (chon == 2) themLoaiSPTuBanPhim();
        else if (chon == 3) suaLoaiSPTuBanPhim();
        else if (chon == 4) thongBao(xoaLoaiSP(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timLoaiSPTuBanPhim();
        else if (chon == 6) themNhieuLoaiSP();
    }

    private void themLoaiSPTuBanPhim() {
        String ma = nhapChuoi("Ma moi: ");
        IQuanLyDanhSach<LoaiSP> quanLy = danhSachLoaiSP;
        thongBao(quanLy.them(nhapLoaiSP(ma)));
    }

    private void suaLoaiSPTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachLoaiSP.sua(ma, nhapLoaiSP(ma)));
    }

    private void themNhieuLoaiSP() {
        int n = nhapSoNguyen("So phan tu can them: ", 1, 1000);
        for (int i = 0; i < n; i++)
            themLoaiSPTuBanPhim();
    }

    private void timLoaiSPTuBanPhim() {
        LoaiSP[] kq = danhSachLoaiSP.timKiem(nhapChuoi("Ma/ten (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private void menuKhachHang() {
        int chon = chonTacVu("KhachHang");
        while (chon != 0) {
            xuLyKhachHang(chon);
            chon = chonTacVu("KhachHang");
        }
    }

    private void xuLyKhachHang(int chon) {
        if (chon == 1) danhSachKhachHang.hienThiDanhSach();
        else if (chon == 2) themKhachHangTuBanPhim();
        else if (chon == 3) suaKhachHangTuBanPhim();
        else if (chon == 4) thongBao(xoaKhachHang(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timKhachHangTuBanPhim();
        else if (chon == 6) themNhieuKhachHang();
    }

    private void themKhachHangTuBanPhim() {
        String ma = nhapChuoi("Ma moi: ");
        thongBao(danhSachKhachHang.them(nhapKhachHang(ma)));
    }

    private void suaKhachHangTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachKhachHang.sua(ma, nhapKhachHang(ma)));
    }

    private void themNhieuKhachHang() {
        int n = nhapSoNguyen("So phan tu can them: ", 1, 1000);
        for (int i = 0; i < n; i++)
            themKhachHangTuBanPhim();
    }

    private void timKhachHangTuBanPhim() {
        KhachHang[] kq = danhSachKhachHang.timKiem(nhapChuoi("Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private void menuNhanVien() {
        int chon = chonTacVu("NhanVien");
        while (chon != 0) {
            xuLyNhanVien(chon);
            chon = chonTacVu("NhanVien");
        }
    }

    private void xuLyNhanVien(int chon) {
        if (chon == 1) danhSachNhanVien.hienThiDanhSach();
        else if (chon == 2) themNhanVienTuBanPhim();
        else if (chon == 3) suaNhanVienTuBanPhim();
        else if (chon == 4) thongBao(xoaNhanVien(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timNhanVienTuBanPhim();
        else if (chon == 6) themNhieuNhanVien();
    }

    private void themNhanVienTuBanPhim() {
        String ma = nhapChuoi("Ma moi: ");
        thongBao(danhSachNhanVien.them(nhapNhanVien(ma)));
    }

    private void suaNhanVienTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachNhanVien.sua(ma, nhapNhanVien(ma)));
    }

    private void themNhieuNhanVien() {
        int n = nhapSoNguyen("So phan tu can them: ", 1, 1000);
        for (int i = 0; i < n; i++)
            themNhanVienTuBanPhim();
    }

    private void timNhanVienTuBanPhim() {
        NhanVien[] kq = danhSachNhanVien.timKiem(nhapChuoi("Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private void menuNhaCungCap() {
        int chon = chonTacVu("NhaCungCap");
        while (chon != 0) {
            xuLyNhaCungCap(chon);
            chon = chonTacVu("NhaCungCap");
        }
    }

    private void xuLyNhaCungCap(int chon) {
        if (chon == 1) danhSachNhaCungCap.hienThiDanhSach();
        else if (chon == 2) themNhaCungCapTuBanPhim();
        else if (chon == 3) suaNhaCungCapTuBanPhim();
        else if (chon == 4) thongBao(xoaNhaCungCap(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timNhaCungCapTuBanPhim();
        else if (chon == 6) themNhieuNhaCungCap();
    }

    private void themNhaCungCapTuBanPhim() {
        String ma = nhapChuoi("Ma moi: ");
        thongBao(danhSachNhaCungCap.them(nhapNhaCungCap(ma)));
    }

    private void suaNhaCungCapTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSachNhaCungCap.sua(ma, nhapNhaCungCap(ma)));
    }

    private void themNhieuNhaCungCap() {
        int n = nhapSoNguyen("So phan tu can them: ", 1, 1000);
        for (int i = 0; i < n; i++)
            themNhaCungCapTuBanPhim();
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

    private void menuHoaDon() {
        int chon = chonTacVuChungTu("HoaDon");
        while (chon != 0) {
            xuLyHoaDon(chon);
            chon = chonTacVuChungTu("HoaDon");
        }
    }

    private void xuLyHoaDon(int chon) {
        if (chon == 1) danhSachHoaDon.hienThiDanhSach();
        else if (chon == 2) themHoaDonTuBanPhim();
        else if (chon == 3) suaHoaDonTuBanPhim();
        else if (chon == 4) thongBao(xoaHoaDon(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timHoaDonTuBanPhim();
        else if (chon == 6) xemChiTietHoaDon();
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

    // Danh sach tra ban sao: huy/sua ban soan khong lam doi chung tu da luu.
    private void soanHoaDon(HoaDon phieu) {
        int chon = chonChiTiet();
        while (chon != 0) {
            xuLyChiTietHoaDon(phieu, chon);
            chon = chonChiTiet();
        }
    }

    private void xuLyChiTietHoaDon(HoaDon phieu, int chon) {
        if (chon == 1) hienThiChiTiet(phieu);
        else if (chon == 2) thongBao(phieu.themChiTiet(nhapChiTietHoaDon(chonSanPham())));
        else if (chon == 3) suaDongHoaDon(phieu);
        else if (chon == 4) thongBao(phieu.xoaChiTiet(nhapChuoi("Ma SP can xoa: ")));
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

    private void menuPhieuNhap() {
        int chon = chonTacVuChungTu("PhieuNhap");
        while (chon != 0) {
            xuLyPhieuNhap(chon);
            chon = chonTacVuChungTu("PhieuNhap");
        }
    }

    private void xuLyPhieuNhap(int chon) {
        if (chon == 1) danhSachPhieuNhap.hienThiDanhSach();
        else if (chon == 2) themPhieuNhapTuBanPhim();
        else if (chon == 3) suaPhieuNhapTuBanPhim();
        else if (chon == 4) thongBao(xoaPhieuNhap(nhapChuoi("Ma can xoa: ")));
        else if (chon == 5) timPhieuNhapTuBanPhim();
        else if (chon == 6) xemChiTietPhieuNhap();
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

    // Chi cap nhat kho khi luu thanh cong toan bo phieu.
    private void soanPhieuNhap(PhieuNhap phieu) {
        int chon = chonChiTiet();
        while (chon != 0) {
            xuLyChiTietPhieuNhap(phieu, chon);
            chon = chonChiTiet();
        }
    }

    private void xuLyChiTietPhieuNhap(PhieuNhap phieu, int chon) {
        if (chon == 1) hienThiChiTiet(phieu);
        else if (chon == 2) thongBao(phieu.themChiTiet(nhapChiTietPhieuNhap(chonSanPham())));
        else if (chon == 3) suaDongPhieuNhap(phieu);
        else if (chon == 4) thongBao(phieu.xoaChiTiet(nhapChuoi("Ma SP can xoa: ")));
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
