package eu.planlos.javapretixconnector.model;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.planlos.javapretixconnector.PretixTestDataUtility.*;
import static eu.planlos.javapretixconnector.model.dto.PretixSupportedActions.ORDER_APPROVED;
import static org.junit.jupiter.api.Assertions.*;

class PretixEventFilterTest {

    @Test
    public void filtersCreatedEqually_areEqual() {
        assertEquals(newFilter(), newFilter());
    }

    @Test
    public void filterForActionAndEvent_isRecognized() {
        PretixEventFilter pretixEventFilter = new PretixEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, new HashMap<>());
        assertTrue(pretixEventFilter.isForAction(ORDER_APPROVED.getAction()));
        assertTrue(pretixEventFilter.isForEvent(EVENT));
    }

    @Test
    public void filtersNotCreatedEqually_areNotEqual() {
        assertNotEquals(newFilter(), newDifferentFilter());
    }

    @Test
    public void allQnaMatchFilter_isFiltered() {
        assertTrue(filterOK().filterQnA(correctQnaMap()));
    }

    @Test
    public void noQuestionsMatchFilter_isNotFiltered() {
        assertFalse(filterOK().filterQnA(allQuestionsMissingQnaMap()));
    }

    @Test
    public void noAnswerMatchFilter_isNotFiltered() {
        assertFalse(filterOK().filterQnA(noAnswerCorrectMap()));
    }

    @Test
    public void notAllQuestionsMatchFilter_isNotFiltered() {
        assertFalse(filterOK().filterQnA(missingQuestionQnaMap()));
    }

    @Test
    public void notAllAnswersMatchFilter_isNotFiltered() {
        assertFalse(filterOK().filterQnA(notAllAnswersCorrectMap()));
    }

    /*
     * Data helper
     */

    private PretixEventFilter newFilter() {
        return new PretixEventFilter(
                PretixSupportedActions.ORDER_NEED_APPROVAL.getAction(),
                ORGANIZER,
                EVENT,
                Map.of("Question?", List.of("Wrong Answer!", "Answer!")));
    }

    private PretixEventFilter newDifferentFilter() {
        return new PretixEventFilter(
                PretixSupportedActions.ORDER_NEED_APPROVAL.getAction(),
                ORGANIZER,
                EVENT,
                Map.of("Different Question?", List.of("Wrong Answer!", "Answer!")));
    }
}