# Story Generator Project Report

## Challenges Faced
 - Controller Crowding
	- Since the Story encompasses potentially so many pieces of data, the StoryController routing to StoryModel seemed like it was going to be very crowded.
	- To solve this and to separate data elements that may be exclusive from others, I created 4 separate controllers including: StoryController, CharacterController, SettingsController, and WorldController. This way, stories which include these elements based on their chosen story type can be edited in their own unique windows using their respective controllers.
 - Full-scale UI Integration
   - Having never worked with Java Swing prior to this class, the scale of the implementation was far greater than anticipated. An emphasis was thus placed on basic functionality as opposed to perfect consistency and aesthetic appeal.

## Design Pattern Justification
 - Strategy Pattern: The strategy pattern was used to implement the different story modes that the user can choose from. The four strategies implement the AIStrategy interface. Depending on which strategy is chosen, a different prompt will be constructed for submission to the api.
 - Factory Pattern: The factory pattern was utilized for making the InputValidator. Depending on the type of input passed in, it will choose how to check the input based on the switch statement.
 - Observer Pattern: The observer pattern was implemented within the StoryController with the StoryListener and in the StoryPanel with the GenerateListener which implements ActionListener. These notify when actions are being executed and completed.
 - Singleton Pattern: The singleton pattern is used in the OpenAIService object class. This is utilized with getInstance when the different strategies are making their api calls.

## OOP Pillars
 - Encapsulation: Important base classes like Chapter, Character, Story, StoryModel, StorySettings, World, + Controller(s) are appropriately encapsulated with the necessary getters/setters provided for safe use.
 - Inheritance: Inheritance is used extensively for the view implementation, namely in extending JFrame for the MainFrame, StoryEditorFrame, and SettingsFrame; similarly for extending JPanel in StoryPanel, CharacterPanel, GenrePanel, SettingsPanel, and WorldPanel.
 - Polymorphism: With the implementation of the AIStrategy interface, the different "Strategy" classes can be all be used to add/generate inputs.
 - Abstraction: The main utilization of abstraction was to simplify controller interactions with different sections of the UI. In this way, multiple "Controller" classes were made to separate duties when dealing with all the editable settings/mode specific customizations.


## AI Usage
 - Used ChatGPT for help with error handling and project structure
   - How should I structure UI element windows such as settings given I need that to encapsulate several data inputs, etc.
   - By what Swing mechanism can I connect the editor window to the main screen; as in how can I hide the main screen when entering the editor and make it reappear updated automatically when the editor is closed.

## Time Spent: ~35 hours

