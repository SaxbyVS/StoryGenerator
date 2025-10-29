package control;

import model.StoryModel;
import model.World;
import util.InputValidator;

import java.util.List;
import java.util.Map;

/*
WorldController Class:
    - middleman between view and model
    - validates input using InputValidator before sending to model
    - handles story world editing
 */

public class WorldController {
    private StoryModel storyModel;


    public WorldController(StoryModel storyModel) {
        this.storyModel = storyModel;
    }

    public void setWorld(String title, String worldName, List<String> worldRules, Map<String, String> locations, String description) {
        if (InputValidator.validate(worldName, "world_name") && InputValidator.validate(description, "world_description")){
            storyModel.setWorld(title, worldName, worldRules, locations, description);
        }else{
            throw new IllegalArgumentException("ERROR: Invalid inputs");
        }
    }

    public String getWorldName(String title) {
        World w = getWorld(title);
        return w.getWorldName();
    }
    public String getWorldDescription(String title) {
        World w = getWorld(title);
        return w.getWorldDescription();
    }
    public List<String> getWorldRules(String title) {
        World w = getWorld(title);
        return w.getWorldRules();
    }
    public Map<String, String> getLocations(String title) {
        World w = getWorld(title);
        return w.getLocations();
    }

    private World getWorld(String title){
        return storyModel.getWorld(title);
    }
}
