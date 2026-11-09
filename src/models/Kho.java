package models;

import services.DanhSachSanPham;

// Kho dung chung danh sach san pham; khong luu mot mang ton rieng.
public class Kho {
    private DanhSachSanPham danhSachSanPham;

    //không tham số
    public Kho() {

    }
    //có tham số
    public Kho(DanhSachSanPham danhSachSanPham) {
        setDanhSachSanPham(danhSachSanPham);
    }

    public void setDanhSachSanPham(DanhSachSanPham danhSachSanPham) {
        if (danhSachSanPham == null)
            throw new IllegalArgumentException("Danh sach SP khong duoc rong.");
        this.danhSachSanPham = danhSachSanPham;
    }

    public int traCuuTon(String maSP) {
        if (danhSachSanPham == null)
            return -1;
        SanPham sp = danhSachSanPham.timTheoMa(maSP);
        return sp == null ? -1 : sp.getSoLuongTon();
    }

    private int soLuong(HoaDon phieu, String ma) {
        if (phieu == null)
            return 0;
        ChiTietHoaDon ct = phieu.timChiTiet(ma);
        return ct == null ? 0 : ct.getSoLuong();
    }

    private boolean dungSanPham(HoaDon phieu) {
        if (phieu == null)
            return true;
        ChiTietHoaDon[] ds = phieu.getChiTiet();
        for (int i = 0; i < ds.length; i++)
            if (danhSachSanPham.timTheoMa(ds[i].getSanPham().getMaSanPham()) != ds[i].getSanPham())
                return false;
        return true;
    }

    private int[] tinhChenhLech(HoaDon cu, HoaDon moi) {
        SanPham[] ds = danhSachSanPham.getTatCa();
        int[] chenhLech = new int[ds.length];
        for (int i = 0; i < ds.length; i++) {
            String ma = ds[i].getMaSanPham();
            chenhLech[i] = soLuong(cu, ma) - soLuong(moi, ma);
        }
        return chenhLech;
    }

    public boolean capNhatTheoHoaDon(HoaDon cu, HoaDon moi) {
        if (danhSachSanPham == null || (cu == null && moi == null))
            return false;
        if (!dungSanPham(cu) || !dungSanPham(moi))
            return false;
        int[] chenhLech = tinhChenhLech(cu, moi);
        if (!duTon(chenhLech))
            return false;
        apDung(chenhLech);
        return true;
    }

    private int soLuong(PhieuNhap phieu, String ma) {
        if (phieu == null)
            return 0;
        ChiTietPhieuNhap ct = phieu.timChiTiet(ma);
        return ct == null ? 0 : ct.getSoLuong();
    }

    private boolean dungSanPham(PhieuNhap phieu) {
        if (phieu == null)
            return true;
        ChiTietPhieuNhap[] ds = phieu.getChiTiet();
        for (int i = 0; i < ds.length; i++)
            if (danhSachSanPham.timTheoMa(ds[i].getSanPham().getMaSanPham()) != ds[i].getSanPham())
                return false;
        return true;
    }

    private int[] tinhChenhLech(PhieuNhap cu, PhieuNhap moi) {
        SanPham[] ds = danhSachSanPham.getTatCa();
        int[] chenhLech = new int[ds.length];
        for (int i = 0; i < ds.length; i++) {
            String ma = ds[i].getMaSanPham();
            chenhLech[i] = soLuong(moi, ma) - soLuong(cu, ma);
        }
        return chenhLech;
    }

    public boolean capNhatTheoPhieuNhap(PhieuNhap cu, PhieuNhap moi) {
        if (danhSachSanPham == null || (cu == null && moi == null))
            return false;
        if (!dungSanPham(cu) || !dungSanPham(moi))
            return false;
        int[] chenhLech = tinhChenhLech(cu, moi);
        if (!duTon(chenhLech))
            return false;
        apDung(chenhLech);
        return true;
    }

    private boolean duTon(int[] chenhLech) {
        SanPham[] ds = danhSachSanPham.getTatCa();
        for (int i = 0; i < ds.length; i++) {
            long tonMoi = (long) ds[i].getSoLuongTon() + chenhLech[i];
            if (tonMoi < 0 || tonMoi > Integer.MAX_VALUE)
                return false;
        }
        return true;
    }

    private void apDung(int[] chenhLech) {
        SanPham[] ds = danhSachSanPham.getTatCa();
        for (int i = 0; i < ds.length; i++)
            ds[i].capNhatTon(chenhLech[i]);
    }

}
