package model;
import java.util.Map;

/*
StoryModel class:
    - holds library of story objects
    - can create new stories/remove stories
    - can continue stories (generate text/chapters) based on strategy
    - save/load stories to library
 */

public class StoryModel {
    private Map<String, Story> Library;
}
