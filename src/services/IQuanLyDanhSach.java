package services;

import java.util.List;

public interface IQuanLyDanhSach<T> {
    boolean them(T item);
    boolean suaTheoMa(String ma, T item);
    boolean xoaTheoMa(String ma);
    T CheckMaTrung(String ma);
    List<T> TimKiemTheoMaHoacTen(String tuKhoa);
}
