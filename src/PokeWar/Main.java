package PokeWar;


public class Main {
    static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.TeamBuilder();
        gameEngine.TeamPlacer(1); // German Side Place First
        gameEngine.TeamPlacer(2); // Than Russian Side
        gameEngine.Initiative();
    }

}