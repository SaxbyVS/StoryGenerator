package model;
import java.util.HashMap;
import java.util.Map;

/*
StoryModel class:
    - holds library of story objects
    - can create new stories/remove stories
    - can continue stories (generate text/chapters) based on strategy
    - save/load stories to library
 */

/*
Incomplete/To-do:
 (appropriate controller will handle input validation)
 (different controllers for different ui screens?)
    - get/add/remove tags (possible favorite)
    - get strategy
    - get/set title
    - get/set summary (setSummary here should generate summary)
    - get/add/remove chapter (consolidate/save current output)
        - get content - full story (might not need this)
    - get/set story settings
    - get/set world
    - get/add/remove character
    - get/add/clear output

    -----------------------------
    - AI API integration + whatever is needed for that
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

}
