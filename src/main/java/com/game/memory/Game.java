package com.game.memory;

public class Game {
    private int theme;        // variabile per il tema delle carte
    private int mode;        // modalità: vite (1) o tempo (2)
    private int level;

    public Game(int theme, int mode) {
        this.theme = theme;
        this.mode = mode;
        level = 0;
    }

    public Game(int theme, int mode, int level) {
        this.theme = theme;
        this.mode = mode;
        this.level = level;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getTheme() {
        return theme;
    }

    public int getMode() {
        return mode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
