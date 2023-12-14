package eu.planlos.javapretixconnector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.planlos.javapretixconnector.model.dto.PretixQnaFilterCreateDTO;
import eu.planlos.javapretixconnector.model.dto.PretixQnaFilterUpdateDTO;
import eu.planlos.javapretixconnector.model.validation.ValidAction;
import eu.planlos.javapretixconnector.model.validation.ValidEvent;
import eu.planlos.javapretixconnector.model.validation.ValidFilterMap;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.planlos.javapretixconnector.model.QnaMapUtility.extractQnaMap;

@Entity
@EqualsAndHashCode
@NoArgsConstructor /* Required for ObjectMapper*/
@AllArgsConstructor
@ToString
public final class PretixQnaFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @JsonProperty("action")
    @NotNull
    @ValidAction
    private String action;

    @JsonProperty("event")
    @NotNull
    @ValidEvent
    private String event;

    @JsonProperty("qna-list")
    @Convert(converter = PretixQnaFilterMapToStringDBConverter.class)
    @NotNull
    @ValidFilterMap
    private Map<String, List<String>> filterMap = new HashMap<>();

    public PretixQnaFilter(@NotNull String action, @NotNull String event, @NotNull Map<String, List<String>> filterMap) {
        this.action = action;
        this.event = event;
        this.filterMap.putAll(filterMap);
    }

    /**
     * Constructor package private for tests
     * Only used in test
     * @param action action name
     * @return true if action matches
     */
    boolean isForAction(String action) {
        return this.action.equals(action);
    }

    /**
     * Constructor package private for tests
     * Only used in test
     * @param event event name
     * @return true if event matches
     */
    boolean isForEvent(String event) {
        return this.event.equals(event);
    }

    public boolean filterQnA(Map<Question, Answer> qnaMap) {

        Map<String, String> extractedQnaMap = extractQnaMap(qnaMap);

        return filterMap.entrySet().stream().allMatch(entry -> {
            String filterQuestion = entry.getKey();
            List<String> filterAnswerList = entry.getValue();
            String givenAnswer = extractedQnaMap.get(filterQuestion);
            return givenAnswer != null && filterAnswerList.contains(givenAnswer);
        });
    }

    public PretixQnaFilter(PretixQnaFilterCreateDTO dto) {
        this(dto.action(), dto.event(), dto.filterMap());
    }

    public PretixQnaFilter(PretixQnaFilterUpdateDTO dto) {
        this(dto.id(), dto.action(), dto.event(), dto.filterMap());
    }
}