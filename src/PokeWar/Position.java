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

    public static Position numericToPosition (int numeric){
        for (Position p: Position.values()){
            if (p.numeric == numeric){
                return p;
            }
        }
        return Position.NONE;
    }
}
