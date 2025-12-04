package view;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import control.StoryController;
import model.Chapter;
import java.util.List;

public class StoryPanel extends JPanel {
    private final StoryController controller;
//    private final JTextField titleField;
    private final JTextArea promptArea;
    private final JTextArea outputArea;
//    private final JComboBox<String> strategyBox;
    private final JButton generateButton;
    private final JButton clearButton;
    private final JProgressBar progressBar;
    private final String currStory;

    public StoryPanel(StoryController controller, String story_title) {
        this.controller = controller;
        this.currStory = story_title;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setBackground(new Color(245, 247, 250));

        // Input Panel (top)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Story Title: "+story_title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
//        titleField = new JTextField();
//        styleInput(titleField);

        JLabel promptLabel = new JLabel("Prompt / Idea:");
        promptLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        promptArea = new JTextArea(4, 20);
        styleTextArea(promptArea);

        JLabel strategyLabel = new JLabel("Story Mode: "+controller.getStrategy(currStory));
        strategyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
//        strategyBox = new JComboBox<>(new String[]{"character", "cyoa", "genre", "setting"});
//        styleCombo(strategyBox);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(titleLabel, gbc);
//        gbc.gridx = 1; gbc.gridy = 0;
//        inputPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(promptLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(new JScrollPane(promptArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(strategyLabel, gbc);
//        gbc.gridx = 1; gbc.gridy = 2;
//        inputPanel.add(strategyBox, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Output area (center)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Serif", Font.PLAIN, 15));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Generated Story"));
//        add(outputScroll, BorderLayout.CENTER);

        //CHAPTER BAR (center) - dynamically change output to saved chapters
        DefaultListModel<String> chapterModel = new DefaultListModel<>();
        List<Chapter> chapList = controller.getModel().getChapters(currStory);
        chapterModel.addElement("Output");
        for (Chapter c: chapList){
            chapterModel.addElement(c.getTitle());
        }
            //list
        JList<String> jChapterList = new JList<>(chapterModel);
        jChapterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jChapterList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jChapterList.setVisibleRowCount(1);
            //scroll pane
        JScrollPane chapterScrollPane = new JScrollPane(jChapterList);
        chapterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chapterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jChapterList.addListSelectionListener(e->{
            if (!e.getValueIsAdjusting()){
                String currChap = jChapterList.getSelectedValue();
                if (currChap != null){
                    if (currChap.equals("Output")){
                        outputArea.setText(controller.getOutput(currStory));
                    }else{
                        String selChap = controller.getModel().getChapters(currStory).get(Integer.parseInt(currChap)).getText();
                        outputArea.setText(selChap);
                    }
                }
            }
        });

        //add/remove chapter buttons
        JButton addChapterButton = new JButton("Add Chapter");
        JButton removeChapterButton = new JButton("Remove Chapter");
        addChapterButton.addActionListener(e->{
            int result = JOptionPane.showConfirmDialog(
              this,
              "Add current output as chapter?",
              "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION){
                int chapNum = controller.getChapterCount(currStory);
                chapterModel.addElement(String.valueOf(chapNum));
                controller.addChapter(currStory);
            }
        });
        removeChapterButton.addActionListener(e->{
            String selChap = jChapterList.getSelectedValue();
            if (selChap == null){
                return;
            }
            String removeMSG = "Remove Chapter "+selChap+"?";
           int result = JOptionPane.showConfirmDialog(
                   this,
                   removeMSG,
                   "Confirmation",
                   JOptionPane.YES_NO_OPTION
           );
           if (result == JOptionPane.YES_OPTION){
               if (selChap == "Output"){
                   JOptionPane.showMessageDialog(this,"Cannot remove output");
               }else {
                   chapterModel.removeElement(selChap);
                   controller.removeChapter(currStory, Integer.parseInt(selChap));
               }
           }
        });
        //summarybutton
        JButton getSummaryButton = new JButton("Get Current Summary");
        getSummaryButton.addActionListener(e->{
            String currSummary = controller.getSummary(currStory);
            JTextArea summaryArea = new JTextArea(currSummary);
            summaryArea.setEditable(false);
            summaryArea.setLineWrap(true);
            summaryArea.setWrapStyleWord(true);

            JScrollPane summaryScroll = new JScrollPane(summaryArea);

            JOptionPane.showMessageDialog(this, summaryScroll, "Story Summary", JOptionPane.INFORMATION_MESSAGE);
        });


        JPanel chapterButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chapterButtonPanel.add(addChapterButton);
        chapterButtonPanel.add(removeChapterButton);
        chapterButtonPanel.add(getSummaryButton);


        //add central panel - output area and chapter bar
        JPanel central_panel = new JPanel(new BorderLayout());
        central_panel.add(outputScroll, BorderLayout.CENTER);
        central_panel.add(chapterScrollPane, BorderLayout.NORTH);
        central_panel.add(chapterButtonPanel, BorderLayout.SOUTH);
        add(central_panel, BorderLayout.CENTER);



        // Bottom controls (buttons + progress)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 247, 250));

        generateButton = new JButton("Generate Story");
        clearButton = new JButton("Clear");
        styleButton(generateButton, new Color(88, 101, 242));
        styleButton(clearButton, new Color(230, 71, 89));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(200, 20));

        buttonPanel.add(generateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(progressBar);

        add(buttonPanel, BorderLayout.SOUTH);

        //SETTINGS BUTTON - makes new settingsFrame window
        JButton SettingsButton = new JButton("Settings");
        SettingsButton.setBackground(Color.blue);
        SettingsButton.setForeground(Color.white);
        SettingsButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        SettingsButton.setFocusPainted(false);
        SettingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        SettingsButton.addActionListener(e -> {
            SettingsFrame settingsWindow = new SettingsFrame(controller, currStory);
            settingsWindow.setVisible(true);
        });

        add(SettingsButton, BorderLayout.EAST);




        // Event listeners
        generateButton.addActionListener(new GenerateListener());
        clearButton.addActionListener(e -> {
//            titleField.setText("");
            promptArea.setText("");
            outputArea.setText("");
        });
    }

    // Styling helpers
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleInput(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private void styleTextArea(JTextArea area) {
        area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    // Async generation using SwingWorker
    private class GenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = currStory;
            String prompt = promptArea.getText().trim();
//            String strategy = controller.getStrategy(currStory);

            if (title.isEmpty() || prompt.isEmpty()) {
                JOptionPane.showMessageDialog(
                        StoryPanel.this,
                        "Please enter a prompt before generating.",
                        "Missing Input",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            generateButton.setEnabled(false);
            progressBar.setVisible(true);

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
//                    controller.createStory(title, strategy);
                    controller.onGenerate(title, prompt);
                    return null;
                }

                @Override
                protected void done() {
                    String storyOutput = controller.getOutput(title);
//                    String summary = controller.getSummary(title);

                    outputArea.setText(
                            "Story generated successfully!\n\n"
                                    + "── STORY ──\n\n"
                                    + storyOutput
//                                    + "\n\n── SUMMARY ──\n"
//                                    + summary
                    );

                    generateButton.setEnabled(true);
                    progressBar.setVisible(false);
                }

            }.execute();
        }
    }
}

