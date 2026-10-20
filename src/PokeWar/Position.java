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

    Position(String str,int capacity,int numeric){
        this.str = str;
        this.capacity = capacity;
        this.numeric = numeric;
    }

    boolean is_valid (Position target_position,int[] filled_positions){
        boolean valid;
        if (this == Position.TAUNTER || this == Position.FRONT){
            if (target_position == Position.TAUNTER || target_position == Position.FRONT){
                valid = true;
            } else if (target_position == Position.BACK && filled_positions[Position.FRONT.numeric] == 0 && filled_positions[Position.TAUNTER.numeric] == 0) {
                valid = true;
            } else {
                valid = false;
            }
        } else if (this == Position.BACK) {
            if (target_position == Position.FRONT || target_position == Position.BACK || target_position == Position.TAUNTER){
                valid = true;
            } else {
                valid = false; // For Now Those places safe
            }
        } else{
            valid = false;
        }

        return valid;
    }
}
