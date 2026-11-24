package Mini;

public class _11_24_HW_1 {
    public static boolean isPerfect (int number){
        if (number <= 1) return false;
        int total = 0;
        for (int current_number = number - 1 ; current_number != 0 ; current_number--){
            if (number % current_number == 0) total += current_number;
        }
        //System.out.println(total);
        return total == number;
    }
    public static void main (String[] args){
        boolean print;
        for (int i = 1; i < 10000; i++){
            print = isPerfect(i);
            if (print) System.out.println(i);
        }
    }
}
