package model;

import service.OpenAIService;
import service.RequestBuilder;

public class CYOAStrategy implements AIStrategy {

    @Override
    public String generateContent(String userPrompt, Story story) {
        try {
            OpenAIService ai = OpenAIService.getInstance();
            String prompt = RequestBuilder.buildPrompt(userPrompt, story, "cyoa");
            return ai.generateStory(prompt); // works if generateStory() exists
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating CYOA content: " + e.getMessage();
        }
    }
}
