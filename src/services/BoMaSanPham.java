package services;

import java.math.BigDecimal;
import models.*;

/** Kiểm tra kiểu lớp con chỉ tại biên tuần tự hóa, không ở nghiệp vụ Kho. */
final class BoMaSanPham {
    private BoMaSanPham() {
    }

    static String ghi(SanPham sp) {
        String chung = FileText.dong("SP", kieu(sp), sp.getMaSanPham(), sp.getTenSanPham(),
                sp.getThuongHieu(), sp.getGiaBan().toPlainString(),
                Integer.toString(sp.getSoLuongTon()), sp.getLoaiSP().getMaLoaiSP());
        return chung + "\t" + thuocTinhRieng(sp);
    }

    private static String kieu(SanPham sp) {
        if (sp instanceof GiayTheThao) {
            return "GIAY";
        }
        if (sp instanceof QuanAoTheThao) {
            return "QUAN_AO";
        }
        if (sp instanceof DungCuTheThao) {
            return "DUNG_CU";
        }
        throw new IllegalArgumentException("Lop san pham khong duoc ho tro.");
    }

    private static String thuocTinhRieng(SanPham sp) {
        if (sp instanceof GiayTheThao giay) {
            return FileText.dong(Double.toString(giay.getKichCoGiay()), giay.getMauSac(),
                    giay.getChatLieu(), giay.getLoaiDeGiay());
        }
        if (sp instanceof QuanAoTheThao quanAo) {
            return FileText.dong(quanAo.getKichCoQuanAo(), quanAo.getMauSac(), quanAo.getChatLieu());
        }
        DungCuTheThao dungCu = (DungCuTheThao) sp;
        return FileText.dong(dungCu.getMonTheThao(), dungCu.getVatLieu(),
                Double.toString(dungCu.getKhoiLuongKg()));
    }

    static SanPham doc(String[] cot, QuanLyCuaHang cuaHang) {
        if (cot.length < 8) {
            throw new IllegalArgumentException("Thieu cot san pham.");
        }
        LoaiSP loai = KiemTra.khacNull(cuaHang.getLoaiSP().timTheoMa(cot[7]), "Loai san pham");
        SanPham sp = switch (cot[1]) {
            case "GIAY" -> docGiay(cot, loai);
            case "QUAN_AO" -> docQuanAo(cot, loai);
            case "DUNG_CU" -> docDungCu(cot, loai);
            default -> throw new IllegalArgumentException("Kieu san pham khong hop le: " + cot[1]);
        };
        return sp.voiTonKho(Integer.parseInt(cot[6]));
    }

    private static SanPham docGiay(String[] cot, LoaiSP loai) {
        FileText.soCot(cot, 12);
        return new GiayTheThao(cot[2], cot[3], cot[4], new BigDecimal(cot[5]), loai,
                Double.parseDouble(cot[8]), cot[9], cot[10], cot[11]);
    }

    private static SanPham docQuanAo(String[] cot, LoaiSP loai) {
        FileText.soCot(cot, 11);
        return new QuanAoTheThao(cot[2], cot[3], cot[4], new BigDecimal(cot[5]), loai,
                cot[8], cot[9], cot[10]);
    }

    private static SanPham docDungCu(String[] cot, LoaiSP loai) {
        FileText.soCot(cot, 11);
        return new DungCuTheThao(cot[2], cot[3], cot[4], new BigDecimal(cot[5]), loai,
                cot[8], cot[9], Double.parseDouble(cot[10]));
    }
}
