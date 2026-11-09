package services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

/** Chi tiết định dạng text và thay file nguyên tử nằm tại một nơi. */
final class FileText {
    private FileText() {
    }

    static String[][] doc(Path tep, String... tieuDeHopLe) throws IOException {
        List<String> dong = Files.readAllLines(tep, StandardCharsets.UTF_8);
        if (dong.isEmpty()) {
            throw new IOException("File rong: " + tep);
        }
        String tieuDe = dong.get(0).replaceFirst("^\\uFEFF", "");
        if (!Arrays.asList(tieuDeHopLe).contains(tieuDe)) {
            throw new IOException("Sai dinh dang: " + tep);
        }
        String[][] ketQua = new String[dong.size() - 1][];
        for (int i = 1; i < dong.size(); i++) {
            try {
                ketQua[i - 1] = tach(dong.get(i));
            } catch (IllegalArgumentException e) {
                throw loiDong(tep, i + 1, e);
            }
        }
        return ketQua;
    }

    static void ghi(Path tep, List<String> dong) throws IOException {
        Files.createDirectories(tep.toAbsolutePath().getParent());
        Path tam = Files.createTempFile(tep.toAbsolutePath().getParent(), "sport-store-", ".tmp");
        try {
            Files.write(tam, dong, StandardCharsets.UTF_8);
            if (Files.exists(tep)) {
                Files.copy(tep, tep.resolveSibling(tep.getFileName() + ".bak"),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            // Không hạ xuống phép ghi thường: nếu không hỗ trợ atomic move thì giữ file cũ.
            Files.move(tam, tep, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(tam);
        }
    }

    static String dong(String... cot) {
        String[] daMaHoa = new String[cot.length];
        for (int i = 0; i < cot.length; i++) {
            daMaHoa[i] = cot[i].replace("\\", "\\\\").replace("\t", "\\t")
                    .replace("\n", "\\n").replace("\r", "\\r");
        }
        return String.join("\t", daMaHoa);
    }

    private static String[] tach(String dong) {
        String[] cot = dong.split("\t", -1);
        for (int i = 0; i < cot.length; i++) {
            cot[i] = giaiMa(cot[i]);
        }
        return cot;
    }

    private static String giaiMa(String giaTri) {
        StringBuilder ketQua = new StringBuilder();
        for (int i = 0; i < giaTri.length(); i++) {
            char kyTu = giaTri.charAt(i);
            if (kyTu != '\\') {
                ketQua.append(kyTu);
                continue;
            }
            if (++i == giaTri.length()) {
                throw new IllegalArgumentException("Escape khong hop le.");
            }
            ketQua.append(kyTuEscape(giaTri.charAt(i)));
        }
        return ketQua.toString();
    }

    private static char kyTuEscape(char kyTu) {
        return switch (kyTu) {
            case '\\' -> '\\';
            case 't' -> '\t';
            case 'n' -> '\n';
            case 'r' -> '\r';
            default -> throw new IllegalArgumentException("Escape khong hop le.");
        };
    }

    static void soCot(String[] cot, int soLuong) {
        if (cot.length != soLuong) {
            throw new IllegalArgumentException("Can " + soLuong + " cot, nhan " + cot.length + ".");
        }
    }

    static IOException loiDong(Path tep, int dong, RuntimeException loi) {
        return new IOException(tep + " - dong " + dong + ": " + loi.getMessage(), loi);
    }
}
