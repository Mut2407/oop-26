package models;

import java.math.BigDecimal;

public abstract class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private BigDecimal giaBan;
    private int soLuongTon;
    private LoaiSP loaiSP;

    //không tham số
    public SanPham() {
        maSanPham = "";
        tenSanPham = "";
        thuongHieu = "";
        giaBan = BigDecimal.ZERO;
    }

    //có tham số
    public SanPham(String maSanPham, String tenSanPham, String thuongHieu, BigDecimal giaBan, LoaiSP loaiSP) {
        kiemTraChuoi(maSanPham);
        this.maSanPham = maSanPham.trim();
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
        kiemTraChuoi(tenSanPham);
        this.tenSanPham = tenSanPham;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        kiemTraChuoi(thuongHieu);
        this.thuongHieu = thuongHieu;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        if (giaBan == null || giaBan.signum() < 0)
            throw new IllegalArgumentException("Gia ban phai >= 0.");
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public LoaiSP getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(LoaiSP loaiSP) {
        if (loaiSP == null || !loaiSP.kiemTraHopLe())
            throw new IllegalArgumentException("Loai SP khong hop le.");
        this.loaiSP = loaiSP;
    }

    public void capNhatTon(int chenhLech) {
        long tonMoi = (long) soLuongTon + chenhLech;
        if (tonMoi < 0 || tonMoi > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Ton kho ngoai gioi han.");
        soLuongTon = (int) tonMoi;
    }

    public boolean kiemTraHopLe() {
        return !maSanPham.isEmpty() && !tenSanPham.isEmpty() && !thuongHieu.isEmpty() && loaiSP != null && loaiSP.kiemTraHopLe();
    }

    public void capNhatTu(SanPham moi) {
        setTenSanPham(moi.getTenSanPham());
        setThuongHieu(moi.getThuongHieu());
        setGiaBan(moi.getGiaBan());
        setLoaiSP(moi.getLoaiSP());
    }

    protected String thongTinChung() {
        return maSanPham + " | " + tenSanPham + " | " + thuongHieu
                + " | Gia: " + giaBan.toPlainString() + " | Ton: " + soLuongTon
                + " | Loai: " + loaiSP.getMaLoaiSP();
    }

    public abstract String hienThiThongTin();

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
