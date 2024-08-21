package eu.planlos.javapretixconnector.controller;

import eu.planlos.javapretixconnector.model.Booking;
import eu.planlos.javapretixconnector.service.PretixBookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(BookingRestController.URL_FILTER)
@Slf4j
public class BookingRestController {

    public static final String URL_FILTER = "/api/v1/booking";

    private final PretixBookingService pretixBookingService;

    public BookingRestController(PretixBookingService pretixBookingService) {
        this.pretixBookingService = pretixBookingService;
    }

    /*
     * CRUD
     */

    //TODO test
    /**
     * curl -s -X GET http://localhost:8080/api/v1/booking
     * @return JSON representation
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        log.info("Read all filters");
        return ResponseEntity.ok().body(pretixBookingService.loadAllLocal());
    }
}