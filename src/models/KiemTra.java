package models;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;

/** Các quy tắc giá trị dùng chung; quy tắc liên kết thuộc QuanLyCuaHang. */
public final class KiemTra {
    public static final int SUC_CHUA_MAC_DINH = 10;

    private KiemTra() {
    }

    public static String batBuoc(String giaTri, String tenTruong) {
        if (giaTri == null || giaTri.isBlank()) {
            throw new IllegalArgumentException(tenTruong + " khong duoc rong.");
        }
        return giaTri.trim();
    }

    public static String ma(String giaTri) {
        String ma = batBuoc(giaTri, "Ma").toUpperCase(Locale.ROOT);
        if (!ma.matches("[A-Z0-9_-]+")) {
            throw new IllegalArgumentException("Ma chi gom chu, so, _ hoac -.");
        }
        return ma;
    }

    public static String dienThoai(String giaTri) {
        String so = batBuoc(giaTri, "So dien thoai");
        if (!so.matches("\\+?[0-9]{9,15}")) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so.");
        }
        return so;
    }

    public static BigDecimal tien(BigDecimal giaTri) {
        if (giaTri == null || giaTri.signum() < 0) {
            throw new IllegalArgumentException("So tien phai >= 0.");
        }
        return giaTri;
    }

    public static int soDuong(int giaTri) {
        if (giaTri <= 0) {
            throw new IllegalArgumentException("So luong phai > 0.");
        }
        return giaTri;
    }

    public static double soDuong(double giaTri) {
        if (!Double.isFinite(giaTri) || giaTri <= 0) {
            throw new IllegalArgumentException("Gia tri phai la so huu han > 0.");
        }
        return giaTri;
    }

    public static <T> T khacNull(T giaTri, String tenTruong) {
        if (giaTri == null) {
            throw new IllegalArgumentException(tenTruong + " khong duoc null.");
        }
        return giaTri;
    }

    public static String tuKhoa(String giaTri) {
        String chuoi = giaTri == null ? "" : giaTri.trim();
        return Normalizer.normalize(chuoi, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").replace('đ', 'd').replace('Đ', 'D')
                .toLowerCase(Locale.ROOT);
    }

    public static boolean chua(String giaTri, String tuKhoa) {
        return tuKhoa(giaTri).contains(tuKhoa(tuKhoa));
    }
}
