package model;

import service.OpenAIService;
import service.RequestBuilder;

public class CharacterDrivenStrategy implements AIStrategy {

    @Override
    public String generateContent(String userPrompt, Story story) {
        try {
            OpenAIService ai = OpenAIService.getInstance();
            String prompt = RequestBuilder.buildPrompt(userPrompt, story, "character");
            return ai.generateStory(prompt); // now this will work
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating character-driven content: " + e.getMessage();
        }
    }
}

