# PokeWar 🛡️⚔️

PokeWar is a turn-based tactical strategy game implemented in Java. Built with strict object-oriented programming (OOP) principles, it features dynamic turn queue management, complex positional attack matrices, and distinct character role mechanics (Tank, Archer, Healer).

---

## 🚀 Core Features

- **Matrix-Based Positioning & Combat (`Position` & `ATTACK_MATRIX`):** Restrictive and conditional line-of-sight and attack rules based on unit placements (Front, Back, Taunter, Fly, Under).
- **Dynamic Initiative System (`GameEngine`):** Real-time speed-based sorting (`Turn Queue`) ensuring accurate turn priority across teams.
- **Specialized Role Mechanics (`Character` & Subclasses):**
    - **Tank:** Taunt mechanics, group defense buffs, and conversion of defense stats into attack power.
    - **Archer:** Precision sniper shots, multi-target volleys, and dynamic combo multiplier scaling.
    - **Healer:** Missing health ratio healing, group buffs, and root-based auxiliary generation.
    - **Witcher:** AOE Damage, mana regen shots, group hp and atk buffs.
- **Terminal UI (`UIManager`):** Clean ANSI/Text-based graphical templates for battle menus, team building, and status displays.

---

## 🏗️ Project Architecture

```
PokeWar/
│
├── Main.java              # Entry point initializing GameEngine and teams
├── GameEngine.java        # Core game loop, turn queues, and action dispatcher
├── Team.java              # Team management, active roster tracking, and validity checks
├── Character.java         # Abstract base class for stats, health management, and guards
├── Position.java          # Enum defining battlefield slots and the ATTACK_MATRIX
└── Roles/
    ├── Tank.java          # Tank subclass (Taunt, Def-to-Atk conversion)
    ├── Archer.java        # Archer subclass (Sniper, Multi-shot, Combo system)
    └── Healer.java        # Healer subclass (Dynamic healing, Root system)
    └── Witch.java         # Witcher subclass (Dynamic gameplay, heal-damage)
```

---

## 🛠️ Tech Stack

- **Language:** Java (JDK 17+)
- **Paradigm:** Object-Oriented Programming (OOP)
- **Data Structures:** Custom arrays, `ArrayList`, Comparator-based sorting, State Matrices.

---

## 🕹️ How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/NK-Enes/PokeWar.git
   ```
2. Open the project in your preferred Java IDE (IntelliJ IDEA, Eclipse, etc.).
3. Compile and run `Main.java`:
   ```bash
   javac PokeWar/*.java
   java PokeWar.Main
   ```

---

## ⚠️ Known Technical Debt & Roadmap (Refactoring Goals)

As part of continuous engineering improvement, the following architectural updates are planned to eliminate technical debt:
- [ ] **Encapsulation Refactor:** Restrict `public` mutable fields in `Character` and implement controlled getters/setters.
- [ ] **O(1) Optimization:** Replace $O(N)$ `ArrayList` stream filtering in `id_to_character` with a high-performance `HashMap<String, Character>` lookup index.
- [ ] **Status Effect Object:** Refactor the index-based `rechange` primitive array into a dedicated `StatusEffect` class to avoid magic numbers.
- [ ] **Team Ownership Coupling:** Decouple team identification from string parsing (`ID.charAt(0)`) by embedding direct team references inside `Character`.

---

## 👤 Author

Developed by **İbrahim Enes ÖZTÜRK** (GitHub: [NK-Enes](https://github.com/NK-Enes)) as part of Computer Engineering simulation and game engine architecture studies.