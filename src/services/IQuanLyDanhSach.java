package services;

import java.util.List;

public interface IQuanLyDanhSach<T> {
    boolean them(T item);
    boolean suaTheoMa(String ma, T item);
    boolean xoaTheoMa(String ma);
    T timTheoMa(String ma);
    List<T> timGanDung(String tuKhoa);
}
