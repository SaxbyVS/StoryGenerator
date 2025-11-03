package model;

import service.RequestBuilder;

public class SettingBasedStrategy implements AIStrategy{
    //private aiservice whatever

    //ctor

    @Override
    public String generateContent(String userPrompt, Story story) {
        String content = "";
        String prompt;

        //configure prompt using request builder for settings based story (add user prompt)
        prompt = RequestBuilder.buildPrompt(userPrompt, story, "setting");

        //content = aiservice generate(prompt, temperature, tokens)
        return content;
    }
}
