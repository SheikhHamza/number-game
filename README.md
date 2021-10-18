# Game of Numbers

## Components:

### Game Component:

Contains complete business logic for the game including creation of players, player's move and calculating the game
result.

### Player Component:

Contains controller for the players, handles automatic input and also store game id for player so that the players don't
have to remember it. Player component communicates with Game component via REST APIs.

## To Run The Application:

Simply run the following command in terminal:

 ```
 mvn spring-boot:run
```

## To Play The Game:

###API Path:

```
Through Player Componend (Recommended): 
localhost:8080/v1/api/player/actions

Through Game Componend: 
localhost:8080/v1/api/game
```

# Missing Implementation:

Unit and integrations tests for player component.