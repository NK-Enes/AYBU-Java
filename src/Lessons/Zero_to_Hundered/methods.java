package Lessons.Zero_to_Hundered;

import java.util.Locale;

public class methods {
    static void main(String[] args) {
        char[] name = new char[] {'E','n','e','s'};
        String name_str = "Enes";
        System.out.println(name_str.charAt(2));
        System.out.println(name[2]);
        name[2] = 'b';
        for(char c: name){
            System.out.println(c);
        }
    }

    public void write(){
        return;
    }

    public int turn_int (){
        return 1;
    }

    public int[] turn_int_list() {
        return new int[] {1,2,3};
    }
}
