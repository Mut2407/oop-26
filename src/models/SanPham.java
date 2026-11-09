package models;

import java.math.BigDecimal;
import java.util.Locale;

public abstract class SanPham {
    public static final int SUC_CHUA_BAN_DAU = 10;
    private final String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private BigDecimal giaBan;
    private int soLuongTon;
    private LoaiSP loaiSP;

    public SanPham() {
        maSanPham = "";
        tenSanPham = "";
        thuongHieu = "";
        giaBan = BigDecimal.ZERO;
        soLuongTon = 0;
    }

    public SanPham(String ma, String ten, String thuongHieu, BigDecimal gia, LoaiSP loai) {
        if (ma == null || !ma.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma san pham khong hop le.");
        }
        maSanPham = ma.trim().toUpperCase(Locale.ROOT);
        setTenSanPham(ten);
        setThuongHieu(thuongHieu);
        setGiaBan(gia);
        setLoaiSP(loai);
    }

    public SanPham(SanPham cu) {
        this(cu.maSanPham, cu.tenSanPham, cu.thuongHieu, cu.giaBan, cu.loaiSP);
        soLuongTon = cu.soLuongTon;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String ten) {
        kiemTraChuoi(ten, "Ten san pham");
        tenSanPham = ten.trim();
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        kiemTraChuoi(thuongHieu, "Thuong hieu");
        this.thuongHieu = thuongHieu.trim();
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal gia) {
        if (gia == null || gia.signum() < 0) {
            throw new IllegalArgumentException("Gia ban phai >= 0.");
        }
        giaBan = gia;
    }

    public LoaiSP getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(LoaiSP loai) {
        if (loai == null) {
            throw new IllegalArgumentException("Loai san pham khong duoc null.");
        }
        loai.kiemTraHopLe();
        loaiSP = loai;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    /** Theo quy uoc: chi Kho va LuuTruFileText goi ham nay, menu khong goi. */
    public void capNhatTon(int chenhLech) {
        long tonMoi = (long) soLuongTon + chenhLech;
        if (tonMoi < 0 || tonMoi > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Ton kho khong hop le: " + maSanPham);
        }
        soLuongTon = (int) tonMoi;
    }

    public void capNhatThongTin(SanPham moi) {
        setTenSanPham(moi.tenSanPham);
        setThuongHieu(moi.thuongHieu);
        setGiaBan(moi.giaBan);
        setLoaiSP(moi.loaiSP);
    }

    protected static void kiemTraChuoi(String giaTri, String ten) {
        if (giaTri == null || giaTri.trim().isEmpty()) {
            throw new IllegalArgumentException(ten + " khong duoc rong.");
        }
    }

    protected String thongTinChung() {
        return maSanPham + " | " + tenSanPham + " | " + thuongHieu + " | "
                + giaBan.toPlainString() + " | Ton: " + soLuongTon;
    }

    public abstract void kiemTraHopLe();
    public abstract String hienThiThongTin();
}
