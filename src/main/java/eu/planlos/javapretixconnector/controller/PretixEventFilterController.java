package eu.planlos.javapretixconnector.controller;

import eu.planlos.javapretixconnector.model.dto.PretixSupportedActions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PretixEventFilterController {

    @GetMapping("/pretixconnector/eventfilters")
    public String getAllEvents(Model model) {
        model.addAttribute("pretixSupportedActions", PretixSupportedActions.values());
        return "event-filters";
    }
}