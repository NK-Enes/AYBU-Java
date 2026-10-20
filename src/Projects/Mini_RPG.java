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
        double monster_atc = random.nextDouble(difficulty/10,difficulty*(win_effect)/2.5);
        double monster_def = random.nextDouble(difficulty/10,difficulty*(win_effect)/2.5);
        return new double[] { monster_hp, monster_atc, monster_def };
    }

    static double[] Player_vs_Monster (String name,double[] player,double[] player_max,double[] monster,double[] monster_max,boolean[] status){

        Scanner input = new Scanner(System.in);
        Random random = new Random();

        int attack_counter = 0;

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
            String player_state,monster_state;
            if (!status[0]){
                player_state = "Ready";
            } else {
                player_state = "Stunned";
            }

            if (!status[1]){
                monster_state = "Ready";
            } else {
                monster_state = "Stunned";
            }

            System.out.print("\n\n\n===================================\n"); //35
            System.out.printf("%14s (%s)\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    name,player_state,current_hp,MAX_HP,current_atk,MAX_ATK,current_def,MAX_DEFENCE);
            System.out.print("-----------------------------------\n");
            System.out.printf("%14s (%s)\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    "Monster",monster_state,monster_hp,monster_max[0],monster_atk,monster_max[1],monster_def,monster_max[2]);
            System.out.print("-----------------------------------\n");
            if (!status[0]){
                System.out.print("What Action you take\n");
                System.out.print("1- Basic Attack\n");
                System.out.print("2- Basic Defence\n");
                System.out.print("3- Shield Break\n");
                System.out.print("4- Class Ability (0/X)\n"); // Future Purposes
                System.out.print("5- Flee\n");
                System.out.print("===================================\n");
                int chose = input.nextInt();

                if (chose == 1){
                    double hit = random.nextDouble(0,100);
                    if (hit >= monster_def){
                        attack_counter ++;
                        double multipler = random.nextDouble(0.5,1.5) + (attack_counter * 0.1); //Crit or Miss
                        System.out.printf("SUCCESSFUL| You Slash Enemy Deal %.2f Damage (%.2f Multipler %d Combo)\n",
                                multipler*current_atk+(attack_counter * 0.1),multipler,attack_counter);
                        monster_hp -= multipler * current_atk;
                        if (monster_hp <= 0){
                            boolean atk_def = random.nextBoolean();
                            System.out.print("You Slain The Monster\n");
                            if (atk_def){
                                double reward = random.nextDouble(MAX_ATK*0.1,MAX_ATK*0.2);
                                System.out.printf("You Gain +%.2f Attack\n",reward);
                                return new double[]{1, current_hp, current_atk+reward, current_def,0};
                            } else{
                                double reward = random.nextDouble(MAX_DEFENCE*0.1,MAX_DEFENCE*0.2);
                                System.out.printf("You Gain +%.2f Defence\n",reward);
                                return new double[]{1, current_hp, current_atk, current_def,reward};
                            }
                        }
                    } else{
                        System.out.print("FAILED| You Missed Your Attack\n");
                    }

                    if (!status[1]){
                        hit = random.nextDouble(0,100);
                        if (hit >= current_def){
                            double multipler = random.nextDouble(0.5,1.2); //Crit or Miss
                            System.out.printf("FAILED| You take hit from Monster Deal %.2f Damage (%.2f Multipler)\n",multipler*monster_atk,multipler);
                            current_hp -= multipler * monster_atk;
                            if (current_hp <= 0){
                                return new double[]{1, current_hp, current_atk, current_def,0};
                            }
                        } else{
                            System.out.print("SUCCESSFUL| You dodged Monster's Attack\n");
                        }
                    } else {
                        System.out.print("STUNNED| Monster is stunned\n");
                        status[1] = false;
                    }
                } else if (chose == 2) {
                    System.out.print("SUCCESSFUL| You Gain 50 Defence Temporary\nSUCCESSFUL| You Gain 10 Defence Permanent\n");
                    current_def = Math.min(player_max[2],current_def+10);
                    if (!status[1]){
                        double hit = random.nextDouble(0,100);
                        if (hit >= current_def + 40){
                            double multipler = random.nextDouble(0.5,1.2); //Crit or Miss
                            System.out.printf("FAILED| You take hit from Monster Deal %.2f Damage (%.2f Multipler)\n",multipler*monster_atk,multipler);
                            current_hp -= multipler * monster_atk;
                            if (current_hp <= 0){
                                boolean atk_def = random.nextBoolean();
                                System.out.print("You Slain The Monster\n");
                                if (atk_def){
                                    double reward = random.nextDouble(MAX_ATK*0.1,MAX_ATK*0.2);
                                    System.out.printf("You Gain +%.2f Attack\n",reward);
                                    return new double[]{1, current_hp, current_atk+reward, current_def,0};
                                } else{
                                    double reward = random.nextDouble(MAX_DEFENCE*0.1,MAX_DEFENCE*0.2);
                                    System.out.printf("You Gain +%.2f Defence\n",reward);
                                    return new double[]{1, current_hp, current_atk, current_def,reward};
                                }
                            }
                        } else{
                            System.out.print("SUCCESSFUL| You dodged Monster's Attack\n");
                        }
                    } else {
                        System.out.print("STUNNED| Monster is stunned\n");
                        status[1] = false;
                    }
                } else if (chose == 3) {
                    double break_percent = random.nextDouble(5,100);
                    System.out.printf("SUCCESSFUL| You Break %c%.2f (-%.2f) of Monster defence\n",'%',break_percent,break_percent*monster_def / 100);
                    monster_def -= break_percent*monster_def / 100;
                    if (!status[1]){
                        double hit = random.nextDouble(0,100);
                        if (hit >= current_def + 40){
                            double multipler = random.nextDouble(0.5,1.2); //Crit or Miss
                            System.out.printf("FAILED| You take hit from Monster Deal %.2f Damage (%.2f Multipler)\n",multipler*monster_atk,multipler);
                            current_hp -= multipler * monster_atk;
                            if (current_hp <= 0){
                                boolean atk_def = random.nextBoolean();
                                System.out.print("You Slain The Monster\n");
                                if (atk_def){
                                    double reward = random.nextDouble(MAX_ATK*0.1,MAX_ATK*0.2);
                                    System.out.printf("You Gain +%.2f Attack\n",reward);
                                    return new double[]{1, current_hp, current_atk+reward, current_def,0};
                                } else{
                                    double reward = random.nextDouble(MAX_DEFENCE*0.1,MAX_DEFENCE*0.2);
                                    System.out.printf("You Gain +%.2f Defence\n",reward);
                                    return new double[]{1, current_hp, current_atk, current_def,reward};
                                }
                            }
                        } else{
                            System.out.print("SUCCESSFUL| You dodged Monster's Attack\n");
                        }
                    } else {
                        System.out.print("STUNNED| Monster is stunned\n");
                        status[1] = false;
                    }
                } else if (chose == 4) {
                    System.out.print("DUMB BITCH IT'S STILL NOT INCLUDE\n");
                } else if (chose == 5) {
                    double run_value = random.nextDouble(0,300);
                    if (run_value >= 160 || status[1]) { // 160 Const
                        System.out.print("Successful| You Run Away From Monster\n");
                        return new double[]{0, current_hp, current_atk, current_def,0};
                    } else {
                        System.out.print("FAILED| Monster Catch You and Slash You \n");
                        double multipler = random.nextDouble(0.5,1.2); //Crit or Miss
                        System.out.printf("FAILED| You take hit from Monster Deal %.2f Damage (%.2f Multipler)\n",multipler*monster_atk,multipler);
                        current_hp -= multipler * monster_atk;
                        if (current_hp <= 0) {
                            boolean atk_def = random.nextBoolean();
                            System.out.print("You Slain The Monster\n");
                            if (atk_def) {
                                double reward = random.nextDouble(MAX_ATK * 0.1, MAX_ATK * 0.2);
                                System.out.printf("You Gain +%.2f Attack\n", reward);
                                return new double[]{1, current_hp, current_atk + reward, current_def, 0};
                            } else {
                                double reward = random.nextDouble(MAX_DEFENCE * 0.1, MAX_DEFENCE * 0.2);
                                System.out.printf("You Gain +%.2f Defence\n", reward);
                                return new double[]{1, current_hp, current_atk, current_def, reward};
                            }
                        }
                    }
                }
            } else {
                // Player Stunned
                System.out.print("STUNNED| You are stunned\n");
                status[0] = false;
                if (!status[1]){
                    double hit = random.nextDouble(0,100);
                    if (hit >= current_def + 40){
                        double multipler = random.nextDouble(0.5,1.2); //Crit or Miss
                        System.out.printf("FAILED| You take hit from Monster Deal %.2f Damage (%.2f Multipler)\n",multipler*monster_atk,multipler);
                        current_hp -= multipler * monster_atk;
                        if (current_hp <= 0){
                            System.out.print("You Slain by Monster\n");
                            return new double[]{0, current_hp, current_atk, current_def,0};
                        }
                    } else{
                        System.out.print("SUCCESSFUL| You dodged Monster's Attack\n");
                    }
                } else {
                    System.out.print("STUNNED| Monster is stunned\n");
                    status[1] = false;
                }
            }
            monster_def = Math.min(monster_max[2],monster_def + monster_max[2]*0.1);
        }

        return new double[] {0,current_hp,current_atk,current_def,0};
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
                MAX_DEFENCE = 40;
                MAX_ATK = 30;
                break;
            case 2:
                character_class = "Archer";
                MAX_HP = 80;
                MAX_DEFENCE = 30;
                MAX_ATK = 45;
                break;
            case 3:
                character_class = "Mage";
                MAX_HP = 50;
                MAX_DEFENCE = 15;
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
        double [] result = new double[5];

        while (current_hp > 0){ // If player alive
            difficulty = 50 * (((current_hp / MAX_HP) - 0.3) + (current_atk / MAX_ATK)) + (MAX_DEFENCE/2);
            double[] monster = Monster_Generation(difficulty, win_streak);
            double monster_hp = monster[0];
            double monster_atc = monster[1];
            if (monster_atc*7 > current_hp){
                monster_atc /= 2;
            }
            double monster_def = monster[2] * 0.1;
            boolean monster_stunned = false;
            boolean player_stunned = false;

            System.out.print("\n\n\n===================================\n"); //35
            System.out.printf("%14s (%s)\nHP: %22.2f / %.2f\nAtk: %22.2f / %.2f\nDef: %22.2f / %.2f\n",
                    name,"Ready",current_hp,MAX_HP,current_atk,MAX_ATK,current_def,MAX_DEFENCE);
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
                result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                        new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
            } else if (chose == 2) {
                double run_value = random.nextDouble(0,300);
                if (run_value >= difficulty){
                    System.out.print("Successful| You stunned Monster\n");
                    monster_stunned = true;
                    result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
                else{
                    System.out.print("Failed| You're stunned by Monster\n");
                    player_stunned = true;
                    result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
            } else if (chose == 3) {
                double run_value = random.nextDouble(0,300);
                if (run_value >= difficulty){
                    win_streak = 0;
                    System.out.print("Successful| You Run Away From Monster\n");
                    result = new double[] {0,current_hp,current_atk,current_def,0};
                }
                else{
                    result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
            } else{
                System.out.print("DEFAULT ACTION: Flee\n");
                double run_value = random.nextDouble(0,300);
                if (run_value >= difficulty){
                    win_streak = 0;
                    System.out.print("Successful| You Run Away From Monster\n");
                    result = new double[] {0,current_hp,current_atk,current_def,0};
                }
                else{
                    result = Player_vs_Monster(name,new double[] {current_hp,current_atk,current_def},new double[] {MAX_HP,MAX_ATK,MAX_DEFENCE},
                            new double[] {monster_hp, monster_atc,monster_def},monster,new boolean[] {player_stunned,monster_stunned});
                }
            }
            current_hp = Math.min(result[1]+(MAX_HP*0.3),MAX_HP); // %30 heal
            current_def = result[3]*0.5; // Lose some defense
            current_atk = result[2];
            MAX_DEFENCE += result[4];
            if (result[0] == 1){
                win_streak ++;
            } else {
                win_streak = 0;
            }
        }

    }
}
