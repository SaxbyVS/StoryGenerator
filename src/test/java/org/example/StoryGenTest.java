package org.example;

import model.StoryModel;
import model.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.OpenAIService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StoryModel and OpenAIService
 * Uses local fallback summary when API key is not set.
 */
public class StoryGenTest {

    private StoryModel storyModel;

    @BeforeEach
    void setUp() {
        // Initializes StoryModel with a dummy key (does not call API)
        storyModel = new StoryModel("test-key");
    }

    // --- Basic Story Management ---

    @Test
    void testCreateAndRemoveStory() {
        storyModel.createStory("Adventure", "cyoa");
        assertNotNull(storyModel.getStrategy("Adventure"));
        storyModel.removeStory("Adventure");
        assertThrows(IllegalArgumentException.class, () -> storyModel.getStrategy("Adventure"));
    }

    @Test
    void testAddTagAndRemoveTag() {
        storyModel.createStory("SciFi", "genre");
        storyModel.addTag("SciFi", "space");
        assertTrue(storyModel.getTags("SciFi").contains("space"));

        storyModel.removeTag("SciFi", "space");
        assertFalse(storyModel.getTags("SciFi").contains("space"));
    }

    @Test
    void testAddCharacterAndEdit() {
        storyModel.createStory("Fantasy", "character");
        storyModel.addCharacter("Fantasy", "Aria", java.util.List.of("brave", "curious"), "A forest wanderer.");
        assertEquals(1, storyModel.getCharacters("Fantasy").size());

        storyModel.editCharacter("Fantasy", "Aria", "Aria Windwalker", java.util.List.of("wise", "adventurous"), "An explorer.");
        assertEquals("Aria Windwalker", storyModel.getCharacters("Fantasy").get(0).getName());
    }

    // --- Summary Generation ---

    @Test
    void testLocalSummaryFallback() {
        storyModel.createStory("ShortStory", "genre");
        String title = "ShortStory";

        // Add short story content manually
        storyModel.getStoryPublic(title).addOutput("Once upon a time...");

        // Should generate a local fallback summary
        storyModel.setSummary(title);
        String summary = storyModel.getSummary(title);
        assertTrue(summary.contains("Summary") || summary.length() > 0);
    }

    // --- Save / Load Persistence ---

    @Test
    void testSaveAndLoadSession() {
        storyModel.createStory("Mystery", "genre");
        storyModel.addTag("Mystery", "detective");
        storyModel.saveSession();

        StoryModel loaded = new StoryModel("test-key");
        loaded.loadSession();

        assertTrue(loaded.getTags("Mystery").contains("detective"));
    }

    // --- OpenAI Service ---

    @Test
    void testLoadApiKey() {
        // Even if config file not present, should not crash
        String key = OpenAIService.loadApiKey();
        assertDoesNotThrow(() -> OpenAIService.loadApiKey());
        assertTrue(key == null || key.length() >= 0);
    }

    @Test
    void testLocalFallbackSummary() throws IOException {
        OpenAIService service = new OpenAIService();
        String result = service.summarize("The quick brown fox jumps over the lazy dog.");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
}
