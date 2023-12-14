package eu.planlos.javapretixconnector.model;

import eu.planlos.javautilities.GermanStringsUtility;

import java.util.Map;
import java.util.stream.Collectors;

public class QnaMapUtility {

    public static Map<String, String> extractQnaMap(Map<Question, Answer> qnaMap) {
        return qnaMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> GermanStringsUtility.normalizeGermanCharacters(entry.getKey().getText()),
                        entry -> GermanStringsUtility.normalizeGermanCharacters(entry.getValue().getText())));
    }
}
