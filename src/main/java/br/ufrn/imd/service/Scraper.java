package br.ufrn.imd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Scraper {
    private String[] urls;
    private Map<String, List<String>> deckPositions;

    public Scraper(String[] urls) {
        this.urls = urls;
        this.deckPositions = new HashMap<>();
    }

    public void scrape() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

        for (String url : urls) {
            try {
                Document doc = Jsoup.connect(url).headers(headers).get();
                Elements column4Tags = doc.select("td.column-4");
                Elements column9Tags = doc.select("td.column-9");

                if (column4Tags.size() == column9Tags.size()) {
                    for (int i = 0; i < column4Tags.size(); i++) {
                        String deckName = column4Tags.get(i).text().trim();
                        Matcher matcher = Pattern.compile("\\d+").matcher(column9Tags.get(i).text());
                        while (matcher.find()) {
                            String position = matcher.group();
                            if (!deckPositions.containsKey(deckName)) {
                                deckPositions.put(deckName, new ArrayList<String>());
                            }
                            List<String> positions = deckPositions.get(deckName);
                            positions.add(position);
                        }
                    }
                } else {
                    System.out.println("Mismatch in the number of tags in columns 4 and 9 for URL: " + url);
                }
            } catch (Exception e) {
                System.out.println("Failed to retrieve the page. URL: " + url + ", Error: " + e.getMessage());
            }
        }
    }

    public Map<String, List<String>> getDeckPositions() {
        return deckPositions;
    }
}

