package service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

/*
OpenAIService:
 - Singleton pattern
 - Loads API key from config.properties
 - Handles summarization via OpenAI API (uses fallback if key missing)
*/

public class OpenAIService {
    private static OpenAIService instance;
    private final String apiKey;
    private final HttpClient httpClient;

    // constructor is public so StoryModel can also instantiate if needed
    public OpenAIService() throws IOException {
        Properties props = new Properties();
        String key = null;

        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            // Replace with your private key inside config.properties as:
            // OPENAI_API_KEY=sk-your-api-key
            key = props.getProperty("OPENAI_API_KEY");
        } catch (IOException e) {
            System.err.println("config.properties not found. Running in offline mode.");
        }

        this.apiKey = key;
        this.httpClient = HttpClient.newHttpClient();
    }

    // constructor for direct use
    public OpenAIService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    // Singleton accessor
    public static OpenAIService getInstance() throws IOException {
        if (instance == null) {
            instance = new OpenAIService();
        }
        return instance;
    }

    // Summarize story text (fallback if no key)
    public String summarize(String content) {
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("No API key found. Using local summary fallback.");
            return localFallbackSummary(content);
        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", "Summarize this story in 3 concise sentences:\n" + content);
            messages.put(message);

            requestBody.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (!jsonResponse.has("choices")) {
                return "Error: Unexpected response format.";
            }

            return jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating summary: " + e.getMessage();
        }
    }

    // Local fallback if API not configured
    private String localFallbackSummary(String content) {
        if (content == null || content.isEmpty()) {
            return "No story content available to summarize yet.";
        }
        int end = Math.min(content.length(), 200);
        return "Summary: " + content.substring(0, end) + "...";
    }

    // Static loader for config file (used by MainFrame)
    public static String loadApiKey() {
        try (InputStream input = new FileInputStream("src/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("OPENAI_API_KEY");
        } catch (IOException e) {
            System.err.println("Error loading API key: " + e.getMessage());
            return null;
        }
    }
}
