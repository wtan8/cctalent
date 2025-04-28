package com.cctalent.demo.model;



public record CodeWordsLeaderBoard(String playerName, int gamesWon, int totalAttempts) {

    public CodeWordsLeaderBoard(String playerName) {
        this(playerName, 0, 0);
    }

    public CodeWordsLeaderBoard incrementWins() {
        return new CodeWordsLeaderBoard(playerName, gamesWon + 1, totalAttempts);
    }

    public CodeWordsLeaderBoard incrementAttempts() {
        return new CodeWordsLeaderBoard(playerName, gamesWon, totalAttempts + 1);
    }
}
