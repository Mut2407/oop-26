import models.*;
import services.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Chuong trinh kiem thu rieng, khong tham gia menu cua do an.
public class KiemThu {
    private static int soKiemTra;
    private static final LocalDate NGAY = LocalDate.of(2026, 11, 9);

    public static void main(String[] args) throws IOException {
        kiemThuMang();
        kiemThuChiTiet();
        kiemThuBanHang();
        kiemThuNhapHang();
        kiemThuThamChieu();
        kiemThuFile();
        kiemThuDuLieuSai();
        System.out.println("DAT " + soKiemTra + " kiem tra.");
    }

    private static void dung(boolean dieuKien, String ten) {
        soKiemTra++;
        if (!dieuKien)
            throw new AssertionError(ten);
    }

    private static BigDecimal tien(String so) {
        return new BigDecimal(so);
    }

    private static QuanLyCuaHang taoCuaHang(String thuMuc) {
        QuanLyCuaHang q = new QuanLyCuaHang(thuMuc);
        q.getDanhSachLoaiSP().them(new LoaiSP("L1", "Thể thao", "Loại mẫu"));
        q.getDanhSachKhachHang().them(new KhachHang("KH1", "Khách An", "0901234567", "TP.HCM"));
        q.getDanhSachNhanVien().them(new NhanVien("NV1", "Nhân viên Bình", "0902345678", "Sáng"));
        q.getDanhSachNhaCungCap().them(new NhaCungCap("CC1", "Nhà cung cấp", "0903456789", "Hà Nội"));
        themSanPhamMau(q);
        return q;
    }

    private static void themSanPhamMau(QuanLyCuaHang q) {
        LoaiSP l = q.getDanhSachLoaiSP().timTheoMa("L1");
        dung(q.themSanPham(new GiayTheThao("SP1", "Giày", "A", tien("150"), l, 42, "Đen", "Vải", "Cao su")), "Them giay");
        dung(q.themSanPham(new DungCuTheThao("SP2", "Bóng", "B", tien("250"), l, "Bóng đá", "Da", 0.45)), "Them bong");
        dung(q.themSanPham(new DungCuTheThao("SP3", "Vợt", "C", tien("350"), l, "Cầu lông", "Carbon", 0.085)), "Them dung cu");
    }

    private static HoaDon hoaDon(QuanLyCuaHang q, String ma, int a, int b) {
        HoaDon h = new HoaDon(ma, NGAY, q.getDanhSachKhachHang().timTheoMa("KH1"), q.getDanhSachNhanVien().timTheoMa("NV1"));
        if (a > 0)
            h.themChiTiet(new ChiTietHoaDon(q.getDanhSachSanPham().timTheoMa("SP1"), a, tien("150")));
        if (b > 0)
            h.themChiTiet(new ChiTietHoaDon(q.getDanhSachSanPham().timTheoMa("SP2"), b, tien("250")));
        return h;
    }

    private static PhieuNhap phieuNhap(QuanLyCuaHang q, String ma, int a, int b) {
        PhieuNhap p = new PhieuNhap(ma, NGAY, q.getDanhSachNhaCungCap().timTheoMa("CC1"), q.getDanhSachNhanVien().timTheoMa("NV1"));
        if (a > 0)
            p.themChiTiet(new ChiTietPhieuNhap(q.getDanhSachSanPham().timTheoMa("SP1"), a, tien("100")));
        if (b > 0)
            p.themChiTiet(new ChiTietPhieuNhap(q.getDanhSachSanPham().timTheoMa("SP2"), b, tien("200")));
        return p;
    }

    private static int ton(QuanLyCuaHang q, String ma) {
        return q.getKho().traCuuTon(ma);
    }

    private static void kiemThuMang() {
        kiemThuMangLoai();
        kiemThuMangSanPham();
        kiemThuMangNguoi();
        kiemThuMangChungTu();
    }

    private static void kiemThuMangLoai() {
        DanhSachLoaiSP ds = new DanhSachLoaiSP(1);
        for (int i = 0; i < 30; i++)
            dung(ds.them(new LoaiSP("L" + i, "Loai " + i, "Mo ta")), "Mo rong mang");
        dung(!ds.them(new LoaiSP(" l1 ", "Trung", "Trung")), "Ma trung khong phan biet hoa thuong");
        dung(ds.xoa("L0") && ds.xoa("L15") && ds.xoa("L29"), "Xoa dau giua cuoi");
        dung(ds.getSoLuong() == 27 && ds.timTheoMa("L16") != null, "Don mang dung");
        dung(!ds.sua("L1", new LoaiSP("Lkhac", "Moi", "Mo ta")), "Cam doi ma");
        kiemThuSuaLoai(ds);
    }

    private static void kiemThuSuaLoai(DanhSachLoaiSP ds) {
        LoaiSP cu = ds.timTheoMa("L1");
        IQuanLyDanhSach<LoaiSP> quanLy = ds;
        dung(quanLy.sua("L1", new LoaiSP("L1", "Da sua", "Moi")), "Goi qua interface");
        dung(ds.timTheoMa("L1") == cu && cu.getTenLoaiSP().equals("Da sua"), "Giu tham chieu loai");
        dung(quanLy.timKiem("da SUA").length == 1, "Tim gan dung");
        LoaiSP[] banSao = ds.getTatCa();
        banSao[0] = null;
        dung(ds.timTheoMa("L1") != null && !ds.them(null), "Bao ve cau truc mang/null");
    }

    private static void kiemThuMangSanPham() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        DanhSachSanPham ds = new DanhSachSanPham(1);
        dung(ds.themNhieu(q.getDanhSachSanPham().getTatCa()) == 3, "Them k SP da hinh");
        dung(ds.locTheoLoaiSP(" l1 ").length == 3, "Loc theo loai");
        dung(ds.locTheoKhoangGia(tien("150"), tien("250")).length == 2, "Loc gia gom hai bien");
        SanPham sp = ds.timTheoMa("SP1");
        dung(sp.hienThiThongTin().contains("42.0"), "Goi hien thi da hinh");
        kiemThuSuaSanPham(q, ds, sp);
    }

    private static void kiemThuSuaSanPham(QuanLyCuaHang q, DanhSachSanPham ds, SanPham cu) {
        LoaiSP l = q.getDanhSachLoaiSP().timTheoMa("L1");
        cu.capNhatTon(7);
        SanPham moi = new GiayTheThao("SP1", "Giay moi", "X", tien("900"), l, 43, "Do", "Da", "Cao su");
        dung(ds.sua("SP1", moi) && cu.getSoLuongTon() == 7, "Sua SP giu ton");
        dung(ds.timTheoMa("SP1") == cu && cu.getTenSanPham().equals("Giay moi"), "Sua SP tai cho");
        dung(!ds.sua("SP1", new DungCuTheThao("SP1", "Bong", "A", tien("1"), l, "Bong da", "Da", 0.45)), "Cam doi lop con");
        dung(ds.xoa("SP2") && ds.xoa("SP1") && ds.xoa("SP3"), "Xoa mang SP");
        dung(ds.getSoLuong() == 0 && ds.timKiem("x").length == 0, "Mang SP rong");
    }

    private static void kiemThuMangNguoi() {
        kiemThuMangKhach();
        kiemThuMangNhanVien();
        kiemThuMangNhaCungCap();
    }

    private static void kiemThuMangKhach() {
        DanhSachKhachHang ds = new DanhSachKhachHang(1);
        for (int i = 0; i < 3; i++)
            dung(ds.them(new KhachHang("K" + i, "An", "0901234567", "HCM")), "Them KH");
        KhachHang cu = ds.timTheoMa("K0");
        dung(ds.sua("K0", new KhachHang("K0", "Binh", "0901234567", "HN")), "Sua KH");
        dung(ds.timTheoMa("K0") == cu && ds.timKiem("Binh").length == 1, "Tham chieu/tim KH");
        dung(!ds.them(cu) && !ds.xoa("khongco"), "Trung/thieu KH");
        dung(ds.xoa("K1") && ds.xoa("K0") && ds.xoa("K2") && ds.getSoLuong() == 0, "Xoa KH");
    }

    private static void kiemThuMangNhanVien() {
        DanhSachNhanVien ds = new DanhSachNhanVien(1);
        for (int i = 0; i < 3; i++)
            dung(ds.them(new NhanVien("N" + i, "An", "0901234567", "Sang")), "Them NV");
        NhanVien cu = ds.timTheoMa("N0");
        dung(ds.sua("N0", new NhanVien("N0", "Binh", "0901234567", "Toi")), "Sua NV");
        dung(ds.timTheoMa("N0") == cu && ds.timKiem("Binh").length == 1, "Tham chieu/tim NV");
        dung(!ds.them(cu) && !ds.xoa("khongco"), "Trung/thieu NV");
        dung(ds.xoa("N1") && ds.xoa("N0") && ds.xoa("N2") && ds.getSoLuong() == 0, "Xoa NV");
    }

    private static void kiemThuMangNhaCungCap() {
        DanhSachNhaCungCap ds = new DanhSachNhaCungCap(1);
        for (int i = 0; i < 3; i++)
            dung(ds.them(new NhaCungCap("C" + i, "An", "0901234567", "HCM")), "Them NCC");
        NhaCungCap cu = ds.timTheoMa("C0");
        dung(ds.sua("C0", new NhaCungCap("C0", "Binh", "0901234567", "HN")), "Sua NCC");
        dung(ds.timTheoMa("C0") == cu && ds.timKiem("Binh").length == 1, "Tham chieu/tim NCC");
        dung(!ds.them(cu) && !ds.xoa("khongco"), "Trung/thieu NCC");
        dung(ds.xoa("C1") && ds.xoa("C0") && ds.xoa("C2") && ds.getSoLuong() == 0, "Xoa NCC");
    }

    private static void kiemThuMangChungTu() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        DanhSachHoaDon hd = new DanhSachHoaDon(1);
        DanhSachPhieuNhap pn = new DanhSachPhieuNhap(1);
        for (int i = 0; i < 3; i++) {
            dung(hd.them(hoaDon(q, "H" + i, 1, 0)), "Mo rong mang HD");
            dung(pn.them(phieuNhap(q, "P" + i, 1, 0)), "Mo rong mang PN");
        }
        kiemThuSuaMangChungTu(q, hd, pn);
    }

    private static void kiemThuSuaMangChungTu(QuanLyCuaHang q, DanhSachHoaDon hd, DanhSachPhieuNhap pn) {
        dung(!hd.them(hoaDon(q, "h0", 1, 0)) && !pn.them(phieuNhap(q, "p0", 1, 0)), "Trung ma chung tu");
        dung(hd.sua("H0", hoaDon(q, "H0", 2, 0)) && pn.sua("P0", phieuNhap(q, "P0", 2, 0)), "Sua chung tu");
        dung(hd.timKiem("KH1").length == 3 && pn.timKiem("CC1").length == 3, "Tim ma doi tac");
        dung(hd.thongKeTongTien().compareTo(tien("600")) == 0, "Tong HD");
        dung(pn.thongKeTongTien(NGAY, NGAY).compareTo(tien("400")) == 0, "Tong PN gom bien ngay");
        dung(hd.thongKeTongTien(NGAY.plusDays(1), NGAY.plusDays(2)).signum() == 0, "Khoang ngay khong co HD");
        dung(hd.xoa("H1") && hd.xoa("H0") && hd.xoa("H2"), "Xoa HD dau giua cuoi");
        dung(pn.xoa("P1") && pn.xoa("P0") && pn.xoa("P2"), "Xoa PN dau giua cuoi");
        dung(hd.thongKeTongTien().signum() == 0 && pn.thongKeTongTien().signum() == 0, "Thong ke rong");
    }

    private static void kiemThuChiTiet() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        HoaDon h = hoaDon(q, "H1", 2, 1);
        SanPham sp = q.getDanhSachSanPham().timTheoMa("SP1");
        dung(!h.themChiTiet(new ChiTietHoaDon(sp, 1, tien("1"))), "Tu choi dong trung SP");
        HoaDon banSao = new HoaDon(h);
        dung(banSao.suaChiTiet("SP1", new ChiTietHoaDon(sp, 5, tien("12.5"))), "Sua dong");
        dung(h.timChiTiet("SP1").getSoLuong() == 2, "Sao chep ca dong chi tiet");
        dung(banSao.xoaChiTiet("SP2") && banSao.tinhTongTien().compareTo(tien("62.5")) == 0, "Xoa/tinh tien thap phan");
        kiemThuMoRongChiTiet(q);
    }

    private static void kiemThuMoRongChiTiet(QuanLyCuaHang q) {
        HoaDon h = hoaDon(q, "H1", 0, 0);
        PhieuNhap p = phieuNhap(q, "P1", 0, 0);
        for (int i = 0; i < 12; i++) {
            SanPham sp = new DungCuTheThao("X" + i, "Bong", "A", tien("1"), q.getDanhSachLoaiSP().timTheoMa("L1"), "Da bong", "Da", 0.4);
            dung(h.themChiTiet(new ChiTietHoaDon(sp, 1, tien("1"))), "Mo rong chi tiet HD");
            dung(p.themChiTiet(new ChiTietPhieuNhap(sp, 1, tien("2"))), "Mo rong chi tiet PN");
        }
        dung(h.getSoLuongChiTiet() == 12 && p.getSoLuongChiTiet() == 12, "So dong chi tiet");
    }

    private static void kiemThuBanHang() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        dung(q.themPhieuNhap(phieuNhap(q, "P1", 10, 10)), "Nhap dau ky");
        HoaDon h = hoaDon(q, "H1", 3, 0);
        dung(q.themHoaDon(h) && ton(q, "SP1") == 7, "Ban 3, ton 7");
        h.xoaChiTiet("SP1");
        dung(q.getDanhSachHoaDon().timTheoMa("H1").getSoLuongChiTiet() == 1, "Giu ban luu doc lap");
        q.getDanhSachSanPham().timTheoMa("SP1").setGiaBan(tien("999"));
        dung(q.getDanhSachHoaDon().thongKeTongTien().compareTo(tien("450")) == 0, "Giu don gia lich su");
        kiemThuSuaBanHang(q);
    }

    private static void kiemThuSuaBanHang(QuanLyCuaHang q) {
        dung(q.suaHoaDon("H1", hoaDon(q, "H1", 5, 0)) && ton(q, "SP1") == 5, "Sua ban 3 thanh 5");
        dung(!q.suaHoaDon("H1", hoaDon(q, "H1", 1, 11)), "Tu choi sua thieu dong thu hai");
        dung(ton(q, "SP1") == 5 && ton(q, "SP2") == 10, "Sua loi giu nguyen moi ton");
        dung(q.getDanhSachHoaDon().timTheoMa("H1").timChiTiet("SP1").getSoLuong() == 5, "Sua loi giu chung tu");
        dung(q.suaHoaDon("H1", hoaDon(q, "H1", 0, 4)), "Doi SP tren hoa don");
        dung(ton(q, "SP1") == 10 && ton(q, "SP2") == 6, "Tinh ca SP chi co o ban cu");
        dung(q.xoaHoaDon("H1") && ton(q, "SP2") == 10, "Xoa HD hoan ton");
        dung(!q.themHoaDon(hoaDon(q, "H2", 1, 11)) && ton(q, "SP1") == 10, "Them loi khong tru mot phan");
        kiemThuHoaDonNgoai(q);
    }

    private static void kiemThuHoaDonNgoai(QuanLyCuaHang q) {
        HoaDon h = hoaDon(q, "H3", 1, 0);
        LoaiSP l = q.getDanhSachLoaiSP().timTheoMa("L1");
        SanPham ngoai = new GiayTheThao("SP1", "Gia mao", "A", tien("1"), l, 42, "Do", "Vai", "De");
        h.suaChiTiet("SP1", new ChiTietHoaDon(ngoai, 1, tien("150")));
        dung(!q.themHoaDon(h) && ton(q, "SP1") == 10, "Tu choi tham chieu ngoai danh sach");
        dung(q.getDanhSachHoaDon().getSoLuong() == 0, "Khong luu HD loi");
        dung(!q.themHoaDon(hoaDon(q, "RONG", 0, 0)), "Tu choi HD rong");
    }

    private static void kiemThuNhapHang() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        dung(q.themPhieuNhap(phieuNhap(q, "P1", 10, 8)), "Nhap 10 va 8");
        dung(q.themHoaDon(hoaDon(q, "H1", 8, 0)), "Ban 8");
        dung(!q.xoaPhieuNhap("P1") && ton(q, "SP1") == 2, "Khong xoa PN lam am ton");
        dung(!q.suaPhieuNhap("P1", phieuNhap(q, "P1", 6, 8)), "Khong giam nhap qua ton");
        dung(q.getDanhSachPhieuNhap().timTheoMa("P1").timChiTiet("SP1").getSoLuong() == 10, "PN loi giu ban cu");
        kiemThuSuaNhapHang(q);
        kiemThuTranTon();
    }

    private static void kiemThuSuaNhapHang(QuanLyCuaHang q) {
        dung(q.suaPhieuNhap("P1", phieuNhap(q, "P1", 9, 8)) && ton(q, "SP1") == 1, "Giam nhap 1");
        dung(q.suaPhieuNhap("P1", phieuNhap(q, "P1", 12, 8)) && ton(q, "SP1") == 4, "Tang nhap 3");
        dung(q.xoaHoaDon("H1") && ton(q, "SP1") == 12, "Hoan ban");
        dung(q.suaPhieuNhap("P1", phieuNhap(q, "P1", 0, 3)), "Doi dong PN");
        dung(ton(q, "SP1") == 0 && ton(q, "SP2") == 3, "PN tinh SP chi co o ban cu");
        dung(q.xoaPhieuNhap("P1") && ton(q, "SP2") == 0, "Xoa PN tru ton");
        dung(!q.xoaPhieuNhap("P1"), "Khong xoa hai lan");
    }

    private static void kiemThuTranTon() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        q.getDanhSachSanPham().timTheoMa("SP1").capNhatTon(Integer.MAX_VALUE);
        dung(!q.themPhieuNhap(phieuNhap(q, "P1", 1, 0)), "Khong tran so nguyen ton");
        dung(ton(q, "SP1") == Integer.MAX_VALUE && q.getDanhSachPhieuNhap().getSoLuong() == 0, "Tran ton giu du lieu");
    }

    private static void kiemThuThamChieu() {
        QuanLyCuaHang q = taoCuaHang("out/khong-dung");
        q.themPhieuNhap(phieuNhap(q, "P1", 10, 0));
        q.themHoaDon(hoaDon(q, "H1", 1, 0));
        dung(!q.xoaLoaiSP(" L1 ") && !q.xoaSanPham(" SP1 "), "Cam xoa loai/SP dang dung");
        dung(!q.xoaKhachHang(" KH1 ") && !q.xoaNhanVien(" NV1 "), "Cam xoa KH/NV dang dung");
        dung(!q.xoaNhaCungCap(" CC1 "), "Cam xoa NCC dang dung");
        q.getDanhSachKhachHang().sua("KH1", new KhachHang("KH1", "Tên mới", "0901234567", "Huế"));
        dung(q.getDanhSachHoaDon().timTheoMa("H1").getKhachHang().getHoTen().equals("Tên mới"), "Sua nguoi giu lien ket");
        kiemThuXoaSauThamChieu(q);
    }

    private static void kiemThuXoaSauThamChieu(QuanLyCuaHang q) {
        dung(q.xoaHoaDon("H1") && q.xoaKhachHang("KH1"), "Xoa KH khi het tham chieu");
        dung(q.xoaPhieuNhap("P1") && q.xoaNhaCungCap("CC1") && q.xoaNhanVien("NV1"), "Xoa NCC/NV het tham chieu");
        dung(q.xoaSanPham("SP1") && q.xoaSanPham("SP2") && q.xoaSanPham("SP3"), "Xoa SP het ton va tham chieu");
        dung(q.xoaLoaiSP("L1"), "Xoa loai het SP");
    }

    private static String thuMucMoi() throws IOException {
        File f = new File("out/kiemthu-" + System.nanoTime());
        if (!f.mkdirs())
            throw new IOException("Khong tao duoc thu muc kiem thu.");
        return f.getPath();
    }

    private static String docChuoi(File f) throws IOException {
        Reader in = new InputStreamReader(new FileInputStream(f), "UTF-8");
        String kq = "";
        char[] dem = new char[4096];
        int n;
        while ((n = in.read(dem)) != -1)
            kq += new String(dem, 0, n);
        in.close();
        return kq;
    }

    private static void ghiChuoi(File f, String s) throws IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        out.write(s);
        out.close();
    }

    private static void kiemThuFile() throws IOException {
        kiemThuGhiDoc();
        kiemThuFileLoi();
        kiemThuGhiLoi();
        kiemThuThuMucRong();
        kiemThuFileKhachHang();
        kiemThuFileRieng();
        kiemThuDuLieuMau();
    }

    private static void kiemThuGhiDoc() throws IOException {
        String dir = thuMucMoi();
        QuanLyCuaHang q = taoCuaHang(dir);
        q.themPhieuNhap(phieuNhap(q, "P1", 10, 10));
        q.themHoaDon(hoaDon(q, "H1", 2, 1));
        q.ghiDuLieu();
        QuanLyCuaHang moi = new QuanLyCuaHang(dir);
        moi.docDuLieu();
        kiemTraSauDoc(moi);
        kiemThuDocNhieuLan(q, moi);
    }

    private static void kiemTraSauDoc(QuanLyCuaHang q) {
        dung(ton(q, "SP1") == 8 && ton(q, "SP2") == 9, "Doc khong cong/tru lai ton");
        dung(q.getDanhSachSanPham().timTheoMa("SP1") instanceof GiayTheThao, "Doc dung lop giay");
        dung(q.getDanhSachSanPham().timTheoMa("SP2") instanceof DungCuTheThao, "Doc dung lop bong");
        dung(q.getDanhSachSanPham().timTheoMa("SP3") instanceof DungCuTheThao, "Doc dung lop dung cu");
        dung(q.getDanhSachLoaiSP().timTheoMa("L1").getTenLoaiSP().equals("Thể thao"), "Text UTF-8");
        HoaDon h = q.getDanhSachHoaDon().timTheoMa("H1");
        dung(h.timChiTiet("SP1").getSanPham() == q.getDanhSachSanPham().timTheoMa("SP1"), "Noi lai SP");
        dung(h.getKhachHang() == q.getDanhSachKhachHang().timTheoMa("KH1"), "Noi lai KH");
        dung(h.tinhTongTien().compareTo(tien("550")) == 0, "Giu don gia HD qua file");
    }

    private static void kiemThuDocNhieuLan(QuanLyCuaHang q, QuanLyCuaHang moi) throws IOException {
        moi.docDuLieu();
        dung(ton(moi, "SP1") == 8 && moi.getDanhSachHoaDon().getSoLuong() == 1, "Doc lai khong nhan doi");
        q.getDanhSachSanPham().timTheoMa("SP1").setGiaBan(tien("999"));
        q.ghiDuLieu();
        moi.docDuLieu();
        dung(moi.getDanhSachSanPham().timTheoMa("SP1").getGiaBan().compareTo(tien("999")) == 0, "Ghi de lan hai");
        dung(moi.getDanhSachHoaDon().thongKeTongTien().compareTo(tien("550")) == 0, "Gia hien tai khong doi gia lich su");
        dung(moi.xoaHoaDon("H1") && ton(moi, "SP1") == 10, "Kho van dung sau nap lai");
    }

    private static void kiemThuFileLoi() throws IOException {
        String dir = thuMucMoi();
        QuanLyCuaHang q = taoCuaHang(dir);
        q.themPhieuNhap(phieuNhap(q, "P1", 10, 0));
        q.themHoaDon(hoaDon(q, "H1", 2, 0));
        q.ghiDuLieu();
        File f = new File(dir, "chitiethoadon.txt");
        String dung = docChuoi(f);
        thuDocLoi(q, f, dung.replace("SP1|2|150", "KHONGCO|2|150"));
        thuDocLoi(q, f, dung.substring(0, dung.length() / 2));
        thuDocLoi(q, f, dung.replace("CHI_TIET_HOA_DON", "SAI_NHAN"));
        thuDocLoi(q, f, dung.replace("H1|SP1", "KHONGCO|SP1"));
        thuDocLoi(q, f, dung + "H1|SP1|1|150\n");
        thuDocLoi(q, f, dung.replace("SP1|2|150", "SP1|abc|150"));
    }

    private static void thuDocLoi(QuanLyCuaHang q, File f, String noiDung) throws IOException {
        ghiChuoi(f, noiDung);
        try {
            q.docDuLieu();
            dung(false, "Phai bao file loi");
        } catch (IOException e) {
            dung(ton(q, "SP1") == 8 && q.getDanhSachHoaDon().getSoLuong() == 1, "Doc loi giu RAM");
            dung(docChuoi(f).equals(noiDung), "Doc loi khong ghi de file");
            dung(e.getMessage().contains("dong") || e.getMessage().contains("Dong"), "Bao dong loi");
        }
    }

    private static void kiemThuGhiLoi() throws IOException {
        String dir = thuMucMoi();
        QuanLyCuaHang q = taoCuaHang(dir);
        q.ghiDuLieu();
        File f = new File(dir, "sanpham.txt");
        String cu = docChuoi(f);
        LoaiSP loaiNgoai = new LoaiSP("NGOAI", "Ngoai", "Ngoai");
        q.getDanhSachSanPham().them(new DungCuTheThao("NGOAI", "Bong", "A", tien("1"), loaiNgoai, "Bong", "Da", 1));
        thuGhiLoi(q, f, cu);
    }

    private static void thuGhiLoi(QuanLyCuaHang q, File f, String cu) throws IOException {
        try {
            q.ghiDuLieu();
            dung(false, "Phai tu choi file tam thieu tham chieu");
        } catch (IOException e) {
            dung(docChuoi(f).equals(cu), "Ghi loi giu file chinh");
        }
    }

    private static void kiemThuThuMucRong() throws IOException {
        String dir = thuMucMoi();
        QuanLyCuaHang q = new QuanLyCuaHang(dir);
        q.docDuLieu();
        dung(q.getDanhSachSanPham().getSoLuong() == 0, "Thu muc moi co danh sach rong");
        q.ghiDuLieu();
        dung(demFileText(new File(dir)) == 9, "Ghi du chin file text rieng");
        q.docDuLieu();
        dung(q.getDanhSachHoaDon().getSoLuong() == 0, "Doc lai bo file rong");
    }

    private static int demFileText(File thuMuc) {
        int dem = 0;
        for (String ten : thuMuc.list())
            if (ten.endsWith(".txt"))
                dem++;
        return dem;
    }

    private static void kiemThuFileKhachHang() throws IOException {
        String dir = thuMucMoi();
        File cu = new File(dir, "khachhang.txt");
        ghiChuoi(cu, "KHACH_HANG\nKH1|Khách cũ|0901234567|Địa chỉ cũ\n\n");
        QuanLyCuaHang q = new QuanLyCuaHang(dir);
        q.docDuLieu();
        dung(q.getDanhSachKhachHang().getSoLuong() == 1, "Doc rieng file khach hang");
        q.ghiDuLieu();
        q.docDuLieu();
        dung(q.getDanhSachKhachHang().getSoLuong() == 1 && cu.exists(), "Luu va doc lai file khach hang");
    }

    private static void kiemThuFileRieng() throws IOException {
        String dir = thuMucMoi();
        QuanLyCuaHang q = taoCuaHang(dir);
        q.themPhieuNhap(phieuNhap(q, "P1", 10, 10));
        q.themHoaDon(hoaDon(q, "H1", 2, 1));
        q.ghiDuLieu();
        File f = new File(dir, "nhanvien.txt");
        ghiChuoi(f, "\uFEFF" + docChuoi(f).replace("Nhân viên Bình", "Nhân viên mới") + "\n");
        ghiChuoi(new File(dir, "khachhang copy.txt"), "Ban sao khong duoc nap them\n");
        q.docDuLieu();
        dung(q.getDanhSachNhanVien().timTheoMa("NV1").getHoTen().equals("Nhân viên mới"), "Doc sua truc tiep file rieng, BOM va dong trong");
        kiemTraSauDoc(q);
        kiemThuLuuLaiFileRieng(q, dir);
    }

    private static void kiemThuLuuLaiFileRieng(QuanLyCuaHang q, String dir) throws IOException {
        q.getDanhSachKhachHang().timTheoMa("KH1").setHoTen("Khách đã sửa");
        q.ghiDuLieu();
        dung(docChuoi(new File(dir, "khachhang.txt")).contains("Khách đã sửa"), "Luu vao dung file khachhang");
        dung(docChuoi(new File(dir, "chitiethoadon.txt")).contains("H1|SP1|2|150"), "Ghi chi tiet kem ma chung tu");
        dung(docChuoi(new File(dir, "sanpham.txt")).contains("DUNG_CU|SP2"), "Ghi nhan san pham thong nhat");
        dung(new File(dir, ".luu-tam/sanpham.txt.bak").isFile(), "Co ban sao truoc khi thay file");
        q.docDuLieu();
        dung(q.getDanhSachKhachHang().timTheoMa("KH1").getHoTen().equals("Khách đã sửa"), "Doc lai noi dung vua luu");
        kiemTraSauDoc(q);
        kiemThuFileDichLoi(q, dir);
    }

    private static void kiemThuFileDichLoi(QuanLyCuaHang q, String dir) throws IOException {
        File sp = new File(dir, "sanpham.txt");
        String cu = docChuoi(sp);
        File loi = new File(dir, "chitietphieunhap.txt");
        dung(loi.delete() && loi.mkdir(), "Tao thu muc trung ten file dich");
        q.getDanhSachSanPham().timTheoMa("SP1").setGiaBan(tien("999"));
        try {
            q.ghiDuLieu();
            dung(false, "Phai bao khong ghi duoc file dich");
        } catch (IOException e) {
            dung(docChuoi(sp).equals(cu), "Kiem tra du file dich truoc khi thay file cu");
        }
    }

    private static void kiemThuDuLieuMau() throws IOException {
        QuanLyCuaHang q = new QuanLyCuaHang();
        q.docDuLieu();
        dung(q.getDanhSachLoaiSP().getSoLuong() == 2, "Mau co 2 loai");
        dung(q.getDanhSachKhachHang().getSoLuong() == 10, "Giu 10 khach hang da nhap");
        dung(q.getDanhSachNhanVien().getSoLuong() == 5, "Doc 5 nhan vien");
        dung(q.getDanhSachNhaCungCap().getSoLuong() == 5, "Doc 5 nha cung cap");
        dung(q.getDanhSachSanPham().getSoLuong() == 6, "Mau co 6 san pham");
        dung(q.getDanhSachHoaDon().getSoLuong() == 5, "Mau co 5 hoa don");
        dung(q.getDanhSachPhieuNhap().getSoLuong() == 3, "Mau co 3 phieu nhap");
        kiemTraThongKeMau(q);
        kiemTraTonMau(q);
    }

    private static void kiemTraThongKeMau(QuanLyCuaHang q) {
        dung(q.getDanhSachHoaDon().thongKeTongTien().compareTo(tien("7250000")) == 0, "Tong ban mau");
        dung(q.getDanhSachPhieuNhap().thongKeTongTien().compareTo(tien("35340000")) == 0, "Tong nhap mau");
        LocalDate ngay = LocalDate.of(2026, 11, 9);
        dung(q.getDanhSachHoaDon().thongKeTongTien(ngay, ngay).compareTo(tien("2700000")) == 0, "Thong ke mau theo ngay");
        dung(q.getDanhSachHoaDon().timTheoMa("HD000001").getSoLuongChiTiet() == 2, "HD mau co chi tiet");
        dung(q.getDanhSachPhieuNhap().timTheoMa("PN000003").getSoLuongChiTiet() == 3, "PN mau co chi tiet");
    }

    private static void kiemTraTonMau(QuanLyCuaHang q) throws IOException {
        for (SanPham sp : q.getDanhSachSanPham().getTatCa()) {
            int tonDuKien = 0;
            for (PhieuNhap pn : q.getDanhSachPhieuNhap().getTatCa())
                if (pn.timChiTiet(sp.getMaSanPham()) != null)
                    tonDuKien += pn.timChiTiet(sp.getMaSanPham()).getSoLuong();
            for (HoaDon hd : q.getDanhSachHoaDon().getTatCa())
                if (hd.timChiTiet(sp.getMaSanPham()) != null)
                    tonDuKien -= hd.timChiTiet(sp.getMaSanPham()).getSoLuong();
            dung(sp.getSoLuongTon() == tonDuKien, "Ton mau khop nhap tru ban: " + sp.getMaSanPham());
        }
        q.docDuLieu();
        dung(ton(q, "SP01") == 10 && ton(q, "SP03") == 8, "Doc mau nhieu lan khong doi ton");
    }

    private static void kiemThuDuLieuSai() {
        kiemThuGiaAm();
        kiemThuSoLuongSai();
        kiemThuChuoiSai();
        kiemThuNgaySai();
        kiemThuMacDinh();
    }

    private static void kiemThuGiaAm() {
        try {
            new ChiTietHoaDon(taoCuaHang("out/khong-dung").getDanhSachSanPham().timTheoMa("SP1"), 1, tien("-1"));
            dung(false, "Khong nhan gia am");
        } catch (IllegalArgumentException e) {
            dung(true, "Gia am bi chan");
        }
    }

    private static void kiemThuSoLuongSai() {
        try {
            new ChiTietPhieuNhap(taoCuaHang("out/khong-dung").getDanhSachSanPham().timTheoMa("SP1"), 0, tien("1"));
            dung(false, "Khong nhan SL 0");
        } catch (IllegalArgumentException e) {
            dung(true, "SL 0 bi chan");
        }
    }

    private static void kiemThuChuoiSai() {
        try {
            new LoaiSP("L1", "Ten|sai", "Mo ta");
            dung(false, "Khong nhan dau phan cach");
        } catch (IllegalArgumentException e) {
            dung(true, "Dau phan cach bi chan");
        }
    }

    private static void kiemThuNgaySai() {
        try {
            new DanhSachHoaDon().thongKeTongTien(NGAY, NGAY.minusDays(1));
            dung(false, "Khong nhan ngay nguoc");
        } catch (IllegalArgumentException e) {
            dung(true, "Ngay nguoc bi chan");
        }
    }

    private static void kiemThuMacDinh() {
        dung(!new DanhSachSanPham().them(new GiayTheThao()), "Giay mac dinh chua du du lieu");
        dung(!new DanhSachKhachHang().them(new KhachHang()), "KH mac dinh chua du du lieu");
        dung(!new DanhSachHoaDon().them(new HoaDon()), "HD mac dinh chua du du lieu");
        dung(!new DanhSachPhieuNhap().them(new PhieuNhap()), "PN mac dinh chua du du lieu");
        dung(new Kho().traCuuTon("SP1") == -1, "Kho chua gan danh sach");
    }
}
