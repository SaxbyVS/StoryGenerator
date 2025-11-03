package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
World Class:
    - Used for setting driven stories or for adding detail to story
    - contains a name, list of rules, and map of locations w/ descriptions
 */

public class World {
    private String worldName;
    private String worldDescription;
    private List<String> worldRules;
    private Map<String, String> locations;

    public World(){
        this.worldName = "";
        this.worldDescription = "";
        this.worldRules = new ArrayList<String>();
        this.locations = new HashMap<String, String>();
    }

    public World(String worldName, List<String> worldRules, Map<String, String> locations, String worldDescription){
        this.worldName = worldName;
        this.worldRules = worldRules;
        this.locations = locations;
        this.worldDescription = worldDescription;
    }

    //GETTERS & SETTERS
    public String getWorldName() {
        return worldName;
    }
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
    public List<String> getWorldRules() {
        return worldRules;
    }
    public void setWorldRules(List<String> worldRules) {
        this.worldRules = worldRules;
    }
    public Map<String, String> getLocations() {
        return locations;
    }
    public void setLocations(Map<String, String> locations) {
        this.locations = locations;
    }
    public void addWorldRule(String rule){
        worldRules.add(rule);
    }
    public void addLocation(String location, String description){
        locations.put(location, description);
    }
    public void removeWorldRule(int pos){
        worldRules.remove(pos);
    }
    public void removeLocation(String location){
        locations.remove(location);
    }
    public String getWorldDescription(){
        return worldDescription;
    }
    public void setWorldDescription(String worldDescription){
        this.worldDescription = worldDescription;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("World {name = ").append(worldName).append("\n");
        sb.append("description = ").append(worldDescription).append("\n");
        sb.append("rules = ").append(String.join(", ", worldRules)).append("\n");
        sb.append("locations:\n");

        for (Map.Entry<String, String> entry : locations.entrySet()){
            sb.append("\t").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();

    }
}
