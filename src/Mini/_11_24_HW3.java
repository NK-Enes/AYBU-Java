package Mini;
import java.util.Scanner;
public class _11_24_HW3 {
    public static boolean isPalindrome(String word){
        if (word.length() <= 1) return true;
        if (word.charAt(0) == word.charAt(word.length() - 1)){
            return isPalindrome(word.substring(1,word.length()-1));
        }
        return false;
    }

    public static boolean isPalindrome(int n) {
        if (n < 0) return false;
        return isPalindrome(String.valueOf(n));
    }

    public static void main(String[] args) { // Change Here to use int version
        Scanner input = new Scanner(System.in);
        System.out.print("Your Palindrome Check: ");
        int word = input.nextInt();
        System.out.print("\n" + isPalindrome(word));
    }
}
