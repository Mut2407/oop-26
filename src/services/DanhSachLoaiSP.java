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

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        String maCanTim = ma.trim();
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaLoaiSP().equalsIgnoreCase(maCanTim))
                return i;
        }
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public LoaiSP timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return ds.get(viTri);
    }

    @Override
    public boolean them(LoaiSP doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaLoaiSP()))
            return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(LoaiSP[] cacDoiTuong) {
        if (cacDoiTuong == null)
            return 0;
        int daThem = 0;
        for (LoaiSP loaiSP : cacDoiTuong) {
            if (them(loaiSP))
                daThem++;
        }
        return daThem;
    }

    @Override
    public boolean sua(String ma, LoaiSP moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds.get(viTri).getMaLoaiSP().equalsIgnoreCase(moi.getMaLoaiSP()))
            return false;
        ds.get(viTri).capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return false;
        ds.remove(viTri);
        return true;
    }

    @Override
    public LoaiSP[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<LoaiSP> kq = new ArrayList<>();
        for (LoaiSP x : ds) {
            if (phuHop(x, key))
                kq.add(x);
        }
        return kq.toArray(new LoaiSP[0]);
    }

    private boolean phuHop(LoaiSP x, String key) {
        return x.getMaLoaiSP().toLowerCase().contains(key)
                || x.getTenLoaiSP().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) {
            System.out.println("Danh sach rong.");
            return;
        }
        for (LoaiSP x : ds) {
            System.out.println(x.hienThiThongTin());
        }
    }
}