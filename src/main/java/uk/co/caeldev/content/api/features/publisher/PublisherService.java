package uk.co.caeldev.content.api.features.publisher;

public interface PublisherService {
    Publisher getPublisherByUUIDAndUsername(String publisherUUID, String username);

    Publisher getPublisherByUUID(String publisherUUID);

    Publisher create(String username, String firstName, String lastName, String email);

    Publisher getPublisherByUsername(String username);

    void delete(String publisherUUID);

    Publisher update(Publisher publisherToBeUpdated);
}
