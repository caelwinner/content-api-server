package uk.co.caeldev.content.api.features.publisher.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.Status;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class PublisherRepositoryImpl implements PublisherRepositoryBase {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PublisherRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Publisher findByUUIDAndUsername(String publisherUUID, String username) {
        final Query query = new Query(where("publisherUUID").is(publisherUUID)
                .andOperator(where("username").is(username)));

        return mongoTemplate.findOne(query, Publisher.class);
    }

    @Override
    public Publisher findByUUID(String publisherUUID) {
        final Query query = new Query(where("publisherUUID").is(publisherUUID));
        return mongoTemplate.findOne(query, Publisher.class);
    }

    @Override
    public Publisher findByUsername(String username) {
        final Query query = new Query(where("username").is(username));
        return mongoTemplate.findOne(query, Publisher.class);
    }

    @Override
    public Publisher updateStatus(String publisherUUID, Status status) {
        final Query query = new Query(where("publisherUUID").is(publisherUUID));

        Update update = new Update();
        update.set("status", status);

        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Publisher.class);
    }
}
