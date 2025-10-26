package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
Story Class:
    - story class encapsulates one story
 */


/*
Incomplete:
    - setSummary (summary generator)
 */

public class Story {
    private final List<String> tags;
    private String title;
    private String genre;
    private String summary;
    private final List<Chapter> chapters;
    private final List<Character> characters;
    private final World world;
    private final StorySettings settings;
    private final String strategy;

    public Story(String title, String strategy){ //strategy here?
        this.title = title;
        this.settings = new StorySettings(); //default - short, child-friendly, descriptive
        this.tags = new ArrayList<String>();
        this.chapters = new ArrayList<Chapter>();
        this.characters = new ArrayList<Character>();
        this.strategy = strategy;
        this.world = new World();
    }


    //GETTERS & SETTERS
    //title
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    //------------------------------------------------------------------------
    //genre
    public void setGenre(String genre){ //for genre-driven stories
        this.genre = genre;
    }
    public String getGenre(){
        return this.genre;
    }
    //------------------------------------------------------------------------
    //summary
    public void setSummary(){
        //generate summary - maybe with apiservice?
        //this.summary = generatedSummary;
    }
    public String getSummary(){
        return this.summary;
    }
    //------------------------------------------------------------------------
    //chapter
    public List<Chapter> getChapters(){
        return this.chapters;
    }
    public void addChapter(String text){
        Chapter newChapter = new Chapter(Integer.toString(this.chapters.size()+1), text);
        this.chapters.add(newChapter);
    }
    public void removeChapter(int pos){
        this.chapters.remove(pos);
    }
    //------------------------------------------------------------------------
    //tag
    public List<String> getTags(){
        return this.tags;
    }
    public void addTag(String tag){
        this.tags.add(tag);
    }
    public void removeTag(String tag){
        this.tags.remove(tag);
    }
    //------------------------------------------------------------------------
    //story settings
    public StorySettings getSettings(){
        return this.settings;
    }
    public void setSettings(String length, String complexity, String style){
        this.settings.setLength(length);
        this.settings.setComplexity(complexity);
        this.settings.setStyle(style);
    }
    //------------------------------------------------------------------------
    //world
    public World getWorld(){
        return this.world;
    }
    //when there is already a world
    public void setWorld(String worldName, List<String> worldRules, Map<String, String> locations, String worldDescription){
        this.world.setWorldName(worldName);
        this.world.setWorldRules(worldRules);
        this.world.setLocations(locations);
        this.world.setWorldDescription(worldDescription);
    }
    //------------------------------------------------------------------------
    //character
    public List<Character> getCharacters(){
        return this.characters;
    }
    public void addCharacter(String cName, List<String> traits, String backstory){
        this.characters.add(new Character(cName, traits, backstory));
    }
    public void removeCharacter(int pos){
        this.characters.remove(pos);
    }
}
