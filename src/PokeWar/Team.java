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

    public void change_member_position (String ID,Position target_position,boolean all){
        if (all){
            for (Character member: this.members){
                member.position_change(target_position,false);
            }
            return;
        }
        for (Character member: this.members){
            if (member.ID.equals(ID)){
                member.position_change(target_position,false);
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
