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

        int random_number = random.nextInt(0,100);
        System.out.println(random_number);

        System.out.print("Boyunuz Kac cm: ");
        double lenght = scanner.nextDouble();
        scanner.nextLine(); // if this not here the 2. next line take just "\n"
        if (lenght >= 190){
            System.out.println("Baya Uzun Boylusun");
        }
        else if (lenght >= 165){
            System.out.println("Genel Kitleye Uymussun");
        }
        else {
            System.out.print("\nPUHAHAHAHA sadece " + lenght + " cm really?");
        }

        scanner.close();

    }
}