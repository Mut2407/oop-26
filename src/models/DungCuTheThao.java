package models;

import java.math.BigDecimal;

public class DungCuTheThao extends SanPham {
    private String monTheThao;
    private String vatLieu;
    private double khoiLuongKg;

    public DungCuTheThao() {
        super();
        monTheThao = "";
        vatLieu = "";
    }

    public DungCuTheThao(String ma, String ten, String thuongHieu, BigDecimal gia,
            LoaiSP loai, String monTheThao, String vatLieu, double khoiLuongKg) {
        super(ma, ten, thuongHieu, gia, loai);
        setMonTheThao(monTheThao);
        setVatLieu(vatLieu);
        setKhoiLuongKg(khoiLuongKg);
    }

    public DungCuTheThao(DungCuTheThao cu) {
        super(cu);
        setMonTheThao(cu.monTheThao);
        setVatLieu(cu.vatLieu);
        setKhoiLuongKg(cu.khoiLuongKg);
    }

    public String getMonTheThao() {
        return monTheThao;
    }

    public void setMonTheThao(String monTheThao) {
        kiemTraChuoi(monTheThao, "monTheThao");
        this.monTheThao = monTheThao.trim();
    }

    public String getVatLieu() {
        return vatLieu;
    }

    public void setVatLieu(String vatLieu) {
        kiemTraChuoi(vatLieu, "vatLieu");
        this.vatLieu = vatLieu.trim();
    }

    public double getKhoiLuongKg() {
        return khoiLuongKg;
    }

    public void setKhoiLuongKg(double khoiLuongKg) {
        if (!Double.isFinite(khoiLuongKg) || khoiLuongKg <= 0) {
            throw new IllegalArgumentException("khoiLuongKg phai > 0.");
        }
        this.khoiLuongKg = khoiLuongKg;
    }

    @Override
    public void capNhatThongTin(SanPham moi) {
        if (!(moi instanceof DungCuTheThao)) {
            throw new IllegalArgumentException("Khong duoc doi lop san pham.");
        }
        DungCuTheThao hopLe = new DungCuTheThao((DungCuTheThao) moi);
        super.capNhatThongTin(hopLe);
        this.monTheThao = hopLe.monTheThao;
        this.vatLieu = hopLe.vatLieu;
        this.khoiLuongKg = hopLe.khoiLuongKg;
    }

    @Override
    public void kiemTraHopLe() {
        new DungCuTheThao(this);
    }

    @Override
    public String hienThiThongTin() {
        return thongTinChung() + " | DungCuTheThao | " + monTheThao + " | " + vatLieu + " | " + khoiLuongKg;
    }
}
