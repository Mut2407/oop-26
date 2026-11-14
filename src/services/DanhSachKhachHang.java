package services;

import models.*;
import java.util.ArrayList;

public class DanhSachKhachHang implements IQuanLyDanhSach<KhachHang> {
    private ArrayList<KhachHang> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public KhachHang[] getTatCa() {
        return ds.toArray(new KhachHang[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public KhachHang timTheoMa(String ma) {
        if (ma == null) return null;
        for (KhachHang kh : ds)
            if (kh.getMaKhachHang().equalsIgnoreCase(ma.trim()))
                return kh;
        return null;
    }

    @Override
    public boolean them(KhachHang doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaKhachHang())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(KhachHang[] cacDoiTuong) {
        int daThem = 0;
        for (KhachHang kh : cacDoiTuong)
            if (them(kh)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, KhachHang moi) {
        KhachHang cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaKhachHang().equalsIgnoreCase(moi.getMaKhachHang())) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        KhachHang kh = timTheoMa(ma);
        if (kh == null) return false;
        return ds.remove(kh);
    }

    @Override
    public KhachHang[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<KhachHang> kq = new ArrayList<>();
        for (KhachHang kh : ds)
            if (phuHop(kh, key)) kq.add(kh);
        return kq.toArray(new KhachHang[0]);
    }

    private boolean phuHop(KhachHang x, String key) {
        return x.getMaKhachHang().toLowerCase().contains(key)
                || x.getHoTen().toLowerCase().contains(key)
                || x.getSoDienThoai().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (KhachHang kh : ds)
            System.out.println(kh.hienThiThongTin());
    }
}