package control;
import model.StoryModel;
import util.InputValidator;

import java.util.List;

/*
StoryController Class:
    - middleman between view and model
    - validates input using InputValidator before sending to model
    - handles main story funcs - creation, removal, summary, content, tags/favorite, save/load
 */

/*
To-do:
    - handle buffers if needed?
    - implement observer to monitor api call status
 */


public class StoryController {
    private boolean onCall = false; //api call in progress or not (for locking ui control)
    private StoryModel storyModel;

    public StoryController(StoryModel storyModel) {
        this.storyModel = storyModel;
    }


    public void onGenerate(String title, String userPrompt){
        try {
            continueStory(title, userPrompt);
            storyModel.setSummary(title);
            onCall = true; //while true, ui interactive elements are locked
            //api observer/handler goes here? to monitor state of api call; unlock onCall

        } catch (Exception e) {
            onCall = false;
            e.printStackTrace();
        }
    }

    //model routers

    //save/load
    public void saveSession(){storyModel.saveSession();}
    public void loadSession(){storyModel.loadSession();}

    //create/remove/continue story
    public void createStory(String title, String strategy){
        if (InputValidator.validate(title, "story_title")){
            storyModel.createStory(title, strategy);
        }else{
            throw new IllegalArgumentException("ERROR: invalid story title.");
        }
    }
    public void removeStory(String title){
        storyModel.removeStory(title);
    }
    private void continueStory(String title, String userPrompt){
        if (InputValidator.validate(userPrompt, "user_prompt")){
            storyModel.continueStory(title, userPrompt);
        }else{
            throw new IllegalArgumentException("ERROR: invalid user prompt.");
        }
    }

    //summary
    public String getSummary(String title){ //story outline/summary feature
        return storyModel.getSummary(title);
    }

    //tags
    public List<String> getTags(String title){
        return storyModel.getTags(title);
    }
    public void addTag(String title, String tag){
        if (InputValidator.validate(tag, "tag")){
            storyModel.addTag(title, tag);
        }else{
            throw new IllegalArgumentException("ERROR: Invalid tag");
        }
    }
    public void removeTag(String title, String tag){
        storyModel.removeTag(title, tag);
    }
}
