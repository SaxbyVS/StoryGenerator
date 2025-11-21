package model;

import service.OpenAIService;
import service.RequestBuilder;

public class GenreDrivenStategy implements AIStrategy {

    @Override
    public String generateContent(String userPrompt, Story story) {
        try {
            OpenAIService ai = OpenAIService.getInstance();
            String prompt = RequestBuilder.buildPrompt(userPrompt, story, "genre");
            return ai.generateStory(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating genre-driven content: " + e.getMessage();
        }
    }
}
