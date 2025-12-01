package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import control.StoryController;
import model.Story;
import model.StoryModel;
import service.OpenAIService;


//FIRST MAIN SCREEN - library list of stories + createStory
public class MainFrame extends JFrame {
    private StoryController storyController;
    private JPanel centerPanel;
    public MainFrame() {
        super("AI Story Generator ✨");

        //MAINFRAME SETUP ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        // Global theme
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 600));
        setLayout(new BorderLayout());

        // Create model + controller
        StoryModel storyModel = new StoryModel(OpenAIService.loadApiKey());
        this.storyController = new StoryController(storyModel);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


        //FRAME LAYOUT + CUSTOMIZATION -------------------------------------
        //north panel - title/create story button
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JLabel title = new JLabel("Story Library", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 28));
        title.setForeground(new Color(40, 44, 52));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        //CREATE STORY BUTTON - asks for title/strat first before going to StoryEditorFrame <- (story is created here)
        JButton createStoryButton = new JButton("Create Story");
        createStoryButton.addActionListener(e -> {
            this.setVisible(false);
            StorySetup.SetupResult setup = StorySetup.show(this);

            if (setup == null){ //invalid input or cancel
                return;
            }

            StoryEditorFrame editor = new StoryEditorFrame(this, storyController, setup.title, setup.strat);
            editor.setVisible(true);
        });

        northPanel.add(title);
        northPanel.add(createStoryButton);

        add(northPanel, BorderLayout.NORTH);

        //center panel - list of stories
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        buildList(centerPanel);



        setVisible(true);
    }

    //PACKAGE PRIVATE
    void refresh(){ // refreshes screen/library list contents
        //redraw Library list contents - in case anything new
        buildList(centerPanel);
    }

    private void buildList(JPanel center){
        //create story rows
        System.out.println("DEBUG: buildList");
        Map<String, Story> StoryList = this.storyController.getLibrary();

        for (Map.Entry<String, Story> entry : StoryList.entrySet()) {
            System.out.println("DEBUG: entry - "+entry.getValue().getTitle());
            center.add(createStoryRow(entry.getValue()));
        }

        JScrollPane scrollPane = new JScrollPane(center);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStoryRow(Story story) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //name and tags
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(story.getTitle()));
        infoPanel.add(new JLabel("Tags: " + story.getTags()));

        //buttons - EDIT, REMOVE, FAVORITE?, TAGS?
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);

        return row;
    }
}



//STORY EDITOR FRAME - includes StoryPanel.java obj within
class StoryEditorFrame extends JFrame{
    private final StoryController storyController;
    private final MainFrame parent;
    private final String currStory;
    private final String currStrat;
    public StoryEditorFrame(MainFrame parent, StoryController storyController, String story_title, String story_strat) {
        //pass along controller and parent frame
        this.storyController = storyController;
        this.parent = parent;
        this.currStory = story_title;
        this.currStrat = story_strat;

        //in case save and close btn is not used
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                parent.setVisible(true);
                parent.refresh();
            }
        });


        setSize(1000, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 600));
        setLayout(new BorderLayout());

        //NORTH PANEL---------------
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());


        //title
        JLabel title = new JLabel("AI Story Generator " , SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 28));
        title.setForeground(new Color(40, 44, 52));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        northPanel.add(title);

        //close button - save too?
        JButton closeEditorButton = new JButton("Save & Close Editor");
        closeEditorButton.setBackground(Color.blue);
        closeEditorButton.setForeground(Color.white);
        closeEditorButton.setFont(new Font("SansSerif", Font.BOLD, 28));
        closeEditorButton.setFocusPainted(false);
        closeEditorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeEditorButton.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
            parent.refresh();
        });
        northPanel.add(closeEditorButton);

        add(northPanel, BorderLayout.NORTH);
        //--------------------------

        /*
        ************************
        * CREATE STORY
        * **********************
         */
        storyController.createStory(currStory, currStrat);

        // Main panel
        add(new StoryPanel(storyController, currStory), BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Powered by OpenAI • Designed by Rayan", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);



//        setVisible(true);
    }
}



class StorySetup { //used when creating stories to get title/strat first; prior to editor frame switch
    public static SetupResult show(Component parent){
        JTextField titleField = new JTextField(20);
        String[] strats = {"character", "setting", "cyoa", "genre"};
        JComboBox<String> stratsComboBox = new JComboBox<>(strats);
        stratsComboBox.setSelectedIndex(0);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1,5,5));
        panel.add(new JLabel("Story Title"));
        panel.add(titleField);
        panel.add(new JLabel("Choose Strategy:"));
        panel.add(stratsComboBox);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Story Setup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){ //user ready to move to storyeditor
            String title = titleField.getText();
            String strategy = (String) stratsComboBox.getSelectedItem();

            if (title.isEmpty()){ //check title has been entered
                JOptionPane.showMessageDialog(parent, "Please enter a story title before continuing.");
                return null;
            }
            return new SetupResult(title, strategy);
        }
        return null;
    }

    public static class SetupResult{
        public final String title;
        public final String strat;
        public SetupResult(String title, String strat){
            this.title = title;
            this.strat = strat;
        }
    }
}

