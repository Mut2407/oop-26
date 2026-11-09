package models;

import java.math.BigDecimal;

public class QuanAoTheThao extends SanPham {
    private String kichCoQuanAo;
    private String mauSac;
    private String chatLieu;

    public QuanAoTheThao() {
        super();
        kichCoQuanAo = "";
        mauSac = "";
        chatLieu = "";
    }

    public QuanAoTheThao(String ma, String ten, String thuongHieu, BigDecimal gia,
            LoaiSP loai, String kichCoQuanAo, String mauSac, String chatLieu) {
        super(ma, ten, thuongHieu, gia, loai);
        setKichCoQuanAo(kichCoQuanAo);
        setMauSac(mauSac);
        setChatLieu(chatLieu);
    }

    public QuanAoTheThao(QuanAoTheThao cu) {
        super(cu);
        setKichCoQuanAo(cu.kichCoQuanAo);
        setMauSac(cu.mauSac);
        setChatLieu(cu.chatLieu);
    }

    public String getKichCoQuanAo() {
        return kichCoQuanAo;
    }

    public void setKichCoQuanAo(String kichCoQuanAo) {
        kiemTraChuoi(kichCoQuanAo, "kichCoQuanAo");
        this.kichCoQuanAo = kichCoQuanAo.trim();
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

    @Override
    public void capNhatThongTin(SanPham moi) {
        if (!(moi instanceof QuanAoTheThao)) {
            throw new IllegalArgumentException("Khong duoc doi lop san pham.");
        }
        QuanAoTheThao hopLe = new QuanAoTheThao((QuanAoTheThao) moi);
        super.capNhatThongTin(hopLe);
        this.kichCoQuanAo = hopLe.kichCoQuanAo;
        this.mauSac = hopLe.mauSac;
        this.chatLieu = hopLe.chatLieu;
    }

    @Override
    public void kiemTraHopLe() {
        new QuanAoTheThao(this);
    }

    @Override
    public String hienThiThongTin() {
        return thongTinChung() + " | QuanAoTheThao | " + kichCoQuanAo + " | " + mauSac + " | " + chatLieu;
    }
}
