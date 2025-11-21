package model;

import service.OpenAIService;
import service.RequestBuilder;

public class SettingBasedStrategy implements AIStrategy {

    @Override
    public String generateContent(String userPrompt, Story story) {
        try {
            OpenAIService ai = OpenAIService.getInstance();
            String prompt = RequestBuilder.buildPrompt(userPrompt, story, "setting");
            return ai.generateStory(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating setting-driven content: " + e.getMessage();
        }
    }
}

