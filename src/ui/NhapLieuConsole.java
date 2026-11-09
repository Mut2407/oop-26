package ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;
import models.KiemTra;

public final class NhapLieuConsole {
    private final Scanner scanner;

    public NhapLieuConsole(Scanner scanner) {
        this.scanner = KiemTra.khacNull(scanner, "Scanner");
    }

    public String docDong(String loiNhac) {
        System.out.print(loiNhac);
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("Ket thuc nhap lieu.");
        }
        return scanner.nextLine().trim();
    }

    public String batBuoc(String loiNhac) {
        return docHopLe(loiNhac, giaTri -> KiemTra.batBuoc(giaTri, "Thong tin"));
    }

    public String ma(String loiNhac) {
        return docHopLe(loiNhac, KiemTra::ma);
    }

    public String dienThoai() {
        return docHopLe("So dien thoai: ", KiemTra::dienThoai);
    }

    public int soNguyen(String loiNhac, int nhoNhat) {
        return docHopLe(loiNhac, giaTri -> {
            int so = Integer.parseInt(giaTri);
            if (so < nhoNhat) {
                throw new IllegalArgumentException("So phai >= " + nhoNhat);
            }
            return so;
        });
    }

    public double soDuong(String loiNhac) {
        return docHopLe(loiNhac, giaTri -> KiemTra.soDuong(Double.parseDouble(giaTri)));
    }

    public BigDecimal tien(String loiNhac) {
        return docHopLe(loiNhac, giaTri -> KiemTra.tien(new BigDecimal(giaTri)));
    }

    public LocalDate ngay(String loiNhac) {
        return docHopLe(loiNhac, LocalDate::parse);
    }

    private <T> T docHopLe(String loiNhac, Function<String, T> chuyenDoi) {
        while (true) {
            String giaTri = docDong(loiNhac);
            try {
                return chuyenDoi.apply(giaTri);
            } catch (IllegalArgumentException | DateTimeException e) {
                System.out.println("Du lieu khong hop le: " + e.getMessage());
            }
        }
    }
}
