package com.example.numbergame.game.service;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceIntegrationTest {

  @Autowired private GameService service;

  @Test
  public void completeGameFlow() {
    // Player One start game
    GameDto playerOneRegistration = service.startGame();

    assertNotNull(playerOneRegistration);
    assertNotNull(playerOneRegistration.getPlayerId());
    assertTrue(playerOneRegistration.isPlayerTurn());

    // player two start game
    GameDto playerTwoRegistration = service.startGame();

    assertNotNull(playerTwoRegistration);
    assertNotNull(playerTwoRegistration.getPlayerId());
    assertFalse(playerTwoRegistration.isPlayerTurn());

    // Player one turn -> send number 5
    Integer move = 2;
    GameDto playerOneTurn =
        service.sendNumber(
            new SendNumberRequest(
                playerOneRegistration.getPlayerId(), playerOneRegistration.getGameId(), move));

    assertNotNull(playerOneTurn);
    assertNotNull(playerOneTurn.getPlayerId());
    assertFalse(playerOneTurn.isPlayerTurn());
    assertEquals(move, playerOneTurn.getLastMove());
    assertEquals(move, playerOneTurn.getCurrentNumber());
    assertNull(playerOneTurn.getGameResult());

    // Player two turn -> send number 1
    Integer secondMove = 1;
    GameDto playerTwoMove =
        service.sendNumber(
            new SendNumberRequest(
                playerTwoRegistration.getPlayerId(),
                playerTwoRegistration.getGameId(),
                secondMove));

    assertNotNull(playerTwoMove);
    assertNotNull(playerTwoMove.getPlayerId());
    assertFalse(playerTwoMove.isPlayerTurn());
    assertEquals(secondMove, playerTwoMove.getLastMove());
    assertEquals(Integer.valueOf(1), playerTwoMove.getCurrentNumber());
    assertEquals(GameDto.GameResult.WINNER, playerTwoMove.getGameResult());

    // Player one game status check
    GameDto playerOneGameStatus =
        service.getGameStatus(
            playerOneRegistration.getGameId(), playerOneRegistration.getPlayerId());
    assertNotNull(playerOneGameStatus);
    assertNotNull(playerOneGameStatus.getPlayerId());
    assertEquals(secondMove, playerOneGameStatus.getLastMove());
    assertEquals(Integer.valueOf(1), playerOneGameStatus.getCurrentNumber());
    assertEquals(GameDto.GameResult.LOSER, playerOneGameStatus.getGameResult());
  }
}
