package Mini;
import java.util.Scanner;
public class _11_24_HW2 {
    public static boolean isVowel (char c){
        String vowels = "aeiıoöoüu";
        c = Character.toLowerCase(c);
        return vowels.indexOf(c) != -1;
    }

    public static int countX (String word, boolean type){
        int count = 0;
        for (char c : word.toCharArray()){
            if (Character.isLetter(c)){
                if (type && isVowel(c)) count++;
                else if (!type && !isVowel(c)) count++;
            }
        }
        return count;

    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Input To Count: ");
        String word = input.nextLine();
        System.out.println();
        System.out.print("Input To What Count (1:Vowels 0:Consonants): ");
        boolean type = input.nextInt() == 1;
        System.out.println();
        System.out.print(countX(word,type));
    }
}
