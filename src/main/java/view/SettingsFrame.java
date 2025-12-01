package view;

import control.*;
import model.Character;
import model.StoryModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//ACCESSIBLE FROM STORYPANEL (add settings button to storyPanel) -> need story title passed along

public class SettingsFrame extends JFrame {
    private StoryController storyController;
    private String currStory;
    public SettingsFrame(StoryController storyController, String currStory) {
        this.currStory = currStory;
        this.storyController = storyController;
        StoryModel storyModel = storyController.getModel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 350));
        setLayout(new BorderLayout());

        setTitle("Settings");

        //top universal settings ---------------------------
        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        SettingsController settingsController = new SettingsController(storyModel);

        //add length, style, complexity buttons/lists here
        String[] lengths = {"short", "medium", "long"};
        String[] complexities = {"child-friendly", "high-school level", "adult"};
        String[] styles = {"descriptive", "dialogue-heavy", "action-packed"};

        JComboBox<String> lengthBox = new JComboBox<>(lengths);
        lengthBox.setSelectedItem(settingsController.getLength(currStory));
        JComboBox<String> complexityBox = new JComboBox<>(complexities);
        complexityBox.setSelectedItem(settingsController.getComplexity(currStory));
        JComboBox<String> styleBox = new JComboBox<>(styles);
        styleBox.setSelectedItem(settingsController.getStyle(currStory));

        JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(e -> {
            settingsController.setSettings(currStory, (String) lengthBox.getSelectedItem(), (String) complexityBox.getSelectedItem(), (String) styleBox.getSelectedItem());
        });

        topBar.add(new JLabel("Length:"));
        topBar.add(lengthBox);
        topBar.add(new JLabel("Complexity:"));
        topBar.add(complexityBox);
        topBar.add(new JLabel("Style:"));
        topBar.add(styleBox);
        topBar.add(saveButton);
        //--------------------------------------------------


        //OVERALL SETTINGS WINDOW
        add(topBar, BorderLayout.NORTH);


        //MODE SPECIFIC PANEL
        String strat = storyController.getStrategy(currStory);
        switch (strat){
            case "character":
                add(new CharacterPanel(this.storyController.getModel(), currStory), BorderLayout.CENTER);
                break;
            case "setting":
                add(new WorldPanel(this.storyController.getModel(), currStory), BorderLayout.CENTER);
                break;
            case "genre":
                add(new GenrePanel(this.storyController, currStory), BorderLayout.CENTER);
                break;
            default:
                break;
        }

        setVisible(true);

    }
}

class CharacterPanel extends JPanel{
    private CharacterController characterController;
    private String currStory;
    public CharacterPanel(StoryModel model, String currStory){
        this.characterController = new CharacterController(model);
        this.currStory = currStory;

        setLayout(new BorderLayout());

        JLabel CLabel = new JLabel("Characters:");
        add(CLabel, BorderLayout.NORTH);

        //character list editing
        DefaultListModel<String> characterModel = new DefaultListModel<>();
        List<Character> characters = characterController.getCharacters(currStory);
        for (Character character : characters){
            characterModel.addElement(character.getName());
        }
        JList<String> cJList = new JList<>(characterModel);
        cJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTextArea characterTextArea = new JTextArea(4,20);

        characterTextArea.setEditable(false);
        characterTextArea.setLineWrap(true);
        characterTextArea.setWrapStyleWord(true);
        cJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int thisCharacter = cJList.getSelectedIndex();
                if (thisCharacter != -1){
                    characterTextArea.setText(characterController.getCharacters(currStory).get(thisCharacter).toString());
                }
            }
        });

        //add
        JButton addCharacterButton = new JButton("Add Character");
        JButton removeCharacterButton = new JButton("Remove Character");
        addCharacterButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name: ");
            if (newName ==null || newName.trim().equals("")){
                return;
            }
            List<String> traits = new ArrayList<>();
            while(true){
                String traitInput = JOptionPane.showInputDialog("Enter a trait (Cancel to finish): ");
                if (traitInput == null || traitInput.trim().equals("")){
                    break;
                }
                traits.add(traitInput);
            }
            String backstory = JOptionPane.showInputDialog("Enter a backstory: ");
            if (backstory == null){
                backstory = "";
            }
            characterModel.addElement(newName);
            characterController.addCharacter(currStory, newName, traits, backstory);

        });
        removeCharacterButton.addActionListener(e -> {
            int thisCharacter = cJList.getSelectedIndex();
            if (thisCharacter != -1){
                characterController.removeCharacter(currStory, thisCharacter);
                characterModel.remove(thisCharacter);
            }
        });

        JPanel centralCharPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centralCharPanel.add(cJList);
        centralCharPanel.add(characterTextArea);

        JPanel charButtonPanel = new JPanel();
        charButtonPanel.setLayout(new BoxLayout(charButtonPanel, BoxLayout.Y_AXIS));
        charButtonPanel.add(addCharacterButton);
        charButtonPanel.add(removeCharacterButton);


        add(centralCharPanel, BorderLayout.CENTER);
        add(charButtonPanel, BorderLayout.EAST);


    }
}

class WorldPanel extends JPanel{
    private WorldController worldController;
    private String currStory;
    public WorldPanel(StoryModel model, String currStory){
        this.worldController = new WorldController(model);
        this.currStory = currStory;

        setLayout(new BorderLayout());

        //world name !!!!!!!!!!!!!!!!!!!!!
        JTextField worldNameField = new JTextField(20);
        String currWorldName = worldController.getWorldName(currStory);
        if(currWorldName != null){
            worldNameField.setText(currWorldName);
        }
        JScrollPane descScrollPane = new JScrollPane(worldNameField);
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        //World Description @@@@@@@@@@@@@@@@@@@@@@@@@@
        JTextArea worldDescriptionArea = new JTextArea(5, 30);
        worldDescriptionArea.setLineWrap(true);
        worldDescriptionArea.setWrapStyleWord(true);
        String currWorldDescription = worldController.getWorldDescription(currStory);
        if(currWorldDescription != null){
            worldDescriptionArea.setText(currWorldDescription);
        }
        JScrollPane worldScrollPane = new JScrollPane(worldDescriptionArea);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //north panel - world name + world description
        JPanel north_panel = new JPanel();
        north_panel.setLayout(new BoxLayout(north_panel, BoxLayout.Y_AXIS));
        north_panel.add(new JLabel("World Name: "));
        north_panel.add(descScrollPane);
        north_panel.add(new JLabel("Description: "));
        north_panel.add(worldScrollPane);
        add(north_panel, BorderLayout.NORTH);

        //world rules *********************************
        DefaultListModel<String> rulesModel = new DefaultListModel<>();
        worldController.getWorldRules(currStory).forEach(rulesModel::addElement);

        JList<String> rulesList = new JList<>(rulesModel);
        rulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rulesScrollPane = new JScrollPane(rulesList);

        JButton addRule = new JButton("Add Rule");
        JButton removeRule = new JButton("Remove Rule");
        addRule.addActionListener(e -> {
            String rule = JOptionPane.showInputDialog("Enter New Rule: ");
            if (rule != null && !rule.isEmpty()) {
                rulesModel.addElement(rule.trim());
            }
        });
        removeRule.addActionListener(e -> {
            int index = rulesList.getSelectedIndex();
            if (index != -1) {
                rulesModel.remove(index);
            }
        });
        //*********************************************
//        add(rulesScrollPane, BorderLayout.CENTER);
//        add(addRule, BorderLayout.CENTER);
//        add(removeRule, BorderLayout.CENTER);

        //world locations = name + desc -------------------------
        DefaultListModel<String> locationNameModel = new DefaultListModel<>();
        worldController.getLocations(currStory).keySet().forEach(locationNameModel::addElement);
        JList<String> locationsList = new JList<>(locationNameModel);
        locationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTextArea locationDescriptionArea = new JTextArea(4, 20);
        locationDescriptionArea.setEditable(false);
        JScrollPane locationScrollPane = new JScrollPane(locationDescriptionArea);

        //display description when selected
        locationsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String name = locationsList.getSelectedValue();
                if (name != null && !name.isEmpty()) {
                    locationDescriptionArea.setText(worldController.getLocations(currStory).get(name));
                }
            }
        });

        JButton addLocation = new JButton("Add Location");
        addLocation.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Location Name: ");
            if (name == null){
                return;
            }
            String description = JOptionPane.showInputDialog("Enter "+name+" Description: ");
            if (description == null){
                description = "";
            }
            locationNameModel.addElement(name.trim());
            worldController.getLocations(currStory).put(name, description);
        });

        JButton removeLocation = new JButton("Remove Location");
        removeLocation.addActionListener(e -> {
           String name = locationsList.getSelectedValue();
            if (name != null && !name.isEmpty()) {
                locationNameModel.removeElement(name);
                worldController.getLocations(currStory).remove(name);
            }
        });
        //--------------------------------------
//        add(locationsList, BorderLayout.CENTER);
//        add(locationDescriptionArea, BorderLayout.CENTER);

        //center panel add
        JPanel center_panel = new JPanel(new GridBagLayout());

        JLabel rulesLabel = new JLabel("Rules:");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy=0;
        center_panel.add(rulesLabel, c);

        c.gridy = 1; c.gridx=0;
        c.gridwidth = 2;
        center_panel.add(rulesScrollPane, c);

        c.gridwidth = 1;
        c.gridy=2; c.gridx=0;
        center_panel.add(addRule, c);
        c.gridy=2; c.gridx=1;
        center_panel.add(removeRule, c);

        JLabel locationLabel = new JLabel("Locations List:");
        c.gridy=3; c.gridx=0;
        center_panel.add(locationLabel, c);

        c.gridy=4; c.gridx=0;
        c.gridwidth = 2;
        center_panel.add(locationsList, c);


        c.gridy=5; c.gridx=0;
        c.gridwidth = 2;
        center_panel.add(locationScrollPane, c);

        c.gridwidth = 1;
        c.gridy=6; c.gridx=0;
        center_panel.add(addLocation, c);
        c.gridy=6; c.gridx=1;
        center_panel.add(removeLocation, c);

        add(center_panel, BorderLayout.CENTER);


        //sAVE ALL
        JButton saveWorldButton = new JButton("Save World Settings");
        saveWorldButton.setBackground(Color.blue);
        saveWorldButton.setForeground(Color.white);
        saveWorldButton.addActionListener(e -> {
           worldController.setWorld(currStory, worldNameField.getText(), Collections.list(rulesModel.elements()), worldController.getLocations(currStory),worldDescriptionArea.getText());
        });
        add(saveWorldButton, BorderLayout.EAST);
    }
}

class GenrePanel extends JPanel{
    private StoryController storyController;
    private String currStory;
    public GenrePanel(StoryController storyController, String currStory){
        this.storyController = storyController;
        this.currStory = currStory;
        setLayout(new BorderLayout());

        String[] genreChoices = {"comedy", "fantasy", "romance", "horror", "mystery", "sci-fi", "action"};
        JList<String> genreList = new JList<>(genreChoices);
        String currGenre = storyController.getGenre(currStory);
        if (!(currGenre ==null)){
            int index=0;
            for (int i=0; i<genreChoices.length; i++) {
                if (genreChoices[i].equals(currGenre)) {
                    index = i;
                    break;
                }
            }
            genreList.setSelectedIndex(index);
        }
        genreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(genreList, BorderLayout.CENTER);

        JButton saveGenreButton = new JButton("Save Genre");
        saveGenreButton.addActionListener(e -> {
            String selected = genreList.getSelectedValue();
            if (selected == null){
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a genre to save.",
                        "Warning: No Genre Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
           storyController.setGenre(currStory, selected);
            JOptionPane.showMessageDialog(this, "Genre Saved!");
        });

        add(saveGenreButton, BorderLayout.SOUTH);
    }
}


