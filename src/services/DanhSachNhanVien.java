package services;

import models.*;
import java.util.Arrays;

public class DanhSachNhanVien implements IQuanLyDanhSach<NhanVien> {
    private NhanVien[] ds;
    private int soLuong;
    private static final int SUC_CHUA_MAC_DINH = 10;

    public DanhSachNhanVien() {
        this(SUC_CHUA_MAC_DINH);
    }

    public DanhSachNhanVien(int sucChua) {
        if (sucChua <= 0)
            throw new IllegalArgumentException("Suc chua phai > 0.");
        ds = new NhanVien[sucChua];
    }

    public int getSoLuong() {
        return soLuong;
    }

    public NhanVien[] getTatCa() {
        NhanVien[] kq = new NhanVien[soLuong];
        for (int i = 0; i < soLuong; i++)
            kq[i] = ds[i];
        return kq;
    }

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getMaNhanVien().equalsIgnoreCase(ma.trim()))
                return i;
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public NhanVien timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return ds[viTri];
    }

    private void moRongMang() {
        ds = Arrays.copyOf(ds, ds.length + SUC_CHUA_MAC_DINH);
    }

    @Override
    public boolean them(NhanVien doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaNhanVien()))
            return false;
        if (soLuong == ds.length)
            moRongMang();
        ds[soLuong++] = doiTuong;
        return true;
    }

    public int themNhieu(NhanVien[] cacDoiTuong) {
        int daThem = 0;
        for (int i = 0; i < cacDoiTuong.length; i++)
            if (them(cacDoiTuong[i]))
                daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, NhanVien moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds[viTri].getMaNhanVien().equalsIgnoreCase(moi.getMaNhanVien()))
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
    public NhanVien[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        NhanVien[] kq = new NhanVien[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (phuHop(ds[i], key))
                kq[dem++] = ds[i];
        return Arrays.copyOf(kq, dem);
    }

    private boolean phuHop(NhanVien x, String key) {
        return x.getMaNhanVien().toLowerCase().contains(key)
                || x.getHoTen().toLowerCase().contains(key)
                || x.getSoDienThoai().contains(key);
    }

    public void hienThiDanhSach() {
        for (int i = 0; i < soLuong; i++)
            System.out.println(ds[i].hienThiThongTin());
        if (soLuong == 0)
            System.out.println("Danh sach rong.");
    }

}
