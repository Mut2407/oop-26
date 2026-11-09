package services;

import models.*;
import java.util.Arrays;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DanhSachPhieuNhap implements IQuanLyDanhSach<PhieuNhap> {
    private PhieuNhap[] ds;
    private int soLuong;
    private static final int SUC_CHUA_MAC_DINH = 10;

    public DanhSachPhieuNhap() {
        this(SUC_CHUA_MAC_DINH);
    }

    public DanhSachPhieuNhap(int sucChua) {
        if (sucChua <= 0)
            throw new IllegalArgumentException("Suc chua phai > 0.");
        ds = new PhieuNhap[sucChua];
    }

    public int getSoLuong() {
        return soLuong;
    }

    public PhieuNhap[] getTatCa() {
        PhieuNhap[] kq = new PhieuNhap[soLuong];
        for (int i = 0; i < soLuong; i++)
            kq[i] = new PhieuNhap(ds[i]);
        return kq;
    }

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getMaPhieuNhap().equalsIgnoreCase(ma.trim()))
                return i;
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public PhieuNhap timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return new PhieuNhap(ds[viTri]);
    }

    private void moRongMang() {
        ds = Arrays.copyOf(ds, ds.length + SUC_CHUA_MAC_DINH);
    }

    @Override
    public boolean them(PhieuNhap doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaPhieuNhap()))
            return false;
        if (soLuong == ds.length)
            moRongMang();
        ds[soLuong++] = new PhieuNhap(doiTuong);
        return true;
    }

    public int themNhieu(PhieuNhap[] cacDoiTuong) {
        int daThem = 0;
        for (int i = 0; i < cacDoiTuong.length; i++)
            if (them(cacDoiTuong[i]))
                daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, PhieuNhap moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds[viTri].getMaPhieuNhap().equalsIgnoreCase(moi.getMaPhieuNhap()))
            return false;
        ds[viTri] = new PhieuNhap(moi);
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
    public PhieuNhap[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        PhieuNhap[] kq = new PhieuNhap[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (phuHop(ds[i], key))
                kq[dem++] = new PhieuNhap(ds[i]);
        return Arrays.copyOf(kq, dem);
    }

    private boolean phuHop(PhieuNhap x, String key) {
        return x.getMaPhieuNhap().toLowerCase().contains(key)
                || x.getNhaCungCap().getMaNhaCungCap().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        for (int i = 0; i < soLuong; i++)
            System.out.println(ds[i].hienThiThongTin());
        if (soLuong == 0)
            System.out.println("Danh sach rong.");
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuong; i++)
            tong = tong.add(ds[i].tinhTongTien());
        return tong;
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den))
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuong; i++)
            if (!ds[i].getNgayLap().isBefore(tu) && !ds[i].getNgayLap().isAfter(den))
                tong = tong.add(ds[i].tinhTongTien());
        return tong;
    }

}
