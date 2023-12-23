package eu.planlos.javapretixconnector.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StringToPretixEventFilterConverter {

    private final ObjectMapper objectMapper;

    public StringToPretixEventFilterConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<PretixEventFilter> convertAll(List<String> filterList) throws JsonProcessingException {

        if(filterList == null) {
            log.debug("Not converting: List is null");
            return new ArrayList<>();
        }

        List<PretixEventFilter> pretixEventFilterList = new ArrayList<>();
        for (String filter : filterList) {
            pretixEventFilterList.add(convert(filter));
        }
        return pretixEventFilterList;
    }

    private PretixEventFilter convert(String filterString) throws JsonProcessingException {
        PretixEventFilter filter = objectMapper.readValue(filterString, PretixEventFilter.class);
        log.debug("Converting: \"{}\" to \"{}\"", filterString, filter);
        return filter;
    }
}