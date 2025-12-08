package org.example;

import model.StoryModel;
import model.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.OpenAIService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StoryModel and OpenAIService
 * Uses local fallback summary when API key is not set.
 */

public class StoryGenTest {

    private StoryModel storyModel;

    @BeforeEach
    void setUp() {
        storyModel = new StoryModel("test-key");
    }

    @Test
    void testCreateAndRemoveStory() {
        storyModel.createStory("Adventure", "cyoa");
        assertNotNull(storyModel.getStrategy("Adventure"));

        storyModel.removeStory("Adventure");
        assertThrows(IllegalArgumentException.class,
                () -> storyModel.getStrategy("Adventure"));
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
        storyModel.addCharacter("Fantasy", "Aria", List.of("brave", "curious"), "A forest wanderer.");

        assertEquals(1, storyModel.getCharacters("Fantasy").size());

        storyModel.editCharacter("Fantasy", "Aria",
                "Aria Windwalker",
                List.of("wise", "adventurous"),
                "An explorer.");

        assertEquals("Aria Windwalker",
                storyModel.getCharacters("Fantasy").get(0).getName());
    }

    @Test
    void testLocalSummaryFallback() {
        storyModel.createStory("ShortStory", "genre");
        String title = "ShortStory";

        storyModel.getStoryPublic(title).addOutput("Once upon a time...");

        storyModel.setSummary(title);
        String summary = storyModel.getSummary(title);

        assertFalse(summary.isEmpty());
    }

    @Test
    void testSaveAndLoadSession() {
        storyModel.createStory("Mystery", "genre");
        storyModel.addTag("Mystery", "detective");

        assertDoesNotThrow(() -> storyModel.saveSession());
        StoryModel loaded = new StoryModel("test-key");
        assertDoesNotThrow(loaded::loadSession);
    }

    @Test
    void testLoadApiKey() {
        assertDoesNotThrow(OpenAIService::loadApiKey);

        String key = OpenAIService.loadApiKey();
        assertTrue(key == null || !key.isEmpty()); // key may be null
    }

    @Test
    void testLocalFallbackSummaryService() {
        OpenAIService service = new OpenAIService();
        String result = service.summarize("The quick brown fox jumps over the lazy dog.");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testCreateStoryOverwritesExisting() {
        storyModel.createStory("Overwrite", "genre");
        storyModel.createStory("Overwrite", "character");
        assertEquals("character", storyModel.getStrategy("Overwrite"));
    }

    @Test
    void testRemoveStoryThenAccessThrows() {
        storyModel.createStory("ToDelete", "genre");
        storyModel.removeStory("ToDelete");
        assertThrows(IllegalArgumentException.class,
                () -> storyModel.getStrategy("ToDelete"));
    }

    @Test
    void testGetTagsInitiallyEmpty() {
        storyModel.createStory("TagTest", "genre");
        assertTrue(storyModel.getTags("TagTest").isEmpty());
    }

    @Test
    void testGetCharactersInitiallyEmpty() {
        storyModel.createStory("CharInit", "character");
        assertTrue(storyModel.getCharacters("CharInit").isEmpty());
    }

    @Test
    void testRemoveCharacterByIndex() {
        storyModel.createStory("RemoveIndex", "character");

        storyModel.addCharacter("RemoveIndex", "A", List.of("trait"), "bio");
        storyModel.addCharacter("RemoveIndex", "B", List.of("trait2"), "bio2");

        storyModel.removeCharacter("RemoveIndex", 0);

        assertEquals(1, storyModel.getCharacters("RemoveIndex").size());
        assertEquals("B",
                storyModel.getCharacters("RemoveIndex").get(0).getName());
    }

    @Test
    void testRemoveCharacterIndexOutOfBoundsThrows() {
        storyModel.createStory("BadRemove", "character");
        assertThrows(IndexOutOfBoundsException.class,
                () -> storyModel.removeCharacter("BadRemove", 5));
    }

    @Test
    void testSummaryGeneratedWhenOutputExists2() {
        storyModel.createStory("SumStory", "genre");
        storyModel.getStoryPublic("SumStory").addOutput("Hello world!");

        storyModel.setSummary("SumStory");
        String summary = storyModel.getSummary("SumStory");

        assertNotNull(summary);
        assertFalse(summary.isEmpty());
    }

    @Test
    void testClearOutputWorks() {
        storyModel.createStory("ClearOut", "genre");
        Story s = storyModel.getStoryPublic("ClearOut");

        s.addOutput("Line 1");
        assertFalse(s.getOutput().isEmpty());

        storyModel.clearOutput("ClearOut");
        assertEquals("", storyModel.getOutput("ClearOut"));
    }

    @Test
    void testAddChapterIncreasesCount() {
        storyModel.createStory("Chaptered", "genre");
        storyModel.addChapter("Chaptered", "Intro");

        assertEquals(1, storyModel.getChapterCount("Chaptered"));
    }

    @Test
    void testRemoveChapter() {
        storyModel.createStory("ChapRemove", "genre");
        storyModel.addChapter("ChapRemove", "A");
        storyModel.addChapter("ChapRemove", "B");

        storyModel.removeChapter("ChapRemove", "A");

        assertEquals(1, storyModel.getChapterCount("ChapRemove"));
        assertEquals("B",
                storyModel.getChapters("ChapRemove").get(0).getTitle());
    }
}
