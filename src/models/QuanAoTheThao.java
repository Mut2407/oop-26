package models;

import java.math.BigDecimal;

public final class QuanAoTheThao extends SanPham {
    private String kichCoQuanAo;
    private String mauSac;
    private String chatLieu;

    public QuanAoTheThao() {
        super();
    }

    public QuanAoTheThao(String ma, String ten, String thuongHieu, BigDecimal giaBan,
            LoaiSP loaiSP, String kichCoQuanAo, String mauSac, String chatLieu) {
        super(ma, ten, thuongHieu, giaBan, loaiSP);
        setKichCoQuanAo(kichCoQuanAo);
        setMauSac(mauSac);
        setChatLieu(chatLieu);
    }

    public String getKichCoQuanAo() {
        return kichCoQuanAo;
    }

    public void setKichCoQuanAo(String kichCoQuanAo) {
        this.kichCoQuanAo = KiemTra.batBuoc(kichCoQuanAo, "Kich co quan ao");
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

    @Override
    public QuanAoTheThao saoChep() {
        return giuTonKho(new QuanAoTheThao(getMaSanPham(), getTenSanPham(), getThuongHieu(),
                getGiaBan(), getLoaiSP(), kichCoQuanAo, mauSac, chatLieu));
    }

    @Override
    public String hienThiThongTin() {
        return thongTinChung() + " | QuanAoTheThao | " + kichCoQuanAo + " | " + mauSac + " | " + chatLieu;
    }
}
