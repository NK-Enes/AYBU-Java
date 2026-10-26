package PokeWar.Roles;

/*
Ne işe yarayacak? Her bir sınıfın kendine özgü kimliğini ve özel yetenek mekaniğini temsil edecek.

Nasıl çalışmalı? Temel Character sınıfından özellik alarak (extends), kendi rollerine uygun özel yetenekleri
(Örn: Şifacının takım arkadaşını iyileştirmesi, Tankın yüksek defansla ön safta durması, Suikastçının yüksek hızla kritik vurması)
kendi içlerinde özelleştirmeliler.
*/

import PokeWar.Character;
import PokeWar.Position;

public class Healer extends Character {
    private int root;
    public Healer (String name,String ID){
        root = 0;
        super(
                name,ID,"CANLANDIRMA",Position.NONE,
                new double[] {100.00,40.0,10.0,20.0},
                new double[] {100.00,40.0,10.0},
                Position.BACK,
                new double[] {0.1,0.3,0.7}
        );
    }

    public void tick (){
        double heal_amount = root * current_atk / 5;
        System.out.printf("%s Healed with roots %.0f",this.name,heal_amount);
        this.current_hp += heal_amount;
        this.attack_change(root); // add root times atc
    }

    public boolean Skill_1 (Character target, Character[] allies, Character[] enemies){
        // Heal with missing health ratio
        double heal_amount = target.missing_health_percent() * current_atk * 2; // Approx: 0.9(T_MAX) * 40 * 2 = 72
        System.out.printf("%s Heal %s %.0f of Health",this.name,target.name,heal_amount);
        target.buff(new double[] {heal_amount,0,0});
        return true;
    }
    public boolean Skill_2 (Character target, Character[] allies, Character[] enemies){
        // Group Heal
        for (Character ally: allies){
            System.out.printf("%s Heal %s %.0f of Health",this.name,target.name,this.current_atk);
            ally.buff(new double[] {this.current_atk,0,0});
        }
        return true;
    }
    public boolean Skill_3 (Character target, Character[] allies, Character[] enemies){
        // Grown Roots that heal you
        System.out.printf("%s Gain 1 Additional Root",this.name);
        root ++;
        return true;
    }

    @Override
    public boolean Type_return () {return false;};

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "CANLAN";
            case 2: return "UYANIN";
            case 3: return "KÖKLEN";
            default: return "Bilinmeyen Yetenek";
        }
    }

    @Override
    public int get_target (int skillIndex) {
        switch (skillIndex) {
            case 1: return -1;
            case 2: return 0;
            case 3: return 0;
            default: return 0;
        }
    }

    @Override
    public String infoAbility(int skillIndex){
        return switch (skillIndex){
            case 1 -> "Eksik Canına Oranla Heallar";
            case 2 -> "Tüm Ally Unitlere ATK değerine Oranla Heallar";
            case 3 -> "Her tur kendine Heallayıp ATK kazandıracak kökler salar";
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
