import java.math.BigDecimal;
import java.time.LocalDate;
// import models.*;
// import danhsach.*;
// import quanly.*;

public class TestSupport {
    public static int soKiemTra;
    public static final LocalDate NGAY = LocalDate.of(2026, 11, 9);

    public static void dung(boolean dieuKien) {
        soKiemTra++;
        if (!dieuKien) {
            throw new AssertionError("Sai o kiem tra thu " + soKiemTra);
        }
    }

    public static void tien(String kyVong, BigDecimal thucTe) {
        dung(new BigDecimal(kyVong).compareTo(thucTe) == 0);
    }
}
