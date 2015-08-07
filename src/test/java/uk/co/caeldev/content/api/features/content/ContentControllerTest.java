package uk.co.caeldev.content.api.features.content;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.caeldev.content.api.builders.UserBuilder;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherService;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder.contentResourceBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;

@RunWith(MockitoJUnitRunner.class)
public class ContentControllerTest {

    @Mock
    private ContentResourceAssembler contentResourceAssembler;

    @Mock
    private ContentService contentService;

    @Mock
    private PublisherService publisherService;

    private ContentController contentController;

    @Before
    public void testee() {
        contentController = new ContentController(contentService, publisherService, contentResourceAssembler);
    }

    @Test
    public void shouldPublishContentForAPublisher() throws Exception {
        //Given
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUID = UUID.randomUUID().toString();
        final String content = string().next();
        final User user = UserBuilder.userBuilder().build();
        final String publisherId = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .id(publisherId)
                .publisherUUID(publisherUUID.toString())
                .username(user.getUsername())
                .build();

        given(publisherService.getPublisherByUUID(publisherUUID.toString())).willReturn(expectedPublisher);

        //And
        final Content expectedContent = ContentBuilder.contentBuilder().content(content).contentUUID(contentUUID).publisherId(publisherId).build();
        given(contentService.publish(content, expectedPublisher.getId())).willReturn(expectedContent);

        //And
        given(contentResourceAssembler.toResource(expectedContent)).willReturn(contentResourceBuilder().content(content).contentUUID(contentUUID).build());

        //When
        final ContentResource contentResource = contentResourceBuilder().content(content).build();
        final ResponseEntity<ContentResource> response = contentController.publish(publisherUUID, contentResource);

        //Then
        assertThat(response.getBody()).is(new Condition<ContentResource>() {
            @Override
            public boolean matches(ContentResource value) {
                return value.getContentUUID().equals(contentUUID) &&
                        value.getContent().equals(content);
            }
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
