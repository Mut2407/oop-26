package services;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.RandomAccess;
import models.NhanVien;

/** A List snapshot carrying legacy metadata outside the employee model. */
final class BanSaoNhanVien extends AbstractList<NhanVien> implements RandomAccess {
    private final List<NhanVien> ds;
    private final Map<String, String> diaChiCu;

    BanSaoNhanVien(NhanVien[] source, Map<String, String> metadata) {
        this(saoChep(source), metadata);
    }

    private BanSaoNhanVien(List<NhanVien> snapshot, Map<String, String> metadata) {
        ds = snapshot;
        Map<String, String> addresses = new LinkedHashMap<>();
        for (NhanVien nv : ds) {
            String ma = nv.getMaNhanVien().toUpperCase(Locale.ROOT);
            if (metadata.containsKey(ma)) {
                addresses.put(ma, metadata.get(ma));
            }
        }
        diaChiCu = Map.copyOf(addresses);
    }

    private static List<NhanVien> saoChep(NhanVien[] source) {
        List<NhanVien> copy = new ArrayList<>();
        for (NhanVien nv : source) {
            copy.add(saoChep(nv));
        }
        return List.copyOf(copy);
    }

    Map<String, String> diaChiCu() {
        return diaChiCu;
    }

    @Override
    public NhanVien get(int index) {
        return ds.get(index);
    }

    @Override
    public int size() {
        return ds.size();
    }

    @Override
    public List<NhanVien> subList(int from, int to) {
        return new BanSaoNhanVien(ds.subList(from, to), diaChiCu);
    }

    private static NhanVien saoChep(NhanVien nv) {
        return new NhanVien(nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDienThoai(), nv.getCaLamViec());
    }
}
