package eu.planlos.javapretixconnector.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PretixEventFilterConfigTest {

    @Test
    public void noSourceConfigured_returnsAppropriate() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("", null);
        assertTrue(config.isPropertiesSourceConfigured());
    }

    @Test
    public void propertiesSourceConfigured_returnsAppropriate() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("properties", "");
        assertTrue(config.isPropertiesSourceConfigured());
        assertFalse(config.isUserSourceConfigured());
    }

    @Test
    public void userSourceConfigured_returnsAppropriate() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("user", "");
        assertFalse(config.isPropertiesSourceConfigured());
        assertTrue(config.isUserSourceConfigured());
    }

    @Test
    public void filterListIsEmptyString_producesEmptyList() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("properties", "");
        assertTrue(config.getFilterList().isEmpty());
    }

    @Test
    public void filterListIsNull_producesEmptyList() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("properties", null);
        assertTrue(config.getFilterList().isEmpty());
    }

    @Test
    public void filterListContainsTwo_producesTwoEntries() {
        PretixEventFilterConfig config = new PretixEventFilterConfig("properties",
                "{\"action\": \"pretix.event.order.placed\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}}||" +
                        "{\"action\": \"pretix.event.order.placed.require_approval\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}}");
        assertEquals(2, config.getFilterList().size());
    }

    @Test
    public void filterListContainsBrokenJson_producesTwoEntries() {
        new PretixEventFilterConfig("properties", "{\"action\": \"pretix.event.order.placed\", \"organizer\": \"organizer\", \"event\": \"event\", \"qna-list\": {}");
    }
}