package model;

import service.RequestBuilder;

public class GenreDrivenStategy implements AIStrategy {
    //private aiservice whatever

    //ctor

    @Override
    public String generateContent(String userPrompt, Story story) {
        String content = "";
        String prompt;

        //configure prompt using request builder for genre driven story (add user prompt)
        prompt = RequestBuilder.buildPrompt(userPrompt, story, "genre");

        //content = aiservice generate(prompt, temperature, tokens)
        return content;
    }
}
