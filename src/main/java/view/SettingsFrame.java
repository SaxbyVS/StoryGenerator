package view;

import control.*;
import model.StoryModel;

import javax.swing.*;
import java.awt.*;

//ACCESSIBLE FROM STORYPANEL (add settings button to storyPanel) -> need story title passed along

public class SettingsFrame extends JFrame {
    private StoryController storyController;
    public SettingsFrame(StoryController storyController) {
        this.storyController = storyController;
        StoryModel storyModel = storyController.getModel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 350));
        setLayout(new BorderLayout());

        setTitle("Settings");

        //top universal settings
        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        SettingsController settingsController = new SettingsController(storyModel);
           //add length, style, complexity buttons/lists here


    }
}

class CharacterPanel extends JPanel{}

class WorldPanel extends JPanel{}


