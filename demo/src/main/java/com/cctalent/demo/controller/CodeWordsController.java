package com.cctalent.demo.controller;


import com.cctalent.demo.model.CodeWordsGame;
import com.cctalent.demo.model.CodeWordsLeaderBoard;
import com.cctalent.demo.service.CodeWordsService;
import com.cctalent.demo.service.CodeWordsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/game")
public class CodeWordsController {
    @Autowired
    public CodeWordsService codeWordsService;

    @PostMapping
    public ResponseEntity<CodeWordsGame> startGame(@RequestParam String playerName) {
        CodeWordsGame game = codeWordsService.startGame(playerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @PostMapping("/{gameId}/guess")
    public ResponseEntity<CodeWordsGame> makeGuess(@PathVariable String gameId, @RequestParam String playerName, @RequestBody String guess) {
        CodeWordsGame game = codeWordsService.makeGuess(gameId, playerName, guess);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<CodeWordsLeaderBoard>> getLeaderboard() {
        List<CodeWordsLeaderBoard> leaderboard = codeWordsService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}
