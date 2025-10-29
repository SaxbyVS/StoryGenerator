package model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
StoryModel class:
    - holds library of story objects
    - can create new stories/remove stories
    - can continue stories (generate text/chapters) based on strategy
    - save/load stories to library
    - edit stories
 */

/*
Incomplete/To-do:
 (appropriate controller will handle input validation)
 (different controllers for different ui screens?)
    - (possibly favorite)
    - set summary (setSummary here should generate summary)

    -----------------------------
    - AI API integration + whatever is needed for that (strategy classes should probably take in the ai client)
 */

public class StoryModel {
    private Map<String, Story> Library;
    //private OpenAIService client;

    public StoryModel(String api_key) {
        Library = new HashMap<>();
        //this.client = new OpenAIService(api_key)
    }

    public void createStory(String title, String strategy){ //creates new story object in library
        this.Library.put(title, new Story(title, strategy));
    }

    public void removeStory(String title){ //delete story from library
        this.Library.remove(title);
    }

    public void continueStory(String title, String userPrompt){ //generate new output for given story
        Story story = this.Library.get(title);
        String strat = story.getStrategy();
        switch(strat){
            case "character":
                CharacterDrivenStrategy cds = new CharacterDrivenStrategy();
                story.addOutput(cds.generateContent(userPrompt));
                break;
            case "cyoa":
                CYOAStrategy cyoa = new CYOAStrategy();
                story.addOutput(cyoa.generateContent(userPrompt));
                break;
            case "genre":
                GenreDrivenStategy gds = new GenreDrivenStategy();
                story.addOutput(gds.generateContent(userPrompt));
                break;
            case "setting":
                SettingBasedStrategy sbs = new SettingBasedStrategy();
                story.addOutput(sbs.generateContent(userPrompt));
        }
    }

    //Save/Load
    public void saveSession(){

    }
    public void loadSession(){

    }

    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    //Story Editing


    //Tags
    public List<String> getTags(String title){ //return tags of given story
        Story st = Library.get(title);
        return st.getTags();
    }
    public void addTag(String title, String tag){ //add tag to given story
        Story st = Library.get(title);
        st.addTag(tag);
    }
    public void removeTag(String title, String tag){ //remove tag from given story
        Story st = Library.get(title);
        st.removeTag(tag);
    }

    //Strategy
    public String getStrategy(String title){ //return strategy of given story
        Story st = Library.get(title);
        return st.getStrategy();
    }

    //Titles
    public void setTitle(String title, String new_title){ //new title for given story
        Story st = Library.get(title);
        st.setTitle(new_title);
    }

    //Summary
    public String getSummary(String title){ //return summary of given story
        Story st = Library.get(title);
        return st.getSummary();
    }
    public void setSummary(String title){
        Story st = Library.get(title);
        //String generated_summary = client.summarize(st);
        //ai service's summarize (wherever it is) will get content of story and call api to summarize
        //st.setSummary(generated_summary);
    }

    //Chapter
    public List<Chapter> getChapters(String title){
        Story st = Library.get(title);
        return st.getChapters();
    }
    public void addChapter(String title){
        Story st = Library.get(title);
        st.addChapter();
    }
    public void removeChapter(String title, int pos){
        Story st = Library.get(title);
        st.removeChapter(pos);
    }

    //StorySettings
    public StorySettings getSettings(String title){
        Story st = Library.get(title);
        return st.getSettings();
    }
    public void setSettings(String title, String length, String complexity, String style){
        Story st = Library.get(title);
        st.setSettings(length, complexity, style);
    }

    //World
    public World getWorld(String title){
        Story st = Library.get(title);
        return st.getWorld();
    }
    public void setWorld(String title, String worldName, List<String> worldRules, Map<String, String> locations, String worldDescription){
        Story st = Library.get(title);
        st.setWorld(worldName, worldRules, locations, worldDescription);
    }

    //Character
    public List<Character> getCharacters(String title){
        Story st = Library.get(title);
        return st.getCharacters();
    }
    public void addCharacter(String title, String cName, List<String> traits, String backstory){
        Story st = Library.get(title);
        st.addCharacter(cName, traits, backstory);
    }
    public void removeCharacter(String title, int pos){
        Story st = Library.get(title);
        st.removeCharacter(pos);
    }
    public void editCharacter(String title, String charName, String cName, List<String> traits, String backstory){
        Story st = Library.get(title);
        st.editCharacter(charName, cName, traits, backstory);
    }

    //Output
    public String getOutput(String title){
        Story st = Library.get(title);
        return st.getOutput();
    }
    public void clearOutput(String title){
        Story st = Library.get(title);
        st.clearOutput();
    }



}
