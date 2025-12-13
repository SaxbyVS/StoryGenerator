package service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;

/*
OpenAIService (FINAL FIXED VERSION):

  Loads config.properties correctly using classpath
  Supports generateStory()
  Supports summarize()
  Uses gpt-4o-mini
  Provides offline fallback when no key is present
*/

public class OpenAIService {
    private static OpenAIService instance;

    private final String apiKey;
    private final HttpClient httpClient;

    // -------------------------------------------------------------
    // Constructor (loads config.properties from the classpath)
    // -------------------------------------------------------------
    public OpenAIService() {
        Properties props = new Properties();
        String key = null;
        System.out.println("DEBUG: ctor for OpenAIService");
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("config.properties NOT found in classpath.");
            } else {
                props.load(input);
                key = props.getProperty("OPENAI_API_KEY");
                System.out.println("Loaded API key from config.properties.");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }

        this.apiKey = key;
        this.httpClient = HttpClient.newHttpClient();
    }

    // Direct constructor
    public OpenAIService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    // Singleton instance
    public static OpenAIService getInstance() {
        if (instance == null) {
            instance = new OpenAIService();
        }
        return instance;
    }

    // -------------------------------------------------------------
    // Story Generation
    // -------------------------------------------------------------
    public String generateStory(String prompt) {

        if (apiKey == null || apiKey.isEmpty()) {
            return "ERROR: No API key found. Please check your configuration.";
        }

        try {
            JSONObject req = new JSONObject();
            req.put("model", "gpt-4o-mini");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject()
                    .put("role", "user")
                    .put("content", prompt));

            req.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle API-level errors first
            if (response.statusCode() != 200) {
                return "ERROR: OpenAI API error (HTTP " + response.statusCode() + ").";
            }

            JSONObject json = new JSONObject(response.body());

            if (!json.has("choices")) {
                return "ERROR: Invalid response from OpenAI.";
            }

            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

        }
        // Network / connectivity issues
        catch (java.net.UnknownHostException e) {
            return "ERROR: Network unavailable or DNS blocked.";

        } catch (java.net.http.HttpTimeoutException e) {
            return "ERROR: Connection timed out.";

        }
        // Everything else (parsing, unexpected failures)
        catch (Exception e) {
            return "ERROR: Unexpected error occurred.";
        }
    }

    // -------------------------------------------------------------
    // Summarization
    // -------------------------------------------------------------
    public String summarize(String content) {
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("No API key—using local summary fallback.");
            return localFallback(content);
        }

        try {
            JSONObject req = new JSONObject();
            req.put("model", "gpt-4o-mini");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject()
                    .put("role", "user")
                    .put("content", "Summarize this in 3 sentences:\n" + content));

            req.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());

            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            return "Error summarizing: " + e.getMessage();
        }
    }

    // -------------------------------------------------------------
    // Fallback summary
    // -------------------------------------------------------------
    private String localFallback(String content) {
        if (content == null || content.isEmpty()) {
            return "No story content available to summarize yet.";
        }
        int end = Math.min(content.length(), 200);
        return "Summary: " + content.substring(0, end) + "...";
    }

    // static loader if StoryModel uses it
    public static String loadApiKey() {
        try (InputStream input =
                     OpenAIService.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                return null;
            }

            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("OPENAI_API_KEY");

        } catch (IOException e) {
            return null;
        }
    }
}

