/*
1- Section: hypotenuse
2- Section: circle values
3- Section: printf's
4- Section: Section
 */



import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Lesson_0 {
    static void main(String[] args) {
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
        /*
        double radius = input.nextDouble();

        System.out.print("Circle Area: ");
        System.out.println(Math.PI * Math.pow(radius,2));
        System.out.print("Circle Perimeter: ");
        System.out.println(2 * Math.PI * radius);
        System.out.print("If this is a sphere: ");
        System.out.println(4.0 / 3.0 * Math.PI * Math.pow(radius,3));
         */

        //Section 3
        /*
        String name = "Enes";
        char grade = 'A';
        double height = 1.78;
        int age = 20;
        boolean is_student = true;
        double neglet = -50.24;
        double neglit = 50.25;
        long neglot = 40000000;

        System.out.printf("Hello %s (Age: %d Grade: %c)\nHeight: %.2f m\nStudent: %b\n",name,age,grade,height,is_student);
        System.out.printf("Complex Double: %.2f\n",neglet);
        System.out.printf("Complex Double: %+.2f\n",neglit);
        System.out.printf("Long Number: %,d",neglot);
         */

        //Section 4
        /*
        double principal = input.nextDouble();
        double rate = input.nextDouble() / 100;
        int times_comp = input.nextInt();
        int years = input.nextInt();
        double amount;

        amount = principal * Math.pow(1 + rate / times_comp,times_comp * years);
        System.out.println(amount);
        */

        //Section 5
        /*
        String context = "AYBU has the worst exam system I ever participate";
        int lenght = context.length();
        char letter = context.charAt(0);
        int index = context.indexOf("I");
        System.out.printf("%d\n",lenght);
        System.out.printf("First Letter: %c And first 'I': %d index\n",letter,index);
        System.out.printf("UPPER: %s \nlower: %s\n",context.toUpperCase(),context.toLowerCase());
        System.out.printf("Without _'s: %s\n",context.replace(" ",""));
        System.out.printf("Empty Check: %b \nIs 'Fener 6-0 Aston Villa' Contain '6-0' : %b\n","".isEmpty(),"fener 6-0 aston villa".contains("6-0"));
        System.out.printf("NonCaseSens String Check: %b\n","Password".equalsIgnoreCase("word"));
         */

        

    }
}
