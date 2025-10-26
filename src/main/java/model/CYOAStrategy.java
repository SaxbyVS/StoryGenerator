package model;

//CHOOSE YOUR OWN ADVENTURE
public class CYOAStrategy implements AIStrategy{
    //private aiservice whatever

    //ctor

    @Override
    public String generateContent(String userPrompt) {
        String content = "";
        String prompt;

        //configure prompt using request builder for CYOA story (add user prompt)
        //content = aiservice generate(prompt, temperature, tokens)
        //high temperature? - most barebones gen mode - no genre,world,characters (give it high creativity to balance)

        return content;
    }
}
