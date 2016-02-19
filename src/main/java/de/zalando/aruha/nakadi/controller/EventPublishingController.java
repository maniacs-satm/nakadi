package de.zalando.aruha.nakadi.controller;

import com.codahale.metrics.annotation.Timed;
import de.zalando.aruha.nakadi.exceptions.NakadiException;
import de.zalando.aruha.nakadi.exceptions.NoSuchEventTypeException;
import de.zalando.aruha.nakadi.repository.EventTypeRepository;
import de.zalando.aruha.nakadi.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.zalando.problem.spring.web.advice.Responses.create;

@RestController
public class EventPublishingController {

    private static final Logger LOG = LoggerFactory.getLogger(EventPublishingController.class);

    private final TopicRepository topicRepository;
    private final EventTypeRepository eventTypeRepository;

    public EventPublishingController(final TopicRepository topicRepository,
            final EventTypeRepository eventTypeRepository) {
        this.topicRepository = topicRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    @Timed(name = "post_events", absolute = true)
    @RequestMapping(value = "/event-types/{eventTypeName}/events", method = POST)
    public ResponseEntity postEvent(@PathVariable final String eventTypeName, @RequestBody final String event,
            final NativeWebRequest nativeWebRequest) {
        LOG.trace("Received event {} for event type {}", event, eventTypeName);

        try {
            eventTypeRepository.findByName(eventTypeName);

            // Will be replaced later:
            final String partitionId = "1";
            topicRepository.postEvent(eventTypeName, partitionId, event);
            return status(HttpStatus.CREATED).build();
        } catch (NoSuchEventTypeException e) {
            LOG.debug("Could not process event.", e);
            return create(e.asProblem(), nativeWebRequest);
        } catch (final NakadiException e) {
            LOG.error("error posting to partition", e);
            return create(e.asProblem(), nativeWebRequest);
        }
    }

}