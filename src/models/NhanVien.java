package models;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String soDienThoai;
    private String caLamViec;

    public NhanVien() {
        maNhanVien = "";
        hoTen = "";
        soDienThoai = "";
        caLamViec = "";
    }

    public NhanVien(String maNhanVien, String hoTen, String soDienThoai, String caLamViec) {
        kiemTraChuoi(maNhanVien);
        this.maNhanVien = maNhanVien.trim();
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setCaLamViec(caLamViec);
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        kiemTraChuoi(hoTen);
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        kiemTraChuoi(soDienThoai);
        if (!soDienThoai.matches("[0-9]{9,11}"))
            throw new IllegalArgumentException("SDT can 9 den 11 chu so.");
        this.soDienThoai = soDienThoai;
    }

    public String getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(String caLamViec) {
        kiemTraChuoi(caLamViec);
        this.caLamViec = caLamViec;
    }

    public boolean kiemTraHopLe() {
        return !maNhanVien.isEmpty() && !hoTen.isEmpty();
    }

    public void capNhatTu(NhanVien moi) {
        setHoTen(moi.getHoTen());
        setSoDienThoai(moi.getSoDienThoai());
        setCaLamViec(moi.getCaLamViec());
    }

    public String hienThiThongTin() {
        return maNhanVien + " | " + hoTen + " | " + soDienThoai + " | " + caLamViec;
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
