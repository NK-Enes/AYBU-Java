package PokeWar;

/*
Ne işe yarayacak? Oyundaki tüm karakterlerin (Tank, Şifacı, Savaşçı vb.) atası olacak. Ortak özellikleri ve kuralları burada toplayacağız.

Nasıl çalışmalı? Her karakterin sahip olması gereken temel statları (Can, Saldırı Gücü, Defans, Hız, Pozisyon) ve ortak eylemleri
(hasar alma, hayatta olup olmadığını kontrol etme) barındırmalı. Ayrıca tüm alt sınıfların kendine has bir yeteneği olmasını zorunlu kılacak genel bir
yetenek kuralı içermeli, ancak detayını alt sınıflara bırakmalı.
*/

import java.util.Random;

public abstract class Character {
    Random random = new Random();

    String name,ID;
    double MAX_HP,MAX_DEF,MAX_ATK,SPEED;
    double current_hp,current_def,current_atk;
    Position position = Position.NONE;
    double hit_change,dodge_change,guard_change;

    double damage_dealt,damage_sustained;
    int dodge_count,guard_count;

    public Character(String name,String ID,double[] MAXs,double[] currents,Position position,double[] changes){
        this.name = name;
        this.ID = ID;
        this.position = position;

        this.hit_change = changes[0];
        this.dodge_change = changes[1];
        this.guard_change = changes[2];

        this.MAX_HP = MAXs[0];
        this.MAX_DEF = MAXs[1];
        this.MAX_ATK = MAXs[2];
        this.SPEED = MAXs[3];

        this.current_hp = currents[0];
        this.current_def = currents[1];
        this.current_atk = currents[2];

        // Runtime Values
        this.damage_dealt = 0;
        this.damage_sustained = 0;
    }

    public String toString (){
        return """
        %s [%s] Character
        Current Position: %s
        (Hit: %c%.2f Dodge: %c%.2f Guard: %c%.2f)
        HP: %.2f/%.2f
        DEF: %.2f/%.2f
        ATK: %.2f/%.2f
        Stats [DEALT: %.2f\t SUSTAINED: %.2f]""".formatted(
                name, ID, position.str,
                '%', hit_change,
                '%', dodge_change,
                '%', guard_change,
                current_hp, MAX_HP,
                current_def, MAX_DEF,
                current_atk, MAX_ATK,
                damage_dealt, damage_sustained
        );
    }

    protected void health_change (double amount){
        this.current_hp += amount;
        if (amount < 0){
            this.damage_sustained -= amount; // We calculate total damage take this turn
        }
    }

    protected void defence_change (double amount){
        this.current_def += amount;
    }

    protected void attack_change (double amount){
        this.current_atk += amount;
    }

    public boolean is_alive (){
        return this.current_hp > 0;
    }

    public double[] attack (){
        double hit_happen = random.nextDouble();
        if (hit_happen >= hit_change){
            return new double[] {1,this.position.numeric,current_atk}; // Successful
        } else {
            return new double[] {0,this.position.numeric,current_atk}; // Failed
        }
    }

    public abstract void useAbility(Character target, Character[] allies, Character[] enemies);

    public void guard (double expected_damage) { //todo Add Damage Type
        double dodge_happen = random.nextDouble();
        if (dodge_happen >= dodge_change){
            // Total Dodge
            System.out.printf("%s [%s] Dodged This Attack\n",this.name,this.ID);
            this.dodge_count ++;
            return;
        }
        this.dodge_count = 0;
        double guard_happen = random.nextDouble();
        if (guard_happen >= this.guard_change){
            // %75 Absorb
            this.health_change(-0.25 * expected_damage);
            this.guard_count ++;
        } else if (guard_happen >= this.guard_change/2) {
            // %50 Absorb
            this.health_change(-0.5 * expected_damage);
            this.guard_count ++;
        } else {
            // %0 Absorb
            this.health_change(-1 * expected_damage);
            this.guard_count = 0; // Count down to zero
        }
    }

}
