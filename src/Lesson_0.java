/*
1- Section: hypotenuse
2- Section: circle values
 */



import java.util.Scanner;

public class Lesson_0 {
    static void main() {
        Scanner input = new Scanner(System.in);

        // Section 1
        /*
        double a = input.nextDouble();
        double b = input.nextDouble();

        a = Math.pow(a,2);
        b = Math.pow(b,2);
        System.out.println(Math.sqrt(a+b));

        input.close();
        */

        //Section 2
        double radius = input.nextDouble();

        System.out.print("Circle Area: ");
        System.out.println(Math.PI * Math.pow(radius,2));
        System.out.print("Circle Perimeter: ");
        System.out.println(2 * Math.PI * radius);
        System.out.print("If this is a sphere: ");
        System.out.println(4.0 / 3.0 * Math.PI * Math.pow(radius,3));

    }
}
