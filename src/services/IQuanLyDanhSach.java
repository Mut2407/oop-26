package services;

public interface IQuanLyDanhSach<T> {
    boolean them(T doiTuong);
    boolean sua(String ma, T moi);
    boolean xoa(String ma);
    T[] timKiem(String tuKhoa);
}
