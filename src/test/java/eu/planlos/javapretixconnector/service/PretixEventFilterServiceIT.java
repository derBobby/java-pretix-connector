package eu.planlos.javapretixconnector.service;

import eu.planlos.javapretixconnector.TestContextConfiguration;
import eu.planlos.javapretixconnector.model.PretixEventFilter;
import eu.planlos.javapretixconnector.model.dto.PretixEventFilterUpdateDTO;
import eu.planlos.javapretixconnector.repository.PretixEventFilterRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static eu.planlos.javapretixconnector.PretixTestDataUtility.*;
import static eu.planlos.javapretixconnector.model.dto.PretixSupportedActions.ORDER_APPROVED;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestContextConfiguration.class)
public class PretixEventFilterServiceIT {

    @Autowired
    private PretixEventFilterRepository pretixEventFilterRepository;

    private PretixEventFilterService pretixEventFilterService;

    @BeforeEach
    public void setUp() {
        pretixEventFilterService = new PretixEventFilterService(pretixEventFilterRepository, filterOKList());
    }

    /*
     * CRUD tests
     */

    @Test
    public void createWithGivenId_throwsException() {
        PretixEventFilterUpdateDTO pretixEventFilterUpdateDTO = updateFilterOK(1L);
        PretixEventFilter updatePretixEventFilter = new PretixEventFilter(pretixEventFilterUpdateDTO);
        assertThrows(IllegalArgumentException.class, () -> pretixEventFilterService.addFilter(updatePretixEventFilter));
    }

    @Test
    public void createAndFindFilter_successful() {
        PretixEventFilter givenPretixEventFilter = filterOK();
        pretixEventFilterService.addFilter(givenPretixEventFilter);
        PretixEventFilter databasePretixEventFilter = pretixEventFilterService.getFilter(givenPretixEventFilter.getId()).get();
        assertEquals(databasePretixEventFilter, givenPretixEventFilter);
    }

    @Test
    public void createAndDeleteFilter_successful() {
        PretixEventFilter pretixEventFilter = filterOK();
        pretixEventFilterService.addFilter(pretixEventFilter);
        pretixEventFilterRepository.flush();
        assertTrue(pretixEventFilterService.getFilter(pretixEventFilter.getId()).isPresent());

        pretixEventFilterService.deleteFilter(pretixEventFilter.getId());
        pretixEventFilterRepository.flush();
        assertFalse(pretixEventFilterService.getFilter(pretixEventFilter.getId()).isPresent());
    }

    @Test
    public void createAndUpdateFilter_successfull() {
        PretixEventFilter originalpretixEventFilter = filterOK();
        pretixEventFilterService.addFilter(originalpretixEventFilter);
        pretixEventFilterRepository.flush();

        PretixEventFilterUpdateDTO pretixEventFilterUpdateDTO = updateFilterOK(originalpretixEventFilter.getId());
        PretixEventFilter updatePretixEventFilter = new PretixEventFilter(pretixEventFilterUpdateDTO);
        pretixEventFilterService.updateFilter(updatePretixEventFilter);
        pretixEventFilterRepository.flush();

        assertEquals(pretixEventFilterService.getFilter(originalpretixEventFilter.getId()).get(), updatePretixEventFilter);
    }

    /*
     * Violation tests
     */

    @Test
    public void duplicateAnswer_throwsException() {
        pretixEventFilterService.addFilter(filterWithDuplicateAnswer());
        assertFlushResultsInConstraingViolation();
    }

    @Test
    public void invalidAction_throwsException() {
        pretixEventFilterService.addFilter(filterWithInvalidAction());
        assertFlushResultsInConstraingViolation();
    }

    /*
     * Filter tests on booking level
     */

    @Test
    public void ticketBooking_matchesFilter() {
        assertFalse(pretixEventFilterService.bookingNotWantedByAnyFilter(ORDER_APPROVED, ticketBooking()));
    }

    @Test
    public void addonBooking_doesntMatchFilter() {
        assertTrue(pretixEventFilterService.bookingNotWantedByAnyFilter(ORDER_APPROVED, addonBooking()));
    }

    @Test
    public void noPositionBooking_doesntMatchFilter() {
        assertTrue(pretixEventFilterService.bookingNotWantedByAnyFilter(ORDER_APPROVED, noPositionsBooking()));
    }

    /*
     * Filter tests
     */

    @Test
    public void goodQnA_matchesFilter() {
        assertTrue(pretixEventFilterService.matchesEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, correctQnaMap()));
    }

    @Test
    public void missingQuestionQna_doesntMatchFilter() {
        assertFalse(pretixEventFilterService.matchesEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, missingQuestionQnaMap()));
    }

    @Test
    public void moreQnAThanInFilter_matchesFilter() {
        assertTrue(pretixEventFilterService.matchesEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, additionalQuestionsQnaMap()));
    }

    @Test
    public void noQuestionAnswered_isFalse() {
        assertFalse(pretixEventFilterService.matchesEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, allQuestionsMissingQnaMap()));
    }

    @Test
    public void notAllAnswersCorrect_isFalse() {
        assertFalse(pretixEventFilterService.matchesEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, notAllAnswersCorrectMap()));
    }

    @Test
    public void allAnswersWrong_isFalse() {
        assertFalse(pretixEventFilterService.matchesEventFilter(ORDER_APPROVED.getAction(), ORGANIZER, EVENT, noAnswerCorrectMap()));
    }

    /*
     * Assert helpers
     */

    private void assertFlushResultsInConstraingViolation() {
        assertThrows(ConstraintViolationException.class, () -> pretixEventFilterRepository.flush());
    }
}