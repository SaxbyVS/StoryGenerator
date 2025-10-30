package control;

import model.StoryModel;
import model.Character;
import util.InputValidator;

import java.util.List;

/*
CharacterController Class:
    - middleman between view and model
    - handles story character editing/adding/removal
 */

public class CharacterController {

    private StoryModel storyModel;

    public CharacterController(StoryModel storyModel) {
        this.storyModel = storyModel;
    }

    public void editCharacter(String title, String charName, String cName, List<String> traits, String backstory){
        storyModel.editCharacter(title, charName, cName, traits, backstory);
    }

    public void addCharacter(String title, String cName, List<String> traits, String backstory){
        storyModel.addCharacter(title, cName, traits, backstory);
    }

    public void removeCharacter(String title, int pos){
        storyModel.removeCharacter(title, pos);
    }

    public List<Character> getCharacters(String title){
        return storyModel.getCharacters(title);
    }

}
