package Projects;

/*
This is a basic RPG game on terminal
 */

import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class Mini_RPG {
    static double[] Monster_Generation (double difficulty,int win_streak){
        Random random = new Random();
        win_streak = Math.max(1,win_streak);
        double monster_hp = random.nextDouble(difficulty,difficulty*(win_streak*1.01));
        double win_effect = Math.log(Math.max(win_streak*2,3));
        double monster_atc = random.nextDouble(difficulty/5,difficulty*(win_effect)/2);
        double monster_def = random.nextDouble(difficulty/5,difficulty*(win_effect)/2);
        return new double[] { monster_hp, monster_atc, monster_def };
    }

    static double[] Player_vs_Monster (String name,double[] player,double[] player_max,double[] monster,double[] monster_max,boolean[] status){

        Scanner input = new Scanner(System.in);
        Random random = new Random();

        double current_hp = player[0];
        double current_atk = player[1];
        double current_def = player[2];

        double MAX_HP = player_max[0];
        double MAX_ATK = player_max[1];
        double MAX_DEFENCE = player_max[2];

        double monster_hp = monster[0];
        double monster_atk = monster[1];
        double monster_def = monster[2];

        while (current_hp > 0 && monster_hp > 0){
            System.out.print("\n\n\n===================================\n"); //35
            System.out.printf("%14s (%s)\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    name,"Uncrowded",current_hp,MAX_HP,current_atk,MAX_ATK,current_def,MAX_DEFENCE);
            System.out.print("-----------------------------------\n");
            System.out.printf("%19s\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    "Monster",monster_hp,monster_max[0],monster_atk,monster_max[1],monster_def,monster_max[2]);
            System.out.print("-----------------------------------\n");
            System.out.print("What Action you take\n");
            System.out.print("1- Basic Attak\n");
            System.out.print("2- Basic Defence\n");
            System.out.print("3- Class Ability (0/X)\n"); // Future Purposes
            System.out.print("4- Flee\n");
            System.out.print("===================================\n");
            int chose = input.nextInt();

            if (chose == 1){
                double hit = random.nextDouble(0,100);
                if (hit >= monster_def){
                    double multipler = random.nextDouble(0.5,1.5); //Crit or Miss
                    System.out.printf("SUCCESSFUL| You Slash Enemy Deal %.2f Damage (%.2f Multipler)\n",multipler*current_atk,multipler);
                    monster_hp -= multipler * current_atk;
                    if (monster_hp <= 0){
                        System.out.print("You Slain The Monster\n");
                        return new double[]{1, current_hp, current_atk, current_def};
                    }
                } else{
                    System.out.print("FAILED| You Missed Your Attack");
                }

                hit = random.nextDouble(0,100);
                if (hit >= current_def){
                    double multipler = random.nextDouble(0.5,1.5); //Crit or Miss
                    System.out.printf("FAILED| You take hit from Monster Deal %.2f Damage (%.2f Multipler)\n",multipler*monster_atk,multipler);
                    current_hp -= multipler * current_atk;
                    if (current_hp <= 0){
                        System.out.print("You Slain by Monster\n");
                        return new double[]{0, current_hp, current_atk, current_def};
                    }
                } else{
                    System.out.print("SUCCESSFUL| You dodged Monster's Attack\n");
                }
            } else if (chose == 2) {

            } else if (chose == 3) {
                System.out.print("DUMB BITCH IT'S STILL NOT INCLUDE\n");
            } else if (chose == 4) {
                double run_value = random.nextDouble(0,300);
                if (run_value >= 160) { // 160 Const
                    System.out.print("Successful| You Run Away From Monster\n");
                    return new double[]{0, current_hp, current_atk, current_def};
                }
            }
        }

        return new double[] {0,current_hp,current_atk,current_def};
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();

        //Character Stats
        String name = "";
        String character_class = "None";
        double MAX_HP = 0;
        double MAX_DEFENCE = 0;
        double MAX_ATK = 0;

        int win_streak = 0;
        double difficulty;

        double current_hp = 0;
        double current_def = 0;
        double current_atk = 0;

        // Name Entry
        System.out.print("===================================\n"); //35
        System.out.print("Your Name Adventurer: ");
        name = input.nextLine();
        System.out.print("===================================\n\n\n");

        // Class Selection
        System.out.print("===================================\n");
        System.out.printf("%s Chose Your Character Class    \n",name);
        System.out.print("1- Warrior    (HIGH Defensive)\n");
        System.out.print("2- Archer     (Balanced stats)\n");
        System.out.print("3- Mage        (HIGH Ofansive)\n");
        System.out.print("===================================\n");
        System.out.print("Your Chose: ");
        int class_chose = input.nextInt();

        switch (class_chose){
            case 1:
                character_class = "Warrior";
                MAX_HP = 100;
                MAX_DEFENCE = 20;
                MAX_ATK = 30;
                break;
            case 2:
                character_class = "Archer";
                MAX_HP = 80;
                MAX_DEFENCE = 10;
                MAX_ATK = 45;
                break;
            case 3:
                character_class = "Mage";
                MAX_HP = 50;
                MAX_DEFENCE = 5;
                MAX_ATK = 70;
                break;
            case 1100:
                character_class = "God";
                MAX_HP = 100000;
                MAX_DEFENCE = 100;
                MAX_ATK = 100;
                break;
            default:
                character_class = "Farmer";
                MAX_HP = 50;
                MAX_DEFENCE = 5;
                MAX_ATK = 30;
                break;
        };

        //Assigning current values
        current_hp = MAX_HP * 0.5; // Gain by Restore
        current_def = 0; // Gain by Armors
        current_atk = MAX_ATK * 0.1; // Gain by Lvl And weapons

        while (current_hp > 0){ // If player alive
            difficulty = 50 * (((current_hp / MAX_HP) - 0.3) + (current_atk / MAX_ATK) + (current_def / MAX_DEFENCE));
            double[] monster = Monster_Generation(difficulty, win_streak);
            double monster_hp = monster[0];
            double monster_atc = monster[1];
            double monster_def = monster[2];
            boolean monster_stunned = false;
            boolean player_stunned = false;

            System.out.print("\n\n\n===================================\n"); //35
            System.out.printf("%14s (%s)\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    name,"Uncrowded",current_hp,MAX_HP,current_atk,MAX_ATK,current_def,MAX_DEFENCE);
            System.out.print("-----------------------------------\n");
            System.out.printf("%19s\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    "Monster_"+win_streak,monster_hp,monster[0],monster_atc,monster[1],monster_def,monster[2]);
            System.out.print("-----------------------------------\n");
            System.out.print("What Action You take against Monster\n");
            System.out.print("1- Encounter\n");
            System.out.print("2- Sneak Attack\n");
            System.out.print("3- Flee\n");
            System.out.print("0- Leave Game\n");
            System.out.print("-----------------------------------\n");
            int chose = input.nextInt();
            if (chose == 0){
                break; // Leave While
            } else if (chose == 1) {
                double[] result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                        new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
            } else if (chose == 2) {
                double run_value = random.nextDouble(0,300);
                if (run_value >= difficulty){
                    System.out.print("Successful| You stunned Monster");
                    monster_stunned = true;
                    double[] result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
                else{
                    System.out.print("Failed| You're stunned by Monster");
                    player_stunned = true;
                    double[] result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
            } else if (chose == 3) {
                double run_value = random.nextDouble(0,300);
                if (run_value >= difficulty){
                    win_streak = 0;
                    System.out.print("Successful| You Run Away From Monster");
                }
                else{
                    double[] result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
            } else{
                System.out.print("DEFAULT ACTION: Flee");
                double run_value = random.nextDouble(0,300);
                if (run_value >= difficulty){
                    win_streak = 0;
                    System.out.print("Successful| You Run Away From Monster");
                }
                else{
                    double[] result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
            }
            //win_streak ++;
        }

    }
}
