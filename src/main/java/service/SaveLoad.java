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
            e.printStackTrace();
        }
    }

    public Map<String, Story> load(){
        File file = new File(file_path);
        if (!file.exists()){return new HashMap<>();}

        try (Reader reader = new FileReader(file)){
            return gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, Story>>(){}.getType());
        }catch (IOException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
