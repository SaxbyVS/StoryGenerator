package model;

/*
StorySettings Class:
    - universal settings for a story
    - includes length, complexity, and writing style
    - can be altered at any time
 */

public class StorySettings {
    private String length;
    private String complexity;
    private String style;

    public StorySettings() {
        this.length = "short"; //or medium, long
        this.complexity = "child-friendly"; //or high-school level, adult
        this.style = "descriptive"; //or dialogue-heavy, action-packed
    }


    //GETTERS & SETTERS
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }
    public String getComplexity() {
        return complexity;
    }
    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }
    public String getStyle() {
        return style;
    }
    public void setStyle(String style) {
        this.style = style;
    }
}
