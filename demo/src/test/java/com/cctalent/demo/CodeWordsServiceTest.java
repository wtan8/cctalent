package com.cctalent.demo;

import com.cctalent.demo.enums.CodeWordsGameStatus;
import com.cctalent.demo.model.CodeWordsGame;
import com.cctalent.demo.service.CodeWordsService;
import com.cctalent.demo.service.CodeWordsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class CodeWordsServiceTest {

	private CodeWordsService gameService;
	@Value("${game.words}")
	private String[] words;

	@BeforeEach
	void setUp() {
		gameService = new CodeWordsServiceImpl();
		((CodeWordsServiceImpl) gameService).words = new String[]{"breakfast", "highway", "mountain"};
	}

	@Test
	void testStartGame() {
		String playerName = "Brian";
		CodeWordsGame game = gameService.startGame(playerName);

		assertEquals(null, String.valueOf(6), game.getRemainingAttempts());
		assertEquals(CodeWordsGameStatus.IN_PROGRESS.toString(), game.getStatus(), "Initial game status should be IN_PROGRESS");
	}

	@Test
	void testMakeGuessCorrectLetter() {
		String playerName = "Bob";
		CodeWordsGame game = gameService.startGame(playerName);
		game.setWord("apple"); // Set a fixed word for testing
		game.setMaskedWord("_ _ _ _ _");

		CodeWordsGame updatedGame = gameService.makeGuess(game.getGameId(), playerName, "a");
		assertEquals(null,"a _ _ _ _", updatedGame.getMaskedWord());
		assertEquals(null,CodeWordsGameStatus.IN_PROGRESS.toString(), updatedGame.getStatus());
	}

	@Test
	void testMakeGuessWrongLetter() {
		String playerName = "Bob";
		CodeWordsGame game = gameService.startGame(playerName);
		game.setWord("sky"); // Set a fixed word for testing
		game.setMaskedWord("_ _ _ _ _");

		CodeWordsGame updatedGame = gameService.makeGuess(game.getGameId(), playerName, "z");
		assertEquals(null,"_ _ _ _ _", updatedGame.getMaskedWord());
		assertEquals(null,5, updatedGame.getRemainingAttempts());
		assertEquals(null, CodeWordsGameStatus.IN_PROGRESS.toString(), updatedGame.getStatus());
	}
}