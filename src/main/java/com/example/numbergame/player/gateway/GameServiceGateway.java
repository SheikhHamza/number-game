package com.example.numbergame.player.gateway;

import com.example.numbergame.common.dto.GameDto;
import com.example.numbergame.common.dto.SendNumberRequest;
import com.example.numbergame.common.error.GameException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GameServiceGateway {

  public static final String ERROR_MESSAGE = "Error communicating game service";
  private final RestTemplate restTemplate;
  @Value("${game.service.base.url:http://localhost:8080/v1/api/game}")
  private String gameServiceBaseUrl;
  @Value("${game.service.status.path:/status}")
  private String gameServiceStatusPath;
  @Value("${game.service.status.path:/start-game}")
  private String gameServiceStartPath;
  @Value("${game.service.status.path:/send-number}")
  private String gameServiceSendNumberPath;

  public GameServiceGateway(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public GameDto getGameStatus(String playerId, String gameId) {
    try {
      UriComponentsBuilder builder =
          UriComponentsBuilder.fromHttpUrl(gameServiceBaseUrl + gameServiceStatusPath)
              .queryParam("playerId", playerId)
              .queryParam("gameId", gameId);

      return restTemplate.getForEntity(builder.toUriString(), GameDto.class).getBody();
    } catch (HttpStatusCodeException e) {
      throw new GameException("Error communicating game service", HttpStatus.BAD_GATEWAY);
    }
  }

  public GameDto startGame() {
    try {
      return restTemplate
          .postForEntity(gameServiceBaseUrl + gameServiceStartPath, null, GameDto.class)
          .getBody();
    } catch (HttpStatusCodeException e) {
      throw new GameException(ERROR_MESSAGE, HttpStatus.BAD_GATEWAY);
    }
  }

  public GameDto sendNumber(String playerId, String gameId, Integer number) {
    try {
      SendNumberRequest request = new SendNumberRequest(playerId, gameId, number);
      HttpHeaders httpHeaders = new HttpHeaders();
      HttpEntity<SendNumberRequest> requestEntity =
          new HttpEntity<SendNumberRequest>(request, httpHeaders);

      return restTemplate
          .exchange(
              gameServiceBaseUrl + gameServiceSendNumberPath,
              HttpMethod.PUT,
              requestEntity,
              GameDto.class)
          .getBody();

    } catch (HttpStatusCodeException e) {
      throw new GameException(ERROR_MESSAGE, HttpStatus.BAD_GATEWAY);
    }
  }
}
