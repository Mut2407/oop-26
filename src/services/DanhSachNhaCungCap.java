/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import models.NhaCungCap;
import services.FileText;
import services.IQuanLyDanhSach;

public class DanhSachNhaCungCap
implements IQuanLyDanhSach<NhaCungCap> {
    private static final String TIEU_DE_CU = "NHA_CUNG_CAP_V1";
    private static final String TIEU_DE = "maNhaCungCap\ttenNhaCungCap\tsoDienThoai\tdiaChi";
    private final List<NhaCungCap> ds = new ArrayList<NhaCungCap>();

    public DanhSachNhaCungCap() {
    }

    public DanhSachNhaCungCap(NhaCungCap[] nhaCungCapArray) {
        if (nhaCungCapArray == null) {
            throw new IllegalArgumentException("Danh sach ban dau khong hop le.");
        }
        for (NhaCungCap nhaCungCap : nhaCungCapArray) {
            this.them(nhaCungCap);
        }
    }

    public List<NhaCungCap> getTatCa() {
        ArrayList<NhaCungCap> arrayList = new ArrayList<NhaCungCap>();
        for (NhaCungCap nhaCungCap : this.ds) {
            arrayList.add(DanhSachNhaCungCap.saoChep(nhaCungCap));
        }
        return List.copyOf(arrayList);
    }

    public List<NhaCungCap> getDanhSach() {
        return this.getTatCa();
    }

    @Override
    public boolean them(NhaCungCap nhaCungCap) {
        NhaCungCap nhaCungCap2 = DanhSachNhaCungCap.saoChep(nhaCungCap);
        if (this.chiSo(nhaCungCap2.getMaNhaCungCap()) >= 0) {
            throw new IllegalArgumentException("Ma nha cung cap da ton tai.");
        }
        this.ds.add(nhaCungCap2);
        return true;
    }

    @Override
    public boolean suaTheoMa(String string, NhaCungCap nhaCungCap) {
        int n = this.chiSo(string);
        if (n < 0) {
            return false;
        }
        NhaCungCap nhaCungCap2 = DanhSachNhaCungCap.saoChep(nhaCungCap);
        if (!this.ds.get(n).getMaNhaCungCap().equalsIgnoreCase(nhaCungCap2.getMaNhaCungCap())) {
            throw new IllegalArgumentException("Khong duoc doi ma nha cung cap.");
        }
        this.ds.set(n, nhaCungCap2);
        return true;
    }

    @Override
    public boolean xoaTheoMa(String string) {
        int n = this.chiSo(string);
        if (n < 0) {
            return false;
        }
        this.ds.remove(n);
        return true;
    }

    @Override
    public NhaCungCap CheckMaTrung(String string) {
        int n = this.chiSo(string);
        return n < 0 ? null : DanhSachNhaCungCap.saoChep(this.ds.get(n));
    }

    @Override
    public List<NhaCungCap> TimKiemTheoMaHoacTen(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Tu khoa khong duoc rong.");
        }
        String string2 = DanhSachNhaCungCap.chuanHoa(string.trim());
        ArrayList<NhaCungCap> arrayList = new ArrayList<NhaCungCap>();
        for (NhaCungCap nhaCungCap : this.ds) {
            if (!DanhSachNhaCungCap.chuanHoa(nhaCungCap.getMaNhaCungCap()).contains(string2) && !DanhSachNhaCungCap.chuanHoa(nhaCungCap.getTenNhaCungCap()).contains(string2)) continue;
            arrayList.add(DanhSachNhaCungCap.saoChep(nhaCungCap));
        }
        return List.copyOf(arrayList);
    }

    public void ghiFile(String string) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(TIEU_DE);
        for (NhaCungCap nhaCungCap : this.ds) {
            arrayList.add(FileText.dong(nhaCungCap.getMaNhaCungCap(), nhaCungCap.getTenNhaCungCap(), nhaCungCap.getSoDienThoai(), nhaCungCap.getDiaChi()));
        }
        FileText.ghi(string, arrayList);
    }

    public void docFile(String string) throws IOException {
        DanhSachNhaCungCap danhSachNhaCungCap = new DanhSachNhaCungCap();
        List<String[]> list = FileText.doc(string, TIEU_DE, TIEU_DE_CU);
        for (int i = 0; i < list.size(); ++i) {
            String[] stringArray = list.get(i);
            try {
                if (stringArray.length != 4) {
                    throw new IllegalArgumentException("Phai co 4 cot.");
                }
                danhSachNhaCungCap.them(new NhaCungCap(stringArray[0], stringArray[1], stringArray[2], stringArray[3]));
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 2) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        this.ds.clear();
        this.ds.addAll(danhSachNhaCungCap.ds);
    }

    private int chiSo(String string) {
        if (string != null) {
            for (int i = 0; i < this.ds.size(); ++i) {
                if (!this.ds.get(i).getMaNhaCungCap().equalsIgnoreCase(string.trim())) continue;
                return i;
            }
        }
        return -1;
    }

    private static NhaCungCap saoChep(NhaCungCap nhaCungCap) {
        if (nhaCungCap == null) {
            throw new IllegalArgumentException("Nha cung cap khong duoc null.");
        }
        return new NhaCungCap(nhaCungCap.getMaNhaCungCap(), nhaCungCap.getTenNhaCungCap(), nhaCungCap.getSoDienThoai(), nhaCungCap.getDiaChi());
    }

    private static String chuanHoa(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{M}+", "").replace('\u0111', 'd').replace('\u0110', 'D').toLowerCase(Locale.ROOT);
    }
}
