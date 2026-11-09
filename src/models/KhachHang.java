package models;

public class KhachHang {
    private String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;

    public KhachHang() {
        maKhachHang = "";
        hoTen = "";
        soDienThoai = "";
        diaChi = "";
    }

    public KhachHang(String maKhachHang, String hoTen, String soDienThoai, String diaChi) {
        kiemTraChuoi(maKhachHang);
        this.maKhachHang = maKhachHang.trim();
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
    }

    public String getMaKhachHang() {
        return maKhachHang;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        kiemTraChuoi(diaChi);
        this.diaChi = diaChi;
    }

    public boolean kiemTraHopLe() {
        return !maKhachHang.isEmpty() && !hoTen.isEmpty();
    }

    public void capNhatTu(KhachHang moi) {
        setHoTen(moi.getHoTen());
        setSoDienThoai(moi.getSoDienThoai());
        setDiaChi(moi.getDiaChi());
    }

    public String hienThiThongTin() {
        return maKhachHang + " | " + hoTen + " | " + soDienThoai + " | " + diaChi;
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
