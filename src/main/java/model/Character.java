package model;
import java.util.List;

/*
Character Class:
    - used for Character driven stories or for adding characters to a story
    - includes name, list of traits, and a backstory
 */

public class Character {
    private String name;
    private List<String> traits;
    private String backstory;

    public Character(String name, List<String> traits, String backstory) {
        this.name = name;
        this.traits = traits;
        this.backstory = backstory;
    }


    //GETTERS & SETTERS
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getTraits() {
        return traits;
    }
    public void setTraits(List<String> traits) {
        this.traits = traits;
    }
    public String getBackstory() {
        return backstory;
    }
    public void setBackstory(String backstory) {
        this.backstory = backstory;
    }
    public void addTrait(String trait) {
        this.traits.add(trait);
    }
    public void addBackstory(String backstory) {
        this.backstory += backstory;
    }
    public void removeTrait(String trait){
        this.traits.remove(trait);
    }

    @Override
    public String toString() {
        return "Character {" + "name = "+name + ", traits = " + String.join(", ", traits) + ", backstory = " + backstory + '}';
    }
}
