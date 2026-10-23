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
    public Position current_position;
    public Archer (String name,String ID){
        super(
                name,ID,"HIGH DPS",Position.NONE,
                new double[] {120.00,30.0,45.0,25.0},
                new double[] {120.0,30.0,45.0},
                Position.BACK,
                new double[] {0.6,0.1,0.4}
        );
    }

    public void Skill_1 (Character target, Character[] allies, Character[] enemies){
        // Sniper Shot
        Random random = new Random();
        double multiple = random.nextDouble(0.7,1.5);
        target.guard(this.current_atk*multiple,false);
    }
    public void Skill_2 (Character target, Character[] allies, Character[] enemies){
        // Multi Target Shot
        for (Character enemy : enemies) {
            enemy.guard(this.current_atk,true); // Shot Every Enemy
        }
    }
    public void Skill_3 (Character target, Character[] allies, Character[] enemies){
        // Multi Shot On An Enemy
        Random random = new Random();
        boolean is_shot = true;
        double expected_damage = 0;
        double combo = 0;
        double shot_change;
        double multiple;
        double shot_happen;
        while(is_shot){
            shot_change = 1 - (0.05 * combo);
            combo++;
            shot_happen = random.nextDouble();
            if (shot_happen >= (1 - shot_change)){
                multiple = random.nextDouble(0.5,1.5);
                expected_damage += this.current_atk * (multiple + (combo * 0.1));
            } else {
                is_shot = false;
            }
        }
        target.guard(expected_damage,true);
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
    public void useAbility(int skillIndex, Character target, Character[] allies, Character[] enemies) {
        switch (skillIndex) {
            case 1 -> Skill_1(target,allies,enemies);
            case 2 -> Skill_2(target,allies,enemies);
            case 3 -> Skill_3(target,allies,enemies);
            default -> System.out.println("Geçersiz yetenek!");
        }
    }
}
