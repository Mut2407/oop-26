package services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

final class FileText {
    private FileText() { }

    static void ghi(String duongDan, List<String> dong) throws IOException {
        Path dich = Path.of(duongDan).toAbsolutePath();
        Files.createDirectories(dich.getParent());
        Path tam = Files.createTempFile(dich.getParent(), "store-", ".tmp");
        try {
            Files.write(tam, dong, StandardCharsets.UTF_8);
            try {
                Files.move(tam, dich, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tam, dich, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(tam);
        }
    }

    static List<String[]> doc(String duongDan, String... tieuDe) throws IOException {
        List<String> dong = Files.readAllLines(Path.of(duongDan), StandardCharsets.UTF_8);
        boolean hopLe = false;
        if (!dong.isEmpty())
            for (String tieuDeChoPhep : tieuDe)
                if (tieuDeChoPhep.equals(dong.get(0))) hopLe = true;
        if (!hopLe) throw new IOException("File khong dung dinh dang: " + duongDan);
        List<String[]> ketQua = new ArrayList<>();
        for (int i = 1; i < dong.size(); i++) {
            try {
                String[] cot = dong.get(i).split("\t", -1);
                for (int j = 0; j < cot.length; j++) cot[j] = giaiMa(cot[j]);
                ketQua.add(cot);
            } catch (IllegalArgumentException e) {
                throw new IOException("Dong " + (i + 1) + ": " + e.getMessage(), e);
            }
        }
        return ketQua;
    }

    static String dong(String... cot) {
        String[] daMaHoa = new String[cot.length];
        for (int i = 0; i < cot.length; i++)
            daMaHoa[i] = cot[i].replace("\\", "\\\\").replace("\t", "\\t")
                    .replace("\n", "\\n").replace("\r", "\\r");
        return String.join("\t", daMaHoa);
    }

    private static String giaiMa(String giaTri) {
        StringBuilder ketQua = new StringBuilder();
        for (int i = 0; i < giaTri.length(); i++) {
            char c = giaTri.charAt(i);
            if (c != '\\') { ketQua.append(c); continue; }
            if (++i == giaTri.length()) throw new IllegalArgumentException("Ky tu escape khong hop le.");
            switch (giaTri.charAt(i)) {
                case '\\': ketQua.append('\\'); break;
                case 't': ketQua.append('\t'); break;
                case 'n': ketQua.append('\n'); break;
                case 'r': ketQua.append('\r'); break;
                default: throw new IllegalArgumentException("Ky tu escape khong hop le.");
            }
        }
        return ketQua.toString();
    }
}
