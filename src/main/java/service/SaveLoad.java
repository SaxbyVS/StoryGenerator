package service;




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.*;
import model.Story;

/*

SaveLoad Class:

    - handles gson serialization of storyModel library HashMap
    - Saves current library/loads saved library
 */


public class SaveLoad {
    private static final String file_path = "stories.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void save(Map<String, Story> library){
        try (Writer writer = new FileWriter(file_path)){
            gson.toJson(library, writer);
        }catch (IOException e){
            System.out.println("Failed to save.");
            e.printStackTrace();
        }


    }


    public Map<String, Story> load(){
        File file = new File(file_path);
        if (!file.exists()){return new HashMap<>();}

        try (Reader reader = new FileReader(file)){
            Map<String, Story> libmap = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, Story>>(){}.getType());
            if (libmap == null){return new HashMap<>();}
            return libmap;
        }catch (IOException e){
            System.out.println("Failed to load; starting empty.");
            e.printStackTrace();
            return new HashMap<>();

        }
    }

}