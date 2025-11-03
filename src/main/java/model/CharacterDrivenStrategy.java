package model;

import service.RequestBuilder;

public class CharacterDrivenStrategy implements AIStrategy {
    //private aiservice whatever

    //ctor

    @Override
    public String generateContent(String userPrompt, Story story) {
        String content = "";
        String prompt;

        //configure prompt using request builder for character driven story (add user prompt)
        prompt = RequestBuilder.buildPrompt(userPrompt, story, "character");

        //content = aiservice generate(prompt, temperature, tokens)
        return content;
    }
}
