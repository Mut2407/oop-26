package services;

import models.*;
import java.util.ArrayList;

public class DanhSachNhanVien implements IQuanLyDanhSach<NhanVien> {
    private ArrayList<NhanVien> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public NhanVien[] getTatCa() {
        return ds.toArray(new NhanVien[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public NhanVien timTheoMa(String ma) {
        if (ma == null) return null;
        for (NhanVien nv : ds)
            if (nv.getMaNhanVien().equalsIgnoreCase(ma.trim()))
                return nv;
        return null;
    }

    @Override
    public boolean them(NhanVien doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaNhanVien())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(NhanVien[] cacDoiTuong) {
        int daThem = 0;
        for (NhanVien nv : cacDoiTuong)
            if (them(nv)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, NhanVien moi) {
        NhanVien cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaNhanVien().equalsIgnoreCase(moi.getMaNhanVien())) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        NhanVien nv = timTheoMa(ma);
        if (nv == null) return false;
        return ds.remove(nv);
    }

    @Override
    public NhanVien[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<NhanVien> kq = new ArrayList<>();
        for (NhanVien nv : ds)
            if (phuHop(nv, key)) kq.add(nv);
        return kq.toArray(new NhanVien[0]);
    }

    private boolean phuHop(NhanVien x, String key) {
        return x.getMaNhanVien().toLowerCase().contains(key)
                || x.getHoTen().toLowerCase().contains(key)
                || x.getSoDienThoai().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (NhanVien nv : ds)
            System.out.println(nv.hienThiThongTin());
    }

}
