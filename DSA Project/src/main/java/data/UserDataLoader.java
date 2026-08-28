package data;

import com.google.gson.*;

import java.io.*;
import java.util.*;

public class UserDataLoader {

    private static File getUserFile() {
        File[] candidates = {
                new File("data/users.json"),
                new File("DSA Project/data/users.json"),
                new File("../data/users.json")
        };

        for (File candidate : candidates) {
            if (candidate.exists()) {
                return candidate;
            }
        }

        for (File candidate : candidates) {
            if (candidate.getParentFile() != null && candidate.getParentFile().exists()) {
                return candidate;
            }
        }

        return new File("data/users.json");
    }

    public static boolean validateUser(String username, String password) {
        try {
            JsonObject root = readJson();

            if (root == null || !root.has("users")) {
                return false;
            }

            JsonArray users = root.getAsJsonArray("users");

            for (JsonElement element : users) {
                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject user = element.getAsJsonObject();

                if (!user.has("username") || !user.has("password")) {
                    continue;
                }

                String storedUsername = user.get("username").getAsString();
                String storedPassword = user.get("password").getAsString();

                if (storedUsername.equalsIgnoreCase(username) && storedPassword.equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean userExists(String username) {
        try {
            JsonObject root = readJson();

            if (root == null || !root.has("users")) {
                return false;
            }

            JsonArray users = root.getAsJsonArray("users");

            for (JsonElement element : users) {
                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject user = element.getAsJsonObject();

                if (!user.has("username")) {
                    continue;
                }

                String storedUsername = user.get("username").getAsString();

                if (storedUsername.equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean addUser(String username, String password) {
        try {
            JsonObject root = readJson();

            if (root == null) {
                root = new JsonObject();
            }

            if (!root.has("users")) {
                root.add("users", new JsonArray());
            }

            JsonArray users = root.getAsJsonArray("users");

            if (userExists(username)) {
                return false;
            }

            JsonObject newUser = new JsonObject();
            newUser.addProperty("username", username);
            newUser.addProperty("password", password);
            users.add(newUser);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file = getUserFile();

            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(root, writer);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static JsonObject readJson() throws IOException {
        File file = getUserFile();

        if (!file.exists()) {
            JsonObject root = new JsonObject();
            root.add("users", new JsonArray());

            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(root, writer);
            }

            return root;
        }

        try (FileReader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }
}