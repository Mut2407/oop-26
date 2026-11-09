package models;

import java.math.BigDecimal;

public final class DungCuTheThao extends SanPham {
    private String monTheThao;
    private String vatLieu;
    private double khoiLuongKg;

    public DungCuTheThao() {
        super();
    }

    public DungCuTheThao(String ma, String ten, String thuongHieu, BigDecimal giaBan,
            LoaiSP loaiSP, String monTheThao, String vatLieu, double khoiLuongKg) {
        super(ma, ten, thuongHieu, giaBan, loaiSP);
        setMonTheThao(monTheThao);
        setVatLieu(vatLieu);
        setKhoiLuongKg(khoiLuongKg);
    }

    public String getMonTheThao() {
        return monTheThao;
    }

    public void setMonTheThao(String monTheThao) {
        this.monTheThao = KiemTra.batBuoc(monTheThao, "Mon the thao");
    }

    public String getVatLieu() {
        return vatLieu;
    }

    public void setVatLieu(String vatLieu) {
        this.vatLieu = KiemTra.batBuoc(vatLieu, "Vat lieu");
    }

    public double getKhoiLuongKg() {
        return khoiLuongKg;
    }

    public void setKhoiLuongKg(double khoiLuongKg) {
        this.khoiLuongKg = KiemTra.soDuong(khoiLuongKg);
    }

    @Override
    public DungCuTheThao saoChep() {
        return giuTonKho(new DungCuTheThao(getMaSanPham(), getTenSanPham(), getThuongHieu(),
                getGiaBan(), getLoaiSP(), monTheThao, vatLieu, khoiLuongKg));
    }

    @Override
    public String hienThiThongTin() {
        return thongTinChung() + " | DungCuTheThao | " + monTheThao + " | " + vatLieu + " | " + khoiLuongKg;
    }
}
