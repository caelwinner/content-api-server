package uk.co.caeldev.content.api.features.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import uk.co.caeldev.content.api.config.*;
import uk.co.caeldev.content.api.features.publisher.config.MongoClientTestConfiguration;

@Configuration
@EnableAutoConfiguration(exclude = {MongoClientConfiguration.class, MvcConfiguration.class, MethodSecurityConfiguration.class})
@ComponentScan(basePackages = {"uk.co.caeldev.content.api.config"})
@EnableMongoRepositories(basePackages = {"uk.co.caeldev.content.api.features.*.repository"})
@Import({MongoClientTestConfiguration.class, MongoConfiguration.class, MongoSettings.class})
@ActiveProfiles("test")
public class TestRepositoryConfiguration {

}
