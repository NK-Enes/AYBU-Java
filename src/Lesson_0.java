/*
1-Section) hypotenuse
 */



import java.util.Scanner;

public class Lesson_0 {
    static void main() {
        Scanner input = new Scanner(System.in);

        // Section 1
        double a = input.nextDouble();
        double b = input.nextDouble();

        a = Math.pow(a,2);
        b = Math.pow(b,2);
        System.out.println(Math.sqrt(a+b));

        input.close();

    }
}
