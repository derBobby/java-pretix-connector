package eu.planlos.javapretixconnector.runner;

import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.config.PretixFeatureConfig;
import eu.planlos.javapretixconnector.model.dto.single.EventDTO;
import eu.planlos.javapretixconnector.service.PretixBookingService;
import eu.planlos.javapretixconnector.service.ProductService;
import eu.planlos.javapretixconnector.service.QuestionService;
import eu.planlos.javapretixconnector.service.api.PretixApiEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreloadPretixDataRunner implements ApplicationRunner {

    private final PretixFeatureConfig pretixFeatureConfig;
    private final PretixApiConfig pretixApiConfig;

    private final ProductService productService;
    private final QuestionService questionService;
    private final PretixBookingService pretixBookingService;
    private final PretixApiEventService pretixApiEventService;

    public PreloadPretixDataRunner(PretixFeatureConfig pretixFeatureConfig, PretixApiConfig pretixApiConfig, ProductService productService, QuestionService questionService, PretixBookingService pretixBookingService, PretixApiEventService pretixApiEventService) {
        this.pretixFeatureConfig = pretixFeatureConfig;
        this.pretixApiConfig = pretixApiConfig;
        this.productService = productService;
        this.questionService = questionService;
        this.pretixBookingService = pretixBookingService;
        this.pretixApiEventService = pretixApiEventService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PreloadPretixDataRunner.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) {

        if(!pretixFeatureConfig.preloadEventDataEnabled()) {
            return;
        }

        logSeparator("Starting Preload");

        pretixApiEventService.fetchAllEvents()
                .stream()
                .filter(EventDTO::live)
                .forEach(eventDTO -> preload(pretixApiConfig.organizer(), eventDTO.slug()));

        logSeparator("Preload complete");
    }

    private void preload(String organizer, String eventSlug) {

        log.info("   Preloading event={}", eventSlug);

        questionService.fetchAll(eventSlug);
        productService.fetchAll(eventSlug);
        if(pretixFeatureConfig.preloadOrdersEnabled()) {
            log.info("   Preloading orders for event={}", eventSlug);
            pretixBookingService.fetchAll(organizer, eventSlug);
        }
    }

    private static void logSeparator(String text) {
        log.info("##########################################################################");
        log.info("#   {}", text);
        log.info("##########################################################################");
    }
}