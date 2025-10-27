package control;

/*
StoryController Class:
    - middleman between view and model
    - validates input using InputValidator before sending to model
    - handles main story funcs - creation, removal, summary, content, tags/favorite, save/load
 */

public class StoryController {
    private boolean onCall = false; //api call in progress or not (for locking ui control)
}
