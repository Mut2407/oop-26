package models;

import java.math.BigDecimal;

public final class GiayTheThao extends SanPham {
    private double kichCoGiay;
    private String mauSac;
    private String chatLieu;
    private String loaiDeGiay;

    public GiayTheThao() {
        super();
    }

    public GiayTheThao(String ma, String ten, String thuongHieu, BigDecimal giaBan,
            LoaiSP loaiSP, double kichCoGiay, String mauSac, String chatLieu, String loaiDeGiay) {
        super(ma, ten, thuongHieu, giaBan, loaiSP);
        setKichCoGiay(kichCoGiay);
        setMauSac(mauSac);
        setChatLieu(chatLieu);
        setLoaiDeGiay(loaiDeGiay);
    }

    public double getKichCoGiay() {
        return kichCoGiay;
    }

    public void setKichCoGiay(double kichCoGiay) {
        this.kichCoGiay = KiemTra.soDuong(kichCoGiay);
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = KiemTra.batBuoc(mauSac, "Mau sac");
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu == null ? "" : chatLieu.trim();
    }

    public String getLoaiDeGiay() {
        return loaiDeGiay;
    }

    public void setLoaiDeGiay(String loaiDeGiay) {
        this.loaiDeGiay = KiemTra.batBuoc(loaiDeGiay, "Loai de giay");
    }

    @Override
    public GiayTheThao saoChep() {
        return giuTonKho(new GiayTheThao(getMaSanPham(), getTenSanPham(), getThuongHieu(),
                getGiaBan(), getLoaiSP(), kichCoGiay, mauSac, chatLieu, loaiDeGiay));
    }

    @Override
    public String hienThiThongTin() {
        return thongTinChung() + " | GiayTheThao | " + kichCoGiay + " | " + mauSac + " | " + chatLieu + " | " + loaiDeGiay;
    }
}
