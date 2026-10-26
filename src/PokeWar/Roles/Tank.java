package PokeWar.Roles;

/*
Ne işe yarayacak? Her bir sınıfın kendine özgü kimliğini ve özel yetenek mekaniğini temsil edecek.

Nasıl çalışmalı? Temel Character sınıfından özellik alarak (extends), kendi rollerine uygun özel yetenekleri
(Örn: Şifacının takım arkadaşını iyileştirmesi, Tankın yüksek defansla ön safta durması, Suikastçının yüksek hızla kritik vurması)
kendi içlerinde özelleştirmeliler.
*/

import PokeWar.Character;
import PokeWar.Position;

public class Tank extends Character {
    public Tank (String name,String ID){
        super(
                name,ID,"GRUP DEFANS",Position.NONE,
                new double[] {200.0,60.0,25.0,15.0},
                new double[] {200.0,60.0,25.0},
                Position.FRONT,
                new double[] {0.5,0.05,0.6}
        );
    }

    public boolean Skill_1 (Character target, Character[] allies, Character[] enemies){
        // Taunt Skill
        System.out.printf("%s's Position Change to TAUNT\n",this.name);
        this.rechange[0] = this.current_position.numeric;
        this.current_position = Position.TAUNTER;
        return true;
    }
    public boolean Skill_2 (Character target, Character[] allies, Character[] enemies){
        // Group Buff
        for (Character ally : allies) {
            System.out.printf("%s's Defence Stat +%c20\n",ally.name,'%');
            ally.rechange_change(2,this.current_def * 0.2);
            ally.buff(new double[]{0, this.current_def * 0.2, 0}); // %20 defence buff
        }
        return true;
    }
    public boolean Skill_3 (Character target, Character[] allies, Character[] enemies){
        // Defence to Attack buff
        System.out.printf("%s's Attack Value Increased %.2f\n",this.name,this.current_def);
        this.current_atk += this.current_def;
        return true;
    }

    @Override
    public boolean Type_return () {return false;};

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "TAUNT";
            case 2: return "KALKANLAR";
            case 3: return "KÜKRE";
            default: return "Bilinmeyen Yetenek";
        }
    }

    @Override
    public int get_target (int skillIndex) {
        switch (skillIndex) {
            case 1: return 0;
            case 2: return 0;
            case 3: return 0;
            default: return 0;
        }
    }

    @Override
    public String infoAbility(int skillIndex){
        return switch (skillIndex){
            case 1 -> "TAUNT pozisyonuna geçer.";
            case 2 -> "Tüm grubun defans değerini arttırır.";
            case 3 -> "Defans değeri kadar saldırı gücü kazanır.";
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
