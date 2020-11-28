package cz.czu.nick.chess.backend.service;

import cz.czu.nick.chess.backend.model.Game;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class GameService {

    private Map<String, Game> games = new ConcurrentHashMap<>();

    public Game createGame() {
        Game game = new Game();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        game.setPlayer1(username);
        games.put(UUID.randomUUID().toString(), game);
        return game;
    }

    public Game joinGame(String id) {
        // TODO: handle exp
        Game game = games.get(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        game.setPlayer2(username);
        game.setStarted(true);

        games.put(id, game);

        return game;
    }

    public Map<String, Game> getAvailableGames() {
        System.out.println("getAvailableGames");

        return games.entrySet()
                .stream()
                .filter(map -> !map.getValue().isStarted())
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }
}
