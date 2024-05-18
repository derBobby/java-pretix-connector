package eu.planlos.javapretixconnector.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PretixEventFilterController {

    @GetMapping("/pretixconnector/eventfilters")
    public String getAllEvents() {
        return "event-filters";
    }
}