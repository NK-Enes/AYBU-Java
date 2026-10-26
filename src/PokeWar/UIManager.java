package PokeWar;

import java.lang.reflect.Method;
import java.util.Scanner;

public class UIManager {
    public static void printBanner(String text) {
        System.out.println("================================================================================");
        System.out.println("                                    " + text.toUpperCase());
        System.out.println("================================================================================\n\n");
    }

    public static void load_UI (String  msg, int loop){
        System.out.print(msg);
        for (int i = 0; i < loop; i++){
            try {Thread.sleep(500);} catch (InterruptedException e) {}
            if (i % 3 == 2){
                System.out.print(".\n");
            } else {
                System.out.print(".");
            }
        }
        System.out.print("\n");
    }

    public static String get_player_name (boolean first){
        Scanner input = new Scanner(System.in);
        System.out.println("================================================================================");
        if (first){
            System.out.print("Guten Tag, Kommandant. Wie lautet Ihr Name: ");
        } else {
            System.out.print("Приветствую, командир. Как вас зовут: ");
        }
        String name = input.nextLine();
        System.out.println("================================================================================\n\n");
        return name;
    }

    public static void print_instance_list(String[] text,String[] side_list,double[] stathplist,double[] statatklist) {
        System.out.println("================================================================================");
        for (int i = 0; i < text.length; i++){
            System.out.printf("%d - (%s) %s \t[HP: %.0f | ATK: %.0f]\n",i+1,side_list[i],text[i],stathplist[i],statatklist[i]);
        }

        System.out.println("================================================================================\n\n");
    }


    public static void team_build(Team team) {
        String player_name = team.get_team_name();
        Character[] characters = team.get_team_members();
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
               [ Slot A ] : %s (NO BUFF)
               [ Slot B ] : %s (NO BUFF)
            
               ( ARKA HAT - Maks 1 Karakter )
               [ Slot C ] : %s (NO BUFF)
            
            ================================================================================
             Hangi karakteri nerede konumlandırmak istiyorsun?
             (Örn: "1 A" -> Barbar'ı Slot A'ya koyar, "CLEAR" -> Sıfırlar, "OK" -> Onayla)
            --------------------------------------------------------------------------------
            > """;

        System.out.printf(template, player_name, kadroBuilder.toString(), slotA, slotB, slotC);
    }

    public static void printActionMenu(String teamName, Character activeChar) {
        String template = """
        ================================================================================
                                HAMLE SIRA EKRANI (TUR BİLGİSİ)
        ================================================================================
         Takım: %s
         Hamle Sırası: %s [%s]
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
          (4) Initiative List (Queue)
          (0) Karakter Değişikliği
        ================================================================================
        > """;

        System.out.printf(template,
                teamName,
                activeChar.name, activeChar.ID,activeChar.name,
                activeChar.current_hp, activeChar.MAX_HP,
                activeChar.current_position.str,
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
       \s
         [ SEÇİLEBİLİR DÜŞMAN HEDEFLERİ ]
                 (0) ⬅️ GERİ DÖN (Hamle Sıra Ekranı)
                 \s
        %s--------------------------------------------------------------------------------
         Hangi hedefe saldırmak istiyorsun?
         (Örn: "1" -> İlk sıradaki hedefi seçer, "0" -> İptal edip Hamlı Sıra Ekranı döner)
        --------------------------------------------------------------------------------
        >\s""";

        System.out.printf(template, activeChar.name, activeChar.ID, activeChar.current_atk, targetsBuilder.toString());
    }

    public static void printSkillMenu(Character activeChar, int skill_lenght) {
        StringBuilder skillListBuilder = new StringBuilder();
        for (int i = 0; i < skill_lenght; i++) {
            skillListBuilder.append(String.format("  (%d) %-25s : %s%n",
                    i + 1, activeChar.getSkillName(i + 1), activeChar.infoAbility(i + 1)));
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

    public static void printSkillTargetMenu(Character character,int skill_index, Character[] possibleTargets) {
        System.out.print("Skill Index: " + skill_index + "\n");
        StringBuilder targetsBuilder = new StringBuilder();
        for (int i = 0; i < possibleTargets.length; i++) {
            targetsBuilder.append(String.format("  (%d) %s%n", i + 1, possibleTargets[i].toShortString()));
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

        System.out.printf(template, character.infoAbility(skill_index), targetsBuilder.toString());
    }
}
