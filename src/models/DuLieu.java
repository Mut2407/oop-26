package models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DuLieu {
    private static final Map<String, DanhMuc> DANH_MUC = new LinkedHashMap<>();

    private DuLieu() {
    }

    public static boolean xoaDanhMuc(String ma) {
        return ma != null && DANH_MUC.remove(ma.trim().toUpperCase(Locale.ROOT)) != null;
    }

    public static void dangKyDanhMuc(DanhMuc danhMuc) {
        if (danhMuc == null) {
            throw new IllegalArgumentException("Danh muc khong duoc null.");
        }
        String ma = danhMuc.getMaDanhMuc().toUpperCase(Locale.ROOT);
        if (DANH_MUC.containsKey(ma)) {
            throw new IllegalArgumentException("Ma danh muc da ton tai.");
        }
        DANH_MUC.put(ma, danhMuc);
    }

    public static DanhMuc timDanhMuc(String ma) {
        return ma == null ? null : DANH_MUC.get(ma.trim().toUpperCase(Locale.ROOT));
    }

    public static List<DanhMuc> getDanhMuc() {
        return List.copyOf(DANH_MUC.values());
    }


}
