package quebec.virtualite.backend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.utils.RestClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static quebec.virtualite.backend.Application.TEST_PASSWORD;
import static quebec.virtualite.backend.Application.TEST_USER;
import static quebec.virtualite.backend.utils.RestParam.param;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration
@RunWith(SpringRunner.class)
public class RestServerOldSchoolIT
{
    @Autowired
    private DomainService domainService;

    @Autowired
    private RestClient rest;

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void _init()
    {
        rest._init(serverPort);
        domainService.deleteGreetings();
    }

    @Test
    public void getGreeting()
    {
        rest.login(TEST_USER, TEST_PASSWORD);
        rest.get("/v2/greetings/{name}", param("name", "Toto"));

        assertThat(rest.response().statusCode(), is(SC_OK));
        assertThat(rest.response().asString(), is(
            rest.trim("{" +
                      "  \"content\": \"Hello Toto!\"" +
                      "}")));
    }

    @Test
    public void getGreetingWhenNotLoggedIn()
    {
        rest.get("/v2/greetings/{name}", param("name", "Toto"));
        assertThat(rest.response().statusCode(), is(401));
    }
}
