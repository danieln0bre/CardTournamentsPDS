package br.ufrn.imd.service;

import br.ufrn.imd.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventRankingService {
	
    public static List<Player> sortByEventPoints(List<Player> players) {
    	// Criar outro ArrayList para evitar modificar "players".
        List<Player> sortedPlayers = new ArrayList<>();
        sortedPlayers.addAll(players);
        // Ordena os jogadores com base nos event points e winrate dos oponentes.
        Collections.sort(sortedPlayers, new EventPointsAndOpponentMatchWinrateComparator());
        return sortedPlayers;
    }

    // Comparador para rankear os jogadores baseado nos pontos de evento e taxa de vitória do oponente.
    static class EventPointsAndOpponentMatchWinrateComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {

        	// Compara por pontos de evento (em ordem decrescente)
            int eventPointsComparison = Integer.compare(p2.getEventPoints(), p1.getEventPoints());
            
            // Se os pontos de evento forem diferentes.
            
            if (eventPointsComparison != 0) {
                return eventPointsComparison;
            }
            
            // Se os pontos de evento forem iguais.
            
            // Compara utilizando a taxa de vitória do oponente (em ordem decrescente).
            return Double.compare(p2.getOpponentsMatchWinrate(), p1.getOpponentsMatchWinrate());
        }
    }
}
