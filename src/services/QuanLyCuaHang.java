package services;

import java.math.BigDecimal;
import java.time.LocalDate;
import models.*;

/** Ranh giới nghiệp vụ: UI chỉ thay đổi dữ liệu qua lớp này. */
public final class QuanLyCuaHang {
    private DanhSachLoaiSP loaiSP = new DanhSachLoaiSP();
    private DanhSachSanPham sanPham = new DanhSachSanPham();
    private DanhSachKhachHang khachHang = new DanhSachKhachHang();
    private DanhSachNhanVien nhanVien = new DanhSachNhanVien();
    private DanhSachNhaCungCap nhaCungCap = new DanhSachNhaCungCap();
    private DanhSachHoaDon hoaDon = new DanhSachHoaDon();
    private DanhSachPhieuNhap phieuNhap = new DanhSachPhieuNhap();
    private Kho kho = new Kho(sanPham);

    public QuanLyCuaHang() {
    }

    public DanhSachLoaiSP getLoaiSP() {
        return new DanhSachLoaiSP(loaiSP.getTatCa());
    }

    public DanhSachSanPham getSanPham() {
        return new DanhSachSanPham(sanPham.getTatCa());
    }

    public DanhSachKhachHang getKhachHang() {
        return new DanhSachKhachHang(khachHang.getTatCa());
    }

    public DanhSachNhanVien getNhanVien() {
        return new DanhSachNhanVien(nhanVien.getTatCa());
    }

    public DanhSachNhaCungCap getNhaCungCap() {
        return new DanhSachNhaCungCap(nhaCungCap.getTatCa());
    }

    public DanhSachHoaDon getHoaDon() {
        return new DanhSachHoaDon(hoaDon.getTatCa());
    }

    public DanhSachPhieuNhap getPhieuNhap() {
        return new DanhSachPhieuNhap(phieuNhap.getTatCa());
    }

    public int traCuuTon(String maSP) {
        return kho.traCuuTon(maSP);
    }

    public boolean themLoaiSP(LoaiSP moi) {
        return loaiSP.them(moi);
    }

    public boolean suaLoaiSP(String ma, LoaiSP moi) {
        // Làm trên hai bản sao để đổi loại và cập nhật sản phẩm cùng một lần.
        DanhSachLoaiSP loaiMoi = getLoaiSP();
        if (!loaiMoi.sua(ma, moi)) {
            return false;
        }
        DanhSachSanPham spMoi = getSanPham();
        for (SanPham sp : spMoi.locTheoLoaiSP(ma)) {
            sp.setLoaiSP(moi);
            spMoi.sua(sp.getMaSanPham(), sp);
        }
        loaiSP = loaiMoi;
        sanPham = spMoi;
        kho = new Kho(sanPham);
        return true;
    }

    public boolean xoaLoaiSP(String ma) {
        if (sanPham.locTheoLoaiSP(ma).length > 0) {
            throw new IllegalArgumentException("Loai san pham dang duoc su dung.");
        }
        return loaiSP.xoa(ma);
    }

    public boolean themSanPham(SanPham moi) {
        KiemTra.khacNull(moi, "San pham");
        if (moi.getSoLuongTon() != 0) {
            throw new IllegalArgumentException("San pham moi phai co ton = 0; hay lap phieu nhap.");
        }
        return sanPham.them(chuanHoaSanPham(moi));
    }

    public void themNhieuSanPham(SanPham[] moi) {
        KiemTra.khacNull(moi, "Mang san pham");
        DanhSachSanPham banMoi = getSanPham();
        for (SanPham sp : moi) {
            KiemTra.khacNull(sp, "San pham");
            if (sp.getSoLuongTon() != 0) {
                throw new IllegalArgumentException("San pham moi phai co ton = 0.");
            }
            banMoi.them(chuanHoaSanPham(sp));
        }
        sanPham = banMoi;
        kho = new Kho(sanPham);
    }

    public boolean suaSanPham(String ma, SanPham moi) {
        return sanPham.sua(ma, chuanHoaSanPham(moi));
    }

    private SanPham chuanHoaSanPham(SanPham sp) {
        SanPham moi = KiemTra.khacNull(sp, "San pham").saoChep();
        LoaiSP loai = batBuocTonTai(loaiSP.timTheoMa(moi.getLoaiSP().getMaLoaiSP()));
        moi.setLoaiSP(loai);
        return moi;
    }

    public boolean xoaSanPham(String ma) {
        SanPham cu = sanPham.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        if (cu.getSoLuongTon() != 0 || sanPhamDuocThamChieu(ma)) {
            throw new IllegalArgumentException("San pham con ton hoac dang duoc chung tu tham chieu.");
        }
        return sanPham.xoa(ma);
    }

    private boolean sanPhamDuocThamChieu(String ma) {
        for (HoaDon hd : hoaDon.getTatCa()) {
            if (hd.timChiTiet(ma) != null) {
                return true;
            }
        }
        for (PhieuNhap pn : phieuNhap.getTatCa()) {
            if (pn.timChiTiet(ma) != null) {
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
        for (HoaDon hd : hoaDon.getTatCa()) {
            if (hd.getKhachHang().getMaKhachHang().equals(KiemTra.ma(ma))) {
                throw new IllegalArgumentException("Khach hang dang duoc hoa don tham chieu.");
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
        String maChuan = KiemTra.ma(ma);
        for (HoaDon hd : hoaDon.getTatCa()) {
            if (hd.getNhanVien().getMaNhanVien().equals(maChuan)) {
                throw new IllegalArgumentException("Nhan vien dang duoc hoa don tham chieu.");
            }
        }
        for (PhieuNhap pn : phieuNhap.getTatCa()) {
            if (pn.getNhanVien().getMaNhanVien().equals(maChuan)) {
                throw new IllegalArgumentException("Nhan vien dang duoc phieu nhap tham chieu.");
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
        for (PhieuNhap pn : phieuNhap.getTatCa()) {
            if (pn.getNhaCungCap().getMaNhaCungCap().equals(KiemTra.ma(ma))) {
                throw new IllegalArgumentException("Nha cung cap dang duoc phieu nhap tham chieu.");
            }
        }
        return nhaCungCap.xoa(ma);
    }

    public void themHoaDon(HoaDon moi) {
        HoaDon hopLe = chuanHoaHoaDon(moi, true);
        DanhSachHoaDon danhSachMoi = getHoaDon();
        danhSachMoi.them(hopLe);
        kho.capNhatTheoHoaDon(null, hopLe);
        hoaDon = danhSachMoi;
    }

    public boolean suaHoaDon(String ma, HoaDon moi) {
        HoaDon cu = hoaDon.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        HoaDon hopLe = chuanHoaHoaDon(moi, true);
        DanhSachHoaDon danhSachMoi = getHoaDon();
        danhSachMoi.sua(ma, hopLe);
        kho.capNhatTheoHoaDon(cu, hopLe);
        hoaDon = danhSachMoi;
        return true;
    }

    public boolean xoaHoaDon(String ma) {
        HoaDon cu = hoaDon.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        DanhSachHoaDon danhSachMoi = getHoaDon();
        danhSachMoi.xoa(ma);
        kho.capNhatTheoHoaDon(cu, null);
        hoaDon = danhSachMoi;
        return true;
    }

    public void themPhieuNhap(PhieuNhap moi) {
        PhieuNhap hopLe = chuanHoaPhieuNhap(moi, true);
        DanhSachPhieuNhap danhSachMoi = getPhieuNhap();
        danhSachMoi.them(hopLe);
        kho.capNhatTheoPhieuNhap(null, hopLe);
        phieuNhap = danhSachMoi;
    }

    public boolean suaPhieuNhap(String ma, PhieuNhap moi) {
        PhieuNhap cu = phieuNhap.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        PhieuNhap hopLe = chuanHoaPhieuNhap(moi, true);
        DanhSachPhieuNhap danhSachMoi = getPhieuNhap();
        danhSachMoi.sua(ma, hopLe);
        kho.capNhatTheoPhieuNhap(cu, hopLe);
        phieuNhap = danhSachMoi;
        return true;
    }

    public boolean xoaPhieuNhap(String ma) {
        PhieuNhap cu = phieuNhap.timTheoMa(ma);
        if (cu == null) {
            return false;
        }
        DanhSachPhieuNhap danhSachMoi = getPhieuNhap();
        danhSachMoi.xoa(ma);
        kho.capNhatTheoPhieuNhap(cu, null);
        phieuNhap = danhSachMoi;
        return true;
    }

    private HoaDon chuanHoaHoaDon(HoaDon hd, boolean canChiTiet) {
        hd = KiemTra.khacNull(hd, "Hoa don").saoChep();
        kiemTraChiTiet(hd.getSoLuongChiTiet(), canChiTiet);
        KhachHang kh = batBuocTonTai(khachHang.timTheoMa(hd.getKhachHang().getMaKhachHang()));
        NhanVien nv = batBuocTonTai(nhanVien.timTheoMa(hd.getNhanVien().getMaNhanVien()));
        HoaDon moi = new HoaDon(hd.getMaHoaDon(), hd.getNgayLap(), kh, nv);
        for (ChiTietHoaDon dong : hd.getChiTiet()) {
            SanPham sp = batBuocTonTai(sanPham.timTheoMa(dong.getSanPham().getMaSanPham()));
            moi.themChiTiet(new ChiTietHoaDon(sp, dong.getSoLuong(), dong.getDonGiaBan()));
        }
        return moi;
    }

    private PhieuNhap chuanHoaPhieuNhap(PhieuNhap pn, boolean canChiTiet) {
        pn = KiemTra.khacNull(pn, "Phieu nhap").saoChep();
        kiemTraChiTiet(pn.getSoLuongChiTiet(), canChiTiet);
        NhaCungCap ncc = batBuocTonTai(nhaCungCap.timTheoMa(pn.getNhaCungCap().getMaNhaCungCap()));
        NhanVien nv = batBuocTonTai(nhanVien.timTheoMa(pn.getNhanVien().getMaNhanVien()));
        PhieuNhap moi = new PhieuNhap(pn.getMaPhieuNhap(), pn.getNgayLap(), ncc, nv);
        for (ChiTietPhieuNhap dong : pn.getChiTiet()) {
            SanPham sp = batBuocTonTai(sanPham.timTheoMa(dong.getSanPham().getMaSanPham()));
            moi.themChiTiet(new ChiTietPhieuNhap(sp, dong.getSoLuong(), dong.getDonGiaNhap()));
        }
        return moi;
    }

    private static void kiemTraChiTiet(int soLuong, boolean batBuoc) {
        if (batBuoc && soLuong == 0) {
            throw new IllegalArgumentException("Chung tu phai co it nhat mot dong chi tiet.");
        }
    }

    private static <T> T batBuocTonTai(T doiTuong) {
        if (doiTuong == null) {
            throw new IllegalArgumentException("Ma tham chieu khong ton tai.");
        }
        return doiTuong;
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

    // Chỉ lớp lưu trữ cùng package gọi khi khôi phục; không phát sinh giao dịch kho.
    void khoiPhucSanPham(SanPham sp) {
        sanPham.them(chuanHoaSanPham(sp));
    }

    void khoiPhucHoaDon(HoaDon hd) {
        hoaDon.them(chuanHoaHoaDon(hd, false));
    }

    void khoiPhucPhieuNhap(PhieuNhap pn) {
        phieuNhap.them(chuanHoaPhieuNhap(pn, false));
    }
}
