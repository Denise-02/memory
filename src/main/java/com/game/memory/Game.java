package com.game.memory;

public class Game {
    private String theme;        // variabile per il tema delle carte
    private int mode;        // modalità: vite (1) o tempo (2)
    private int level;


    public Game(String theme, int mode) {
        this.theme = theme;
        this.mode = mode;
        level = 1;
    }

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
