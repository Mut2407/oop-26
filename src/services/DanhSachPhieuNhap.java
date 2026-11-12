package services;

import models.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DanhSachPhieuNhap implements IQuanLyDanhSach<PhieuNhap> {
    private ArrayList<PhieuNhap> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public PhieuNhap[] getTatCa() {
        PhieuNhap[] kq = new PhieuNhap[ds.size()];
        for (int i = 0; i < ds.size(); i++)
            kq[i] = new PhieuNhap(ds.get(i));
        return kq;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public PhieuNhap timTheoMa(String ma) {
        if (ma == null) return null;
        for (PhieuNhap pn : ds)
            if (pn.getMaPhieuNhap().equalsIgnoreCase(ma.trim()))
                return new PhieuNhap(pn);
        return null;
    }

    @Override
    public boolean them(PhieuNhap doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaPhieuNhap())) return false;
        return ds.add(new PhieuNhap(doiTuong));
    }

    public int themNhieu(PhieuNhap[] cacDoiTuong) {
        int daThem = 0;
        for (PhieuNhap pn : cacDoiTuong)
            if (them(pn)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, PhieuNhap moi) {
        if (moi == null || !moi.kiemTraHopLe()) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaPhieuNhap().equalsIgnoreCase(ma == null ? "" : ma.trim())) {
                if (!ds.get(i).getMaPhieuNhap().equalsIgnoreCase(moi.getMaPhieuNhap())) return false;
                ds.set(i, new PhieuNhap(moi));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean xoa(String ma) {
        if (ma == null) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaPhieuNhap().equalsIgnoreCase(ma.trim())) {
                ds.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public PhieuNhap[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<PhieuNhap> kq = new ArrayList<>();
        for (PhieuNhap pn : ds)
            if (phuHop(pn, key)) kq.add(new PhieuNhap(pn));
        return kq.toArray(new PhieuNhap[0]);
    }

    private boolean phuHop(PhieuNhap x, String key) {
        return x.getMaPhieuNhap().toLowerCase().contains(key)
                || x.getNhaCungCap().getMaNhaCungCap().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (PhieuNhap pn : ds)
            System.out.println(pn.hienThiThongTin());
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (PhieuNhap pn : ds)
            tong = tong.add(pn.tinhTongTien());
        return tong;
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den))
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        BigDecimal tong = BigDecimal.ZERO;
        for (PhieuNhap pn : ds)
            if (!pn.getNgayLap().isBefore(tu) && !pn.getNgayLap().isAfter(den))
                tong = tong.add(pn.tinhTongTien());
        return tong;
    }
}