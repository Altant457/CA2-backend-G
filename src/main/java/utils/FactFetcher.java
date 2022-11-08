package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.RandomFactDTO;

import java.io.IOException;

public class FactFetcher {

    public static RandomFactDTO getFact() throws IOException {
        String factJSON = HttpUtils.fetchData("https://uselessfacts.jsph.pl/random.json?language=en");
        System.out.println("JSON Fact: " + factJSON);
        JsonObject json = JsonParser.parseString(factJSON).getAsJsonObject();
        String fact = json.get("text").getAsString();
        return new RandomFactDTO(fact);
    }
}
