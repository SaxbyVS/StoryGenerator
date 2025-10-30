package control;
import model.StoryModel;
import model.StorySettings;
import util.InputValidator;

/*
SettingsController Class:
    - middleman between view and model
    - validates input using InputValidator before sending to model
    - handles universal story settings
 */



public class SettingsController {
    private StoryModel storyModel;

    public SettingsController(StoryModel storyModel) {
        this.storyModel = storyModel;
    }

    //getters/setters
    public String getLength(String title){
        StorySettings sts = storyModel.getSettings(title);
        return sts.getLength();
    }
    public String getComplexity(String title){
        StorySettings sts = storyModel.getSettings(title);
        return sts.getComplexity();
    }
    public String getStyle(String title){
        StorySettings sts = storyModel.getSettings(title);
        return sts.getStyle();
    }

    public void setSettings(String title, String length, String complexity, String style){
        if (InputValidator.validate(length, "settings_length") && InputValidator.validate(complexity, "settings_complexity") && InputValidator.validate(style, "settings_style")){
            storyModel.setSettings(title, length, complexity, style);
        }else{
            throw new IllegalArgumentException("ERROR: StorySetting argument invalid.");
        }
    }
}
