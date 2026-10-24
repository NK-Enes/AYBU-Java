package PokeWar;

/*
Ne işe yarayacak? İki takımın birbirine karşı sırayla nasıl savaşacağını yöneten ana mekanizma olacak.

Nasıl çalışmalı? Her turun başında her iki takımın tüm canlı karakterlerini toplamalı, bu karakterleri hızlarına (speed) göre büyükten küçüğe sıralayarak bir
sıra kuyruğu (Turn Queue) oluşturmalıdır. Hızı yüksek olan karakterin hamleyi önce yapmasını sağlamalı ve döngüyü taraflardan biri yenilene kadar devam
ettirmelidir.
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import PokeWar.Roles.Archer;
import PokeWar.Roles.Tank;

public class GameEngine {
    Scanner input = new Scanner(System.in);
    Team player_1,player_2;
    String [] initiative;
    public GameEngine(){

    }

    public void StartGame (){
        List<Character> characterPool = new ArrayList<>();
        Collections.addAll(characterPool, this.player_1.get_team_members());
        Collections.addAll(characterPool, this.player_2.get_team_members());
        while (this.player_1.is_team_valid() && this.player_2.is_team_valid()){
            String turn_ID = this.initiative[0];

            if (this.initiative != null && this.initiative.length > 1) {
                // 1. Elemanları 1 birim sola kaydır
                System.arraycopy(this.initiative, 1, this.initiative, 0, this.initiative.length - 1);

                // 2. İlk elemanı en sona yaz
                this.initiative[this.initiative.length - 1] = turn_ID;
            }

            boolean done = false;
            while (!done){ // If action doesn't happen turn back to Action UI
                Character character = characterPool.stream()
                        .filter(c -> c.ID.equals(turn_ID))     // 1. Eşleşme koşulu
                        .findFirst()                                    // 2. İlk eşleşeni al
                        .orElse(null);                            // 3. Bulunamazsa null döndür
                assert character != null;
                character.Active_relists();
                UIManager.printActionMenu(turn_ID.startsWith("A") ? this.player_1.get_team_name() : this.player_2.get_team_name(),character);

                int user_chose = input.nextInt();
                if (user_chose == 0){
                    done = UserCharacterChange(character);
                } else if (user_chose == 1) {
                    done = UserAttack(character);
                } else if (user_chose == 2) {
                    done = UserDefend(character);
                } else if (user_chose == 3){
                    done = UserSkill(character);
                } else {
                    System.out.print("Geçersiz Girdi");
                }
            }
            try {Thread.sleep(1500);} catch (InterruptedException e) {}
        }
    }

    private boolean UserAttack (Character character){
        List <Character> possible_targets = new ArrayList<>();
        Character[] enemies = switch (character.ID.charAt(0)){
            case 'A'-> player_2.get_active_team_members();
            case 'R' -> player_1.get_active_team_members();
            default -> throw new IllegalStateException("Unexpected value: " + character.ID.indexOf(0));
        };
        int[] filled_position = new int[5]; // NONE,FRONT,BACK,FLY,UNDER
        for (Character c : enemies) filled_position[c.current_position.numeric]++;
        for (Character c: enemies){
            if (character.current_position.canAttack(c.current_position,filled_position)){
                possible_targets.add(c);
            }
        }

        UIManager.printBasicAttackTargetMenu(character,possible_targets.toArray(new Character[0]));
        int user_chose = input.nextInt();

        if (user_chose == 0){
            return false;
        } else {
            double[] result = character.attack();
            if (result[0] == 1){
                possible_targets.get(user_chose-1).guard(result[1],true);
            } else {
                System.out.printf("%s [%s] Missed Attack\n",character.name,character.ID);
            }
            return true;
        }
    }

    private boolean UserDefend (Character character){
        double new_stat = character.defend(character.Type_return());
        if (character.Type_return()){
            System.out.printf("%s [%s] | Ready To Dodge Attack (%c%.0f)\n",character.name,character.ID,'%',new_stat*100);
        } else {
            System.out.printf("%s [%s] | Ready Block Attack (%c%.0f)\n",character.name,character.ID,'%',new_stat*100);
        }
        return true;
    }

    private boolean UserSkill (Character character){
        return false;
    }

    private boolean UserCharacterChange (Character character){
        return false;
    }

    public void TeamBuilder (){
        Character[] characters_A = new Character[] {new Tank("Panzerkönig", "AT-1"),new Archer("Scharfschütze","AA-1")};
        Character[] characters_R = new Character[] {new Tank("Bronenosets", "RT-1"), new Archer("Sneznyj Bars", "RA-1")};

        UIManager.printBanner("Pocket War");
        this.player_1 = new Team(UIManager.get_player_name(true), characters_A);
        UIManager.printBanner("Pocket War");
        this.player_2 = new Team(UIManager.get_player_name(false), characters_R);
    }

    private void _TeamClear (int index){
        switch (index){
            case 1:
                player_1.change_member_position("",Position.NONE,true);
                break;
            case 2:
                player_2.change_member_position("",Position.NONE,true);
                break;
            default:
                System.out.print("ERROR ON '_TeamClear'");
        }
    }

    public void TeamPlacer (int index){
        Scanner input = new Scanner(System.in);
        int[] slots = new int[] {0,0,0};
        String user_input = "";
        while (!user_input.equals("OK")){
            UIManager.team_build(index == 1 ? this.player_1 : this.player_2);
            user_input = input.nextLine().toUpperCase();
            if (user_input.equals("CLEAR")){
                _TeamClear(index);
                slots = new int[] {0,0,0};
            } else if (!user_input.equals("OK")){
                String[] user_input_strlist = user_input.split("\\s+");
                int char_index = Integer.parseInt(user_input_strlist[0]) - 1;
                int[] slot_indexes = switch (user_input_strlist[1]){
                    case "A" -> new int[] {0,0};
                    case "B" -> new int[] {1,0};
                    case "C" -> new int[] {2,1};
                    default -> new int[] {3,0}; // so we get an error
                };
                if (slots[slot_indexes[0]] != 1){
                    slots[slot_indexes[0]] = 1;
                    switch (index){
                        case 1:
                            player_1.change_member_position
                                    (player_1.get_team_members()[char_index].ID,slot_indexes[1] == 0 ? Position.FRONT : Position.BACK,false);
                            break;
                        case 2:
                            player_2.change_member_position
                                    (player_2.get_team_members()[char_index].ID,slot_indexes[1] == 0 ? Position.FRONT : Position.BACK,false);
                            break;
                    }
                } else {
                    System.out.printf("%s Slotu Dolu Başka Slot Seçiniz veya Sıfırlayınız.\n",user_input_strlist[1]);
                }
            }
        }
    }

    public void Initiative () {
        // 1. Adım: Hayatta olan karakterleri bir listede topla
        List<Character> allCharacters = new ArrayList<>();

        for (Character c : this.player_1.get_team_members()) {
            if (c.is_alive() && c.current_position != Position.NONE) {
                allCharacters.add(c);
            }
        }
        for (Character c : this.player_2.get_team_members()) {
            if (c.is_alive() && c.current_position != Position.NONE) {
                allCharacters.add(c);
            }
        }

        // 2. Adım: SPEED değerine göre büyükten küçüğe sırala
        allCharacters.sort((c1, c2) -> Double.compare(c2.SPEED, c1.SPEED));

        // 3. Adım: Sıralanan karakterlerin ID'lerini String[] dizisine aktar
        String[] sortedIDs = new String[allCharacters.size()];
        for (int i = 0; i < allCharacters.size(); i++) {
            sortedIDs[i] = allCharacters.get(i).ID;
        }

        this.initiative = sortedIDs;
    }
}

