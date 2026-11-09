package services;

import models.*;
import java.util.Arrays;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DanhSachHoaDon implements IQuanLyDanhSach<HoaDon> {
    private HoaDon[] ds;
    private int soLuong;
    private static final int SUC_CHUA_MAC_DINH = 10;

    public DanhSachHoaDon() {
        this(SUC_CHUA_MAC_DINH);
    }

    public DanhSachHoaDon(int sucChua) {
        if (sucChua <= 0)
            throw new IllegalArgumentException("Suc chua phai > 0.");
        ds = new HoaDon[sucChua];
    }

    public int getSoLuong() {
        return soLuong;
    }

    public HoaDon[] getTatCa() {
        HoaDon[] kq = new HoaDon[soLuong];
        for (int i = 0; i < soLuong; i++)
            kq[i] = new HoaDon(ds[i]);
        return kq;
    }

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        for (int i = 0; i < soLuong; i++)
            if (ds[i].getMaHoaDon().equalsIgnoreCase(ma.trim()))
                return i;
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public HoaDon timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return new HoaDon(ds[viTri]);
    }

    private void moRongMang() {
        ds = Arrays.copyOf(ds, ds.length + SUC_CHUA_MAC_DINH);
    }

    @Override
    public boolean them(HoaDon doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaHoaDon()))
            return false;
        if (soLuong == ds.length)
            moRongMang();
        ds[soLuong++] = new HoaDon(doiTuong);
        return true;
    }

    public int themNhieu(HoaDon[] cacDoiTuong) {
        int daThem = 0;
        for (int i = 0; i < cacDoiTuong.length; i++)
            if (them(cacDoiTuong[i]))
                daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, HoaDon moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds[viTri].getMaHoaDon().equalsIgnoreCase(moi.getMaHoaDon()))
            return false;
        ds[viTri] = new HoaDon(moi);
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
    public HoaDon[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        HoaDon[] kq = new HoaDon[soLuong];
        int dem = 0;
        for (int i = 0; i < soLuong; i++)
            if (phuHop(ds[i], key))
                kq[dem++] = new HoaDon(ds[i]);
        return Arrays.copyOf(kq, dem);
    }

    private boolean phuHop(HoaDon x, String key) {
        return x.getMaHoaDon().toLowerCase().contains(key)
                || x.getKhachHang().getMaKhachHang().toLowerCase().contains(key);
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
