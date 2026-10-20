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

### Adım 1: Soyut Temel Sınıf (`Hero.java`)

```java
package game.model;

public abstract class Hero implements Comparable<Hero> {
    protected String name;
    protected String role;
    protected int maxHp;
    protected int currentHp;
    protected int attackPower;
    protected int defense;
    protected int speed;
    protected boolean isAlive;
    protected int teamId; // 1: Oyuncu 1, 2: Oyuncu 2

    public Hero(String name, String role, int hp, int attackPower, int defense, int speed, int teamId) {
        this.name = name;
        this.role = role;
        this.maxHp = hp;
        this.currentHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.speed = speed;
        this.teamId = teamId;
        this.isAlive = true;
    }

    // Polimorfik Metotlar
    public abstract void basicAttack(Hero target);
    public abstract void useSkill(Hero target, Team allyTeam, Team enemyTeam);

    public void takeDamage(int damage) {
        int netDamage = Math.max(1, damage - this.defense);
        this.currentHp -= netDamage;
        System.out.printf("[%s - P%d] %d hasar aldı! Kalan HP: %d/%d\n", name, teamId, netDamage, Math.max(0, currentHp), maxHp);
        if (this.currentHp <= 0) {
            this.currentHp = 0;
            this.isAlive = false;
            System.out.printf("☠️ [%s - P%d] düştü!\n", name, teamId);
        }
    }

    public void heal(int amount) {
        if (!isAlive) return;
        this.currentHp = Math.min(maxHp, this.currentHp + amount);
        System.out.printf("✨ [%s - P%d] %d can kazandı! HP: %d/%d\n", name, teamId, amount, currentHp, maxHp);
    }

    // Hız sıralaması için Comparable arayüzü kullanımı (Büyük hız öne geçer)
    @Override
    public int compareTo(Hero other) {
        return Integer.compare(other.speed, this.speed);
    }

    // Getter & Setter (Encapsulation)
    public boolean isAlive() { return isAlive; }
    public int getSpeed() { return speed; }
    public String getName() { return name; }
    public int getTeamId() { return teamId; }
    public String getRole() { return role; }
}
```

---

### Adım 2: Alt Sınıfların (Rollerin) Oluşturulması

#### 1. Tank (`Tank.java`)
```java
package game.model.roles;

import game.model.Hero;
import game.model.Team;

public class Tank extends Hero {
    public Tank(String name, int teamId) {
        super(name, "Tank", 200, 15, 30, 10, teamId);
    }

    @Override
    public void basicAttack(Hero target) {
        System.out.printf("🛡️ Tank [%s], [%s] birimine kalkanıyla vurdu!\n", name, target.getName());
        target.takeDamage(this.attackPower);
    }

    @Override
    public void useSkill(Hero target, Team allyTeam, Team enemyTeam) {
        System.out.printf("🛡️ Tank [%s] 'Provoke' kullandı! Kendi savunmasını artırıyor.\n", name);
        this.defense += 15;
    }
}
```

#### 2. Şifacı (`Healer.java`)
```java
package game.model.roles;

import game.model.Hero;
import game.model.Team;

public class Healer extends Hero {
    public Healer(String name, int teamId) {
        super(name, "Healer", 90, 10, 8, 18, teamId);
    }

    @Override
    public void basicAttack(Hero target) {
        System.out.printf("🔮 Healer [%s], [%s] birimine ışık fırlattı!\n", name, target.getName());
        target.takeDamage(this.attackPower);
    }

    @Override
    public void useSkill(Hero target, Team allyTeam, Team enemyTeam) {
        Hero lowestHpAlly = allyTeam.getLowestHpActiveHero();
        if (lowestHpAlly != null) {
            System.out.printf("💚 Healer [%s], [%s] müttefiğini iyileştiriyor!\n", name, lowestHpAlly.getName());
            lowestHpAlly.heal(35);
        }
    }
}
```

#### 3. Suikastçı (`Assassin.java`)
```java
package game.model.roles;

import game.model.Hero;
import game.model.Team;

public class Assassin extends Hero {
    public Assassin(String name, int teamId) {
        super(name, "Assassin", 80, 45, 5, 35, teamId); // Çok yüksek hız ve saldırı
    }

    @Override
    public void basicAttack(Hero target) {
        System.out.printf("🗡️ Assassin [%s], [%s] birimine arkadan saldırdı!\n", name, target.getName());
        target.takeDamage(this.attackPower);
    }

    @Override
    public void useSkill(Hero target, Team allyTeam, Team enemyTeam) {
        // Doğrudan arka hattaki (varsa) veya en düşük canı olan düşmana odaklanır
        Hero backline = enemyTeam.getBackline();
        Hero targetHero = (backline != null && backline.isAlive()) ? backline : target;
        
        System.out.printf("⚡ Assassin [%s], 'Shadow Strike' ile [%s] birimine kritik vurdu!\n", name, targetHero.getName());
        targetHero.takeDamage((int)(this.attackPower * 1.8));
    }
}
```

---

### Adım 3: Dizilim ve Takım Yönetimi (`Team.java`)

```java
package game.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private int teamId;
    private Hero front1;
    private Hero front2;
    private Hero back;
    private List<Hero> bench;

    public Team(int teamId) {
        this.teamId = teamId;
        this.bench = new ArrayList<>();
    }

    public void setupFormation(Hero f1, Hero f2, Hero b, List<Hero> reserves) {
        this.front1 = f1;
        this.front2 = f2;
        this.back = b;
        this.bench = reserves;
    }

    // Sahadaki aktif kahramanların listesi (Hız sıralamasına girmek için)
    public List<Hero> getActiveHeroes() {
        List<Hero> active = new ArrayList<>();
        if (front1 != null && front1.isAlive()) active.add(front1);
        if (front2 != null && front2.isAlive()) active.add(front2);
        if (back != null && back.isAlive()) active.add(back);
        return active;
    }

    public Hero getLowestHpActiveHero() {
        Hero lowest = null;
        for (Hero h : getActiveHeroes()) {
            if (lowest == null || h.currentHp < lowest.currentHp) {
                lowest = h;
            }
        }
        return lowest;
    }

    public boolean isTeamDefeated() {
        return getActiveHeroes().isEmpty() && bench.stream().noneMatch(Hero::isAlive);
    }

    public Hero getBackline() { return back; }
}
```

---

### Adım 4: Savaş Motoru ve Hız Mantığı (`BattleEngine.java`)

```java
package game.engine;

import game.model.Hero;
import game.model.Team;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleEngine {
    private Team team1;
    private Team team2;

    public BattleEngine(Team t1, Team t2) {
        this.team1 = t1;
        this.team2 = t2;
    }

    public void startBattle() {
        int round = 1;
        
        while (!team1.isTeamDefeated() && !team2.isTeamDefeated()) {
            System.out.println("\n================ ROUND " + round + " ================");
            
            // 1. Sahadaki tüm canlı birimleri topla
            List<Hero> turnOrder = new ArrayList<>();
            turnOrder.addAll(team1.getActiveHeroes());
            turnOrder.addAll(team2.getActiveHeroes());

            // 2. HIZ (Speed) Statına göre sırala (Polymorphic & Comparable)
            Collections.sort(turnOrder);

            // 3. Sırayla hamle yaptır
            for (Hero hero : turnOrder) {
                if (!hero.isAlive()) continue; // Tur içinde öldüyse pas geç

                Team enemyTeam = (hero.getTeamId() == 1) ? team2 : team1;
                Team allyTeam = (hero.getTeamId() == 1) ? team1 : team2;

                if (enemyTeam.getActiveHeroes().isEmpty()) break;

                // Örnek basit AI hamlesi: Karşı ön hattan rastgele birine veya skille saldırt
                Hero target = enemyTeam.getActiveHeroes().get(0);
                
                // Polimorfizm Burada Çalışır:
                if (Math.random() > 0.4) {
                    hero.useSkill(target, allyTeam, enemyTeam);
                } else {
                    hero.basicAttack(target);
                }
            }

            round++;
            if (round > 20) break; // Sonsuz döngü koruması
        }

        System.out.println("\n🏆 SAVAŞ BİTTİ! KAZANAN: " + (team1.isTeamDefeated() ? "Takım 2" : "Takım 1"));
    }
}
```

---

## 4. Bu Mimari Size Ne Kazandıracak?

1. **Yeni Rol Eklemek Çok Kolay:** Sisteme yarın `Necromancer` veya `Bard` eklemek istediğinizde var olan kodu değiştirmeden sadece `Hero` sınıfını `extend` eden yeni bir sınıf yazmanız yeterli olacaktır (Open/Closed Principle).
2. **Çok Biçimlilik (Polymorphism) Pratiği:** `turnOrder` listesindeki her eleman aslında bir `Hero`'dur, ancak sırası geldiğinde `.useSkill()` denildiğinde her birim kendi özel yeteneğini çalıştırır (`Assassin` arkaya atlar, `Healer` can basar).
3. **Gerçek Oyun Mantığı:** Oyun içi dizilim, hız tabanlı sıra sistemi ve ön hat/arka hat koruma mantığı, profesyonel sıra tabanlı strateji oyunlarının temel mimarisidir.