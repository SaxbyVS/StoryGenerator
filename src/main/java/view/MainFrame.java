package view;

import javax.swing.*;
import java.awt.*;
import control.StoryController;
import model.StoryModel;
import service.OpenAIService;


public class MainFrame extends JFrame {
    public MainFrame() {
        super("AI Story Generator ✨");

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
        StoryController storyController = new StoryController(storyModel);

        // Title bar
        JLabel title = new JLabel("AI Story Generator", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 28));
        title.setForeground(new Color(40, 44, 52));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Main panel
        add(new StoryPanel(storyController), BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Powered by OpenAI • Designed by Rayan", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }
}
