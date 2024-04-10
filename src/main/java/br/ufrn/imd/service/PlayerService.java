package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;
import br.ufrn.imd.repository.PlayerRepository;

import java.util.Comparator;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public static Comparator<Player> getRankComparator() {
        return Comparator
            .comparing(Player::getEventPoints, Comparator.reverseOrder())
            .thenComparing(Player::getRankPoints);
    }

    private void calculateOpponentsMatchWinrate(Player player) {
        int totalOpponents = player.getOpponentIds().size();
        double totalOpponentWinrate = 0.0;

        System.out.println("Calculating opponents match winrate for player: " + player.getUsername());
        
        for (String opponentId : player.getOpponentIds()) {
            Optional<Player> opponentOpt = playerRepository.findById(opponentId);
            if (opponentOpt.isPresent()) {
                Player opponent = opponentOpt.get();
                if (opponent.getEventPoints() > 0) {
                    double opponentWinrate = (double) opponent.getEventPoints() / totalOpponents;
                    System.out.println("Winrate for opponent " + opponent.getUsername() + ": " + opponentWinrate);
                    totalOpponentWinrate += opponentWinrate;
                }
            }
        }

        double meanOpponentWinrate = totalOpponents > 0 ? totalOpponentWinrate / totalOpponents : 0.0;
        System.out.println("Mean opponent winrate for player " + player.getUsername() + ": " + meanOpponentWinrate);
        player.setOpponentsMatchWinrate(meanOpponentWinrate);
        playerRepository.save(player); // Persist changes to the database
    }

    public void calculateWinRates(Player player) {
        System.out.println("Calculating win rates for player: " + player.getUsername());
        calculateOpponentsMatchWinrate(player);
        System.out.println("Win rates calculated for player: " + player.getUsername());
    }
}
