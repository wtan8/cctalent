package com.cctalent.demo.service;


import com.cctalent.demo.enums.CodeWordsGameStatus;
import com.cctalent.demo.model.CodeWordsGame;
import com.cctalent.demo.model.CodeWordsLeaderBoard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public non-sealed class CodeWordsServiceImpl implements CodeWordsService {

    private final ConcurrentHashMap<String, CodeWordsGame> games = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CodeWordsLeaderBoard> leaderboard = new ConcurrentHashMap<>();

    @Value("${game.words}")
    public String[] words;

    @Override
    public CodeWordsGame startGame(String playerName) {
        String word = words[(int) (Math.random() * words.length)];
        String maskedWord = "_ ".repeat(word.length()).trim();

        CodeWordsGame game = new CodeWordsGame();
        game.setGameId(UUID.randomUUID().toString());
        game.setWord(word);
        game.setMaskedWord(maskedWord);
        game.setRemainingAttempts(6);
        game.setStatus(CodeWordsGameStatus.IN_PROGRESS);

        games.put(game.getGameId(), game);
        leaderboard.putIfAbsent(playerName, new CodeWordsLeaderBoard(playerName));
        clearLeaderboardCache();
        return game;
    }

    @Override
    public CodeWordsGame makeGuess(String gameId, String playerName, String guess) {
        CodeWordsGame game = getPrevious(gameId);

        if (guess.length() == 1) {
            handleSingleLetter(game, guess.charAt(0));
        } else {
            handleFullWord(game, guess);
        }

        updateLeaderboard(playerName, game);
        return game;
    }

    @Override
    @Cacheable("leaderboard")
    public List<CodeWordsLeaderBoard> getLeaderboard() {
        return leaderboard.values().stream()
                .sorted(Comparator.comparingInt(CodeWordsLeaderBoard::gamesWon).reversed())
                .toList();
    }

    @CacheEvict(value = "leaderboard", allEntries = true)
    private void clearLeaderboardCache() {
    }

    private CodeWordsGame getPrevious(String gameId) {
        return Optional.ofNullable(games.get(gameId))
                .orElseThrow(() -> new IllegalStateException("Game not found or already ended."));
    }

    private void handleSingleLetter(CodeWordsGame game, char letter) {
        if (game.getStatus() != CodeWordsGameStatus.IN_PROGRESS) {
            return; // Game already concluded
        }

        letter = Character.toLowerCase(letter);
        if (game.getWord().contains(Character.toString(letter))) {
            showLetter(game, letter);
            if (!game.getMaskedWord().contains("_")) {
                game.setStatus(CodeWordsGameStatus.WON);
            }
        } else {
            decreaseAttempts(game);
        }
    }

    private void handleFullWord(CodeWordsGame game, String guess) {
        if (game.getStatus() != CodeWordsGameStatus.IN_PROGRESS) {
            return; // Game already concluded
        }

        if (guess.equalsIgnoreCase(game.getWord())) {
            game.setMaskedWord(game.getWord().replace("", " ").trim());
            game.setStatus(CodeWordsGameStatus.WON);
        } else {
            decreaseAttempts(game);
        }
    }

    private void showLetter(CodeWordsGame game, char letter) {
        StringBuilder masked = new StringBuilder(game.getMaskedWord().replace(" ", ""));
        for (int i = 0; i < game.getWord().length(); i++) {
            if (game.getWord().charAt(i) == letter) {
                masked.setCharAt(i, letter);
            }
        }
        game.setMaskedWord(masked.toString().replace("", " ").trim());
    }

    private void decreaseAttempts(CodeWordsGame game) {
        game.setRemainingAttempts(game.getRemainingAttempts() - 1);
        if (game.getRemainingAttempts() <= 0) {
            game.setStatus(CodeWordsGameStatus.LOST);
        }
    }

    private void updateLeaderboard(String playerName, CodeWordsGame game) {
        CodeWordsLeaderBoard entry = leaderboard.get(playerName);
        if (game.getStatus() == CodeWordsGameStatus.WON) {
            leaderboard.put(playerName, entry.incrementWins().incrementAttempts());
        } else {
            leaderboard.put(playerName, entry.incrementAttempts());
        }
    }
}