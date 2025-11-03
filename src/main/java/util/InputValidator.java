package util;

import java.util.Objects;

public class InputValidator {
    //Factory Pattern


    //ex. tag, settings_length, settings_complexity, settings_style, world_name, world_description
    //    story_title, user_prompt (no bad injection?)
    public static boolean validate(String input, String inputType) {
//        return true;
        return switch (inputType) {
            case "user_prompt" ->
                    (input.length() > 5 && input.length() <= 1000); //prompt safety should be handled in request builder
            case "tag" -> !input.contains(" "); //tags should be one word
            case "settings_length" ->
                    (Objects.equals(input, "short") || Objects.equals(input, "medium") || Objects.equals(input, "long"));
            case "settings_complexity" ->
                    (Objects.equals(input, "child-friendly") || Objects.equals(input, "high-school level") || Objects.equals(input, "adult"));
            case "settings_style" ->
                    (Objects.equals(input, "descriptive") || Objects.equals(input, "dialogue-heavy") || Objects.equals(input, "action-packed"));
            case "world_name", "story_title" -> input.length() <= 50;
            case "world_description" -> input.length() <= 750;
            default -> false;
        };
    }
}
