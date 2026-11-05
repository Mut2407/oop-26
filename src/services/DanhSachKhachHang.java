package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import models.KhachHang;

public final class DanhSachKhachHang implements IQuanLyDanhSach<KhachHang>, LuuTruFile {
    private static final String TIEU_DE_CU = "KHACH_HANG_V1";
    private static final String TIEU_DE_FILE = "maKhachHang\thoTen\tsoDienThoai\tdiaChi";
    private final List<KhachHang> ds = new ArrayList<>();

    public DanhSachKhachHang() {
    }

    /** Adapter for the original array-based callers. */
    public DanhSachKhachHang(KhachHang[] khachHang) {
        nhapDanhSach(khachHang);
    }

    public int getSoLuong() {
        return ds.size();
    }

    public List<KhachHang> getTatCa() {
        List<KhachHang> result = new ArrayList<>();
        for (KhachHang kh : ds) {
            result.add(saoChep(kh));
        }
        return List.copyOf(result);
    }

    public KhachHang[] getDanhSach() {
        return getTatCa().toArray(new KhachHang[0]);
    }

    public void nhapDanhSach(KhachHang[] khachHang) {
        if (khachHang == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachKhachHang moi = new DanhSachKhachHang();
        for (KhachHang kh : khachHang) {
            moi.them(kh);
        }
        thayBang(moi);
    }

    @Override
    public boolean them(KhachHang kh) {
        KhachHang banSao = saoChep(kh);
        if (chiSoTheoMa(banSao.getMaKhachHang()) >= 0) {
            throw new IllegalArgumentException("Ma khach hang da ton tai!");
        }
        ds.add(banSao);
        return true;
    }

    public void themNhieu(KhachHang[] khachHang) {
        if (khachHang == null) {
            throw new IllegalArgumentException("Danh sach khong duoc null.");
        }
        DanhSachKhachHang moi = new DanhSachKhachHang(getDanhSach());
        for (KhachHang kh : khachHang) {
            moi.them(kh);
        }
        thayBang(moi);
    }

    @Override
    public boolean suaTheoMa(String ma, KhachHang khMoi) {
        int index = chiSoTheoMa(ma);
        if (index < 0) {
            return false;
        }
        KhachHang hopLe = saoChep(khMoi);
        String maCu = ds.get(index).getMaKhachHang();
        if (!maCu.equalsIgnoreCase(hopLe.getMaKhachHang())) {
            throw new IllegalArgumentException("Khong duoc doi ma khach hang.");
        }
        ds.set(index, new KhachHang(maCu, hopLe.getHoTen(), hopLe.getSoDienThoai(), hopLe.getDiaChi()));
        return true;
    }

    public boolean sua(String ma, String ten, String sdt, String diaChi) {
        KhachHang cu = timTheoMa(ma);
        return cu != null && suaTheoMa(ma, new KhachHang(cu.getMaKhachHang(), ten, sdt, diaChi));
    }

    @Override
    public boolean xoaTheoMa(String ma) {
        int index = chiSoTheoMa(ma);
        if (index < 0) {
            return false;
        }
        ds.remove(index);
        return true;
    }

    public boolean xoa(String ma) {
        return xoaTheoMa(ma);
    }

    @Override
    public KhachHang timTheoMa(String ma) {
        int index = chiSoTheoMa(ma);
        return index < 0 ? null : saoChep(ds.get(index));
    }

    @Override
    public List<KhachHang> timGanDung(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            throw new IllegalArgumentException("Tu khoa khong duoc rong.");
        }
        String key = chuanHoa(tuKhoa.trim());
        List<KhachHang> result = new ArrayList<>();
        for (KhachHang kh : ds) {
            if (chuanHoa(kh.getMaKhachHang()).contains(key) || chuanHoa(kh.getHoTen()).contains(key)) {
                result.add(saoChep(kh));
            }
        }
        return List.copyOf(result);
    }

    /** Exact phone match; partial phone matching remains available through timKiem. */
    public List<KhachHang> timTheoSoDienThoai(String sdt) {
        if (!soDienThoaiHopLe(sdt)) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        List<KhachHang> result = new ArrayList<>();
        for (KhachHang kh : ds) {
            if (kh.getSoDienThoai().equals(sdt.trim())) {
                result.add(saoChep(kh));
            }
        }
        return List.copyOf(result);
    }

    public KhachHang[] timKiem(String ma, String ten, String sdt, String diaChi, boolean chinhXac) {
        List<KhachHang> result = new ArrayList<>();
        for (KhachHang kh : ds) {
            if (khop(kh.getMaKhachHang(), ma, chinhXac) && khop(kh.getHoTen(), ten, chinhXac)
                    && khop(kh.getSoDienThoai(), sdt, chinhXac) && khop(kh.getDiaChi(), diaChi, chinhXac)) {
                result.add(saoChep(kh));
            }
        }
        return result.toArray(new KhachHang[0]);
    }

    public String thongKe() {
        return "Tong so khach hang: " + ds.size() + "\nTheo dia chi:\n" + thongKeTheoNhom(true)
                + "Theo dau so dien thoai (3 chu so dau, bo dau +):\n" + thongKeTheoNhom(false);
    }

    private String thongKeTheoNhom(boolean diaChi) {
        Map<String, Integer> dem = new LinkedHashMap<>();
        Map<String, String> tenNhom = new LinkedHashMap<>();
        for (KhachHang kh : ds) {
            String value = diaChi ? kh.getDiaChi() : kh.getSoDienThoai().replace("+", "").substring(0, 3);
            String key = value.toLowerCase(Locale.ROOT);
            tenNhom.putIfAbsent(key, value);
            dem.merge(key, 1, Integer::sum);
        }
        if (dem.isEmpty()) {
            return "  Chua co du lieu.\n";
        }
        StringBuilder result = new StringBuilder();
        dem.forEach((key, count) -> result.append("  ").append(tenNhom.get(key))
                .append(": ").append(count).append(" khach hang\n"));
        return result.toString();
    }

    @Override
    public void ghiFile(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(TIEU_DE_FILE);
        for (KhachHang kh : ds) {
            lines.add(FileText.dong(kh.getMaKhachHang(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi()));
        }
        FileText.ghi(path, lines);
    }

    @Override
    public void docFile(String path) throws IOException {
        DanhSachKhachHang moi = new DanhSachKhachHang();
        List<String[]> rows = FileText.doc(path, TIEU_DE_FILE, TIEU_DE_CU);
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            try {
                if (row.length != 4) {
                    throw new IllegalArgumentException("Phai co 4 cot.");
                }
                moi.them(new KhachHang(row[0], row[1], row[2], row[3]));
            } catch (IllegalArgumentException e) {
                throw new IOException("Dong " + (i + 2) + ": " + e.getMessage(), e);
            }
        }
        thayBang(moi);
    }

    private void thayBang(DanhSachKhachHang moi) {
        ds.clear();
        ds.addAll(moi.ds);
    }

    public static boolean maHopLe(String ma) {
        return ma != null && ma.trim().matches("[A-Za-z0-9_-]+");
    }

    public static boolean soDienThoaiHopLe(String sdt) {
        return sdt != null && sdt.trim().matches("\\+?[0-9]{9,15}");
    }

    private int chiSoTheoMa(String ma) {
        if (ma != null) {
            for (int i = 0; i < ds.size(); i++) {
                if (ds.get(i).getMaKhachHang().equalsIgnoreCase(ma.trim())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static KhachHang saoChep(KhachHang kh) {
        if (kh == null) {
            throw new IllegalArgumentException("Khach hang khong duoc null.");
        }
        return new KhachHang(kh.getMaKhachHang(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi());
    }

    private static String chuanHoa(String text) {
        return VanBanConsole.khongDau(text).toLowerCase(Locale.ROOT);
    }

    private static boolean khop(String value, String filter, boolean exact) {
        if (filter == null || filter.isBlank()) {
            return true;
        }
        String a = chuanHoa(value);
        String b = chuanHoa(filter.trim());
        return exact ? a.equals(b) : a.contains(b);
    }
}

