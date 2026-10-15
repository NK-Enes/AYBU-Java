import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main (String[] args){
        // Command Line

        /*
        Command Lines
         */

        String name = "Alper";
        short age = 21;

        System.out.println("Name: " + name + " Age: " + age);

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        double random_number = random.nextDouble(0,100);
        System.out.println(random_number);

        System.out.print("Boyunuz Kac cm: ");
        double length = scanner.nextDouble();
        scanner.nextLine(); // if this not here the 2. next line take just "\n"
        if (length >= 190){
            System.out.println("Baya Uzun Boylusun");
        }
        else if (length >= 165){
            System.out.println("Genel Kitleye Uymussun");
        }
        else {
            System.out.print("\nPUHAHAHAHA sadece " + length + " cm really?");
        }

        scanner.close();

        System.out.println(Math.PI);
        System.out.println(Math.E);
        System.out.println(Math.pow(10,-1));
        System.out.println(Math.abs(-150));
        System.out.println(Math.sqrt(27));

    }
}