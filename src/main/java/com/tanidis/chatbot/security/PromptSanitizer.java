package com.tanidis.chatbot.security;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PromptSanitizer {

    private static final List<Pattern> INJECTION_PATTERNS = List.of(
            Pattern.compile("ignore (all |previous |prior )?instructions", Pattern.CASE_INSENSITIVE),
            Pattern.compile("you are now", Pattern.CASE_INSENSITIVE),
            Pattern.compile("forget (everything|all|previous)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("system prompt", Pattern.CASE_INSENSITIVE),
            Pattern.compile("jailbreak", Pattern.CASE_INSENSITIVE),
            Pattern.compile("pretend (you are|to be)", Pattern.CASE_INSENSITIVE)
    );

    public String sanitize(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return "Το μήνυμα απορρίφθηκε για λόγους ασφαλείας.";
            }
        }

        return input.trim();
    }
}