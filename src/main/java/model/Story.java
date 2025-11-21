/* package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
Story Class:
    - story class encapsulates one story
 */
/*
public class Story {
    private final List<String> tags;
    private String title;
    private String genre;
    private String summary;
    private final StringBuilder output;
    private final List<Chapter> chapters;
    private final List<Character> characters;
    private final World world;
    private final StorySettings settings;
    private final String strategy;

    public Story(String title, String strategy){ //strategy here?
        this.title = title;
        this.settings = new StorySettings(); //default - short, child-friendly, descriptive
        this.tags = new ArrayList<>();
        this.chapters = new ArrayList<>();
        this.characters = new ArrayList<>();
        this.strategy = strategy;
        this.world = new World();
        this.output = new StringBuilder();
    }


    //GETTERS & SETTERS
    //strategy
    public String getStrategy(){
        return strategy;
    }
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
    public void setSummary(String genSummary){
        if (genSummary == null || genSummary.isEmpty()) {
            String content = getContent();
            if (content.isEmpty()) {
                this.summary = "No content available to summarize yet.";
            } else {
                int end = Math.min(content.length(), 200);
                String snippet = content.substring(0, end);
                this.summary = "Summary: " + snippet + "...";
            }
        } else {
            this.summary = genSummary;
        }
    }

    public String getSummary() {
        if (this.summary == null || this.summary.isEmpty()) {
            String content = getContent();
            if (content == null || content.isEmpty()) {
                return "No story content available to summarize yet.";
            } else {
                int end = Math.min(content.length(), 200);
                return "Summary: " + content.substring(0, end) + "...";
            }
        }
        return this.summary;
    }

    //------------------------------------------------------------------------
    //chapter
    public List<Chapter> getChapters(){
        return this.chapters;
    }
    public void addChapter(){ //add current output as new chapter
        Chapter newChapter = new Chapter(Integer.toString(this.chapters.size()+1), this.output.toString());
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
    public void editCharacter(String charName, String cName, List<String> traits, String backstory){
        for (Character character : this.characters){
            if (character.getName().equals(charName)){
                character.setName(cName);
                character.setTraits(traits);
                character.setBackstory(backstory);
            }
        }
    }
    public void addCharacter(String cName, List<String> traits, String backstory){
        this.characters.add(new Character(cName, traits, backstory));
    }
    public void removeCharacter(int pos){
        this.characters.remove(pos);
    }
    //------------------------------------------------------------------------
    //output
    public String getOutput(){
        return this.output.toString();
    }
    public void addOutput(String output){
        this.output.append(output);
    }
    public void clearOutput(){
        this.output.setLength(0);
    }
    //content
    public String getContent(){ //returns full content of the story
        StringBuilder sb = new StringBuilder();
        for (Chapter ch : this.chapters){
            sb.append(ch.getText());
            sb.append("\n");
        }
        return sb.toString();
    }
}
*/

package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
Story Class:
    - story class encapsulates one story
 */

public class Story {
    private final List<String> tags;
    private String title;
    private String genre;
    private String summary;
    private final StringBuilder output;
    private final List<Chapter> chapters;
    private final List<Character> characters;
    private final World world;
    private final StorySettings settings;
    private final String strategy;

    public Story(String title, String strategy){ //strategy here?
        this.title = title;
        this.settings = new StorySettings(); //default - short, child-friendly, descriptive
        this.tags = new ArrayList<>();
        this.chapters = new ArrayList<>();
        this.characters = new ArrayList<>();
        this.strategy = strategy;
        this.world = new World();
        this.output = new StringBuilder();
    }

    //GETTERS & SETTERS
    //strategy
    public String getStrategy(){
        return strategy;
    }

    //title
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    //------------------------------------------------------------------------
    //genre
    public void setGenre(String genre){
        this.genre = genre;
    }
    public String getGenre(){
        return this.genre;
    }

    //------------------------------------------------------------------------
    //summary
    public void setSummary(String genSummary){
        if (genSummary == null || genSummary.isEmpty()) {
            String content = getContent();
            if (content.isEmpty()) {
                this.summary = "No content available to summarize yet.";
            } else {
                int end = Math.min(content.length(), 200);
                String snippet = content.substring(0, end);
                this.summary = "Summary: " + snippet + "...";
            }
        } else {
            this.summary = genSummary;
        }
    }

    public String getSummary(){
        if (this.summary == null || this.summary.isEmpty()) {
            String content = getContent();
            if (content == null || content.isEmpty()) {
                return "No story content available to summarize yet.";
            } else {
                int end = Math.min(content.length(), 200);
                return "Summary: " + content.substring(0, end) + "...";
            }
        }
        return this.summary;
    }

    //------------------------------------------------------------------------
    //chapter
    public List<Chapter> getChapters(){
        return this.chapters;
    }
    public void addChapter(){
        Chapter newChapter = new Chapter(Integer.toString(this.chapters.size()+1), this.output.toString());
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
    public void editCharacter(String charName, String cName, List<String> traits, String backstory){
        for (Character character : this.characters){
            if (character.getName().equals(charName)){
                character.setName(cName);
                character.setTraits(traits);
                character.setBackstory(backstory);
            }
        }
    }
    public void addCharacter(String cName, List<String> traits, String backstory){
        this.characters.add(new Character(cName, traits, backstory));
    }
    public void removeCharacter(int pos){
        this.characters.remove(pos);
    }

    //------------------------------------------------------------------------
    //output
    public String getOutput(){
        return this.output.toString();
    }
    public void addOutput(String output){
        this.output.append(output);
    }
    public void clearOutput(){
        this.output.setLength(0);
    }

    //content
    public String getContent(){
        StringBuilder sb = new StringBuilder();
        for (Chapter ch : this.chapters){
            sb.append(ch.getText());
            sb.append("\n");
        }
        return sb.toString();
    }
}
