package models;

import java.math.BigDecimal;

public class GiayTheThao extends SanPham {
    private double kichCoGiay;
    private String mauSac;
    private String chatLieu;
    private String loaiDeGiay;

    public GiayTheThao() {
        super();
        mauSac = "";
        chatLieu = "";
        loaiDeGiay = "";
    }

    public GiayTheThao(String ma, String ten, String thuongHieu, BigDecimal gia,
            LoaiSP loai, double kichCoGiay, String mauSac, String chatLieu, String loaiDeGiay) {
        super(ma, ten, thuongHieu, gia, loai);
        setKichCoGiay(kichCoGiay);
        setMauSac(mauSac);
        setChatLieu(chatLieu);
        setLoaiDeGiay(loaiDeGiay);
    }

    public GiayTheThao(GiayTheThao cu) {
        super(cu);
        setKichCoGiay(cu.kichCoGiay);
        setMauSac(cu.mauSac);
        setChatLieu(cu.chatLieu);
        setLoaiDeGiay(cu.loaiDeGiay);
    }

    public double getKichCoGiay() {
        return kichCoGiay;
    }

    public void setKichCoGiay(double kichCoGiay) {
        if (!Double.isFinite(kichCoGiay) || kichCoGiay <= 0) {
            throw new IllegalArgumentException("kichCoGiay phai > 0.");
        }
        this.kichCoGiay = kichCoGiay;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        kiemTraChuoi(mauSac, "mauSac");
        this.mauSac = mauSac.trim();
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        if (chatLieu == null) {
            chatLieu = "";
        }
        this.chatLieu = chatLieu.trim();
    }

    public String getLoaiDeGiay() {
        return loaiDeGiay;
    }

    public void setLoaiDeGiay(String loaiDeGiay) {
        kiemTraChuoi(loaiDeGiay, "loaiDeGiay");
        this.loaiDeGiay = loaiDeGiay.trim();
    }

    @Override
    public void capNhatThongTin(SanPham moi) {
        if (!(moi instanceof GiayTheThao)) {
            throw new IllegalArgumentException("Khong duoc doi lop san pham.");
        }
        GiayTheThao hopLe = new GiayTheThao((GiayTheThao) moi);
        super.capNhatThongTin(hopLe);
        this.kichCoGiay = hopLe.kichCoGiay;
        this.mauSac = hopLe.mauSac;
        this.chatLieu = hopLe.chatLieu;
        this.loaiDeGiay = hopLe.loaiDeGiay;
    }

    @Override
    public void kiemTraHopLe() {
        new GiayTheThao(this);
    }

    @Override
    public String hienThiThongTin() {
        return thongTinChung() + " | GiayTheThao | " + kichCoGiay + " | " + mauSac + " | " + chatLieu + " | " + loaiDeGiay;
    }
}
