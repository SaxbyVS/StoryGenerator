package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import control.StoryController;

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
        add(outputScroll, BorderLayout.CENTER);

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
                    String summary = controller.getSummary(title);

                    outputArea.setText(
                            "Story generated successfully!\n\n"
                                    + "── STORY ──\n\n"
                                    + storyOutput
                                    + "\n\n── SUMMARY ──\n"
                                    + summary
                    );

                    generateButton.setEnabled(true);
                    progressBar.setVisible(false);
                }

            }.execute();
        }
    }
}

