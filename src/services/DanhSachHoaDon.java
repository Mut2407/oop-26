package services;

import models.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DanhSachHoaDon implements IQuanLyDanhSach<HoaDon> {
    private ArrayList<HoaDon> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public HoaDon[] getTatCa() {
        HoaDon[] kq = new HoaDon[ds.size()];
        for (int i = 0; i < ds.size(); i++)
            kq[i] = new HoaDon(ds.get(i));
        return kq;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public HoaDon timTheoMa(String ma) {
        if (ma == null) return null;
        for (HoaDon hd : ds)
            if (hd.getMaHoaDon().equalsIgnoreCase(ma.trim()))
                return new HoaDon(hd);
        return null;
    }

    @Override
    public boolean them(HoaDon doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaHoaDon())) return false;
        return ds.add(new HoaDon(doiTuong));
    }

    public int themNhieu(HoaDon[] cacDoiTuong) {
        int daThem = 0;
        for (HoaDon hd : cacDoiTuong)
            if (them(hd)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, HoaDon moi) {
        if (moi == null || !moi.kiemTraHopLe()) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaHoaDon().equalsIgnoreCase(ma == null ? "" : ma.trim())) {
                if (!ds.get(i).getMaHoaDon().equalsIgnoreCase(moi.getMaHoaDon())) return false;
                ds.set(i, new HoaDon(moi));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean xoa(String ma) {
        if (ma == null) return false;
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaHoaDon().equalsIgnoreCase(ma.trim())) {
                ds.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public HoaDon[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<HoaDon> kq = new ArrayList<>();
        for (HoaDon hd : ds)
            if (phuHop(hd, key)) kq.add(new HoaDon(hd));
        return kq.toArray(new HoaDon[0]);
    }

    private boolean phuHop(HoaDon x, String key) {
        return x.getMaHoaDon().toLowerCase().contains(key)
                || x.getKhachHang().getMaKhachHang().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (HoaDon hd : ds)
            System.out.println(hd.hienThiThongTin());
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (HoaDon hd : ds)
            tong = tong.add(hd.tinhTongTien());
        return tong;
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den))
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        BigDecimal tong = BigDecimal.ZERO;
        for (HoaDon hd : ds)
            if (!hd.getNgayLap().isBefore(tu) && !hd.getNgayLap().isAfter(den))
                tong = tong.add(hd.tinhTongTien());
        return tong;
    }

}
