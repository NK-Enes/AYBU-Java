import java.util.Scanner;

public class Mıhtar {
    static void main() {
        Scanner input = new Scanner(System.in);
        System.out.print("Q1: ");
        int Q1 = input.nextInt();
        System.out.print("Q2: ");
        int Q2 = input.nextInt();
        System.out.print("Final: ");
        int Final = input.nextInt();
        System.out.println();

        double Q_weight = 0.2;
        double F_weight = 0.6;

        double Result = (Q1 * Q_weight + Q2 * Q_weight + Final * F_weight);

        System.out.println("Your Score is: " + Result);
        int discount;

        if (Result >= 80){
            discount = 50;
        }
        else if (Result >= 50){
            discount = 20;
        }
        else {
            discount = 0;
        }
        System.out.println("You Earned %"+discount+" DISCOUNT");
        System.out.print("Your Cart Total: ");
        double price = input.nextDouble();

        double payment_percent = 1 - (discount / 100.0f);
        System.out.println(price * payment_percent);
    }
}
