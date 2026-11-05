package Lessons.Zero_to_Hundered;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class basics {
    static void main(String[] args) {
        /* primitive            reference
            int                 String
            double              array
            char                object
            boolean
         */
        // declaration than assigment

        int a = 10; // create direct a value (also hold the address)
        String b = "Irem"; // this just reference chars addresses on ram (create char list)
        char[] c = new char[] {'I','r','e','m'}; // this also reference chars addresses on ram (but now we don't hold a String variable type)

        //System.out.print("Input (int): ");
        Scanner input = new Scanner(System.in); // System.in = keyboard inputs
        // This is the basic object creation style on java

        //a = input.nextInt(); // a's value now change depends on the keyboard input

        /* Arithmetics
        value = value + change_amount
        value += change_amount
        (Basic: +, -, *, /)
        (Inter: % (mod), ++ (increase 1), -- (decrease 1))

         Sequence Import
         ++a vs a++ (Here the ++/-- positions matter) todo try
         */

        if (a == 10){
            System.out.print("A value is Equal 10\n");
        } else if (a > 10) {
            System.out.print("A value is greater than 10\n");
        } else { // Here we don't write (a < 10) because there is no other possibility
            System.out.print("A value is smaller than 10\n");
        }

        // Random Library
        Random random = new Random();

        int d = random.nextInt(0,100); // between 0-100 (to,from)
        //System.out.println(d);
        double e = random.nextDouble(-1.0f,1.0f); // between (-1)-1
        //System.out.println(e);

        /* Math Library (Pre Included Library)
        Math.PI, Math.E
        pow: x power of y (Math.pow(2,5) = 2^5)
        abs: absolute value of x (Math.abs(-52) = 52)
        sqrt: square root (Math.sqrt(16) = √16) todo you can do this with Math.pow(16,(double)(1/2)) // because rooting is division power
        ceil: to minimum integer    (round up)
        floor: to maximum integer   (round down)
        max / min: between 2 int | double select as name (max/min)
         */

        // printf (F-Strings) todo souf
        double f = -5100.222222;
        //System.out.printf("%.2f\n",f); // 2 decimal
        //System.out.printf("%,.2f\n",f); // with commas

        // Padding (_ vs 0 vs -)X
        int g = 12;
        int h = 1234;
        //System.out.printf("%4d\n",g);
        //System.out.printf("%4d\n",h);

        //String Methods (Last Day Topic)
        String name = "Enes";
        //System.out.print("Your Name: ");
        //name = input.nextLine();

        int length = name.length();
        char first_letter = name.charAt(0);
        int index = name.indexOf("e"); // first
        int last_index = name.lastIndexOf("e"); // last
        String upper_name = name.toUpperCase();
        String lower_name = name.toLowerCase();
        String changed = name.replace("e","i");
        Boolean is_empty = name.isEmpty(); // Check for if String is empty or Not (True: empty "" False: not empty "...")

        //subString
        String email,nick,domain;
        email = "someone@gmail.com";
        //System.out.print("Your Mail: ");
        //email = input.nextLine(); // someone@gmail.com
        nick = email.substring(0,email.indexOf("@"));
        domain = email.substring(email.indexOf("@") + 1);
        //System.out.printf("Your Nick '%s' And Domain '%s'",nick,domain);

        // ternary
        int number = 651;
        String even_or_odd = number % 2 == 0 ? "EVEN" : "ODD"; // Template (boolean ? True Value : False Value)

        // Lists (Strict Amount of Slots)
        String[] list_of_str = {"Irem","Enes","Ahmet","Eylül","Mıhtar"}; // also just hold the addresses
        //System.out.printf("Index [3]: %d",list_of_int[3]);
        list_of_str[4] = "Fatma"; // change of value
        length = list_of_str.length;
        Arrays.sort(list_of_str);

        //True Form of Calling
        //System.out.print(list_of_str.get(2));
        //System.out.print(list_of_str[2]);

        // Arrays (Dynamic Amount of Slots)
        ArrayList<String> names_list_0 = new ArrayList<>(); // Creating a new Array
        ArrayList<String> names_list_1 = new ArrayList<>(Arrays.asList(list_of_str));

        // True Form of Calling
        //System.out.print(names_list_1.get(2));
        //System.out.printf(names_list_1[2]);

        names_list_1.add("Yeni Kayıt"); // adding a new value
        //System.out.printf(names_list_1.get(-1));


        // switch case
        // Version 1
        int value;
        System.out.print("Enter a number: ");
        value = input.nextInt();
        switch (value){ // todo break importance
            case 1:
                System.out.print("Monday\n");
                break;
            case 2:
                System.out.print("Tuesday\n");
                break;
            default:
                System.out.print("Any Other Day\n");
                break;
        }

        // Version 2
        String day_as_string = switch (value){ // value output switch-case
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };

        // Loops todo talk about continue and break
        // while loop
        int j = 5;
        while(j > 0){
            System.out.printf("current j value: %d\n",j);
            j--;
        }

        // do-while loop
        j = 5;
        do {
            System.out.printf("current j value: %d\n",j);
            j--;
        } while (j > 0);

        // for loop
        for (int k = 5; k > 0; k--){
            System.out.printf("current k value: %d\n",k);
        }

        // for loop with value type
        for (String s: list_of_str){
            System.out.printf("Names From List (In Order): %s",s);
        }

        input.close();
        return;
    }
}
