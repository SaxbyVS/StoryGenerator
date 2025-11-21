package model;

import service.OpenAIService;
import java.io.*;
import java.util.*;

/*
StoryModel class:
    - Holds library of story objects
    - Can create/remove/edit stories
    - Can continue stories (generate text/chapters) based on strategy
    - Save/load stories to library
    - Generate summaries (local or via OpenAI)
 */

public class StoryModel {
    private final Map<String, Story> library = new HashMap<>();

    public StoryModel(String apiKey) {
        // Currently unused — reserved for OpenAI client initialization if needed
        // Example: this.client = new OpenAIService(apiKey);
    }

    // Create / Remove
    public void createStory(String title, String strategy) {
        library.put(title, new Story(title, strategy));
    }

    public void removeStory(String title) {
        library.remove(title);
    }

    // Continue story using chosen strategy
    public void continueStory(String title, String userPrompt) {
        Story story = getStory(title);
        String strat = story.getStrategy();

        switch (strat) {
            case "character" -> {
                CharacterDrivenStrategy cds = new CharacterDrivenStrategy();
                story.addOutput(cds.generateContent(userPrompt, story));
            }
            case "cyoa" -> {
                CYOAStrategy cyoa = new CYOAStrategy();
                story.addOutput(cyoa.generateContent(userPrompt, story));
            }
            case "genre" -> {
                GenreDrivenStategy gds = new GenreDrivenStategy();
                story.addOutput(gds.generateContent(userPrompt, story));
            }
            case "setting" -> {
                SettingBasedStrategy sbs = new SettingBasedStrategy();
                story.addOutput(sbs.generateContent(userPrompt, story));
            }
            default -> System.err.println("Unknown strategy: " + strat);
        }
//        System.out.println("DEBUG: output\n" + story.getOutput());
    }

    // Save / Load
    public void saveSession() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(System.getProperty("user.home") + "/storyLibrary.dat"))) {
            out.writeObject(library);
            System.out.println("Session saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save session: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSession() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(System.getProperty("user.home") + "/storyLibrary.dat"))) {
            Map<String, Story> loaded = (Map<String, Story>) in.readObject();
            library.clear();
            library.putAll(loaded);
            System.out.println("Session loaded successfully.");
        } catch (Exception e) {
            System.err.println("Failed to load session: " + e.getMessage());
        }
    }

    // -----------------------------
    // Story Editing / Accessors
    // -----------------------------
    private Story getStory(String title) {
        Story story = library.get(title);
        if (story == null) {
            throw new IllegalArgumentException("Story not found: " + title);
        }
        return story;
    }

    // Tags
    public List<String> getTags(String title) { return getStory(title).getTags(); }
    public void addTag(String title, String tag) { getStory(title).addTag(tag); }
    public void removeTag(String title, String tag) { getStory(title).removeTag(tag); }

    // Strategy
    public String getStrategy(String title) { return getStory(title).getStrategy(); }

    // Title
    public void setTitle(String title, String newTitle) { getStory(title).setTitle(newTitle); }

    // Summary
    public String getSummary(String title) { return getStory(title).getSummary(); }

    public void setSummary(String title) {
        Story st = getStory(title);
        String content;
        if (!st.getChapters().isEmpty()){
            content = st.getContent(); // summarize all saved content
        } else{
            //PROTOTYPE DEMOING
            content = st.getOutput(); //if no saved content, summarize current output
        }


        if (content == null || content.isEmpty()) {
            st.setSummary("No story content available to summarize yet.");
            return;
        }

        try {
            // Try using OpenAI if available
            String apiKey = OpenAIService.loadApiKey();
            if (apiKey != null && !apiKey.isEmpty()) {
                OpenAIService ai = new OpenAIService(apiKey);
                String aiSummary = ai.summarize(content);
                st.setSummary(aiSummary);
            } else {
                // Fallback local summary
                int end = Math.min(content.length(), 200);
                String snippet = content.substring(0, end);
                st.setSummary("Summary: " + snippet + "...");
            }
        } catch (Exception e) {
            System.err.println("Failed to generate AI summary: " + e.getMessage());
        }
    }

    // Chapters
    public List<Chapter> getChapters(String title) { return getStory(title).getChapters(); }
    public void addChapter(String title) { getStory(title).addChapter(); }
    public void removeChapter(String title, int pos) { getStory(title).removeChapter(pos); }

    // StorySettings
    public StorySettings getSettings(String title) { return getStory(title).getSettings(); }
    public void setSettings(String title, String length, String complexity, String style) {
        getStory(title).setSettings(length, complexity, style);
    }

    // World
    public World getWorld(String title) { return getStory(title).getWorld(); }
    public void setWorld(String title, String worldName, List<String> worldRules,
                         Map<String, String> locations, String worldDescription) {
        getStory(title).setWorld(worldName, worldRules, locations, worldDescription);
    }

    // Characters
    public List<Character> getCharacters(String title) { return getStory(title).getCharacters(); }
    public void addCharacter(String title, String cName, List<String> traits, String backstory) {
        getStory(title).addCharacter(cName, traits, backstory);
    }
    public void removeCharacter(String title, int pos) { getStory(title).removeCharacter(pos); }
    public void editCharacter(String title, String charName, String cName,
                              List<String> traits, String backstory) {
        getStory(title).editCharacter(charName, cName, traits, backstory);
    }

    // Output
    public String getOutput(String title) { return getStory(title).getOutput(); }
    public void clearOutput(String title) { getStory(title).clearOutput(); }

    public Story getStoryPublic(String title) {
        return getStory(title);
    }
}


