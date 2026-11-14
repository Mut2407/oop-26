package services;

import models.*;
import java.util.Arrays;

public class DanhSachLoaiSP implements IQuanLyDanhSach<LoaiSP> {
    private LoaiSP[] ds;
    private int soLuong;
    private static final int SUC_CHUA_MAC_DINH = 10;

    public DanhSachLoaiSP() {
        this(SUC_CHUA_MAC_DINH);
    }

    public DanhSachLoaiSP(int sucChua) {
        if (sucChua <= 0)
            throw new IllegalArgumentException("Suc chua phai > 0.");
        ds = new LoaiSP[sucChua];
    }

    public int getSoLuong() {
        return soLuong;
    }

    public LoaiSP[] getTatCa() {
        LoaiSP[] kq = new LoaiSP[soLuong];
        for (int i = 0; i < soLuong; i++)
            kq[i] = ds[i];
        return kq;
    }

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getMaLoaiSP().equalsIgnoreCase(ma.trim()))
                return i;
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public LoaiSP timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return ds[viTri];
    }

    private void moRongMang() {
        ds = Arrays.copyOf(ds, ds.length + SUC_CHUA_MAC_DINH);
    }

    @Override
    public boolean them(LoaiSP doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaLoaiSP()))
            return false;
        if (soLuong == ds.length)
            moRongMang();
        ds[soLuong++] = doiTuong;
        return true;
    }

    public int themNhieu(LoaiSP[] cacDoiTuong) {
        int daThem = 0;
        for (int i = 0; i < cacDoiTuong.length; i++)
            if (them(cacDoiTuong[i]))
                daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, LoaiSP moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds[viTri].getMaLoaiSP().equalsIgnoreCase(moi.getMaLoaiSP()))
            return false;
        ds[viTri].capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return false;
        for (int i = viTri; i < soLuong - 1; i++)
            ds[i] = ds[i + 1];
        ds[--soLuong] = null;
        return true;
    }

    @Override
    public LoaiSP[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        LoaiSP[] kq = new LoaiSP[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (phuHop(ds[i], key))
                kq[dem++] = ds[i];
        return Arrays.copyOf(kq, dem);
    }

    private boolean phuHop(LoaiSP x, String key) {
        return x.getMaLoaiSP().toLowerCase().contains(key)
                || x.getTenLoaiSP().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        for (int i = 0; i < soLuong; i++)
            System.out.println(ds[i].hienThiThongTin());
        if (soLuong == 0)
            System.out.println("Danh sach rong.");
    }

}
