package Mini;

/*
Assignment 3: Mini RPG Class Selection and Combat Simulator
Scope: Switch-case, if-else, basic arithmetic, and string concatenation.

Scenario: Create a simple text-based game featuring character selection and a combat menu.

Requirements:

Present 3 classes to the user (use switch-case):

Warrior (High HP, Low Damage)

Mage (Low HP, High Damage)

Archer (Balanced HP and Damage)

Assign starting HP and attack power values to variables based on the selected class.

Introduce a monster (either with random or fixed stats) that also has an HP value.

Provide the user with "Attack" or "Defend" options (use if-else). If the user chooses to attack, subtract your damage value from the monster's HP;
 at the end of the turn, display the remaining HP for both the player and the monster using `printf`.
 */

import java.util.Scanner;
import java.util.Random;

public class _10_19_HW_3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();

        int HP = 0;
        int ATK = 0;
        String type = "";

        System.out.print("=====================================\n");
        System.out.print("1- Warrior      (High HP, Low Damage)\n");
        System.out.print("2- Mage         (Low HP, High Damage)\n");
        System.out.print("3- Archer    (Balanced HP and Damage)\n");
        System.out.print("=====================================\n");

        int chose = input.nextInt();
        switch (chose){
            case 1:
                HP = 100;
                ATK = 20;
                type = "Warrior";
                break;
            case 2:
                HP = 40;
                ATK = 70;
                type = "Mage";
                break;
            case 3:
                HP = 70;
                ATK = 50;
                type = "Archer";
                break;
            case 651:
                HP = 1000;
                ATK = 1000;
                type = "God";
                break;
            default:
                HP = 20;
                ATK = 20;
                type = "Default";
        }
        System.out.printf("Your Character: %s (HP: %d ATK: %d)\n",type,HP,ATK);

        int MonsterHP = random.nextInt(20,100);
        int MonsterATK = random.nextInt(50-(MonsterHP/2),60);
        System.out.printf("Your Oppenent: Monster (HP: %d ATK %d)\n",MonsterHP,MonsterATK);

        System.out.print("=====================================\n");
        System.out.print("1- Attack\n");
        System.out.print("2- Defend\n");
        System.out.print("0- Run Away\n");
        System.out.print("=====================================\n");

        System.out.print("Chose: ");
        chose = input.nextInt();
        if (chose == 0){
            boolean run_attempt = random.nextBoolean();
            if (run_attempt){
                System.out.printf("SUCCESS| You Run successfully (HP: %d)\n",HP);
            }
            else{
                HP -= MonsterATK;
                if (HP > 0){
                    System.out.printf("FAILED| You Take 1 Hit From Monster than Run Away (HP: %d)\n",HP);
                }
                else {
                    System.out.print("FAILED| You fall to ground YOU DIE\n");
                }
            }
        } else if (chose == 1) {
            double crit = random.nextDouble(1,2);
            System.out.printf("Your Crit Multipler: %.2f\n",crit);
            MonsterHP -= (int)(ATK * crit);
            if (MonsterHP > 0){
                HP -= MonsterATK;
                if (HP > 0){
                    System.out.printf("SUCCESS| You Both Take Hit (Your HP: %d Monster HP: %d)\n",HP,MonsterHP);
                }
                else{
                    System.out.printf("FAILED| You fall to ground YOU DIE (Monster HP: %d)\n",MonsterHP);
                }
            }
            else{
                System.out.print("SUCCESS| You Beat the Monster (+100 Gold)\n");
            }
        } else if (chose == 2) {
            boolean defend = random.nextBoolean();
            if (defend) {
                System.out.printf("SUCCESS| You Successfully defend Monster Stunned (Your HP: %d Monster: %d)\n",HP,MonsterHP);
            }
            else {
                HP -= MonsterATK;
                if (HP > 0){
                    System.out.printf("FAILED| You failed and take the hit (Your HP: %d Monster: %d\n",HP,MonsterHP);
                }
                else {
                    System.out.printf("FAILED| You fall to ground YOU DIE (Monster HP: %d)\n",MonsterHP);
                }
            }
        } else {
            System.out.print("INVALID INPUT (DEFAULT: RUN)\n");
            boolean run_attempt = random.nextBoolean();
            if (run_attempt){
                System.out.printf("SUCCESS| You Run successfully (HP: %d)\n",HP);
            }
            else{
                HP -= MonsterATK;
                if (HP > 0){
                    System.out.printf("FAILED| You Take 1 Hit From Monster than Run Away (HP: %d)\n",HP);
                }
                else {
                    System.out.print("FAILED| You fall to ground YOU DIE\n");
                }
            }
        }
    }
}
