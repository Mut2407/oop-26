package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.NhanVien;

public class DanhSachNhanVien implements LuuTruFile {
    private static final String TIEU_DE = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi";
    private final List<NhanVien> danhSach = new ArrayList<>();

    public DanhSachNhanVien(NhanVien[] banDau) {
        for (NhanVien nv : banDau) them(nv);
    }

    public List<NhanVien> getDanhSach() {
        return List.copyOf(danhSach);
    }

    private void them(NhanVien nv) {
        if (nv == null || !DanhSachKhachHang.maHopLe(nv.getMaNhanVien())
                || nv.getHoTen() == null || nv.getHoTen().isBlank()
                || nv.getCaLamViec() == null || nv.getCaLamViec().isBlank()
                || !DanhSachKhachHang.soDienThoaiHopLe(nv.getSoDienThoai())
                || nv.getDiaChi() == null || nv.getDiaChi().isBlank())
            throw new IllegalArgumentException("Thong tin nhan vien khong hop le.");
        for (NhanVien cu : danhSach)
            if (cu.getMaNhanVien().equalsIgnoreCase(nv.getMaNhanVien()))
                throw new IllegalArgumentException("Ma nhan vien da ton tai.");
        danhSach.add(nv);
    }

    @Override
    public void ghiFile(String duongDan) throws IOException {
        List<String> dong = new ArrayList<>();
        dong.add(TIEU_DE);
        for (NhanVien nv : danhSach)
            dong.add(FileText.dong(nv.getMaNhanVien(), nv.getHoTen(), nv.getCaLamViec(),
                    nv.getSoDienThoai(), nv.getDiaChi()));
        FileText.ghi(duongDan, dong);
    }

    @Override
    public void docFile(String duongDan) throws IOException {
        DanhSachNhanVien moi = new DanhSachNhanVien(new NhanVien[0]);
        List<String[]> dong = FileText.doc(duongDan, TIEU_DE);
        for (int i = 0; i < dong.size(); i++) {
            String[] c = dong.get(i);
            try {
                if (c.length != 5) throw new IllegalArgumentException("Phai co 5 cot.");
                moi.them(new NhanVien(c[0], c[1], c[2], c[3], c[4]));
            } catch (IllegalArgumentException e) {
                throw new IOException("Dong " + (i + 2) + ": " + e.getMessage(), e);
            }
        }
        danhSach.clear();
        danhSach.addAll(moi.danhSach);
    }
}
