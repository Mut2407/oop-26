/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

final class FileText {
    private FileText() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void ghi(String string, List<String> list) throws IOException {
        Path path = Path.of(string, new String[0]).toAbsolutePath();
        Files.createDirectories(path.getParent(), new FileAttribute[0]);
        Path path2 = Files.createTempFile(path.getParent(), "store-", ".tmp", new FileAttribute[0]);
        try {
            Files.write(path2, list, StandardCharsets.UTF_8, new OpenOption[0]);
            try {
                Files.move(path2, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
            catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                Files.move(path2, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        finally {
            Files.deleteIfExists(path2);
        }
    }

    static List<String[]> doc(String string, String ... stringArray) throws IOException {
        return docBang(string, stringArray).dong();
    }

    record Bang(String tieuDe, List<String[]> dong) {
    }

    static Bang docBang(String string, String ... stringArray) throws IOException {
        List<String> list = Files.readAllLines(Path.of(string, new String[0]), StandardCharsets.UTF_8);
        if (!list.isEmpty() && list.get(0).startsWith("\uFEFF")) {
            list.set(0, list.get(0).substring(1));
        }
        boolean bl = false;
        if (!list.isEmpty()) {
            for (String string2 : stringArray) {
                if (!string2.equals(list.get(0))) continue;
                bl = true;
            }
        }
        if (!bl) {
            throw new IOException("File khong dung dinh dang: " + string);
        }
        List<String[]> arrayList = new ArrayList<>();
        for (int i = 1; i < list.size(); ++i) {
            try {
                String[] stringArray2 = list.get(i).split("\t", -1);
                for (int j = 0; j < stringArray2.length; ++j) {
                    stringArray2[j] = FileText.giaiMa(stringArray2[j]);
                }
                arrayList.add(stringArray2);
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IOException("Dong " + (i + 1) + ": " + illegalArgumentException.getMessage(), illegalArgumentException);
            }
        }
        return new Bang(list.get(0), arrayList);
    }

    static String dong(String ... stringArray) {
        CharSequence[] charSequenceArray = new String[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            charSequenceArray[i] = stringArray[i].replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
        }
        return String.join((CharSequence)"\t", charSequenceArray);
    }

    private static String giaiMa(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        block6: for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c != '\\') {
                stringBuilder.append(c);
                continue;
            }
            if (++i == string.length()) {
                throw new IllegalArgumentException("Ky tu escape khong hop le.");
            }
            switch (string.charAt(i)) {
                case '\\': {
                    stringBuilder.append('\\');
                    continue block6;
                }
                case 't': {
                    stringBuilder.append('\t');
                    continue block6;
                }
                case 'n': {
                    stringBuilder.append('\n');
                    continue block6;
                }
                case 'r': {
                    stringBuilder.append('\r');
                    continue block6;
                }
                default: {
                    throw new IllegalArgumentException("Ky tu escape khong hop le.");
                }
            }
        }
        return stringBuilder.toString();
    }
}

