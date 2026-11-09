import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import models.KhachHang;
import models.NhanVien;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import services.DanhSachKhachHang;
import services.DanhSachNhanVien;
import services.IQuanLyDanhSach;
import services.LuuTruFileText;

public class CauTrucModelServiceTest {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        var doc = factory.newDocumentBuilder().parse(Path.of("artifacts/Sport_Store.drawio.xml").toFile());
        Element page = null;
        NodeList pages = doc.getElementsByTagName("diagram");
        for (int i = 0; i < pages.getLength(); i++) {
            Element candidate = (Element) pages.item(i);
            if (candidate.getAttribute("name").equals("01 - Tổng quan (20 class)")) {
                page = candidate;
            }
        }
        check(page != null);
        Set<String> khUml = nhom(page, "+KhachHang(");
        Set<String> nvUml = nhom(page, "+NhanVien(");
        Set<String> dsUml = nhom(page, "+DanhSachKhachHang(");
        Set<String> fileUml = nhom(page, "+LuuTruFileText(");
        model(KhachHang.class, "maKhachHang", khUml);
        model(NhanVien.class, "maNhanVien", nvUml);
        KhachHang.class.getConstructor(String.class, String.class, String.class, String.class);
        NhanVien.class.getConstructor(String.class, String.class, String.class, String.class);
        api(KhachHang.class, khUml, "+getMaKhachHang() String", "getMaKhachHang", String.class);
        api(KhachHang.class, khUml, "+getHoTen() String", "getHoTen", String.class);
        api(KhachHang.class, khUml, "+capNhatThongTin(String ten, String sdt, String diaChi) void",
                "capNhatThongTin", void.class, String.class, String.class, String.class);
        api(KhachHang.class, khUml, "+hienThiThongTin() String", "hienThiThongTin", String.class);
        api(NhanVien.class, nvUml, "+getMaNhanVien() String", "getMaNhanVien", String.class);
        api(NhanVien.class, nvUml, "+getHoTen() String", "getHoTen", String.class);
        api(NhanVien.class, nvUml, "+capNhatLienHe(String soDienThoai) void",
                "capNhatLienHe", void.class, String.class);
        api(NhanVien.class, nvUml, "+doiCaLamViec(String caMoi) void", "doiCaLamViec", void.class, String.class);
        api(NhanVien.class, nvUml, "+hienThiThongTin() String", "hienThiThongTin", String.class);
        check(dsUml.contains("-List<KhachHang> ds"));
        check(DanhSachKhachHang.class.getDeclaredField("ds").getGenericType().getTypeName()
                .equals("java.util.List<models.KhachHang>"));
        check(IQuanLyDanhSach.class.isAssignableFrom(DanhSachKhachHang.class));
        check(List.of(DanhSachKhachHang.class.getGenericInterfaces()).stream()
                .anyMatch(t -> t.getTypeName().equals("services.IQuanLyDanhSach<models.KhachHang>")));
        api(DanhSachKhachHang.class, dsUml, "+them(KhachHang kh) boolean", "them", boolean.class, KhachHang.class);
        api(DanhSachKhachHang.class, dsUml, "+suaTheoMa(String ma, KhachHang khMoi) boolean",
                "suaTheoMa", boolean.class, String.class, KhachHang.class);
        api(DanhSachKhachHang.class, dsUml, "+xoaTheoMa(String ma) boolean", "xoaTheoMa", boolean.class, String.class);
        api(DanhSachKhachHang.class, dsUml, "+timTheoMa(String ma) KhachHang", "timTheoMa", KhachHang.class, String.class);
        api(DanhSachKhachHang.class, dsUml, "+timGanDung(String tuKhoa) List<KhachHang>", "timGanDung", List.class, String.class);
        api(DanhSachKhachHang.class, dsUml, "+timTheoSoDienThoai(String sdt) List<KhachHang>",
                "timTheoSoDienThoai", List.class, String.class);
        api(DanhSachKhachHang.class, dsUml, "+getTatCa() List<KhachHang>", "getTatCa", List.class);
        api(LuuTruFileText.class, fileUml, "+ghiKhachHang(String path, DanhSachKhachHang ds) void",
                "ghiKhachHang", void.class, String.class, DanhSachKhachHang.class);
        api(LuuTruFileText.class, fileUml, "+docKhachHang(String path) DanhSachKhachHang",
                "docKhachHang", DanhSachKhachHang.class, String.class);
        api(LuuTruFileText.class, fileUml, "+ghiNhanVien(String path, List<NhanVien> ds) void",
                "ghiNhanVien", void.class, String.class, List.class);
        api(LuuTruFileText.class, fileUml, "+docNhanVien(String path) List<NhanVien>",
                "docNhanVien", List.class, String.class);
        check(LuuTruFileText.class.getMethod("ghiNhanVien", String.class, List.class)
                .getGenericParameterTypes()[1].getTypeName().equals("java.util.List<models.NhanVien>"));
        check(LuuTruFileText.class.getMethod("docNhanVien", String.class)
                .getGenericReturnType().getTypeName().equals("java.util.List<models.NhanVien>"));
        check(DanhSachNhanVien.class.getDeclaredField("danhSach").getType() == NhanVien[].class);
        String main = Files.readString(Path.of("src/Main.java"));
        int start = main.indexOf("public static void main(String[] args)");
        int end = main.indexOf("\n    }", start);
        check(start >= 0 && end > start && main.substring(start, end).lines().count() + 1 <= 10);
        System.out.println("CauTrucModelServiceTest OK (5 groups: KhachHang, NhanVien, customer list/interface, storage signatures, array/main)");
    }

    private static Set<String> nhom(Element page, String constructor) {
        NodeList cells = page.getElementsByTagName("mxCell");
        String parent = null;
        for (int i = 0; i < cells.getLength(); i++) {
            Element cell = (Element) cells.item(i);
            if (cell.getAttribute("value").startsWith(constructor)) {
                parent = cell.getAttribute("parent");
            }
        }
        check(parent != null);
        Set<String> result = new HashSet<>();
        for (int i = 0; i < cells.getLength(); i++) {
            Element cell = (Element) cells.item(i);
            if (cell.getAttribute("parent").equals(parent)) {
                result.add(cell.getAttribute("value"));
            }
        }
        return result;
    }

    private static void model(Class<?> type, String id, Set<String> uml) throws Exception {
        Set<String> fields = new HashSet<>();
        for (String line : uml) {
            if (line.startsWith("-String ")) {
                fields.add(line.substring("-String ".length()));
            }
        }
        check(fields.size() == 4 && type.getDeclaredFields().length == 4);
        for (var field : type.getDeclaredFields()) {
            check(fields.contains(field.getName()) && field.getType() == String.class);
            check(Modifier.isPrivate(field.getModifiers()));
        }
        check(Modifier.isFinal(type.getDeclaredField(id).getModifiers()));
    }

    private static void api(Class<?> type, Set<String> uml, String signature, String name,
                            Class<?> result, Class<?>... args) throws Exception {
        if (!uml.contains(signature)) {
            throw new AssertionError("UML signature missing: " + signature);
        }
        check(type.getMethod(name, args).getReturnType() == result);
    }

    private static void check(boolean value) {
        if (!value) {
            throw new AssertionError();
        }
    }
}
