import java.nio.file.*;
public class Task092ConsoleBaoCaoTest {
    public static void main(String[] args) throws Exception {
        Path dir=Files.createTempDirectory("report-console-");
        try {
            Files.createDirectory(dir.resolve("data"));
            try(var ps=Files.list(Path.of("data"))) {
                for(Path p:ps.filter(Files::isRegularFile).toList()) Files.copy(p,dir.resolve("data").resolve(p.getFileName()));
            }
            String out=ConsoleTestSupport.run(dir,"8\n1\n2026-11-01\n2026-11-08\n2\n2026-11-01\n2026-11-08\n3\n5\n4\n0\n5\n3\n-1\n1\nbad-date\n0\n0\n",0);
            for(String key:new String[]{"BAO CAO THONG KE","Doanh thu da xac nhan: 0","Gia tri da nhap kho: 0","Gia tri ton theo gia ban","Khong the tinh bao cao"}) assert out.contains(key):out;
            try(var ps=Files.list(Path.of("data"))) {
                for(Path p:ps.filter(Files::isRegularFile).toList()) assert Files.mismatch(p,dir.resolve("data").resolve(p.getFileName()))==-1;
            }
            for (String file : new String[]{"phieunhap.txt", "hoadon.txt"}) {
                // A read-only report preserves pre-existing document files as well as their absence.
                assert Files.exists(dir.resolve("data").resolve(file)) == Files.exists(Path.of("data", file));
            }
            System.out.println("Task092ConsoleBaoCaoTest OK");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
}
