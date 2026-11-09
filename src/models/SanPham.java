package models;

import java.math.BigDecimal;

public abstract class SanPham {
    private final String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private BigDecimal giaBan;
    private int soLuongTon;
    private LoaiSP loaiSP;

    protected SanPham() {
        maSanPham = "";
        tenSanPham = "";
        thuongHieu = "";
        giaBan = BigDecimal.ZERO;
    }

    protected SanPham(String maSanPham, String tenSanPham, String thuongHieu,
            BigDecimal giaBan, LoaiSP loaiSP) {
        this.maSanPham = KiemTra.ma(maSanPham);
        setTenSanPham(tenSanPham);
        setThuongHieu(thuongHieu);
        setGiaBan(giaBan);
        setLoaiSP(loaiSP);
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = KiemTra.batBuoc(tenSanPham, "Ten san pham");
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = KiemTra.batBuoc(thuongHieu, "Thuong hieu");
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = KiemTra.tien(giaBan);
    }

    public LoaiSP getLoaiSP() {
        return loaiSP == null ? null : loaiSP.saoChep();
    }

    public void setLoaiSP(LoaiSP loaiSP) {
        this.loaiSP = KiemTra.khacNull(loaiSP, "Loai san pham").saoChep();
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    /** Tạo bản sao; không cho thay đổi trực tiếp tồn của sản phẩm đang lưu. */
    public final SanPham voiTonKho(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Ton kho khong duoc am.");
        }
        SanPham banSao = saoChep();
        banSao.soLuongTon = soLuongTon;
        return banSao;
    }

    protected final <T extends SanPham> T giuTonKho(T banSao) {
        ((SanPham) banSao).soLuongTon = soLuongTon;
        return banSao;
    }

    protected String thongTinChung() {
        return maSanPham + " | " + tenSanPham + " | " + thuongHieu + " | "
                + giaBan.toPlainString() + " | Ton: " + soLuongTon + " | " + (loaiSP == null ? "" : loaiSP.getMaLoaiSP());
    }

    public abstract SanPham saoChep();
    public abstract String hienThiThongTin();
}
