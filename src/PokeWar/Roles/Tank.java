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
    Position current_position;
    public Tank (String name,String ID){
        super(
                name,ID,"GRUP DEFANS",Position.NONE,
                new double[] {200.0,60.0,25.0,15.0},
                new double[] {200.0,60.0,25.0},
                Position.FRONT,
                new double[] {0.5,0.05,0.6}
        );
    }

    public void Skill_1 (Character target, Character[] allies, Character[] enemies){
        // Taunt Skill
        this.rechange[0] = this.current_position.numeric;
        this.current_position = Position.TAUNTER;
    }
    public void Skill_2 (Character target, Character[] allies, Character[] enemies){
        // Group Buff
        for (Character ally : allies) {
            ally.rechange_change(2,this.current_def * 0.2);
            ally.buff(new double[]{0, this.current_def * 0.2, 0}); // %20 defence buff
        }
    }
    public void Skill_3 (Character target, Character[] allies, Character[] enemies){
        // Defence to Attack buff
        this.current_atk += this.current_def;
        this.rechange_change(3,this.current_def);
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
    public void useAbility(int skillIndex, Character target, Character[] allies, Character[] enemies) {
        switch (skillIndex) {
            case 1 -> Skill_1(target,allies,enemies);
            case 2 -> Skill_2(target,allies,enemies);
            case 3 -> Skill_3(target,allies,enemies);
            default -> System.out.println("Geçersiz yetenek!");
        }
    }
}
