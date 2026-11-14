package services;

import models.*;
import java.util.ArrayList;

public class DanhSachLoaiSP implements IQuanLyDanhSach<LoaiSP> {
    private ArrayList<LoaiSP> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public LoaiSP[] getTatCa() {
        return ds.toArray(new LoaiSP[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public LoaiSP timTheoMa(String ma) {
        if (ma == null) return null;
        for (LoaiSP loai : ds)
            if (loai.getMaLoaiSP().equalsIgnoreCase(ma.trim()))
                return loai;
        return null;
    }

    @Override
    public boolean them(LoaiSP doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaLoaiSP())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(LoaiSP[] cacDoiTuong) {
        int daThem = 0;
        for (LoaiSP loai : cacDoiTuong)
            if (them(loai)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, LoaiSP moi) {
        LoaiSP cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaLoaiSP().equalsIgnoreCase(moi.getMaLoaiSP())) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        LoaiSP loai = timTheoMa(ma);
        if (loai == null) return false;
        return ds.remove(loai);
    }

    @Override
    public LoaiSP[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<LoaiSP> kq = new ArrayList<>();
        for (LoaiSP loai : ds)
            if (phuHop(loai, key)) kq.add(loai);
        return kq.toArray(new LoaiSP[0]);
    }

    private boolean phuHop(LoaiSP x, String key) {
        return x.getMaLoaiSP().toLowerCase().contains(key)
                || x.getTenLoaiSP().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (LoaiSP loai : ds)
            System.out.println(loai.hienThiThongTin());
    }

}
