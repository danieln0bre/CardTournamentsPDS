package br.ufrn.imd.controller;

import br.ufrn.imd.Main;
import br.ufrn.imd.model.Pairing;
import br.ufrn.imd.model.Player;
import br.ufrn.imd.service.GeneralRankingService;
import br.ufrn.imd.service.PairingService;
import br.ufrn.imd.service.EventRankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class TournamentRestController {

    @GetMapping("/ranking")
    public ArrayList<Player> getRanking() {
        ArrayList<Player> players = Main.getMockedPlayerData();
        players = EventRankingService.sortByEventPoints(players); // Sort players by event points and opponent match win rate
        GeneralRankingService.displayRanking(players);
        return players;
    }

    @GetMapping("/pairing")
    public ArrayList<Pairing> getPairing() {
        ArrayList<Player> players = Main.getMockedPlayerData();
        ArrayList<Pairing> pairings = PairingService.createPairings(players);
        return pairings;
    }
}

