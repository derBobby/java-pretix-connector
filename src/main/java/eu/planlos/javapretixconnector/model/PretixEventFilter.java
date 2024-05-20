package eu.planlos.javapretixconnector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.planlos.javapretixconnector.model.dto.PretixEventFilterCreateDTO;
import eu.planlos.javapretixconnector.model.dto.PretixEventFilterUpdateDTO;
import eu.planlos.javapretixconnector.model.validation.ValidAction;
import eu.planlos.javapretixconnector.model.validation.ValidEvent;
import eu.planlos.javapretixconnector.model.validation.ValidFilterMap;
import eu.planlos.javapretixconnector.model.validation.ValidOrganizer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.planlos.javapretixconnector.model.QnaMapUtility.extractQnaMap;

@Slf4j
@Entity
@EqualsAndHashCode
@NoArgsConstructor /* Required for ObjectMapper*/
@AllArgsConstructor
@ToString
public final class PretixEventFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @JsonProperty("action")
    @NotNull
    @ValidAction
    private String action;

    @JsonProperty("organizer")
    @NotNull
    @ValidOrganizer
    private String organizer;

    @JsonProperty("event")
    @NotNull
    @ValidEvent
    private String event;

    @JsonProperty("qna-list")
    @Convert(converter = PretixEventFilterMapToStringDBConverter.class)
    @NotNull
    @ValidFilterMap
    private Map<String, List<String>> filterMap = new HashMap<>();

    public PretixEventFilter(@NotNull String action, @NotNull String organizer, @NotNull String event, @NotNull Map<String, List<String>> filterMap) {
        this.action = action;
        this.organizer = organizer;
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

        boolean allMatch = filterMap.entrySet().stream().allMatch(entry -> {
            String filterQuestion = entry.getKey();
            List<String> filterAnswerList = entry.getValue();
            String givenAnswer = extractedQnaMap.get(filterQuestion);
            return givenAnswer != null && filterAnswerList.contains(givenAnswer);
        });

        log.debug("All QnA from {} match filter {} -> allMatch={}", qnaMap, filterMap, allMatch);

        return allMatch;
    }

    public PretixEventFilter(PretixEventFilterCreateDTO dto) {
        this(dto.action(), dto.organizer(), dto.event(), dto.filterMap());
    }

    public PretixEventFilter(Long id, PretixEventFilterUpdateDTO dto) {
        this(id, dto.action(), dto.organizer(), dto.event(), dto.filterMap());
    }
}