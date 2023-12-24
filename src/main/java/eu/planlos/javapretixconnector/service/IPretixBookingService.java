package eu.planlos.javapretixconnector.service;

import eu.planlos.javapretixconnector.model.Booking;

public interface IPretixBookingService {
    Booking loadOrFetch(String organizer, String event, String code);

    void fetchAll(String organizer, String event);

    String getOrderUrl(String event, String orderCode);
}
