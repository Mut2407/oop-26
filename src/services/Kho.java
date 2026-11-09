package services;

import models.ChiTietHoaDon;
import models.ChiTietPhieuNhap;
import models.HoaDon;
import models.PhieuNhap;
import models.SanPham;

public class Kho {
    private DanhSachSanPham danhSachSanPham;

    public Kho() {
    }

    public Kho(DanhSachSanPham danhSachSanPham) {
        setDanhSachSanPham(danhSachSanPham);
    }

    public void setDanhSachSanPham(DanhSachSanPham danhSachSanPham) {
        if (danhSachSanPham == null) {
            throw new IllegalArgumentException("Danh sach san pham khong duoc null.");
        }
        this.danhSachSanPham = danhSachSanPham;
    }

    public int traCuuTon(String maSP) {
        kiemTraDanhSach();
        SanPham sp = danhSachSanPham.timTheoMa(maSP);
        if (sp == null) {
            throw new IllegalArgumentException("Khong tim thay san pham: " + maSP);
        }
        return sp.getSoLuongTon();
    }

    public void capNhatTheoHoaDon(HoaDon cu, HoaDon moi) {
        kiemTraDanhSach();
        if (cu == null && moi == null) {
            throw new IllegalArgumentException("Chua co hoa don de cap nhat.");
        }
        SanPham[] ds = danhSachSanPham.getTatCa();
        int[] chenhLech = new int[ds.length];
        for (int i = 0; i < ds.length; i++) {
            String ma = ds[i].getMaSanPham();
            chenhLech[i] = soLuongHoaDon(cu, ma) - soLuongHoaDon(moi, ma);
        }
        kiemTraVaCapNhat(ds, chenhLech);
    }

    public void capNhatTheoPhieuNhap(PhieuNhap cu, PhieuNhap moi) {
        kiemTraDanhSach();
        if (cu == null && moi == null) {
            throw new IllegalArgumentException("Chua co phieu nhap de cap nhat.");
        }
        SanPham[] ds = danhSachSanPham.getTatCa();
        int[] chenhLech = new int[ds.length];
        for (int i = 0; i < ds.length; i++) {
            String ma = ds[i].getMaSanPham();
            chenhLech[i] = soLuongPhieuNhap(moi, ma) - soLuongPhieuNhap(cu, ma);
        }
        kiemTraVaCapNhat(ds, chenhLech);
    }

    private int soLuongHoaDon(HoaDon hd, String maSP) {
        if (hd == null) {
            return 0;
        }
        ChiTietHoaDon dong = hd.timChiTiet(maSP);
        if (dong == null) {
            return 0;
        }
        return dong.getSoLuong();
    }

    private int soLuongPhieuNhap(PhieuNhap pn, String maSP) {
        if (pn == null) {
            return 0;
        }
        ChiTietPhieuNhap dong = pn.timChiTiet(maSP);
        if (dong == null) {
            return 0;
        }
        return dong.getSoLuong();
    }

    private void kiemTraVaCapNhat(SanPham[] ds, int[] chenhLech) {
        // Vong 1 chi kiem tra, chua thay doi ton cua bat ky san pham nao.
        for (int i = 0; i < ds.length; i++) {
            long tonMoi = (long) ds[i].getSoLuongTon() + chenhLech[i];
            if (tonMoi < 0 || tonMoi > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Ton kho khong hop le: " + ds[i].getMaSanPham());
            }
        }
        // Chi khi tat ca hop le moi chay vong 2 de cap nhat.
        for (int i = 0; i < ds.length; i++) {
            ds[i].capNhatTon(chenhLech[i]);
        }
    }

    private void kiemTraDanhSach() {
        if (danhSachSanPham == null) {
            throw new IllegalArgumentException("Kho chua duoc gan danh sach san pham.");
        }
    }
}
