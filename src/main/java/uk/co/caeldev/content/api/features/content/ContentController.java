package uk.co.caeldev.content.api.features.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherService;
import uk.co.caeldev.spring.mvc.ResponseEntityBuilder;

import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.HttpStatus.*;

@RestController
@EnableOAuth2Resource
public class ContentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContentController.class);

    private final ContentService contentService;
    private final PublisherService publisherService;
    private final ContentResourceAssembler contentResourceAssembler;

    @Autowired
    public ContentController(final ContentService contentService,
                             final PublisherService publisherService,
                             final ContentResourceAssembler contentResourceAssembler) {
        this.contentService = contentService;
        this.publisherService = publisherService;
        this.contentResourceAssembler = contentResourceAssembler;
    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public ResponseEntity<ContentResource> publish(@PathVariable UUID publisherUUID,
                                                   @RequestBody ContentResource contentResource) {

        LOGGER.info("Publishing content");

        String content = contentResource.getContent();

        if (content.isEmpty()) {
            LOGGER.warn("Content is not Invalid");
            return ResponseEntityBuilder.
                    <ContentResource>responseEntityBuilder()
                    .statusCode(BAD_REQUEST)
                    .build();
        }

        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID.toString());

        final Content publishedContent = contentService.publish(content, publisher.getId());

        return ResponseEntityBuilder.
                <ContentResource>responseEntityBuilder()
                .statusCode(CREATED)
                .entity(contentResourceAssembler.toResource(publishedContent))
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents/{contentUUID}",
            method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public ResponseEntity<ContentResource> getContent(@PathVariable String contentUUID,
                                                      @PathVariable String publisherUUID) {
        LOGGER.info("get content");

        final Content content = contentService.findOneByUUID(contentUUID);
        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID);

        checkNotNull(content, "No content with given UUID");
        checkNotNull(publisher, "No publisher with given UUID");

        if (!Objects.equals(content.getPublisherId(), publisher.getId())) {
            LOGGER.warn("Content forbidden");
            return ResponseEntityBuilder.
                    <ContentResource>responseEntityBuilder()
                    .statusCode(FORBIDDEN)
                    .build();
        }

        return ResponseEntityBuilder.
                <ContentResource>responseEntityBuilder()
                .statusCode(OK)
                .entity(contentResourceAssembler.toResource(content))
                .build();

    }
}
