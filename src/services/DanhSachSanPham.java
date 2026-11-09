package services;

import models.*;
import java.util.Arrays;
import java.math.BigDecimal;

public class DanhSachSanPham implements IQuanLyDanhSach<SanPham> {
    private SanPham[] ds;
    private int soLuong;
    private static final int SUC_CHUA_MAC_DINH = 10;

    public DanhSachSanPham() {
        this(SUC_CHUA_MAC_DINH);
    }

    public DanhSachSanPham(int sucChua) {
        if (sucChua <= 0)
            throw new IllegalArgumentException("Suc chua phai > 0.");
        ds = new SanPham[sucChua];
    }

    public int getSoLuong() {
        return soLuong;
    }

    public SanPham[] getTatCa() {
        SanPham[] kq = new SanPham[soLuong];
        for (int i = 0; i < soLuong; i++)
            kq[i] = ds[i];
        return kq;
    }

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getMaSanPham().equalsIgnoreCase(ma.trim()))
                return i;
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public SanPham timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return ds[viTri];
    }

    private void moRongMang() {
        ds = Arrays.copyOf(ds, ds.length + SUC_CHUA_MAC_DINH);
    }

    @Override
    public boolean them(SanPham doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaSanPham()))
            return false;
        if (soLuong == ds.length)
            moRongMang();
        ds[soLuong++] = doiTuong;
        return true;
    }

    public int themNhieu(SanPham[] cacDoiTuong) {
        int daThem = 0;
        for (int i = 0; i < cacDoiTuong.length; i++)
            if (them(cacDoiTuong[i]))
                daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, SanPham moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds[viTri].getMaSanPham().equalsIgnoreCase(moi.getMaSanPham()))
            return false;
        if (ds[viTri].getClass() != moi.getClass())
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
    public SanPham[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        SanPham[] kq = new SanPham[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (phuHop(ds[i], key))
                kq[dem++] = ds[i];
        return Arrays.copyOf(kq, dem);
    }

    private boolean phuHop(SanPham x, String key) {
        return x.getMaSanPham().toLowerCase().contains(key)
                || x.getTenSanPham().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        for (int i = 0; i < soLuong; i++)
            System.out.println(ds[i].hienThiThongTin());
        if (soLuong == 0)
            System.out.println("Danh sach rong.");
    }

    public SanPham[] locTheoLoaiSP(String maLoai) {
        maLoai = maLoai == null ? "" : maLoai.trim();
        SanPham[] kq = new SanPham[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getLoaiSP().getMaLoaiSP().equalsIgnoreCase(maLoai))
                kq[dem++] = ds[i];
        return Arrays.copyOf(kq, dem);
    }

    public SanPham[] locTheoKhoangGia(BigDecimal tu, BigDecimal den) {
        if (tu == null || den == null || tu.signum() < 0 || tu.compareTo(den) > 0)
            throw new IllegalArgumentException("Khoang gia khong hop le.");
        SanPham[] kq = new SanPham[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getGiaBan().compareTo(tu) >= 0 && ds[i].getGiaBan().compareTo(den) <= 0)
                kq[dem++] = ds[i];
        return Arrays.copyOf(kq, dem);
    }

}
