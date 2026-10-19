package Lessons;

import java.util.Scanner;

public class Lesson_1 {
    static void main() {
        Scanner input = new Scanner(System.in);

        // Section 1 - START
        System.out.print("Enter Day(None Case Sens): ");
        String day = input.nextLine().toLowerCase();
        int day_int;

        System.out.printf("Today is %s\n",day);
        switch (day){
            case "monday":
                day_int = 0;
                break;
            case "tuesday":
                day_int = 1;
                break;
            case "wednesday":
                day_int = 2;
                break;
            case "thursday":
                day_int = 3;
                break;
            case "friday":
                day_int = 4;
                break;
            case "saturday":
                day_int = 5;
                break;
            case "sunday":
                day_int = 6;
                break;
            default:
                day_int = -1;
                break;
        }
        System.out.printf("Day As Integer: %d\n",day_int);

        // Section 1 - END

    }
}
