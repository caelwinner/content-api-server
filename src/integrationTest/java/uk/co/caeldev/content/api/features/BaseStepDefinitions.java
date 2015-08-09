package uk.co.caeldev.content.api.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ContextConfiguration(classes = ApplicationTest.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public abstract class BaseStepDefinitions {

    @Value("${local.server.port}")
    protected int port;

    @Value("${server.contextPath}")
    protected String basePath;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected WireMockServer wireMockServer = new WireMockServer(9080);

    protected void givenOauthServerMock(String accessToken, String userJson) {
        stubFor(get(urlEqualTo("/sso/user"))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withHeader(AUTHORIZATION, accessToken)
                        .withBody(userJson)));
    }
}