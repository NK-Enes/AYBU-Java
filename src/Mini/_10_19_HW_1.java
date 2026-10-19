package Mini;

import java.util.Scanner;

/*
Assignment 1: Smart Café/Restaurant Bill System
Scope: Basic variables, operations, `printf`, and simple `if-else` statements.

Scenario: You are working as a cashier at a café. The program should prompt for the price and quantity of 3 different items (e.g., Coffee, Tea, Pasta).

Requirements:

Calculate the subtotal for the items.

Apply a small discount if the total amount exceeds a certain threshold (e.g., 200 TL); otherwise, do not apply a discount (use `if-else`).

Add 10% VAT (Value Added Tax) to the total amount.

The final output should be presented as a neatly formatted bill, with items aligned vertically and decimal values displayed with
two digits of precision, using `printf`.
 */


public class _10_19_HW_1 {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.print("========================================\n");
        System.out.print("             CAFE MENU                  \n");
        System.out.print("========================================\n");
        System.out.print(" 1. Coffee ------------------------$4.99\n");
        System.out.print(" 2. Tea ---------------------------$2.49\n");
        System.out.print(" 3. Cake --------------------------$9.99\n");
        System.out.print("========================================\n\n");
        double coffe_price,tea_price,cake_price;
        coffe_price = 4.99;
        tea_price = 2.49;
        cake_price = 9.99;

        System.out.print("Enter number of coffee ordered: ");
        int coffee = input.nextInt();
        System.out.print("Enter number of tea ordered: ");
        int tea = input.nextInt();
        System.out.print("Enter number of cake ordered: ");
        int cake = input.nextInt();

        System.out.print("\n\n========================================\n");

        double total = (coffee * coffe_price) + (tea * tea_price) + (cake * cake_price);
        if (total >= 100){
            System.out.printf("$%.2f Discount\n",total*0.1);
            total *= 0.9; // %10 discount if more than $100
        } else if (total <= 20) {
            System.out.printf("$%f is bellow minimum ($20) total\n",total);
            total = 20; // minimum total
        }
        System.out.printf("VAT (%s): $%.2f\n","%10",total*0.1);
        total *= 1.1; // %10 VAT (KDV)

        System.out.printf("Your Total is $%.2f Cash OR $%.2f Credit\n",total,total*1.1); // I added %10 for credit user
        System.out.print("========================================\n");
        input.close();
    }
}
