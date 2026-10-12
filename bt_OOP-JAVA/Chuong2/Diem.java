import java.util.Scanner;

public class Diem {
    private float x;
    private float y;
    
    public void Nhap(Scanner sc){
        System.out.print("Nhap x: ");
        x = sc.nextFloat();
        System.out.print("Nhap y: ");
        y = sc.nextFloat();
    }
    public void Xuat(){
        System.out.print("x=" + x + "\n" + "y=" + y);
    }
    public void DiChuyenDiem(float dx ,float dy){
        x += dx;
        y += dy;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Diem d1 = new Diem();
        d1.Nhap(sc);
        d1.Xuat(); 
        d1.DiChuyenDiem(1, 1);
        System.out.println("\nSau khi di chuyen:");
        d1.Xuat();
    }
}
