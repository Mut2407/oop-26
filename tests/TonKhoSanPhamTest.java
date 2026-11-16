import java.io.*;
import java.nio.file.Files;
import models.LoaiSP;
import services.QuanLyCuaHang;

public class TonKhoSanPhamTest {
    public static void main(String[] args) throws Exception {
        File data = Files.createTempDirectory("oop-ton-kho-").toFile();
        QuanLyCuaHang q = new QuanLyCuaHang(data.getPath());
        q.getDanhSachLoaiSP().them(new LoaiSP("L1", "The thao", "San pham the thao"));
        q.ghiDuLieu();
        chay(q, "1\n2\n2\nSP1\n1\nGiay\nNike\n100\nL1\n42\nDen\nVai\nCao su\n10\n"
                + "SP2\n2\nBong\nAdidas\n200\nL1\nBong da\nDa\n0.5\n20\n0\n0\n");
        q.docDuLieu();
        assert q.getKho().traCuuTon("SP1") == 10;
        assert q.getKho().traCuuTon("SP2") == 20;

        chay(q, "1\n3\nSP1\nGiay moi\nNike\n100\nL1\n42\nDen\nVai\nCao su\n3\n"
                + "3\nSP2\nBong moi\nAdidas\n200\nL1\nBong da\nDa\n0.5\n0\n0\n0\n");
        q.docDuLieu();
        assert q.getKho().traCuuTon("SP1") == 3;
        assert q.getKho().traCuuTon("SP2") == 0;

        for (String ton : new String[] {"-1", "abc", "2147483648", ""}) {
            chay(q, "1\n3\nSP1\nKhong luu\nNike\n100\nL1\n42\nDen\nVai\nCao su\n" + ton);
            q.docDuLieu();
            assert q.getKho().traCuuTon("SP1") == 3;
            assert q.getDanhSachSanPham().timTheoMa("SP1").getTenSanPham().equals("Giay moi");
        }
        System.out.println("TonKhoSanPhamTest OK");
    }

    private static void chay(QuanLyCuaHang q, String input) throws Exception {
        InputStream in = System.in;
        PrintStream out = System.out;
        try (PrintStream output = new PrintStream(new ByteArrayOutputStream(), true, "UTF-8")) {
            System.setIn(new ByteArrayInputStream(input.getBytes("UTF-8")));
            System.setOut(output);
            q.chay();
        } finally {
            System.setIn(in);
            System.setOut(out);
        }
    }
}
