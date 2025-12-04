package control;

import model.Story;
import model.StoryModel;
import util.InputValidator;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/*
StoryController Class:
    - middleman between view and model
    - validates input using InputValidator before sending to model
    - handles main story funcs - creation, removal, summary, content, tags/favorite, save/load
    - uses observer pattern to notify UI about API call completion
    - uses SwingWorker for async operations
 */

public class StoryController {
    private boolean onCall = false; // api call in progress or not (for locking UI)
    private StoryModel storyModel;

    // Observer pattern
    private List<StoryListener> listeners = new ArrayList<>();

    public void addListener(StoryListener l) {
        listeners.add(l);
    }

    public void removeListener(StoryListener l) {
        listeners.remove(l);
    }

    private void notifyStoryGenerated(String title) {
        for (StoryListener l : listeners) {
            l.onStoryGenerated(title);
        }
    }

    private void notifyError(String message) {
        for (StoryListener l : listeners) {
            l.onError(message);
        }
    }

    public StoryController(StoryModel storyModel) {
        this.storyModel = storyModel;
    }


    public void onGenerate(String title, String userPrompt) {
        try {
            onCall = true;
            continueStory(title, userPrompt);   // blocking
//            storyModel.setSummary(title);       // blocking

            onCall = false;
            // Notify listeners
            notifyStoryGenerated(title);

        } catch (Exception e) {
            onCall = false;
            notifyError(e.getMessage());
        }

    }

    // ============================
    // Model routers
    // ============================

    public void saveSession() {
        storyModel.saveSession();
    }

    public void loadSession() {
        storyModel.loadSession();
    }

    // Create/remove/continue story
    public void createStory(String title, String strategy) {
        if (InputValidator.validate(title, "story_title")) {
            storyModel.createStory(title, strategy);
        } else {
            throw new IllegalArgumentException("ERROR: invalid story title.");
        }
    }

    public void removeStory(String title) {
        storyModel.removeStory(title);
    }

    private void continueStory(String title, String userPrompt) {
        if (InputValidator.validate(userPrompt, "user_prompt")) {
            storyModel.continueStory(title, userPrompt);
        } else {
            throw new IllegalArgumentException("ERROR: invalid user prompt.");
        }
    }

    public Map<String, Story> getLibrary(){
        return storyModel.getLibrary();
    }

    public StoryModel getModel(){  //ONLY USED FOR SETTINGS CHANGE to construct other controllers
        return this.storyModel;
    }

    public void addChapter(String title, String cTitle){
        //adds current output to new chapter; can be used before output clear if you want to save
        storyModel.addChapter(title, cTitle);
        storyModel.setSummary(title);
    }
    public void removeChapter(String title, String cTitle){

        storyModel.removeChapter(title, cTitle);
        if (storyModel.getChapterCount(title)==0){
            storyModel.setSummaryCustom(title, "");
        }else{
            storyModel.setSummary(title);
        }
    }

    // Output
    public String getOutput(String title) {
        return storyModel.getOutput(title);
    }

    // Summary
    public String getSummary(String title) {
        return storyModel.getSummary(title);
    }

    // Tags
    public List<String> getTags(String title) {
        return storyModel.getTags(title);
    }

    public void addTag(String title, String tag) {
        if (InputValidator.validate(tag, "tag")) {
            storyModel.addTag(title, tag);
        } else {
            throw new IllegalArgumentException("ERROR: Invalid tag");
        }
    }

    public void removeTag(String title, String tag) {
        storyModel.removeTag(title, tag);
    }

    //GENRE
    public String getGenre(String title) {
        return storyModel.getGenre(title);
    }
    public void setGenre(String title, String genre) {
        storyModel.setGenre(title, genre);
    }

    //STRAT
    public String getStrategy(String title) {
        return storyModel.getStrategy(title);
    }

    // ======================================
    // New helper for UI (used by StoryPanel)
    // ======================================
    public String getFullOutputAndSummary(String title) {
        String story = storyModel.getOutput(title);
        String summary = storyModel.getSummary(title);

        return "—— STORY ——\n" +
                story +
                "\n\n—— SUMMARY ——\n" +
                summary;
    }

    public int getChapterCount(String title){
        return storyModel.getChapterCount(title);
    }
}


