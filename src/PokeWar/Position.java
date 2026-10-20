package PokeWar;

public enum Position {
    FRONT(2),
    BACK(1),
    FLY(0),
    UNDER(0);

    final int capacity;

    Position(int capacity){
        this.capacity = capacity;
    }
}
