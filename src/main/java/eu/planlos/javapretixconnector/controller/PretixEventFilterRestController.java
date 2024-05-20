package eu.planlos.javapretixconnector.controller;

import eu.planlos.javapretixconnector.model.PretixEventFilter;
import eu.planlos.javapretixconnector.model.dto.PretixEventFilterCreateDTO;
import eu.planlos.javapretixconnector.model.dto.PretixEventFilterUpdateDTO;
import eu.planlos.javapretixconnector.service.PretixEventFilterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(PretixEventFilterRestController.URL_FILTER)
@Slf4j
public class PretixEventFilterRestController {

    public static final String URL_FILTER = "/api/v1/filter";

    private final PretixEventFilterService pretixEventFilterService;

    public PretixEventFilterRestController(PretixEventFilterService pretixEventFilterService) {
        this.pretixEventFilterService = pretixEventFilterService;
    }

    /*
     * CRUD
     */

    //TODO test
    /**
     * cURL example:
     * curl -v -s -X POST -H "Content-Type: application/json;charset=UTF-8" -d '{"action":"pretix.event.order.approved","event":"event","qna-list":{"test":["test"]}}' http://localhost:8080/api/v1/filter
     * @param pretixEventFilterDTO Transfer object, will be converted to business object
     * @return JSON representation
     */
    @PostMapping
    public ResponseEntity<PretixEventFilter> post(@Valid @RequestBody PretixEventFilterCreateDTO pretixEventFilterDTO) {
        PretixEventFilter pretixEventFilter = new PretixEventFilter(pretixEventFilterDTO);
        log.info("Create filter={}", pretixEventFilter);
        pretixEventFilterService.addFilter(pretixEventFilter);
        log.info("Created filter id={}", pretixEventFilter.getId());

        return ResponseEntity.ok().body(pretixEventFilter);
    }

    //TODO test
    /**
     * curl -s -X GET http://localhost:8080/api/v1/filter/1
     * @return JSON representation
     */
    @GetMapping
    public ResponseEntity<List<PretixEventFilter>> getAll() {
        log.info("Read all filters");
        return ResponseEntity.ok().body(pretixEventFilterService.getAllFilters());
    }

    //TODO test
    /**
     * cURL example:
     * curl -s -X GET http://localhost:8080/api/v1/filter/1
     * @param id of the filter
     * @return PretixEventFilter.class as JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<PretixEventFilter> get(@PathVariable Long id) {
        log.info("Read filter id={}", id);
        Optional<PretixEventFilter> optionalPretixEventFilter = pretixEventFilterService.getFilter(id);
        return optionalPretixEventFilter
                .map(pretixEventFilter -> ResponseEntity.ok().body(pretixEventFilter))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //TODO test
    @PutMapping("/{id}")
    public ResponseEntity<PretixEventFilter> put(@Valid @RequestBody PretixEventFilterUpdateDTO pretixEventFilterDTO, @PathVariable("id") Long id) {
        PretixEventFilter pretixEventFilter = new PretixEventFilter(id, pretixEventFilterDTO);
        log.info("Update filter={}", pretixEventFilter);
        pretixEventFilterService.updateFilter(pretixEventFilter);
        log.info("Updated filter id={}", pretixEventFilter.getId());
        return ResponseEntity.ok().body(pretixEventFilter);
    }

    //TODO test
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        log.info("Delete filter id={}", id);
        pretixEventFilterService.deleteFilter(id);
        log.info("Deleted filter id={}", id);
    }
}