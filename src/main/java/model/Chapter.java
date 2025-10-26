package model;

/*
Chapter Class:
    - encapsulates text within a story for one chapter
 */

public class Chapter {
    private String title;
    private String text;

    public Chapter(String title, String text) {
        this.title = title;
        this.text = text;
    }

    //GETTERS & SETTERS

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }

}
