package PokeWar;


public class Main {
    static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.TeamBuilder();
        gameEngine.TeamPlacer(1); // German Side Place First
        gameEngine.TeamPlacer(2); // Than Russian Side
        System.out.print(gameEngine.player_1.toString());
        System.out.print(gameEngine.player_2.toString());
    }

}