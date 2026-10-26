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

    public String name,ID;
    public String explanation;
    protected double[] rechange,rechange_posibilities;

    protected double MAX_HP,MAX_DEF,MAX_ATK,SPEED;
    protected double current_hp,current_def,current_atk;
    public Position current_position;
    protected double hit_change,dodge_change,guard_change;

    protected double damage_dealt,damage_sustained;
    protected int dodge_count,guard_count;

    public Character(String name,String ID,String explanation,Position current_position,double[] MAXs,double[] currents,Position position,double[] changes){
        Random random = new Random();
        this.name = name;
        this.ID = ID;
        this.explanation = explanation;
        this.current_position = current_position;
        this.rechange = new double[] {-1, 0, 0, 0}; //Position, hp, def, atk

        this.hit_change = changes[0];
        this.dodge_change = changes[1];
        this.guard_change = changes[2];
        this.rechange_posibilities = new double[] {0, 0, 0}; // hit,dodge,guard

        this.MAX_HP = MAXs[0];
        this.MAX_DEF = MAXs[1];
        this.MAX_ATK = MAXs[2];
        this.SPEED = MAXs[3] + random.nextDouble(0.0,0.1); // Add some randomness (between same units)

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
        Stats [DEALT: %.2f\t SUSTAINED: %.2f]\n""".formatted(
                name, ID, current_position.str,
                '%', hit_change*100,
                '%', dodge_change*100,
                '%', guard_change*100,
                current_hp, MAX_HP,
                current_def, MAX_DEF,
                current_atk, MAX_ATK,
                damage_dealt, damage_sustained
        );
    }

    public String toShortString(){
        return """
                [%s]\t-HP: %.0f\t|ATK: %.0f\t|DEF: %.0f|FOCUS: %s
                """.formatted(
            this.name,this.current_hp,this.current_atk,this.current_def,this.explanation
        );
    }

    protected void health_change (double amount){
        this.current_hp = Math.clamp(this.current_hp + amount,0, MAX_HP);
        if (amount < 0){
            this.damage_sustained += amount; // We calculate total damage take this turn
        }
    }

    protected void defence_change (double amount){
        this.current_def = Math.clamp(this.current_def + amount, 0, MAX_DEF);
    }

    protected void attack_change (double amount){
        this.current_atk = Math.clamp(this.current_atk + amount, 0, MAX_ATK);
    }

    public void buff (double[] changes){
        this.health_change(changes[0]);
        this.defence_change(changes[1]);
        this.attack_change(changes[2]);
    }

    public void position_change(Position position,boolean add_to_rechange){
        if (this.current_position != position){
            if (add_to_rechange){
                this.rechange_change(0,this.current_position.numeric);
            }
            this.current_position = position;
        }
    }

    public boolean active (){
        return current_position != Position.NONE;
    }

    public void rechange_change (int index,double value){
        this.rechange[index] += value;
    }

    public void setRechange_posibilities_change (int index, double value){
        this.rechange_posibilities[index] += value;
    }

    public void Active_relists (){
        if (this.rechange[0] != -1){
            this.position_change(Position.numericToPosition((int)(this.rechange[0])),false);
        }
        this.current_hp -= this.rechange[1];
        this.current_def -= this.rechange[2];
        this.current_atk -= this.rechange[3];
        this.rechange = new double[]{-1, 0, 0, 0};

        this.hit_change -= this.rechange_posibilities[0];
        this.dodge_change -= this.rechange_posibilities[1];
        this.guard_change -= this.rechange_posibilities[2];
        this.rechange_posibilities = new double[] {0, 0, 0};
    }

    public boolean is_alive (){
        if (this.current_hp <= 0){
            DIE();
            return false;
        } else {
            return true;
        }
    }

    public boolean on_Area (){
        return !this.current_position.isIn(new Position[]{Position.NONE, Position.DEAD});
    }

    public double[] attack (){
        double hit_happen = random.nextDouble();
        if (hit_happen >= (1-hit_change)){
            return new double[] {1,current_atk}; // Successful
        } else {
            return new double[] {0,0}; // Failed
        }
    }

    public double defend (boolean dexterity) {
        double increase = 0.3;
        if (dexterity){
            if (this.dodge_change > increase){
                increase = 1 - this.dodge_change;
            }
            this.setRechange_posibilities_change(1,increase);
            this.dodge_change += increase;
            return this.dodge_change;
        } else {
            if (this.guard_change > increase){
                increase = 1 - this.guard_change;
            }
            this.setRechange_posibilities_change(2,increase);
            this.guard_change += increase;
            return this.guard_change;
        }
    }

    public abstract boolean useAbility(int skillIndex, Character target, Character[] allies, Character[] enemies);

    public abstract int get_target (int skillIndex);

    public abstract String getSkillName (int skillIndex);

    public abstract String infoAbility (int skillIndex);

    public abstract boolean Type_return ();

    private void DIE (){
        if (this.current_hp <= 0){
            this.current_position = Position.DEAD; // Move to Out of Battle
        }
    }

    public void guard (double expected_damage,boolean defend) { //todo Add Damage Type
        if (!defend){ // If Attack CAN'T defend (Real Damage / True Hit)
            System.out.printf("%s [%s] Guard  %c0 of This Attack (%.1f Damage Taken)\n",this.name,this.ID,'%',expected_damage);
            this.health_change(-1 * expected_damage);
            this.dodge_count = 0;
            this.guard_count = 0;
            DIE();
            return;
        }
        double dodge_happen = random.nextDouble();
        if (dodge_happen >= (1-this.dodge_change)){
            // Total Dodge
            System.out.printf("%s [%s] Dodged This Attack (0 Damage Taken)\n",this.name,this.ID);
            this.dodge_count ++;
            DIE();
            return;
        }
        this.dodge_count = 0;
        double guard_happen = random.nextDouble();
        if (guard_happen >= (1-this.guard_change)){
            // %75 Absorb
            System.out.printf("%s [%s] Guard  %c75 of This Attack (%.1f Damage Taken)\n",this.name,this.ID,'%',expected_damage*0.25);
            this.health_change(-0.25 * expected_damage);
            this.guard_count ++;
        } else if (guard_happen >= (1-this.guard_change/2)) {
            // %50 Absorb
            System.out.printf("%s [%s] Guard  %c50 of This Attack (%.1f Damage Taken)\n",this.name,this.ID,'%',expected_damage*0.5);
            this.health_change(-0.5 * expected_damage);
            this.guard_count ++;
        } else {
            // %0 Absorb
            System.out.printf("%s [%s] Can't Guard of This Attack (%.1f Damage Taken)\n",this.name,this.ID,expected_damage);
            this.health_change(-1 * expected_damage);
            this.guard_count = 0; // Count down to zero
        }
        DIE();
    }

}
