package control;

import model.StoryModel;
import util.InputValidator;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

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

    // ✅ Observer pattern
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

    // ✅ Asynchronous story generation with observer notifications
    public void onGenerate(String title, String userPrompt) {
        try {
            onCall = true; // lock UI during API call

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    continueStory(title, userPrompt);
                    storyModel.setSummary(title);
                    return null;
                }

                @Override
                protected void done() {
                    onCall = false; // unlock UI
                    try {
                        notifyStoryGenerated(title);
                    } catch (Exception e) {
                        notifyError(e.getMessage());
                    }
                }
            }.execute();

        } catch (Exception e) {
            onCall = false;
            notifyError(e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== Model routers =====

    // save/load
    public void saveSession() {
        storyModel.saveSession();
    }

    public void loadSession() {
        storyModel.loadSession();
    }

    // create/remove/continue story
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

    // summary
    public String getSummary(String title) { // story outline/summary feature
        return storyModel.getSummary(title);
    }

    // tags
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
}

