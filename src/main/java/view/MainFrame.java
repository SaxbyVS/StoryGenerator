package view;

import javax.swing.*;
import java.awt.*;
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

        JButton createStoryButton = new JButton("Create Story");
        createStoryButton.addActionListener(e -> {
            this.setVisible(false);
            StoryEditorFrame editor = new StoryEditorFrame(this, storyController);
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
        Map<String, Story> StoryList = this.storyController.getLibrary();

        for (Map.Entry<String, Story> entry : StoryList.entrySet()) {
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
    public StoryEditorFrame(MainFrame parent, StoryController storyController) {
        //pass along controller and parent frame
        this.storyController = storyController;
        this.parent = parent;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 600));
        setLayout(new BorderLayout());

        //NORTH PANEL---------------
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());


        //title
        JLabel title = new JLabel("AI Story Generator", SwingConstants.CENTER);
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

        // Main panel
        add(new StoryPanel(storyController), BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Powered by OpenAI • Designed by Rayan", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);



//        setVisible(true);
    }
}

