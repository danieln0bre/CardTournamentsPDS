package br.ufrn.imd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.ufrn.imd.model.Player;

public class PlayerScraper {

    public List<Player> scrapePlayers() throws IOException {
        List<Player> allPlayers = new ArrayList<>();
        long id = 1; // Início do ID fora do loop para garantir unicidade

        for (int page = 1; page <= 51; page++) {
            String url = "https://onepiece.limitlesstcg.com/players?page=" + page;
            Document document = Jsoup.connect(url).get();
            Elements rows = document.select("table > tbody > tr");

            for (Element row : rows) {
                String playerName = row.select("td:nth-child(2) a").text();
                // Pula jogadores sem nome
                if (playerName.isEmpty()) {
                    continue;
                }
                
                String rankPointsStr = row.select("td:nth-child(4)").text().trim(); // Obtem os pontos de rank como String

                // Verifica se a string dos pontos de rank não está vazia e tenta converter para inteiro
                int rankPoints = 0; // Valor padrão se a string estiver vazia ou não puder ser convertida
                if (!rankPointsStr.isEmpty()) {
                    try {
                        rankPoints = Integer.parseInt(rankPointsStr);
                    } catch (NumberFormatException e) {
                        System.err.println("Unable to parse rank points for player: " + playerName + " with rank points string: " + rankPointsStr);
                        // Decide continuar com valor padrão
                    }
                }

                String username = playerName.toLowerCase().replaceAll(" ", "");
                String email = username + "@card.com";

                Player player = new Player(playerName, username, "senha"); // ID incremental
                player.setRankPoints(rankPoints);
                // Adiciona o jogador à lista de todos os jogadores
                allPlayers.add(player);
            }
        }

        return allPlayers;
    }

}
