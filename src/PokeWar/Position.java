package PokeWar;

public enum Position {
    NONE("NONE",100,0),
    FRONT("FRONT",2,1),
    BACK("BACK",1,2),
    TAUNTER("TAUNTER",3,3),
    FLY("FLY",0,4),
    UNDER("UNDER", 0,5);

    final int capacity;
    final int numeric;
    final String str;

    Position(String str,int capacity,int numeric){
        this.str = str;
        this.capacity = capacity;
        this.numeric = numeric;
    }

    boolean is_valid (Position target_position,int[] filled_positions){
        boolean valid;
        if (this == Position.FRONT){
            if (target_position == Position.FRONT){
                valid = true;
            } else if (target_position == Position.BACK) {
                valid = filled_positions[Position.FRONT.numeric] == 0; // No Front Enemy
            } else {
                valid = false; // For Now Those places safe
            }

        } else if (this == Position.BACK) {
            if (target_position == Position.FRONT || target_position == Position.BACK){
                valid = true;
            } else {
                valid = false; // For Now Those places safe
            }

        } else { // Add Other Positions
            valid = false;
        }

        return valid;
    }
}
