package com.example.numbergame.game.controller;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;
import com.example.numbergame.game.error.GameException;
import com.example.numbergame.game.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/game")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("status")
  GameDto getGameStatus(
      @RequestParam(name = "gameId") String gameId,
      @RequestParam(name = "playerId") String playerId) {
    return gameService.getGameStatus(gameId, playerId);
  }

  @PostMapping("start-game")
  GameDto startGame() {
    return gameService.startGame();
  }

  @PutMapping("send-number")
  GameDto sendNumber(@RequestBody SendNumberRequest sendNumberRequest) {
    validateRequest(sendNumberRequest);
    return gameService.sendNumber(sendNumberRequest);
  }

  private void validateRequest(SendNumberRequest sendNumberRequest) {
    if (sendNumberRequest.getNumber() == null) {
      throw new GameException("Please provide a number", HttpStatus.BAD_REQUEST);
    }
    if (sendNumberRequest.getGameId() == null || sendNumberRequest.getGameId().isEmpty()) {
      throw new GameException("Please provide game id", HttpStatus.BAD_REQUEST);
    }
    if (sendNumberRequest.getPlayerId() == null || sendNumberRequest.getPlayerId().isEmpty()) {
      throw new GameException("Please provide player id", HttpStatus.BAD_REQUEST);
    }
  }
}
