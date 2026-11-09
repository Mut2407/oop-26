package services;

// T la kieu doi tuong: SanPham, KhachHang, HoaDon,... (chuong 5).
public interface IQuanLyDanhSach<T> {
    boolean them(T doiTuong);
    boolean sua(String ma, T moi);
    boolean xoa(String ma);
    T timTheoMa(String ma);
    T[] timKiem(String tuKhoa);
    T[] getTatCa();
    int getSoLuong();
}
