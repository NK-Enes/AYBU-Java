# OOP Tabanlı Taktiksel Savaş Sistemi (Java Mimari Rehberi)

Bu doküman, Nesne Yönelimli Programlama (OOP) prensiplerini pekiştirmek amacıyla tasarlanmış, **Pokemon stili / Sıra tabanlı 6v6 Taktik Savaş Simülatörü** mimarisini ve kodlama rehberini içerir.

---

## 1. Sistem Tasarımı ve Oyun Kuralları

### Takım Yapısı ve Saha Düzeni (Formation)
Her oyuncunun 6 karakterden oluşan aynı kadrosu vardır:
* **Ön Hat (2 Slot):** Doğrudan saldırıları karşılar.
* **Arka Hat (1 Slot):** Ön hatta korunan, destek veya alan hasarı veren birim.
* **Yedek Kulübesi (3 Slot):** Sahadakiler öldüğünde veya oyuncu değiştirdiğinde sahaya giren birimler.

```
       [ OYUNCU 1 ]                 [ OYUNCU 2 ]
    
    (Ön 1)      (Ön 2)           (Ön 1)      (Ön 2)
       [ ]        [ ]               [ ]        [ ]
            (Arka)                       (Arka)
             [ ]                          [ ]
```

### Hız (Turn Speed) Mantığı
Tüm sahadaki aktif birimler (Her takımdan 3'er, toplam 6 birim) `Speed` statlarına göre sıralanır. Her turda en hızlı birim ilk hamleyi yapar.

---

## 2. Kullanılacak Temel OOP Prensipleri

1. **Abstraction (Soyutlama):** Tüm birimlerin ortak nitelikleri ve metotları `Hero` isimli `abstract class` içinde tanımlanır.
2. **Inheritance (Kalıtım):** `Warrior`, `Tank`, `Healer`, `Mage`, `Assassin`, `Ranger` sınıfları `Hero` sınıfından türetilir.
3. **Polymorphism (Çok Biçimlilik):** Her kahramanın `useSkill()` ve `basicAttack()` metotları aynı isme sahip olsa da kendi sınıfına özel davranışlar sergiler.
4. **Encapsulation (Kapsülleme):** Can (`hp`), Saldırı (`atk`), Hız (`speed`) gibi veriler `private`/`protected` tutulur; değişiklikler kontrol altındaki `getter/setter` veya metotlarla yapılır.

---

## 3. Sınıf Mimarisi ve Kod Örnekleri

### Adım 1: Soyut Temel Sınıf (`Character.java`)

```java
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

    protected String name,ID;
    public String explanation;
    protected double[] rechange,rechange_posibilities;

    protected double MAX_HP,MAX_DEF,MAX_ATK,SPEED;
    protected double current_hp,current_def,current_atk;
    protected Position position = Position.NONE;
    public Position current_position;
    protected double hit_change,dodge_change,guard_change;

    protected double damage_dealt,damage_sustained;
    protected int dodge_count,guard_count;

    public Character(String name,String ID,double[] MAXs,double[] currents,Position position,double[] changes){
        this.name = name;
        this.ID = ID;
        this.position = position;
        this.rechange = new double[] {0, 0, 0, 0}; //Position, hp, def, atk

        this.hit_change = changes[0];
        this.dodge_change = changes[1];
        this.guard_change = changes[2];
        this.rechange_posibilities = new double[] {0, 0, 0}; // hit,dodge,guard

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
                [%s]\t-HP: %d\t|ATK: %d\t|DEF: %d|FOCUS: %s
                """.formatted(
                this.name,this.current_hp,this.current_atk,this.current_def,this.explanation
        );
    }

    protected void health_change (double amount){
        this.current_hp = Math.clamp(this.current_hp + amount,0, MAX_HP);
        if (amount < 0){
            this.damage_sustained -= amount; // We calculate total damage take this turn
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

    public void position_change(Position position){
        if (this.position != position){
            this.rechange_change(0,this.position.numeric);
            this.position = position;
        }
    }

    public void rechange_change (int index,double value){
        this.rechange[index] += value;
    }

    public void setRechange_posibilities_change (int index, double value){
        this.rechange_posibilities[index] += value;
    }

    public boolean is_alive (){
        return this.current_hp > 0;
    }

    public double[] attack (){
        double hit_happen = random.nextDouble();
        if (hit_happen >= (1-hit_change)){
            return new double[] {1,this.position.numeric,current_atk}; // Successful
        } else {
            return new double[] {0,this.position.numeric,current_atk}; // Failed
        }
    }

    public void defend () {
        this.setRechange_posibilities_change(2,0.5);
        this.guard_change += 0.5;
    }

    public abstract void useAbility(int skillIndex, Character target, Character[] allies, Character[] enemies);

    public abstract String infoAbility (int skillIndex);

    public void guard (double expected_damage,boolean defend) { //todo Add Damage Typei
        if (!defend){ // If Attack CAN'T guard (Real Damage / True Hit)
            System.out.printf("%s [%s] Guard  %c75 of This Attack\n",this.name,this.ID,'%');
            this.health_change(expected_damage);
            this.dodge_count++;
            this.guard_count++;
            return;
        }
        double dodge_happen = random.nextDouble();
        if (dodge_happen >= (1-this.dodge_change)){
            // Total Dodge
            System.out.printf("%s [%s] Dodged This Attack\n",this.name,this.ID);
            this.dodge_count ++;
            return;
        }
        this.dodge_count = 0;
        double guard_happen = random.nextDouble();
        if (guard_happen >= (1-this.guard_change)){
            // %75 Absorb
            System.out.printf("%s [%s] Guard  %c75 of This Attack\n",this.name,this.ID,'%');
            this.health_change(-0.25 * expected_damage);
            this.guard_count ++;
        } else if (guard_happen >= (1-this.guard_change/2)) {
            // %50 Absorb
            System.out.printf("%s [%s] Guard  %c50 of This Attack\n",this.name,this.ID,'%');
            this.health_change(-0.5 * expected_damage);
            this.guard_count ++;
        } else {
            // %0 Absorb
            System.out.printf("%s [%s] Can't Guard of This Attack\n",this.name,this.ID);
            this.health_change(-1 * expected_damage);
            this.guard_count = 0; // Count down to zero
        }
    }

}

```

---

### Adım 2: Alt Sınıfların (Rollerin) Oluşturulması

#### 1. Tank (`Tank.java`)
```java
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
                name,ID,
                new double[] {200.0,60.0,25.0,15.0},
                new double[] {200.0,60.0,25.0},
                Position.FRONT,
                new double[] {0.5,0.05,0.6}
        );
        this.current_position = Position.NONE;
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

```

#### 2. Ranger (`Archer.java`)
```java
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
                name,ID,
                new double[] {120.00,30.0,45.0,25.0},
                new double[] {120.0,30.0,45.0},
                Position.BACK,
                new double[] {0.6,0.1,0.4}
        );
        this.current_position = Position.NONE;
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

```

---

### Adım 3: Dizilim ve Takım Yönetimi (`Team.java`)

```java
package PokeWar;

/*
Ne işe yarayacak? 6 kişilik kadroyu (1 şifacı / 1 tank / 1 warrior / 1 assasin / 1 mage / 1 ranger) bir arada tutacak ve kurallarına göre konumlandıracak.

Nasıl çalışmalı? İstediğiniz gibi aynı anda 2 ön, 1 arka (veya toplam dizilimi) slotlarını yönetmeli. Takımdaki üyelerin durumunu izlemeli, ölenleri ayıklamalı
ve takımda hala savaşabilecek kimse kalıp kalmadığını kontrol etmelidir.
*/

public class Team {
    private String name;
    private Character[] members;

    public Team (String name,Character[] members){
        this.name = name;
        this.members = members;
    }

    public void change_member_position (String ID,Position target_position){
        for (Character member: this.members){
            if (member.ID.equals(ID)){
                member.position_change(target_position);
                return;
            }
        }
    }

    public boolean is_team_valid (){
        for (Character member : this.members){
            if (member.is_alive()){
                return true;
            }
        }
        return false;
    }

    public String get_team_name (){
        return this.name;
    }

    public Character[] get_team_members (){
        return this.members;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- ").append(this.name).append(" Durumu ---\n");

        for (Character c : this.members) {
            String durum = c.current_hp > 0 ? "Can: " + c.current_hp : "ÖLÜ";
            sb.append("- ").append(c.name).append(" (").append(durum).append(")\n");
        }

        return sb.toString();
    }
}

```

---

### Adım 4: Savaş Motoru ve Hız Mantığı (`GameEngine.java`)

```java
package PokeWar;

/*
Ne işe yarayacak? İki takımın birbirine karşı sırayla nasıl savaşacağını yöneten ana mekanizma olacak.

Nasıl çalışmalı? Her turun başında her iki takımın tüm canlı karakterlerini toplamalı, bu karakterleri hızlarına (speed) göre büyükten küçüğe sıralayarak bir
sıra kuyruğu (Turn Queue) oluşturmalıdır. Hızı yüksek olan karakterin hamleyi önce yapmasını sağlamalı ve döngüyü taraflardan biri yenilene kadar devam
ettirmelidir.
*/


public class GameEngine {

}


```


```java
package PokeWar;

import PokeWar.Roles.Archer;
import PokeWar.Roles.Tank;

public class Main {

}
```

---

### 4. Enums

#### Position.java (Enum)

```java
package PokeWar;

public enum Position {
    NONE("NONE",100,0),
    FRONT("FRONT",2,1),
    BACK("BACK",1,2),
    TAUNTER("TAUNTER",3,3),
    FLY("FLY",0,4),
    UNDER("UNDER", 0,5);

    public final int capacity;
    public final int numeric;
    public final String str;

    // Saldırı Matrisi: [Saldıranın_Pozisyonu][Hedefin_Pozisyonu]
    // 0: Saldıramaz | 1: Koşulsuz Saldırır | -1: Koşullu Saldırır
    private static final int[][] ATTACK_MATRIX = {
            //                NONE  FRONT  BACK  TAUNTER   FLY  UNDER
            /* NONE    */ {    0,     0,    0,     0,      0,     0  },
            /* FRONT   */ {    0,     1,   -1,     1,      0,     0  },
            /* BACK    */ {    0,     1,    1,     1,      0,     0  },
            /* TAUNTER */ {    0,     1,   -1,     1,      0,     0  },
            /* FLY     */ {    0,     1,    1,     1,      1,     0  },
            /* UNDER   */ {    0,     1,    1,     1,      0,     1  }
    };

    Position(String str,int capacity,int numeric){
        this.str = str;
        this.capacity = capacity;
        this.numeric = numeric;
    }


    public boolean canAttack(Position target_position, int[] filled_positions) {
        int rule = ATTACK_MATRIX[this.numeric][target_position.numeric];

        if (rule == 0) {
            return false; // Asla saldıramaz
        }
        else if (rule == 1) {
            return true;  // Koşulsuz saldırabilir
        }
        else if (rule == -1) {
            if (this == Position.FRONT && target_position == Position.BACK) { // FRONT to BACK

                boolean isFrontEmpty = filled_positions[Position.FRONT.numeric] == 0;
                boolean isTaunterEmpty = filled_positions[Position.TAUNTER.numeric] == 0;

                if (isFrontEmpty && isTaunterEmpty) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        return false;
    }
}

```

---

### 4. UI Manager


```java
package PokeWar;

import java.lang.reflect.Method;

public class UIManager {
public static void printBanner(String text) {
System.out.println("========================================");
System.out.println("       " + text.toUpperCase() + "       ");
System.out.println("========================================");
}


    public static void team_build(String player_name, Character[] characters) {
        // Step 1: Mevcut kadro listesini dinamik olarak oluşturuyoruz
        StringBuilder kadroBuilder = new StringBuilder();
        for (int i = 0; i < characters.length; i++) {
            kadroBuilder.append(String.format("          (%d) %s%n", i + 1, characters[i].toShortString()));
        }

        // Step 2: Karakterlerin pozisyonlarına göre slotları dolduruyoruz
        String slotA = "(Boş)";
        String slotB = "(Boş)";
        String slotC = "(Boş)";

        int frontIndex = 0;
        for (Character c : characters) {
            if (c.current_position == Position.FRONT) {
                if (frontIndex == 0) {
                    slotA = c.name;
                } else if (frontIndex == 1) {
                    slotB = c.name;
                }
                frontIndex++;
            } else if (c.current_position == Position.BACK) {
                slotC = c.name;
            }
        }

        // Step 3: Arayüz şablonunu hazırlayıp ekrana basıyoruz
        String template = """
            ================================================================================
                                  TAKTİK DİZİLİM SEÇİMİ (2 ÖN - 1 ARKA)
            ================================================================================
             Oyuncu: %s (Diğer oyuncu bakmasın!)
            --------------------------------------------------------------------------------
            
             [ MEVCUT KADRON ]
            %s--------------------------------------------------------------------------------
             [ POZİSYON SLOTLARI ]
            
               ( ÖN HAT - Maks 2 Karakter )
               [ Slot A ] : NO BUFF
               [ Slot B ] : NO BUFF
            
               ( ARKA HAT - Maks 1 Karakter )
               [ Slot C ] : NO BUFF
            
            ================================================================================
             Hangi karakteri nerede konumlandırmak istiyorsun?
             (Örn: "1 A" -> Barbar'ı Slot A'ya koyar, "CLEAR" -> Sıfırlar, "OK" -> Onayla)
            --------------------------------------------------------------------------------
            > """;

        System.out.printf(template, player_name, kadroBuilder.toString(), slotA, slotB, slotC);
    }

    public static void printActionMenu(String playerName, String teamName, Character activeChar) {
        String template = """
        ================================================================================
                                HAMLE SIRA EKRANI (TUR BİLGİSİ)
        ================================================================================
         Oyuncu: %s | Takım: %s
         Sıradaki Karakter: %s [%s]
        --------------------------------------------------------------------------------
         
         [ %s STATLAR ]
          - Can (HP)     : %.2f / %.2f
          - Pozisyon     : %s
          - Saldırı (ATK): %.2f
          - Savunma (DEF): %.2f
          - Hız (SPEED)  : %.2f
        
        --------------------------------------------------------------------------------
         [ YAPILACAK EYLEMİ SEÇİN ]
          (1) Temel Saldırı (Basic Attack)
          (2) Temel Savunma (Guard Stance)
          (3) Özel Yetenekler (Skills)
        ================================================================================
        > """;

        System.out.printf(template,
                playerName, teamName,
                activeChar.name, activeChar.ID,activeChar.name,
                activeChar.current_hp, activeChar.MAX_HP,
                activeChar.position.str,
                activeChar.current_atk,
                activeChar.current_def,
                activeChar.SPEED
        );
    }

    public static void printBasicAttackTargetMenu(Character activeChar, Character[] possibleTargets) {
        StringBuilder targetsBuilder = new StringBuilder();
        for (int i = 0; i < possibleTargets.length; i++) {
            targetsBuilder.append(String.format("          (%d) %s%n", i + 1, possibleTargets[i].toShortString()));
        }

        String template = """
        ================================================================================
                             TEMEL SALDIRI - HEDEF SEÇİMİ
        ================================================================================
         Saldıran: %s [%s] (Mevcut ATK: %.1f)
        --------------------------------------------------------------------------------
        
         [ SEÇİLEBİLİR DÜŞMAN HEDEFLERİ ]
          (0) ⬅️ GERİ DÖN (Ana Menüye)
        %s--------------------------------------------------------------------------------
         Hangi hedefe saldırmak istiyorsun?
         (Örn: "1" -> İlk sıradaki hedefi seçer, "0" -> İptal edip ana menüye döner)
        --------------------------------------------------------------------------------
        > """;

        System.out.printf(template, activeChar.name, activeChar.ID, activeChar.current_atk, targetsBuilder.toString());
    }

    public static void printSkillMenu(Character activeChar, Method[] skills) {
        StringBuilder skillListBuilder = new StringBuilder();
        for (int i = 0; i < skills.length; i++) {
            skillListBuilder.append(String.format("          (%d) %-25s : %s%n",
                    i + 1, skills[i].getName(), activeChar.infoAbility(i+1)));
        }

        String template = """
        ================================================================================
                              ÖZEL YETENEK SEÇİMİ (SKILLS)
        ================================================================================
         Karakter: %s [%s]
        --------------------------------------------------------------------------------
        
         [ KULLANILABİLİR YETENEKLER ]
          (0) ⬅️ GERİ DÖN (Ana Menüye)
        %s--------------------------------------------------------------------------------
         Kullanmak istediğin yeteneği seç:
         (Örn: "1" -> 1. Yeteneği seçer, "0" -> İptal edip ana menüye döner)
        --------------------------------------------------------------------------------
        > """;

        System.out.printf(template, activeChar.name, activeChar.ID, skillListBuilder.toString());
    }

    public static void printSkillTargetMenu(Method selectedSkill, Character[] possibleTargets) {
        StringBuilder targetsBuilder = new StringBuilder();
        for (int i = 0; i < possibleTargets.length; i++) {
            targetsBuilder.append(String.format("          (%d) %s%n", i + 1, possibleTargets[i].toShortString()));
        }

        String template = """
        ================================================================================
                             YETENEK HEDEFİ SEÇİMİ
        ================================================================================
         Seçilen Yetenek: %s
        --------------------------------------------------------------------------------
        
         [ SEÇİLEBİLİR HEDEFLER ]
          (0) ⬅️ GERİ DÖN (Yetenek Seçim Ekranına)
        %s--------------------------------------------------------------------------------
         Yeteneğin uygulanacağı hedefi seç:
         (Örn: "1" -> Target 1, "0" -> İptal edip yetenek listesine döner)
        --------------------------------------------------------------------------------
        > """;

        System.out.printf(template, selectedSkill.getName(), targetsBuilder.toString());
    }
}
```

---

## 6. Bu Mimari Size Ne Kazandıracak?

1. **Yeni Rol Eklemek Çok Kolay:** Sisteme yarın `Necromancer` veya `Bard` eklemek istediğinizde var olan kodu değiştirmeden sadece `Hero` sınıfını `extend` eden yeni bir sınıf yazmanız yeterli olacaktır (Open/Closed Principle).
2. **Çok Biçimlilik (Polymorphism) Pratiği:** `turnOrder` listesindeki her eleman aslında bir `Hero`'dur, ancak sırası geldiğinde `.useSkill()` denildiğinde her birim kendi özel yeteneğini çalıştırır (`Assassin` arkaya atlar, `Healer` can basar).
3. **Gerçek Oyun Mantığı:** Oyun içi dizilim, hız tabanlı sıra sistemi ve ön hat/arka hat koruma mantığı, profesyonel sıra tabanlı strateji oyunlarının temel mimarisidir.