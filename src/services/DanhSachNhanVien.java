package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import models.NhanVien;

public final class DanhSachNhanVien implements LuuTruFile {
    private static final int SUC_CHUA_BAN_DAU = 10;
    private static final String TIEU_DE =
            "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi";

    private NhanVien[] danhSach;
    private int soLuong;

    public DanhSachNhanVien() {
        danhSach = new NhanVien[SUC_CHUA_BAN_DAU];
    }

    public DanhSachNhanVien(NhanVien[] banDau) {
        this();
        nhapDanhSach(banDau);
    }

    public int getSoLuong() {
        return soLuong;
    }

    public NhanVien[] getDanhSach() {
        NhanVien[] ketQua = new NhanVien[soLuong];

        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = saoChep(danhSach[i]);
        }

        return ketQua;
    }

    public void nhapDanhSach(NhanVien[] duLieu) {
        if (duLieu == null) {
            throw new IllegalArgumentException(
                    "Danh sach khong duoc null.");
        }

        DanhSachNhanVien moi = new DanhSachNhanVien();

        for (NhanVien nv : duLieu) {
            moi.them(nv);
        }

        danhSach = moi.danhSach;
        soLuong = moi.soLuong;
    }

    public void them(NhanVien nv) {
        NhanVien hopLe = kiemTraVaSaoChep(nv);

        if (chiSoTheoMa(hopLe.getMaNhanVien()) >= 0) {
            throw new IllegalArgumentException(
                    "Ma nhan vien da ton tai!");
        }

        if (soLuong == danhSach.length) {
            danhSach = Arrays.copyOf(
                    danhSach, Math.max(1, danhSach.length * 2));
        }

        danhSach[soLuong++] = hopLe;
    }

    public void themNhieu(NhanVien[] boSung) {
        if (boSung == null) {
            throw new IllegalArgumentException(
                    "Danh sach khong duoc null.");
        }

        DanhSachNhanVien moi =
                new DanhSachNhanVien(getDanhSach());

        for (NhanVien nv : boSung) {
            moi.them(nv);
        }

        danhSach = moi.danhSach;
        soLuong = moi.soLuong;
    }

    public boolean sua(
            String ma, String ten, String ca,
            String sdt, String diaChi) {

        int viTri = chiSoTheoMa(ma);

        if (viTri < 0) {
            return false;
        }

        danhSach[viTri] = kiemTraVaSaoChep(
                new NhanVien(
                        danhSach[viTri].getMaNhanVien(),
                        ten, ca, sdt, diaChi
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

    public NhanVien timTheoMa(String ma) {
        int viTri = chiSoTheoMa(ma);

        return viTri < 0
                ? null
                : saoChep(danhSach[viTri]);
    }

    // Bo trong khoa khong can loc.
    // Cac khoa da nhap duoc ket hop bang AND.
    public NhanVien[] timKiem(
            String ma, String ten, String ca,
            String sdt, String diaChi, boolean chinhXac) {

        NhanVien[] ketQua = new NhanVien[soLuong];
        int dem = 0;

        for (int i = 0; i < soLuong; i++) {
            NhanVien nv = danhSach[i];

            if (khop(nv.getMaNhanVien(), ma, chinhXac)
                    && khop(nv.getHoTen(), ten, chinhXac)
                    && khop(nv.getCaLamViec(), ca, chinhXac)
                    && khop(nv.getSoDienThoai(), sdt, chinhXac)
                    && khop(nv.getDiaChi(), diaChi, chinhXac)) {

                ketQua[dem++] = saoChep(nv);
            }
        }

        return Arrays.copyOf(ketQua, dem);
    }

    public String thongKe() {
        return "Tong so nhan vien: " + soLuong
                + "\nTheo ca lam viec:\n"
                + thongKeTheoNhom(true)
                + "Theo dia chi:\n"
                + thongKeTheoNhom(false);
    }

    private String thongKeTheoNhom(boolean theoCa) {
        String[] nhom = new String[soLuong];
        int[] dem = new int[soLuong];
        int soNhom = 0;

        for (int i = 0; i < soLuong; i++) {
            String khoa = theoCa
                    ? danhSach[i].getCaLamViec()
                    : danhSach[i].getDiaChi();

            int viTri = 0;

            while (viTri < soNhom
                    && !nhom[viTri].equalsIgnoreCase(khoa)) {
                viTri++;
            }

            if (viTri == soNhom) {
                nhom[soNhom++] = khoa;
            }

            dem[viTri]++;
        }

        if (soNhom == 0) {
            return "  Chua co du lieu.\n";
        }

        StringBuilder ketQua = new StringBuilder();

        for (int i = 0; i < soNhom; i++) {
            ketQua.append("  ")
                    .append(nhom[i])
                    .append(": ")
                    .append(dem[i])
                    .append(" nhan vien\n");
        }

        return ketQua.toString();
    }

    @Override
    public void ghiFile(String duongDan) throws IOException {
        List<String> dong = new ArrayList<>();
        dong.add(TIEU_DE);

        for (int i = 0; i < soLuong; i++) {
            NhanVien nv = danhSach[i];

            dong.add(FileText.dong(
                    nv.getMaNhanVien(),
                    nv.getHoTen(),
                    nv.getCaLamViec(),
                    nv.getSoDienThoai(),
                    nv.getDiaChi()
            ));
        }

        FileText.ghi(duongDan, dong);
    }

    @Override
    public void docFile(String duongDan) throws IOException {
        DanhSachNhanVien moi = new DanhSachNhanVien();

        List<String[]> dong = FileText.doc(
                duongDan, TIEU_DE);

        for (int i = 0; i < dong.size(); i++) {
            String[] cot = dong.get(i);

            try {
                if (cot.length != 5) {
                    throw new IllegalArgumentException(
                            "Phai co 5 cot.");
                }

                moi.them(new NhanVien(
                        cot[0], cot[1], cot[2], cot[3], cot[4]));

            } catch (IllegalArgumentException e) {
                throw new IOException(
                        "Dong " + (i + 2) + ": " + e.getMessage(), e);
            }
        }

        // Chi thay danh sach khi doc thanh cong toan bo file.
        danhSach = moi.danhSach;
        soLuong = moi.soLuong;
    }

    private int chiSoTheoMa(String ma) {
        if (ma != null) {
            for (int i = 0; i < soLuong; i++) {
                if (danhSach[i].getMaNhanVien()
                        .equalsIgnoreCase(ma.trim())) {
                    return i;
                }
            }
        }

        return -1;
    }

    private static NhanVien kiemTraVaSaoChep(NhanVien nv) {
        if (nv == null
                || !DanhSachKhachHang.maHopLe(nv.getMaNhanVien())) {
            throw new IllegalArgumentException(
                    "Ma chi gom chu, so, dau _ hoac -.");
        }

        if (rong(nv.getHoTen())
                || rong(nv.getCaLamViec())
                || rong(nv.getDiaChi())) {
            throw new IllegalArgumentException(
                    "Ho ten, ca lam viec va dia chi khong duoc rong.");
        }

        if (!DanhSachKhachHang.soDienThoaiHopLe(
                nv.getSoDienThoai())) {
            throw new IllegalArgumentException(
                    "So dien thoai phai co 9-15 chu so, "
                    + "co the bat dau bang +.");
        }

        return new NhanVien(
                nv.getMaNhanVien().trim(),
                nv.getHoTen().trim(),
                nv.getCaLamViec().trim(),
                nv.getSoDienThoai().trim(),
                nv.getDiaChi().trim()
        );
    }

    private static boolean rong(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static NhanVien saoChep(NhanVien nv) {
        return new NhanVien(
                nv.getMaNhanVien(),
                nv.getHoTen(),
                nv.getCaLamViec(),
                nv.getSoDienThoai(),
                nv.getDiaChi()
        );
    }

    private static boolean khop(
            String giaTri, String khoa, boolean chinhXac) {

        if (rong(khoa)) {
            return true;
        }

        String a = giaTri.toLowerCase(Locale.ROOT);
        String b = khoa.trim().toLowerCase(Locale.ROOT);

        return chinhXac ? a.equals(b) : a.contains(b);
    }

    // ================= MENU NHAN VIEN =================

    public void quanLy(Scanner sc, String duongDan) {
    while (true) {
        System.out.println("\n===== QUAN LY NHAN VIEN =====");
        System.out.println("1. Xem danh sach nhan vien");
        System.out.println("2. Them 1 nhan vien");
        System.out.println("3. Sua theo ma");
        System.out.println("4. Xoa theo ma");
        System.out.println("5. Nhap danh sach moi gom n nhan vien");
        System.out.println("6. Them k nhan vien");
        System.out.println("7. Tim kiem theo nhieu khoa");
        System.out.println("8. Thong ke");
        System.out.println("9. Doc lai tu file (thay danh sach hien tai)");
        System.out.println("10. Luu vao file");
        System.out.println("0. Quay lai");

        String chon = docDong(sc, "Nhap lua chon: ");

        if (chon == null || chon.equals("0")) {
            return;
        }

        try {
            switch (chon) {
                case "1":
                    hienThi(getDanhSach());
                    break;

                case "2":
                    // Them ngay 1 nguoi, khong hoi so luong.
                    if (nhapTuBanPhim(sc, false, 1)) {
                        luu(duongDan);
                    }
                    break;

                case "3":
                    if (suaTuBanPhim(sc)) {
                        luu(duongDan);
                    }
                    break;

                case "4": {
                    String ma = batBuoc(sc, "Ma nhan vien can xoa: ");

                    if (ma == null) {
                        return;
                    }

                    if (xoa(ma)) {
                        System.out.println("Da xoa nhan vien.");
                        luu(duongDan);
                    } else {
                        System.out.println("Khong tim thay nhan vien!");
                    }
                    break;
                }

                case "5":
                    if (nhapTuBanPhim(sc, true)) {
                        luu(duongDan);
                    }
                    break;

                case "6":
                    // Hoi so luong k can them.
                    if (nhapTuBanPhim(sc, false)) {
                        luu(duongDan);
                    }
                    break;

                case "7":
                    timTuBanPhim(sc);
                    break;

                case "8":
                    System.out.println(thongKe());
                    break;

                case "9":
                    docFile(duongDan);
                    System.out.println("Da doc file nhan vien.");
                    break;

                case "10":
                    luu(duongDan);
                    break;

                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Dung khi nhap danh sach moi hoac them k nhan vien.
private boolean nhapTuBanPhim(Scanner sc, boolean thayMoi) {
    if (thayMoi) {
        System.out.println(
                "Danh sach moi se thay the danh sach hien tai "
                + "sau khi nhap xong.");
    }

    Integer n = nhapSoLuong(
            sc,
            thayMoi
                    ? "Nhap so luong nhan vien: "
                    : "Nhap so luong nhan vien muon them (0 de huy): "
    );

    if (n == null || (!thayMoi && n == 0)) {
        return false;
    }

    return nhapTuBanPhim(sc, thayMoi, n);
}

// Xu ly nhap thong tin cho n nhan vien.
private boolean nhapTuBanPhim(Scanner sc, boolean thayMoi, int n) {
    DanhSachNhanVien moi = thayMoi
            ? new DanhSachNhanVien()
            : new DanhSachNhanVien(getDanhSach());

    for (int i = 0; i < n; i++) {
        System.out.println(
                "--- Nhan vien " + (i + 1) + "/" + n + " ---");

        String ma;

        while (true) {
            ma = batBuoc(sc, "Ma nhan vien: ");

            if (ma == null) {
                return false;
            }

            if (!DanhSachKhachHang.maHopLe(ma)) {
                System.out.println("Ma khong hop le.");
            } else if (moi.timTheoMa(ma) != null) {
                System.out.println("Ma nhan vien da ton tai!");
            } else {
                break;
            }
        }

            String ten = batBuoc(sc, "Ho ten: ");
            if (ten == null) {
                return false;
            }

            String ca = batBuoc(sc, "Ca lam viec: ");
            if (ca == null) {
                return false;
            }

            String sdt = nhapDienThoai(
                    sc, "So dien thoai: ", false);
            if (sdt == null) {
                return false;
            }

            String diaChi = batBuoc(sc, "Dia chi: ");
            if (diaChi == null) {
                return false;
            }

            moi.them(new NhanVien(ma, ten, ca, sdt, diaChi));
        }

        danhSach = moi.danhSach;
        soLuong = moi.soLuong;

        System.out.println(
                "Da " + (thayMoi ? "nhap moi " : "them ")
                + n + " nhan vien.");

        return true;
    }
    private boolean suaTuBanPhim(Scanner sc) {
        String ma = batBuoc(sc, "Ma nhan vien can sua: ");

        if (ma == null) {
            return false;
        }

        NhanVien cu = timTheoMa(ma);

        if (cu == null) {
            System.out.println("Khong tim thay nhan vien!");
            return false;
        }

        hienThi(new NhanVien[]{cu});

        System.out.println(
                "Nhan Enter de giu gia tri cu. "
                + "Ma nhan vien duoc giu nguyen.");

        String ten = docDong(sc, "Ho ten moi: ");
        if (ten == null) {
            return false;
        }

        String ca = docDong(sc, "Ca lam viec moi: ");
        if (ca == null) {
            return false;
        }

        String sdt = nhapDienThoai(
                sc, "So dien thoai moi: ", true);
        if (sdt == null) {
            return false;
        }

        String diaChi = docDong(sc, "Dia chi moi: ");
        if (diaChi == null) {
            return false;
        }

        sua(
                ma,
                ten.isEmpty() ? cu.getHoTen() : ten,
                ca.isEmpty() ? cu.getCaLamViec() : ca,
                sdt.isEmpty() ? cu.getSoDienThoai() : sdt,
                diaChi.isEmpty() ? cu.getDiaChi() : diaChi
        );

        System.out.println("Da cap nhat nhan vien.");
        return true;
    }

    private void timTuBanPhim(Scanner sc) {
        String kieu = docDong(
                sc, "Tim 1=chinh xac, 2=chua tu khoa: ");

        if (kieu == null) {
            return;
        }

        if (!kieu.equals("1") && !kieu.equals("2")) {
            System.out.println("Lua chon khong hop le!");
            return;
        }

        System.out.println(
                "Bo trong khoa khong can loc; "
                + "cac khoa duoc ket hop bang AND.");

        String[] nhan = {
            "Ma nhan vien: ",
            "Ho ten: ",
            "Ca lam viec: ",
            "So dien thoai: ",
            "Dia chi: "
        };

        String[] khoa = new String[5];

        for (int i = 0; i < khoa.length; i++) {
            khoa[i] = docDong(sc, nhan[i]);

            if (khoa[i] == null) {
                return;
            }
        }

        hienThi(timKiem(
                khoa[0], khoa[1], khoa[2],
                khoa[3], khoa[4], kieu.equals("1")
        ));
    }

    private static void hienThi(NhanVien[] ds) {
        System.out.println("===== DANH SACH NHAN VIEN =====");

        if (ds.length == 0) {
            System.out.println("Danh sach nhan vien dang trong.");
        }

        for (NhanVien nv : ds) {
            System.out.println(
                    nv.thongTinNhanVien()
                    + "\nCa lam viec: " + nv.getCaLamViec()
                    + "\n------------------------------");
        }

        System.out.println("Tong so nhan vien: " + ds.length);
    }

    private void luu(String duongDan) {
        try {
            ghiFile(duongDan);
            System.out.println("Da luu file nhan vien.");
        } catch (IOException e) {
            System.out.println(
                "CHUA LUU DUOC " + duongDan
                + ": " + e.getMessage());
            System.out.println(
                "Du lieu moi chi dang trong bo nho. "
                + "Chon 10 de thu luu lai.");
        }
    }

    private static String docDong(Scanner sc, String nhan) {
        System.out.print(nhan);

        return sc.hasNextLine()
                ? sc.nextLine().trim()
                : null;
    }

    private static String batBuoc(Scanner sc, String nhan) {
        while (true) {
            String s = docDong(sc, nhan);

            if (s == null || !s.isEmpty()) {
                return s;
            }

            System.out.println("Thong tin khong duoc rong!");
        }
    }

    private static Integer nhapSoLuong(
            Scanner sc, String nhan) {

        while (true) {
            String s = docDong(sc, nhan);

            if (s == null) {
                return null;
            }

            try {
                int n = Integer.parseInt(s);

                if (n >= 0) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // Cho nhap lai.
            }

            System.out.println("Nhap so nguyen >= 0.");
        }
    }

    private static String nhapDienThoai(
            Scanner sc, String nhan, boolean boQua) {

        while (true) {
            String s = docDong(sc, nhan);

            if (s == null || (boQua && s.isEmpty())) {
                return s;
            }

            if (DanhSachKhachHang.soDienThoaiHopLe(s)) {
                return s;
            }

            System.out.println(
                    "So dien thoai phai co 9-15 chu so, "
                    + "co the bat dau bang +.");
        }
    }
}