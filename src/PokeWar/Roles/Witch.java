package PokeWar.Roles;

/*
Ne işe yarayacak? Her bir sınıfın kendine özgü kimliğini ve özel yetenek mekaniğini temsil edecek.

Nasıl çalışmalı? Temel Character sınıfından özellik alarak (extends), kendi rollerine uygun özel yetenekleri
(Örn: Şifacının takım arkadaşını iyileştirmesi, Tankın yüksek defansla ön safta durması, Suikastçının yüksek hızla kritik vurması)
kendi içlerinde özelleştirmeliler.
*/

import PokeWar.Character;
import PokeWar.Position;

import java.util.Random;

public class Witch extends Character {
    private int mana;
    public Witch (String name,String ID){
        mana = 10;
        super(
                name,ID,"SOLO Oyuncu",Position.NONE,
                new double[] {140.00,70.0,50.0,17.0},
                new double[] {140.00,30.0,30.0},
                Position.BACK,
                new double[] {0.6,0.3,0.7}
        );
    }

    public void tick (){
        this.mana = Math.clamp(this.mana + 2,0, 10);
    }

    public boolean Skill_1 (Character target, Character[] allies, Character[] enemies){
        // Curse
        for (Character enemy: enemies){
            enemy.guard(this.current_atk,true);
            this.attack_change(this.current_atk * 0.05); // For each hit gain %5 dmg
        }
        return true;
    }
    public boolean Skill_2 (Character target, Character[] allies, Character[] enemies){
        // Mana Regen And Damage
        Random random = new Random();
        boolean done = true;
        while (done){
            done = random.nextBoolean();
            this.mana = Math.clamp(this.mana + 1,0, 10);
            target.guard(this.current_atk * 0.2, false);
        }
        return true;
    }
    public boolean Skill_3 (Character target, Character[] allies, Character[] enemies){
        for (Character ally: allies){
            ally.buff(new double[] {ally.missing_health_percent() * this.current_atk,0,this.current_atk * 0.1});
        }
        return true;
    }

    @Override
    public boolean Type_return () {return false;};

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "LANET";
            case 2: return "TEZAHÜR";
            case 3: return "RÜNİK REZONANS";
            default: return "Bilinmeyen Yetenek";
        }
    }

    @Override
    public int get_target (int skillIndex) {
        switch (skillIndex) {
            case 1: return 0;
            case 2: return 1;
            case 3: return 0;
            default: return 0;
        }
    }

    @Override
    public String infoAbility(int skillIndex){
        return switch (skillIndex){
            case 1 -> "Tüm Rakiplere saldırır ve kendi atk değerini artırır.";
            case 2 -> "Bir rakibe X kere saldırır ve X mana yeniler.";
            case 3 -> "Tüm takima Heal ve Atk boost uygular.";
            default -> "Geçersiz Yetenek";
        };
    }

    @Override
    public boolean useAbility(int skillIndex, Character target, Character[] allies, Character[] enemies) {
        if (this.mana < 3) { return false;}
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
