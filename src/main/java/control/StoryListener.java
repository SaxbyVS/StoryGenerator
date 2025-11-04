package control;

public interface StoryListener {
    void onStoryGenerated(String title);
    void onError(String message);
}
