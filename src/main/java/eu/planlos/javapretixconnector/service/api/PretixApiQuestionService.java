package eu.planlos.javapretixconnector.service.api;

import eu.planlos.javapretixconnector.common.PretixException;
import eu.planlos.javapretixconnector.config.PretixApiConfig;
import eu.planlos.javapretixconnector.model.PretixId;
import eu.planlos.javapretixconnector.model.dto.list.QuestionsDTO;
import eu.planlos.javapretixconnector.model.dto.single.QuestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PretixApiQuestionService extends PretixApiService {

    private static final String FETCH_MESSAGE = "Fetched question from Pretix: {}";

    public PretixApiQuestionService(PretixApiConfig config, @Qualifier("PretixWebClient") WebClient webClient) {
        super(config, webClient);
    }

    /*
     * Query
     */
    public List<QuestionDTO> queryAllQuestions(String event) {

        if(isAPIDisabled()) {
            return Collections.emptyList();
        }

        QuestionsDTO dto = webClient
                .get()
                .uri(questionListUri(event))
                .retrieve()
                .bodyToMono(QuestionsDTO.class)
                .retryWhen(Retry.fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval())))
                .doOnError(error -> log.error("Message fetching all questions from Pretix API: {}", error.getMessage()))
                .block();

        if (dto != null) {
            List<QuestionDTO> questionDTOList = new ArrayList<>(dto.results());
            questionDTOList.forEach(questionDTO -> log.info(FETCH_MESSAGE, questionDTO));
            return questionDTOList;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    public QuestionDTO queryQuestion(String event, PretixId questionId) {

        if(isAPIDisabled()) {
            return null;
        }

        QuestionDTO questionDto = webClient
                .get()
                .uri(specificQuestionUri(event, questionId.getIdValue()))
                .retrieve()
                .bodyToMono(QuestionDTO.class)
                .retryWhen(Retry.fixedDelay(config.retryCount(), Duration.ofSeconds(config.retryInterval())))
                .doOnError(error -> log.error("Message fetching question={} from Pretix API: {}", questionId, error.getMessage()))
                .block();
        if (questionDto != null) {
            log.info(FETCH_MESSAGE, questionDto);
            return questionDto;
        }

        throw new PretixException(PretixException.IS_NULL);
    }

    /*
     * Uri generators
     */
    private String specificQuestionUri(String event, Long questionId) {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/", event,
                "/questions/", questionId.toString(), "/");
    }

    private String questionListUri(String event) {
        return String.join(
                "",
                "api/v1/organizers/", config.organizer(),
                "/events/", event,
                "/questions/");
    }
}