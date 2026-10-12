import java.util.Scanner;

public class HinhChuNhat {
    private float dai;
    private float rong;

    public void Nhap(Scanner sc){
        System.out.print("Nhap chieu dai: ");
        dai = sc.nextFloat();
        System.out.print("Nhap chieu rong: ");
        rong = sc.nextFloat();
    }
    public void Xuat(){
        System.out.print("Chieu dai: " + dai + "\n" + "Chieu rong: " + rong);
    }
    public void ChuVi(){
        System.out.print("\nChu vi: " + (2 * (dai + rong)));
    }
    public void DienTich(){
        System.out.print("\nDien tich: " + (dai * rong));
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        HinhChuNhat hcn = new HinhChuNhat();
        hcn.Nhap(sc);
        hcn.Xuat();
        hcn.ChuVi();
        hcn.DienTich();
    }
}
