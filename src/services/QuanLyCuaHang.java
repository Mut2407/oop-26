package services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import models.*;

public class QuanLyCuaHang {
    private DanhSachLoaiSP loaiSP;
    private DanhSachSanPham sanPham;
    private DanhSachKhachHang khachHang;
    private DanhSachNhanVien nhanVien;
    private DanhSachNhaCungCap nhaCungCap;
    private DanhSachHoaDon hoaDon;
    private DanhSachPhieuNhap phieuNhap;
    private Kho kho;
    private LuuTruFileText luuTru;
    private Scanner scanner;

    public QuanLyCuaHang() {
        this("data");
    }

    public QuanLyCuaHang(String thuMucDuLieu) {
        loaiSP = new DanhSachLoaiSP();
        sanPham = new DanhSachSanPham();
        khachHang = new DanhSachKhachHang();
        nhanVien = new DanhSachNhanVien();
        nhaCungCap = new DanhSachNhaCungCap();
        hoaDon = new DanhSachHoaDon();
        phieuNhap = new DanhSachPhieuNhap();
        kho = new Kho(sanPham);
        luuTru = new LuuTruFileText(thuMucDuLieu);
        scanner = new Scanner(System.in);
    }

    public DanhSachLoaiSP getLoaiSP() {
        return loaiSP;
    }

    public DanhSachSanPham getSanPham() {
        return sanPham;
    }

    public DanhSachKhachHang getKhachHang() {
        return khachHang;
    }

    public DanhSachNhanVien getNhanVien() {
        return nhanVien;
    }

    public DanhSachNhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public DanhSachHoaDon getHoaDon() {
        return hoaDon;
    }

    public DanhSachPhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public int traCuuTon(String maSP) {
        return kho.traCuuTon(maSP);
    }

    public boolean themLoaiSP(LoaiSP moi) {
        return loaiSP.them(moi);
    }

    public boolean suaLoaiSP(String ma, LoaiSP moi) {
        // Danh sach sua doi tuong cu, cac san pham van tro den dung loai nay.
        return loaiSP.sua(ma, moi);
    }

    public boolean xoaLoaiSP(String ma) {
        if (sanPham.locTheoLoaiSP(ma).length > 0) {
            throw new IllegalArgumentException("Loai san pham dang duoc su dung.");
        }
        return loaiSP.xoa(ma);
    }

    private void ganLoaiSanPham(SanPham sp) {
        if (sp == null || sp.getLoaiSP() == null) {
            throw new IllegalArgumentException("Chua co san pham/loai san pham.");
        }
        LoaiSP loai = loaiSP.timTheoMa(sp.getLoaiSP().getMaLoaiSP());
        if (loai == null) {
            throw new IllegalArgumentException("Ma loai san pham khong ton tai.");
        }
        sp.setLoaiSP(loai);
    }

    public boolean themSanPham(SanPham moi) {
        ganLoaiSanPham(moi);
        if (moi.getSoLuongTon() != 0) {
            throw new IllegalArgumentException("San pham moi phai co ton 0; lap phieu nhap de tang ton.");
        }
        return sanPham.them(moi);
    }

    public boolean suaSanPham(String ma, SanPham moi) {
        ganLoaiSanPham(moi);
        // capNhatThongTin cua SanPham khong gan lai soLuongTon.
        return sanPham.sua(ma, moi);
    }

    public boolean themNhieuSanPham(SanPham[] moi) {
        if (moi == null) {
            return false;
        }
        DanhSachSanPham kiemTra = new DanhSachSanPham(sanPham.getTatCa());
        for (int i = 0; i < moi.length; i++) {
            ganLoaiSanPham(moi[i]);
            moi[i].kiemTraHopLe();
            if (moi[i].getSoLuongTon() != 0 || !kiemTra.them(moi[i])) {
                return false;
            }
        }
        for (int i = 0; i < moi.length; i++) {
            sanPham.them(moi[i]);
        }
        return true;
    }

    public boolean xoaSanPham(String ma) {
        SanPham cu = sanPham.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        if (cu.getSoLuongTon() != 0 || sanPhamDuocThamChieu(ma)) {
            throw new IllegalArgumentException("San pham con ton hoac dang co trong chung tu.");
        }
        return sanPham.xoa(ma);
    }

    private boolean sanPhamDuocThamChieu(String ma) {
        HoaDon[] dsHD = hoaDon.getTatCa();
        PhieuNhap[] dsPN = phieuNhap.getTatCa();
        for (int i = 0; i < dsHD.length; i++) {
            if (dsHD[i].timChiTiet(ma) != null) {
                return true;
            }
        }
        for (int i = 0; i < dsPN.length; i++) {
            if (dsPN[i].timChiTiet(ma) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean themKhachHang(KhachHang moi) {
        return khachHang.them(moi);
    }

    public boolean suaKhachHang(String ma, KhachHang moi) {
        return khachHang.sua(ma, moi);
    }

    public boolean xoaKhachHang(String ma) {
        HoaDon[] dsHoaDon = hoaDon.getTatCa();
        for (int i = 0; i < dsHoaDon.length; i++) {
            if (dsHoaDon[i].getKhachHang().getMaKhachHang().equalsIgnoreCase(ma.trim())) {
                throw new IllegalArgumentException("Doi tuong dang duoc chung tu tham chieu.");
            }
        }
        return khachHang.xoa(ma);
    }

    public boolean themNhanVien(NhanVien moi) {
        return nhanVien.them(moi);
    }

    public boolean suaNhanVien(String ma, NhanVien moi) {
        return nhanVien.sua(ma, moi);
    }

    public boolean xoaNhanVien(String ma) {
        HoaDon[] dsHoaDon = hoaDon.getTatCa();
        for (int i = 0; i < dsHoaDon.length; i++) {
            if (dsHoaDon[i].getNhanVien().getMaNhanVien().equalsIgnoreCase(ma.trim())) {
                throw new IllegalArgumentException("Doi tuong dang duoc chung tu tham chieu.");
            }
        }
        PhieuNhap[] dsPhieuNhap = phieuNhap.getTatCa();
        for (int i = 0; i < dsPhieuNhap.length; i++) {
            if (dsPhieuNhap[i].getNhanVien().getMaNhanVien().equalsIgnoreCase(ma.trim())) {
                throw new IllegalArgumentException("Doi tuong dang duoc chung tu tham chieu.");
            }
        }
        return nhanVien.xoa(ma);
    }

    public boolean themNhaCungCap(NhaCungCap moi) {
        return nhaCungCap.them(moi);
    }

    public boolean suaNhaCungCap(String ma, NhaCungCap moi) {
        return nhaCungCap.sua(ma, moi);
    }

    public boolean xoaNhaCungCap(String ma) {
        PhieuNhap[] dsPhieuNhap = phieuNhap.getTatCa();
        for (int i = 0; i < dsPhieuNhap.length; i++) {
            if (dsPhieuNhap[i].getNhaCungCap().getMaNhaCungCap().equalsIgnoreCase(ma.trim())) {
                throw new IllegalArgumentException("Doi tuong dang duoc chung tu tham chieu.");
            }
        }
        return nhaCungCap.xoa(ma);
    }

    public boolean themHoaDon(HoaDon moi) {
        HoaDon hopLe = chuanHoaHoaDon(moi);
        if (hoaDon.timTheoMa(hopLe.getMaHoaDon()) != null) {
            return false;
        }
        kho.capNhatTheoHoaDon(null, hopLe);
        return hoaDon.them(hopLe);
    }

    public boolean suaHoaDon(String ma, HoaDon moi) {
        HoaDon cu = hoaDon.timTheoMa(ma);
        if (cu == null || moi == null || !cu.getMaHoaDon().equals(moi.getMaHoaDon())) {
            return false;
        }
        HoaDon hopLe = chuanHoaHoaDon(moi);
        kho.capNhatTheoHoaDon(cu, hopLe);
        return hoaDon.sua(ma, hopLe);
    }

    public boolean xoaHoaDon(String ma) {
        HoaDon cu = hoaDon.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        kho.capNhatTheoHoaDon(cu, null);
        return hoaDon.xoa(ma);
    }

    private HoaDon chuanHoaHoaDon(HoaDon goc) {
        if (goc == null || goc.getSoLuongChiTiet() == 0) {
            throw new IllegalArgumentException("Chung tu phai co it nhat mot dong.");
        }
        return ganLienKetHoaDon(goc);
    }

    private HoaDon ganLienKetHoaDon(HoaDon goc) {
        HoaDon moi = new HoaDon(goc);
        moi.setKhachHang(khachHang.timTheoMa(goc.getKhachHang().getMaKhachHang()));
        moi.setNhanVien(nhanVien.timTheoMa(goc.getNhanVien().getMaNhanVien()));
        ChiTietHoaDon[] chiTiet = moi.getChiTiet();
        for (int i = 0; i < chiTiet.length; i++) {
            String maSP = chiTiet[i].getSanPham().getMaSanPham();
            chiTiet[i].setSanPham(sanPham.timTheoMa(maSP));
            moi.suaChiTiet(maSP, chiTiet[i]);
        }
        return moi;
    }

    void khoiPhucHoaDon(HoaDon moi) {
        if (!hoaDon.them(ganLienKetHoaDon(moi))) {
            throw new IllegalArgumentException("Ma chung tu trung trong file.");
        }
    }

    public boolean themPhieuNhap(PhieuNhap moi) {
        PhieuNhap hopLe = chuanHoaPhieuNhap(moi);
        if (phieuNhap.timTheoMa(hopLe.getMaPhieuNhap()) != null) {
            return false;
        }
        kho.capNhatTheoPhieuNhap(null, hopLe);
        return phieuNhap.them(hopLe);
    }

    public boolean suaPhieuNhap(String ma, PhieuNhap moi) {
        PhieuNhap cu = phieuNhap.timTheoMa(ma);
        if (cu == null || moi == null || !cu.getMaPhieuNhap().equals(moi.getMaPhieuNhap())) {
            return false;
        }
        PhieuNhap hopLe = chuanHoaPhieuNhap(moi);
        kho.capNhatTheoPhieuNhap(cu, hopLe);
        return phieuNhap.sua(ma, hopLe);
    }

    public boolean xoaPhieuNhap(String ma) {
        PhieuNhap cu = phieuNhap.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        kho.capNhatTheoPhieuNhap(cu, null);
        return phieuNhap.xoa(ma);
    }

    private PhieuNhap chuanHoaPhieuNhap(PhieuNhap goc) {
        if (goc == null || goc.getSoLuongChiTiet() == 0) {
            throw new IllegalArgumentException("Chung tu phai co it nhat mot dong.");
        }
        return ganLienKetPhieuNhap(goc);
    }

    private PhieuNhap ganLienKetPhieuNhap(PhieuNhap goc) {
        PhieuNhap moi = new PhieuNhap(goc);
        moi.setNhaCungCap(nhaCungCap.timTheoMa(goc.getNhaCungCap().getMaNhaCungCap()));
        moi.setNhanVien(nhanVien.timTheoMa(goc.getNhanVien().getMaNhanVien()));
        ChiTietPhieuNhap[] chiTiet = moi.getChiTiet();
        for (int i = 0; i < chiTiet.length; i++) {
            String maSP = chiTiet[i].getSanPham().getMaSanPham();
            chiTiet[i].setSanPham(sanPham.timTheoMa(maSP));
            moi.suaChiTiet(maSP, chiTiet[i]);
        }
        return moi;
    }

    void khoiPhucPhieuNhap(PhieuNhap moi) {
        if (!phieuNhap.them(ganLienKetPhieuNhap(moi))) {
            throw new IllegalArgumentException("Ma chung tu trung trong file.");
        }
    }

    void khoiPhucSanPham(SanPham sp) {
        ganLoaiSanPham(sp);
        if (!sanPham.them(sp)) {
            throw new IllegalArgumentException("Ma san pham trung trong file.");
        }
    }

    public BigDecimal thongKeTienBan() {
        return hoaDon.thongKeTongTien();
    }

    public BigDecimal thongKeTienBan(LocalDate tu, LocalDate den) {
        return hoaDon.thongKeTongTien(tu, den);
    }

    public BigDecimal thongKeTienNhap() {
        return phieuNhap.thongKeTongTien();
    }

    public BigDecimal thongKeTienNhap(LocalDate tu, LocalDate den) {
        return phieuNhap.thongKeTongTien(tu, den);
    }

    public void docDuLieu() throws IOException {
        // Chi nhan bo du lieu moi khi doc tat ca cac file thanh cong.
        QuanLyCuaHang moi = luuTru.docDuLieu();
        loaiSP = moi.loaiSP;
        sanPham = moi.sanPham;
        khachHang = moi.khachHang;
        nhanVien = moi.nhanVien;
        nhaCungCap = moi.nhaCungCap;
        hoaDon = moi.hoaDon;
        phieuNhap = moi.phieuNhap;
        kho = new Kho(sanPham);
    }

    public void ghiDuLieu() throws IOException {
        luuTru.ghiDuLieu(this);
    }

    public boolean chay() {
        try {
            docDuLieu();
            menu();
            ghiDuLieu();
            return true;
        } catch (IOException e) {
            System.err.println("Khong the doc/ghi du lieu: " + e.getMessage());
            return false;
        } finally {
            scanner.close();
        }
    }

    public void menu() {
        try {
            menuChinh();
        } catch (NoSuchElementException e) {
            System.out.println("Ket thuc nhap lieu. Bo qua thao tac dang nhap do.");
        }
    }

    private void menuChinh() {
        int chon;
        do {
            hienThiMenu();
            chon = docSoNguyen("Lua chon: ", 0);
            try {
                xuLyLuaChon(chon);
            } catch (IllegalArgumentException e) {
                System.out.println("Khong thuc hien: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("CHUA LUU DUOC: " + e.getMessage());
            }
        } while (chon != 0);
    }

    private void hienThiMenu() {
        System.out.println("\n===== SPORT STORE =====");
        System.out.println("1. San pham  2. Loai SP  3. Khach hang  4. Nhan vien  5. Nha cung cap");
        System.out.println("6. Hoa don  7. Phieu nhap  8. Luu file  9. Thong ke tong tien");
        System.out.println("10. Nhap n/them k san pham  11. Loc san pham  0. Luu va thoat");
    }

    private void xuLyLuaChon(int chon) throws IOException {
        switch (chon) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                menuDoiTuong(chon);
                break;
            case 8:
                ghiDuLieu();
                System.out.println("Da luu du lieu.");
                break;
            case 9: menuThongKe(); break;
            case 10: nhapNhieuSanPham(); break;
            case 11: locSanPham(); break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void menuDoiTuong(int nhom) {
        int chon;
        do {
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim gan dung  5. Xem tat ca  6. Tim dung ma  0. Quay lai");
            chon = docSoNguyen("Lua chon: ", 0);
            try {
                xuLyMenuDoiTuong(nhom, chon);
            } catch (IllegalArgumentException e) {
                System.out.println("Khong thuc hien: " + e.getMessage());
            }
        } while (chon != 0);
    }

    private void xuLyMenuDoiTuong(int nhom, int chon) {
        switch (nhom) {
            case 1: xuLySanPham(chon); break;
            case 2: xuLyLoaiSP(chon); break;
            case 3: xuLyKhachHang(chon); break;
            case 4: xuLyNhanVien(chon); break;
            case 5: xuLyNhaCungCap(chon); break;
            case 6: xuLyHoaDon(chon); break;
            case 7: xuLyPhieuNhap(chon); break;
            default: break;
        }
    }

    private void baoKetQua(boolean thanhCong) {
        if (thanhCong) {
            System.out.println("Da cap nhat. Chon 8 o menu chinh hoac 0 de luu va thoat.");
        } else {
            System.out.println("Khong thuc hien: ma trung, ma khong ton tai hoac du lieu chua hop le.");
        }
    }

    private void xuLySanPham(int chon) {
        switch (chon) {
            case 1: baoKetQua(themSanPham(nhapSanPham(null))); break;
            case 2: suaSanPhamTuBanPhim(); break;
            case 3: baoKetQua(xoaSanPham(docMa("Ma can xoa: "))); break;
            case 4: hienThiSanPham(sanPham.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiSanPham(sanPham.getTatCa()); break;
            case 6:
                SanPham ketQua = sanPham.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : ketQua.hienThiThongTin());
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaSanPhamTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (sanPham.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaSanPham(ma, nhapSanPham(ma)));
    }

    private void hienThiSanPham(SanPham[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(ds[i].hienThiThongTin());
        }
    }

    private void xuLyLoaiSP(int chon) {
        switch (chon) {
            case 1: baoKetQua(themLoaiSP(nhapLoaiSP(null))); break;
            case 2: suaLoaiSPTuBanPhim(); break;
            case 3: baoKetQua(xoaLoaiSP(docMa("Ma can xoa: "))); break;
            case 4: hienThiLoaiSP(loaiSP.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiLoaiSP(loaiSP.getTatCa()); break;
            case 6:
                LoaiSP ketQua = loaiSP.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : ketQua.hienThiThongTin());
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaLoaiSPTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (loaiSP.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaLoaiSP(ma, nhapLoaiSP(ma)));
    }

    private void hienThiLoaiSP(LoaiSP[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(ds[i].hienThiThongTin());
        }
    }

    private void xuLyKhachHang(int chon) {
        switch (chon) {
            case 1: baoKetQua(themKhachHang(nhapKhachHang(null))); break;
            case 2: suaKhachHangTuBanPhim(); break;
            case 3: baoKetQua(xoaKhachHang(docMa("Ma can xoa: "))); break;
            case 4: hienThiKhachHang(khachHang.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiKhachHang(khachHang.getTatCa()); break;
            case 6:
                KhachHang ketQua = khachHang.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : ketQua.hienThiThongTin());
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaKhachHangTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (khachHang.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaKhachHang(ma, nhapKhachHang(ma)));
    }

    private void hienThiKhachHang(KhachHang[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(ds[i].hienThiThongTin());
        }
    }

    private void xuLyNhanVien(int chon) {
        switch (chon) {
            case 1: baoKetQua(themNhanVien(nhapNhanVien(null))); break;
            case 2: suaNhanVienTuBanPhim(); break;
            case 3: baoKetQua(xoaNhanVien(docMa("Ma can xoa: "))); break;
            case 4: hienThiNhanVien(nhanVien.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiNhanVien(nhanVien.getTatCa()); break;
            case 6:
                NhanVien ketQua = nhanVien.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : ketQua.hienThiThongTin());
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaNhanVienTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (nhanVien.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaNhanVien(ma, nhapNhanVien(ma)));
    }

    private void hienThiNhanVien(NhanVien[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(ds[i].hienThiThongTin());
        }
    }

    private void xuLyNhaCungCap(int chon) {
        switch (chon) {
            case 1: baoKetQua(themNhaCungCap(nhapNhaCungCap(null))); break;
            case 2: suaNhaCungCapTuBanPhim(); break;
            case 3: baoKetQua(xoaNhaCungCap(docMa("Ma can xoa: "))); break;
            case 4: hienThiNhaCungCap(nhaCungCap.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiNhaCungCap(nhaCungCap.getTatCa()); break;
            case 6:
                NhaCungCap ketQua = nhaCungCap.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : ketQua.hienThiThongTin());
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaNhaCungCapTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (nhaCungCap.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaNhaCungCap(ma, nhapNhaCungCap(ma)));
    }

    private void hienThiNhaCungCap(NhaCungCap[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(ds[i].hienThiThongTin());
        }
    }

    private void xuLyHoaDon(int chon) {
        switch (chon) {
            case 1: baoKetQua(themHoaDon(nhapHoaDon(null))); break;
            case 2: suaHoaDonTuBanPhim(); break;
            case 3: baoKetQua(xoaHoaDon(docMa("Ma can xoa: "))); break;
            case 4: hienThiHoaDon(hoaDon.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiHoaDon(hoaDon.getTatCa()); break;
            case 6:
                HoaDon ketQua = hoaDon.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : hienThiHoaDon(ketQua));
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaHoaDonTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (hoaDon.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaHoaDon(ma, nhapHoaDon(ma)));
    }

    private void hienThiHoaDon(HoaDon[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(hienThiHoaDon(ds[i]));
        }
    }

    private void xuLyPhieuNhap(int chon) {
        switch (chon) {
            case 1: baoKetQua(themPhieuNhap(nhapPhieuNhap(null))); break;
            case 2: suaPhieuNhapTuBanPhim(); break;
            case 3: baoKetQua(xoaPhieuNhap(docMa("Ma can xoa: "))); break;
            case 4: hienThiPhieuNhap(phieuNhap.timKiem(docDong("Tu khoa: "))); break;
            case 5: hienThiPhieuNhap(phieuNhap.getTatCa()); break;
            case 6:
                PhieuNhap ketQua = phieuNhap.timTheoMa(docMa("Ma can tim: "));
                System.out.println(ketQua == null ? "Khong tim thay." : hienThiPhieuNhap(ketQua));
                break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void suaPhieuNhapTuBanPhim() {
        String ma = docMa("Ma can sua: ");
        if (phieuNhap.timTheoMa(ma) == null) {
            System.out.println("Khong tim thay.");
            return;
        }
        baoKetQua(suaPhieuNhap(ma, nhapPhieuNhap(ma)));
    }

    private void hienThiPhieuNhap(PhieuNhap[] ds) {
        System.out.println("So ket qua: " + ds.length);
        for (int i = 0; i < ds.length; i++) {
            System.out.println(hienThiPhieuNhap(ds[i]));
        }
    }

    private LoaiSP nhapLoaiSP(String ma) {
        if (ma == null) {
            ma = docMa("Ma loai SP: ");
        }
        return new LoaiSP(ma, docChuoi("Ten loai SP: "), docDong("Mo ta: "));
    }

    private KhachHang nhapKhachHang(String ma) {
        if (ma == null) {
            ma = docMa("Ma khach hang: ");
        }
        return new KhachHang(ma, docChuoi("Ho ten: "), docDienThoai(), docChuoi("Dia chi: "));
    }

    private NhanVien nhapNhanVien(String ma) {
        if (ma == null) {
            ma = docMa("Ma nhan vien: ");
        }
        return new NhanVien(ma, docChuoi("Ho ten: "), docDienThoai(), docChuoi("Ca lam viec: "));
    }

    private NhaCungCap nhapNhaCungCap(String ma) {
        if (ma == null) {
            ma = docMa("Ma nha cung cap: ");
        }
        return new NhaCungCap(ma, docChuoi("Ten nha cung cap: "), docDienThoai(), docChuoi("Dia chi: "));
    }

    private SanPham nhapSanPham(String ma) {
        // Tham chieu interface tro den danh sach cu the, dung nhu chuong 5.
        IQuanLyDanhSach<SanPham> danhSach = sanPham;
        SanPham cu = ma == null ? null : danhSach.timTheoMa(ma);
        if (ma == null) {
            ma = docMa("Ma san pham: ");
        }
        String ten = docChuoi("Ten san pham: ");
        String thuongHieu = docChuoi("Thuong hieu: ");
        BigDecimal gia = docTien("Gia ban: ");
        LoaiSP loai = loaiSP.timTheoMa(docMa("Ma loai SP: "));
        int kieu = cu == null ? docSoNguyen("Kieu (1=giay, 2=quan ao, 3=dung cu): ", 1) : kieuSanPham(cu);
        return taoSanPham(kieu, ma, ten, thuongHieu, gia, loai);
    }

    private int kieuSanPham(SanPham sp) {
        if (sp instanceof GiayTheThao) {
            return 1;
        }
        if (sp instanceof QuanAoTheThao) {
            return 2;
        }
        return 3;
    }

    private SanPham taoSanPham(int kieu, String ma, String ten, String hieu, BigDecimal gia, LoaiSP loai) {
        switch (kieu) {
            case 1: return new GiayTheThao(ma, ten, hieu, gia, loai,
                    docSoDuong("Co giay: "), docChuoi("Mau sac: "), docDong("Chat lieu: "), docChuoi("Loai de giay: "));
            case 2: return new QuanAoTheThao(ma, ten, hieu, gia, loai,
                    docChuoi("Co quan ao: "), docChuoi("Mau sac: "), docDong("Chat lieu: "));
            case 3: return new DungCuTheThao(ma, ten, hieu, gia, loai,
                    docChuoi("Mon the thao: "), docChuoi("Vat lieu: "), docSoDuong("Khoi luong kg: "));
            default: throw new IllegalArgumentException("Kieu san pham khong hop le.");
        }
    }

    private void nhapNhieuSanPham() {
        int soLuong = docSoNguyen("So san pham can nhap: ", 1);
        SanPham[] moi = new SanPham[soLuong];
        for (int i = 0; i < soLuong; i++) {
            System.out.println("San pham " + (i + 1));
            moi[i] = nhapSanPham(null);
        }
        baoKetQua(themNhieuSanPham(moi));
    }

    private void locSanPham() {
        int chon = docSoNguyen("1. Theo loai  2. Theo khoang gia: ", 1);
        if (chon == 1) {
            hienThiSanPham(sanPham.locTheoLoaiSP(docMa("Ma loai: ")));
        } else if (chon == 2) {
            hienThiSanPham(sanPham.locTheoKhoangGia(docTien("Tu gia: "), docTien("Den gia: ")));
        } else {
            System.out.println("Lua chon khong hop le.");
        }
    }

    private HoaDon nhapHoaDon(String ma) {
        HoaDon cu = ma == null ? null : hoaDon.timTheoMa(ma);
        if (ma == null) {
            ma = docMa("Ma chung tu: ");
        }
        LocalDate ngay = docNgay("Ngay lap yyyy-MM-dd: ");
        KhachHang doiTac = khachHang.timTheoMa(docMa("Ma KhachHang: "));
        NhanVien nv = nhanVien.timTheoMa(docMa("Ma nhan vien: "));
        HoaDon moi = new HoaDon(ma, ngay, doiTac, nv);
        if (cu != null) {
            ChiTietHoaDon[] chiTiet = cu.getChiTiet();
            for (int i = 0; i < chiTiet.length; i++) {
                moi.themChiTiet(chiTiet[i]);
            }
        }
        menuChiTietHoaDon(moi);
        return moi;
    }

    private void menuChiTietHoaDon(HoaDon moi) {
        int chon;
        do {
            System.out.println("1. Them dong  2. Sua dong  3. Xoa dong  4. Xem  0. Hoan tat");
            chon = docSoNguyen("Chi tiet: ", 0);
            try {
                xuLyChiTietHoaDon(moi, chon);
            } catch (IllegalArgumentException e) {
                System.out.println("Chi tiet khong hop le: " + e.getMessage());
            }
        } while (chon != 0);
    }

    private void xuLyChiTietHoaDon(HoaDon moi, int chon) {
        switch (chon) {
            case 1: moi.themChiTiet(nhapChiTietHoaDon()); break;
            case 2:
                String maSP = docMa("Ma SP can sua: ");
                System.out.println(moi.suaChiTiet(maSP, nhapChiTietHoaDon()) ? "Da sua dong." : "Khong tim thay.");
                break;
            case 3: System.out.println(moi.xoaChiTiet(docMa("Ma SP can xoa: ")) ? "Da xoa dong." : "Khong tim thay."); break;
            case 4: System.out.println(hienThiHoaDon(moi)); break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private ChiTietHoaDon nhapChiTietHoaDon() {
        SanPham sp = sanPham.timTheoMa(docMa("Ma SP: "));
        if (sp == null) {
            throw new IllegalArgumentException("San pham khong ton tai.");
        }
        System.out.println(sp.hienThiThongTin());
        return new ChiTietHoaDon(sp, docSoNguyen("So luong: ", 1), docTien("DonGiaBan: "));
    }

    private String hienThiHoaDon(HoaDon chungTu) {
        String ketQua = chungTu.hienThiThongTin();
        ChiTietHoaDon[] chiTiet = chungTu.getChiTiet();
        for (int i = 0; i < chiTiet.length; i++) {
            ketQua += "\n  " + chiTiet[i].getSanPham().getMaSanPham() + " x " + chiTiet[i].getSoLuong()
                    + " @ " + chiTiet[i].getDonGiaBan().toPlainString()
                    + " = " + chiTiet[i].tinhThanhTien().toPlainString();
        }
        if (chiTiet.length == 0) {
            ketQua += " (ban nhap chua co chi tiet)";
        }
        return ketQua;
    }

    private PhieuNhap nhapPhieuNhap(String ma) {
        PhieuNhap cu = ma == null ? null : phieuNhap.timTheoMa(ma);
        if (ma == null) {
            ma = docMa("Ma chung tu: ");
        }
        LocalDate ngay = docNgay("Ngay lap yyyy-MM-dd: ");
        NhaCungCap doiTac = nhaCungCap.timTheoMa(docMa("Ma NhaCungCap: "));
        NhanVien nv = nhanVien.timTheoMa(docMa("Ma nhan vien: "));
        PhieuNhap moi = new PhieuNhap(ma, ngay, doiTac, nv);
        if (cu != null) {
            ChiTietPhieuNhap[] chiTiet = cu.getChiTiet();
            for (int i = 0; i < chiTiet.length; i++) {
                moi.themChiTiet(chiTiet[i]);
            }
        }
        menuChiTietPhieuNhap(moi);
        return moi;
    }

    private void menuChiTietPhieuNhap(PhieuNhap moi) {
        int chon;
        do {
            System.out.println("1. Them dong  2. Sua dong  3. Xoa dong  4. Xem  0. Hoan tat");
            chon = docSoNguyen("Chi tiet: ", 0);
            try {
                xuLyChiTietPhieuNhap(moi, chon);
            } catch (IllegalArgumentException e) {
                System.out.println("Chi tiet khong hop le: " + e.getMessage());
            }
        } while (chon != 0);
    }

    private void xuLyChiTietPhieuNhap(PhieuNhap moi, int chon) {
        switch (chon) {
            case 1: moi.themChiTiet(nhapChiTietPhieuNhap()); break;
            case 2:
                String maSP = docMa("Ma SP can sua: ");
                System.out.println(moi.suaChiTiet(maSP, nhapChiTietPhieuNhap()) ? "Da sua dong." : "Khong tim thay.");
                break;
            case 3: System.out.println(moi.xoaChiTiet(docMa("Ma SP can xoa: ")) ? "Da xoa dong." : "Khong tim thay."); break;
            case 4: System.out.println(hienThiPhieuNhap(moi)); break;
            case 0: break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private ChiTietPhieuNhap nhapChiTietPhieuNhap() {
        SanPham sp = sanPham.timTheoMa(docMa("Ma SP: "));
        if (sp == null) {
            throw new IllegalArgumentException("San pham khong ton tai.");
        }
        System.out.println(sp.hienThiThongTin());
        return new ChiTietPhieuNhap(sp, docSoNguyen("So luong: ", 1), docTien("DonGiaNhap: "));
    }

    private String hienThiPhieuNhap(PhieuNhap chungTu) {
        String ketQua = chungTu.hienThiThongTin();
        ChiTietPhieuNhap[] chiTiet = chungTu.getChiTiet();
        for (int i = 0; i < chiTiet.length; i++) {
            ketQua += "\n  " + chiTiet[i].getSanPham().getMaSanPham() + " x " + chiTiet[i].getSoLuong()
                    + " @ " + chiTiet[i].getDonGiaNhap().toPlainString()
                    + " = " + chiTiet[i].tinhThanhTien().toPlainString();
        }
        if (chiTiet.length == 0) {
            ketQua += " (ban nhap chua co chi tiet)";
        }
        return ketQua;
    }

    private void menuThongKe() {
        int chon = docSoNguyen("1. Tat ca  2. Theo khoang ngay: ", 1);
        if (chon == 1) {
            System.out.println("Tong tien ban: " + thongKeTienBan().toPlainString());
            System.out.println("Tong tien nhap: " + thongKeTienNhap().toPlainString());
        } else if (chon == 2) {
            LocalDate tu = docNgay("Tu ngay yyyy-MM-dd: ");
            LocalDate den = docNgay("Den ngay yyyy-MM-dd: ");
            System.out.println("Tong tien ban: " + thongKeTienBan(tu, den).toPlainString());
            System.out.println("Tong tien nhap: " + thongKeTienNhap(tu, den).toPlainString());
        } else {
            System.out.println("Lua chon khong hop le.");
        }
    }

    private String docDong(String loiNhac) {
        while (true) {
            System.out.print(loiNhac);
            String giaTri = scanner.nextLine().trim();
            if (!giaTri.contains("|") && !giaTri.contains("\t")) {
                return giaTri;
            }
            System.out.println("Khong dung dau | hoac tab trong du lieu.");
        }
    }

    private String docChuoi(String loiNhac) {
        String giaTri;
        do {
            giaTri = docDong(loiNhac);
            if (giaTri.isEmpty()) {
                System.out.println("Thong tin khong duoc rong.");
            }
        } while (giaTri.isEmpty());
        return giaTri;
    }

    private String docMa(String loiNhac) {
        while (true) {
            String ma = docDong(loiNhac);
            if (ma.matches("[A-Za-z0-9_-]+")) {
                return ma.toUpperCase(Locale.ROOT);
            }
            System.out.println("Ma chi gom chu, so, _ hoac -.");
        }
    }

    private String docDienThoai() {
        while (true) {
            String so = docDong("So dien thoai: ");
            if (so.matches("\\+?[0-9]{9,15}")) {
                return so;
            }
            System.out.println("So dien thoai phai co 9-15 chu so.");
        }
    }

    private int docSoNguyen(String loiNhac, int nhoNhat) {
        while (true) {
            try {
                int so = Integer.parseInt(docDong(loiNhac));
                if (so >= nhoNhat) {
                    return so;
                }
            } catch (NumberFormatException e) {
                System.out.println("Hay nhap so nguyen.");
            }
            System.out.println("Gia tri phai >= " + nhoNhat);
        }
    }

    private double docSoDuong(String loiNhac) {
        while (true) {
            try {
                double so = Double.parseDouble(docDong(loiNhac));
                if (Double.isFinite(so) && so > 0) {
                    return so;
                }
            } catch (NumberFormatException e) {
                System.out.println("Hay nhap so thuc.");
            }
            System.out.println("Gia tri phai > 0.");
        }
    }

    private BigDecimal docTien(String loiNhac) {
        while (true) {
            try {
                BigDecimal tien = new BigDecimal(docDong(loiNhac));
                if (tien.signum() >= 0) {
                    return tien;
                }
            } catch (NumberFormatException e) {
                System.out.println("Hay nhap so tien.");
            }
            System.out.println("So tien phai >= 0.");
        }
    }

    private LocalDate docNgay(String loiNhac) {
        while (true) {
            try {
                return LocalDate.parse(docDong(loiNhac));
            } catch (DateTimeParseException e) {
                System.out.println("Ngay khong hop le; nhap yyyy-MM-dd.");
            }
        }
    }
}
