package services;

import models.KhachHang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class DanhSachKhachHang implements LuuTruFile {
    private static final int SUC_CHUA_BAN_DAU = 10;
    private static final String TIEU_DE_CU = "KHACH_HANG_V1";
    private static final String TIEU_DE_FILE = "maKhachHang\thoTen\tsoDienThoai\tdiaChi";

    private KhachHang[] danhSach;
    private int soLuong;

    public DanhSachKhachHang() {
        danhSach = new KhachHang[SUC_CHUA_BAN_DAU];
        soLuong = 0;
    }

    public DanhSachKhachHang(KhachHang[] banDau) {
        this();
        nhapDanhSach(banDau);
    }

    public int getSoLuong() {
        return soLuong;
    }

    public KhachHang[] getDanhSach() {
        KhachHang[] ketQua = new KhachHang[soLuong];

        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = saoChep(danhSach[i]);
        }

        return ketQua;
    }

    // Chi thay danh sach khi tat ca phan tu moi deu hop le.
    public void nhapDanhSach(KhachHang[] duLieu) {
        if (duLieu == null) {
            throw new IllegalArgumentException(
                    "Danh sach khong duoc null.");
        }

        DanhSachKhachHang moi = new DanhSachKhachHang();

        for (KhachHang kh : duLieu) {
            moi.them(kh);
        }

        danhSach = moi.danhSach;
        soLuong = moi.soLuong;
    }

    public void them(KhachHang kh) {
        KhachHang hopLe = kiemTraVaSaoChep(kh);

        if (chiSoTheoMa(hopLe.getMaKhachHang()) >= 0) {
            throw new IllegalArgumentException(
                    "Ma khach hang da ton tai!");
        }

        if (soLuong == danhSach.length) {
            danhSach = Arrays.copyOf(
                    danhSach,
                    Math.max(1, danhSach.length * 2)
            );
        }

        danhSach[soLuong++] = hopLe;
    }

    // Khong them do dang neu co ma trung hoac du lieu sai.
    public void themNhieu(KhachHang[] boSung) {
        if (boSung == null) {
            throw new IllegalArgumentException(
                    "Danh sach khong duoc null.");
        }

        DanhSachKhachHang moi =
                new DanhSachKhachHang(getDanhSach());

        for (KhachHang kh : boSung) {
            moi.them(kh);
        }

        danhSach = moi.danhSach;
        soLuong = moi.soLuong;
    }

    // Giu nguyen ma dinh danh khi sua thong tin.
    public boolean sua(
            String ma,
            String hoTen,
            String soDienThoai,
            String diaChi) {

        int viTri = chiSoTheoMa(ma);

        if (viTri < 0) {
            return false;
        }

        danhSach[viTri] = kiemTraVaSaoChep(
                new KhachHang(
                        danhSach[viTri].getMaKhachHang(),
                        hoTen,
                        soDienThoai,
                        diaChi
                )
        );

        return true;
    }

    public boolean xoa(String ma) {
        int viTri = chiSoTheoMa(ma);

        if (viTri < 0) {
            return false;
        }

        for (int i = viTri; i < soLuong - 1; i++) {
            danhSach[i] = danhSach[i + 1];
        }

        danhSach[--soLuong] = null;
        return true;
    }

    public KhachHang timTheoMa(String ma) {
        int viTri = chiSoTheoMa(ma);

        return viTri < 0
                ? null
                : saoChep(danhSach[viTri]);
    }

    // Cac khoa duoc ket hop bang AND.
    // Bo trong khoa nao thi khong loc theo khoa do.
    public KhachHang[] timKiem(
            String ma,
            String hoTen,
            String soDienThoai,
            String diaChi,
            boolean chinhXac) {

        KhachHang[] ketQua = new KhachHang[soLuong];
        int dem = 0;

        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];

            if (khop(kh.getMaKhachHang(), ma, chinhXac)
                    && khop(kh.getHoTen(), hoTen, chinhXac)
                    && khop(kh.getSoDienThoai(), soDienThoai, chinhXac)
                    && khop(kh.getDiaChi(), diaChi, chinhXac)) {

                ketQua[dem++] = saoChep(kh);
            }
        }

        return Arrays.copyOf(ketQua, dem);
    }

    public String thongKe() {
        return "Tong so khach hang: " + soLuong
                + "\nTheo dia chi:\n"
                + thongKeTheoNhom(true)
                + "Theo dau so dien thoai "
                + "(3 chu so dau, bo dau +):\n"
                + thongKeTheoNhom(false);
    }

    private String thongKeTheoNhom(boolean theoDiaChi) {
        String[] nhom = new String[soLuong];
        int[] soKhach = new int[soLuong];
        int soNhom = 0;

        for (int i = 0; i < soLuong; i++) {
            String khoa = danhSach[i].getDiaChi();

            if (!theoDiaChi) {
                khoa = danhSach[i].getSoDienThoai()
                        .replace("+", "")
                        .substring(0, 3);
            }

            int viTri = 0;

            while (viTri < soNhom
                    && !nhom[viTri].equalsIgnoreCase(khoa)) {
                viTri++;
            }

            if (viTri == soNhom) {
                nhom[soNhom++] = khoa;
            }

            soKhach[viTri]++;
        }

        if (soNhom == 0) {
            return "  Chua co du lieu.\n";
        }

        StringBuilder ketQua = new StringBuilder();

        for (int i = 0; i < soNhom; i++) {
            ketQua.append("  ")
                    .append(nhom[i])
                    .append(": ")
                    .append(soKhach[i])
                    .append(" khach hang\n");
        }

        return ketQua.toString();
    }

    @Override
    public void ghiFile(String duongDan) throws IOException {
        List<String> dong = new ArrayList<>();
        dong.add(TIEU_DE_FILE);
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            dong.add(FileText.dong(kh.getMaKhachHang(), kh.getHoTen(),
                    kh.getSoDienThoai(), kh.getDiaChi()));
        }
        FileText.ghi(duongDan, dong);
    }

    @Override
    public void docFile(String duongDan) throws IOException {
        DanhSachKhachHang moi = new DanhSachKhachHang();

        List<String[]> dong = FileText.doc(duongDan, TIEU_DE_FILE, TIEU_DE_CU);
        for (int i = 0; i < dong.size(); i++) {
            String[] cot = dong.get(i);
            try {
                if (cot.length != 4) throw new IllegalArgumentException("Phai co 4 cot.");
                moi.them(new KhachHang(cot[0], cot[1], cot[2], cot[3]));
            } catch (IllegalArgumentException e) {
                throw new IOException("Dong " + (i + 2) + ": " + e.getMessage(), e);
            }
        }

        // Chi thay du lieu sau khi doc thanh cong toan bo file.
        danhSach = moi.danhSach;
        soLuong = moi.soLuong;
    }

    public static boolean maHopLe(String ma) {
        return ma != null
                && ma.trim().matches("[A-Za-z0-9_-]+");
    }

    public static boolean soDienThoaiHopLe(String soDienThoai) {
        return soDienThoai != null
                && soDienThoai.trim().matches("\\+?[0-9]{9,15}");
    }

    private int chiSoTheoMa(String ma) {
        if (ma != null) {
            for (int i = 0; i < soLuong; i++) {
                if (danhSach[i].getMaKhachHang()
                        .equalsIgnoreCase(ma.trim())) {
                    return i;
                }
            }
        }

        return -1;
    }

    private static KhachHang kiemTraVaSaoChep(KhachHang kh) {
        if (kh == null || !maHopLe(kh.getMaKhachHang())) {
            throw new IllegalArgumentException(
                    "Ma chi gom chu, so, dau _ hoac -.");
        }

        if (kh.getHoTen() == null
                || kh.getHoTen().trim().isEmpty()
                || kh.getDiaChi() == null
                || kh.getDiaChi().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Ho ten va dia chi khong duoc de trong.");
        }

        if (!soDienThoaiHopLe(kh.getSoDienThoai())) {
            throw new IllegalArgumentException(
                    "So dien thoai phai co 9-15 chu so, "
                    + "co the bat dau bang +.");
        }

        return new KhachHang(
                kh.getMaKhachHang().trim(),
                kh.getHoTen().trim(),
                kh.getSoDienThoai().trim(),
                kh.getDiaChi().trim()
        );
    }

    private static KhachHang saoChep(KhachHang kh) {
        return new KhachHang(
                kh.getMaKhachHang(),
                kh.getHoTen(),
                kh.getSoDienThoai(),
                kh.getDiaChi()
        );
    }

    private static boolean khop(
            String giaTri, String tuKhoa, boolean chinhXac) {

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return true;
        }

        String a = giaTri.toLowerCase(Locale.ROOT);
        String b = tuKhoa.trim().toLowerCase(Locale.ROOT);

        return chinhXac ? a.equals(b) : a.contains(b);
    }

}
