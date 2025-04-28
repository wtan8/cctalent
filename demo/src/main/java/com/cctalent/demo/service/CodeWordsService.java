package com.cctalent.demo.service;

import com.cctalent.demo.model.CodeWordsGame;
import com.cctalent.demo.model.CodeWordsLeaderBoard;

import java.util.List;


public sealed interface CodeWordsService permits CodeWordsServiceImpl {
    CodeWordsGame startGame(String playerName);
    CodeWordsGame makeGuess(String gameId, String playerName, String guess);
    List<CodeWordsLeaderBoard> getLeaderboard();
}

