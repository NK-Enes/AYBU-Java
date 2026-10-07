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

        System.out.print("Sikiniz Kac cm: ");
        double penis_size = scanner.nextDouble();
        scanner.nextLine(); // if this not here the 2. next line take just "\n"
        if (penis_size >= 19){
            System.out.println("You have huge YARAK be proud");
        }
        else if (penis_size >= 10){
            System.out.println("You have medium YARAK");
        }
        else {
            System.out.print("\nPUHAHAHAHA just " + penis_size + " cm really?");
        }

        scanner.close();

    }
}