package data;

import com.google.gson.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConverter {

    public static void convert(String inputPath, String outputPath) {
        try (FileReader reader = new FileReader(inputPath)) {
            JsonElement root = JsonParser.parseReader(reader);

            if (!root.isJsonArray()) {
                System.out.println("Invalid JSON format. Expected an array.");
                return;
            }

            JsonArray movies = root.getAsJsonArray();
            int count = 0;

            for (JsonElement movieElement : movies) {
                if (!movieElement.isJsonObject()) {
                    continue;
                }

                JsonObject movie = movieElement.getAsJsonObject();

                convertToArray(movie, "genres");
                convertToArray(movie, "keywords");
                convertToArray(movie, "production_companies");
                convertToArray(movie, "production_countries");
                convertToArray(movie, "spoken_languages");

                count++;
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(movies, writer);
            }

            System.out.println("Conversion completed!");
            System.out.println("Movies converted: " + count);
            System.out.println("Output file: " + outputPath);

        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Error while converting JSON:");
            System.out.println(e.getMessage());
        }
    }

    private static void convertToArray(JsonObject movie, String fieldName) {
        if (!movie.has(fieldName) || movie.get(fieldName).isJsonNull()) {
            return;
        }

        JsonElement element = movie.get(fieldName);

        if (element.isJsonArray()) {
            return;
        }

        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String jsonString = element.getAsString();

            if (jsonString == null || jsonString.trim().isEmpty()) {
                movie.add(fieldName, new JsonArray());
                return;
            }

            try {
                JsonElement parsed = JsonParser.parseString(jsonString);

                if (parsed.isJsonArray()) {
                    movie.add(fieldName, parsed);
                }
            } catch (JsonSyntaxException e) {
                System.out.println("Could not convert field: " + fieldName);
            }
        }
    }
}