package models;

import java.math.BigDecimal;

public class GiayTheThao extends SanPham {
    private double kichCoGiay;
    private String mauSac;
    private String chatLieu;
    private String loaiDeGiay;

    //không tham số
    public GiayTheThao() {
        kichCoGiay = 0;
        mauSac = "";
        chatLieu = "";
        loaiDeGiay = "";
    }

    //có tham số
    public GiayTheThao(String maSanPham, String tenSanPham, String thuongHieu,
            BigDecimal giaBan, LoaiSP loaiSP, double kichCoGiay, String mauSac, String chatLieu, String loaiDeGiay) {
        super(maSanPham, tenSanPham, thuongHieu, giaBan, loaiSP);
        setKichCoGiay(kichCoGiay);
        setMauSac(mauSac);
        setChatLieu(chatLieu);
        setLoaiDeGiay(loaiDeGiay);
    }

    public double getKichCoGiay() {
        return kichCoGiay;
    }

    public void setKichCoGiay(double kichCoGiay) {
        if (!Double.isFinite(kichCoGiay) || kichCoGiay <= 0)
            throw new IllegalArgumentException("Kich co/khoi luong phai > 0.");
        this.kichCoGiay = kichCoGiay;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        kiemTraChuoi(mauSac);
        this.mauSac = mauSac;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        kiemTraChuoi(chatLieu);
        this.chatLieu = chatLieu;
    }

    public String getLoaiDeGiay() {
        return loaiDeGiay;
    }

    public void setLoaiDeGiay(String loaiDeGiay) {
        kiemTraChuoi(loaiDeGiay);
        this.loaiDeGiay = loaiDeGiay;
    }

    @Override
    public boolean kiemTraHopLe() {
        return super.kiemTraHopLe() && kichCoGiay > 0 && !mauSac.isEmpty() && !chatLieu.isEmpty() && !loaiDeGiay.isEmpty();
    }

    @Override
    public void capNhatTu(SanPham moi) {
        super.capNhatTu(moi);
        GiayTheThao sp = (GiayTheThao) moi;
        setKichCoGiay(sp.getKichCoGiay());
        setMauSac(sp.getMauSac());
        setChatLieu(sp.getChatLieu());
        setLoaiDeGiay(sp.getLoaiDeGiay());
    }

    @Override
    public String hienThiThongTin() {
        return "GiayTheThao | " + thongTinChung()
                + " | " + kichCoGiay + " | " + mauSac + " | " + chatLieu + " | " + loaiDeGiay;
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
