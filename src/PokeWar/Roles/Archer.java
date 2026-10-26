package PokeWar.Roles;

/*
Ne işe yarayacak? Her bir sınıfın kendine özgü kimliğini ve özel yetenek mekaniğini temsil edecek.

Nasıl çalışmalı? Temel Character sınıfından özellik alarak (extends), kendi rollerine uygun özel yetenekleri
(Örn: Şifacının takım arkadaşını iyileştirmesi, Tankın yüksek defansla ön safta durması, Suikastçının yüksek hızla kritik vurması)
kendi içlerinde özelleştirmeliler.
*/

import java.util.Random;
import PokeWar.Character;
import PokeWar.Position;

public class Archer extends Character {
    public Archer (String name,String ID){
        super(
                name,ID,"YUKSEK DPS",Position.NONE,
                new double[] {120.00,30.0,45.0,25.0},
                new double[] {120.0,30.0,45.0},
                Position.BACK,
                new double[] {0.6,0.1,0.4}
        );
    }

    public boolean Skill_1 (Character target, Character[] allies, Character[] enemies){
        // Sniper Shot
        Random random = new Random();
        double multiple = random.nextDouble(0.7,1.5);
        System.out.printf("%s Attack with %.2f Real Damage\n",this.name,this.current_atk*multiple);
        target.guard(this.current_atk*multiple,false);
        return true;
    }
    public boolean Skill_2 (Character target, Character[] allies, Character[] enemies){
        // Multi Target Shot
        for (Character enemy : enemies) {
            System.out.printf("%s Attack %s with %.2f Attack\n",this.name,enemy.name,this.current_atk);
            enemy.guard(this.current_atk,true); // Shot Every Enemy
        }
        return true;
    }
    public boolean Skill_3 (Character target, Character[] allies, Character[] enemies){
        // Multi Shot On An Enemy
        Random random = new Random();
        boolean is_shot = true;
        double expected_damage = 0;
        int combo = 0;
        double shot_change;
        double multiple;
        double shot_happen;
        while(is_shot){
            shot_change = 1 - (0.05 * combo);
            combo++;
            shot_happen = random.nextDouble();
            if (shot_happen >= (1 - shot_change)){
                System.out.printf("%s's Combo is NOW %d\n",this.name,combo);
                multiple = random.nextDouble(0.5,1.5);
                expected_damage += this.current_atk * (multiple + (combo * 0.1));
            } else {
                is_shot = false;
            }
        }
        System.out.printf("TOTAL Combo: %d Expected Damage: %.2f\n",combo,expected_damage);
        target.guard(expected_damage,true);
        return true;
    }

    @Override
    public boolean Type_return () {return false;};

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "KESKIN NIŞAN";
            case 2: return "OK YAĞMURU";
            case 3: return "ÇOKLU ATIŞ";
            default: return "Bilinmeyen Yetenek";
        }
    }

    @Override
    public int get_target (int skillIndex) {
        switch (skillIndex) {
            case 1: return 1;
            case 2: return 0;
            case 3: return 1;
            default: return 0;
        }
    }

    @Override
    public String infoAbility(int skillIndex){
        return switch (skillIndex){
            case 1 -> "Rakibe kaçamayacağı bir saldırı yapar. (X 0.7-1.5)";
            case 2 -> "Tüm rakiplere birer saldırı yapar.";
            case 3 -> "Bir rakibe ard arda saldırı yapar.";
            default -> "Geçersiz Yetenek";
        };
    }

    @Override
    public boolean useAbility(int skillIndex, Character target, Character[] allies, Character[] enemies) {
        boolean done = false;
        switch (skillIndex) {
            case 1 :
                done = Skill_1(target,allies,enemies);
                break;
            case 2 :
                done = Skill_2(target,allies,enemies);
                break;
            case 3 :
                done = Skill_3(target,allies,enemies);
                break;
            default :
                System.out.println("Geçersiz yetenek!");
                break;
        }
        return done;
    }
}
