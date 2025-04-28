package com.cctalent.demo.model;


import com.cctalent.demo.enums.CodeWordsGameStatus;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CodeWordsGame {
    private String gameId;
    private String word;
    private String maskedWord;
    private int remainingAttempts;
    private CodeWordsGameStatus status; // Sealed interface
    private Set<Character> guessedLetters = new HashSet<>();
}

