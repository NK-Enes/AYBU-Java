package Mini;

/*
Assignment 2: Password Strength Meter and Text Analysis Tool
Scope: String methods (length(), charAt(), toUpperCase(), contains(), etc.) and if-else structures.

Scenario: Write a security program that prompts the user to enter a password.

Requirements:

Check the length of the password (length()). If it is shorter than 6 characters, print "Very Short"; if it is between 6 and 10 characters,
print "Medium"; if it is longer than 10 characters, print "Strong."

Check if the password contains any spaces (contains(" ")). If it does, alert the user.

Retrieve the first character of the password using charAt(0), convert it to uppercase, and display it to the user.

Report the analysis results using printf.
 */

import java.util.Scanner;

public class _10_19_HW_2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Password (At least 6 char): ");
        String password = input.nextLine();

        int pass_lenght = password.length();

        // Length Check
        if (pass_lenght < 6){
            System.out.printf("%d Char is Too Short to be a password\n",pass_lenght);
            return;
        } else if (pass_lenght >= 6 && pass_lenght <= 10) {
            System.out.printf("%d Char can secure your datas\n",pass_lenght);
        }
        else{
            System.out.printf("%d Are you work for CIA ???\n",pass_lenght);
        }

        // " " Blank Check
        if (password.contains(" ")){
            System.out.printf("Dumb or what ' ' (Blank) in %d index\n",password.indexOf(" "));
            return;
        }

        // Index Return
        System.out.printf("Index(0-%d): ",pass_lenght-1);
        int index = input.nextInt();
        System.out.printf("'%c' is your %d index char \nUpper Version: '%c'\n",password.charAt(index),index,password.toUpperCase().charAt(index));
    }
}
