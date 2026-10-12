import java.util.Scanner;

public class SinhVien {
    private String maSinhVien;
    private String hoTen;
    private String lop;
    private float diemMon1;
    private float diemMon2;
    private float diemMon3;
    public void Nhap(Scanner sc) {
        System.out.print("Ma sinh vien: ");
        maSinhVien = sc.nextLine();
        System.out.print("Ho ten: ");
        hoTen = sc.nextLine();
        System.out.print("Lop: ");
        lop = sc.nextLine();
        System.out.print("Diem mon 1: ");
        diemMon1 = sc.nextFloat();
        System.out.print("Diem mon 2: ");
        diemMon2 = sc.nextFloat();
        System.out.print("Diem mon 3: ");
        diemMon3 = sc.nextFloat();
    }
    public void Xuat() {
        System.out.print("Ma sinh vien: " + maSinhVien + "\n" + "Ho ten: " + hoTen + "\n" + "Lop: " + lop + "\n" + "Diem mon 1: " + diemMon1 + "\n" + "Diem mon 2: " + diemMon2 + "\n" + "Diem mon 3: " + diemMon3);
    }

    public void diemTrungBinh(){
        System.out.print("\nDiem trung binh: " + (diemMon1 + diemMon2 + diemMon3) / 3);
    }

    public void xepLoai(){
        float diemTrungBinh = (diemMon1 + diemMon2 + diemMon3) / 3;
        if (diemTrungBinh >= 9){
            System.out.print("\nXep loai: Gioi");
        }
        else if (diemTrungBinh >= 7){
            System.out.print("\nXep loai: Kha");
        }
        else if (diemTrungBinh >= 5){
            System.out.print("\nXep loai: Trung binh");
        }
        else{
            System.out.print("\nXep loai: Kem");
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SinhVien sv = new SinhVien();
        sv.Nhap(sc);
        System.out.println("\n============== Thong tin sinh vien =================");
        sv.Xuat();
        sv.diemTrungBinh();
        sv.xepLoai();
    }
}