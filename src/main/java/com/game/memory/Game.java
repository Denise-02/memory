package com.game.memory;

public class Game {

    /**
     * The theme of the game. The player can choose between: PacMan, Pokemon, SuperMario and Spongebob
     */
    private String theme;

    /**
     * The game mode. The player can choose between life (1) or time (2)
     */
    private int mode;

    /**
     * The level of the game. For each level the number of cards increases by two couples.
     */
    private int level;


    /**
     * The constructor of a Game
     * @param theme The theme of this game. The player can choose between: PacMan, Pokemon, SuperMario and Spongebob
     * @param mode The game mode. The player can choose between life (1) or time (2)
     * @param level The level of this game.
     */
    public Game(String theme, int mode, int level) {
        this.theme = theme;
        this.mode = mode;
        this.level = level;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTheme() {
        return theme;
    }

    public int getMode() {
        return mode;
    }

    public int getLevel() {
        return level;
    }
}
