package services;

public interface IQuanLyDanhSach<T> {
    boolean them(T doiTuong);
    boolean sua(String ma, T doiTuongMoi);
    boolean xoa(String ma);
    T timTheoMa(String ma);
    T[] timKiem(String tuKhoa);
    T[] getTatCa();
    int getSoLuong();
}
