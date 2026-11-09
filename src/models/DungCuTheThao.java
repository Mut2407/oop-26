package models;

import java.math.BigDecimal;

public class DungCuTheThao extends SanPham {
    private String monTheThao;
    private String vatLieu;
    private double khoiLuongKg;

    public DungCuTheThao() {
        monTheThao = "";
        vatLieu = "";
        khoiLuongKg = 0;
    }

    public DungCuTheThao(String maSanPham, String tenSanPham, String thuongHieu,
            BigDecimal giaBan, LoaiSP loaiSP, String monTheThao, String vatLieu, double khoiLuongKg) {
        super(maSanPham, tenSanPham, thuongHieu, giaBan, loaiSP);
        setMonTheThao(monTheThao);
        setVatLieu(vatLieu);
        setKhoiLuongKg(khoiLuongKg);
    }

    public String getMonTheThao() {
        return monTheThao;
    }

    public void setMonTheThao(String monTheThao) {
        kiemTraChuoi(monTheThao);
        this.monTheThao = monTheThao;
    }

    public String getVatLieu() {
        return vatLieu;
    }

    public void setVatLieu(String vatLieu) {
        kiemTraChuoi(vatLieu);
        this.vatLieu = vatLieu;
    }

    public double getKhoiLuongKg() {
        return khoiLuongKg;
    }

    public void setKhoiLuongKg(double khoiLuongKg) {
        if (!Double.isFinite(khoiLuongKg) || khoiLuongKg <= 0)
            throw new IllegalArgumentException("Kich co/khoi luong phai > 0.");
        this.khoiLuongKg = khoiLuongKg;
    }

    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && !monTheThao.isEmpty() && !vatLieu.isEmpty() && khoiLuongKg > 0;
    }

    @Override
    public void capNhatTu(SanPham moi) {
        super.capNhatTu(moi);
        DungCuTheThao sp = (DungCuTheThao) moi;
        setMonTheThao(sp.getMonTheThao());
        setVatLieu(sp.getVatLieu());
        setKhoiLuongKg(sp.getKhoiLuongKg());
    }

    @Override
    public String hienThiThongTin() {
        return "DungCuTheThao | " + thongTinChung()
                + " | " + monTheThao + " | " + vatLieu + " | " + khoiLuongKg;
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
