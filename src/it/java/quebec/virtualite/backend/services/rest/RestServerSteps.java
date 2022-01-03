package quebec.virtualite.backend.services.rest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.utils.RestClient;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD;
import static quebec.virtualite.backend.security.SecurityUsers.TEST_USER;
import static quebec.virtualite.backend.utils.RestParam.param;
import static quebec.virtualite.utils.CollectionUtils.list;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@CucumberContextConfiguration
public class RestServerSteps
{
    private final DomainService domainService;
    private final RestClient rest;

    public RestServerSteps(
        DomainService domainService,
        RestClient rest,
        @Value("${local.server.port}") int serverPort)
    {
        this.domainService = domainService;
        this.rest = rest;

        rest.connect(serverPort);
    }

    @Before
    public void beforeEachScenario()
    {
        domainService.deleteAll();
    }

    @Given("^we are logged in$")
    public void weAreLoggedIn()
    {
        rest.login(TEST_USER, TEST_PASSWORD);
    }

    @Given("^we are not logged in$")
    public void weAreNotLoggedIn()
    {
        // Nothing to do here
    }

    /**
     * Server Unit Test: {@link RestServerTest#getWheelDetails()}
     */
    @When("we ask for details for {string}")
    public void weAskForDetailsFor(String name)
    {
        rest.get("/wheels/{name}", param("name", name));
    }

    @Then("we get the wheel details:")
    public void weGetTheWheelDetails(DataTable expected)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);

        WheelResponse response = rest.response().as(WheelResponse.class);
        DataTable actual = DataTable.create(list(
            list("Brand", response.getBrand()),
            list("Name", response.getName())));

        expected.diff(actual);
    }

    @Given("we know about these wheels:")
    public void weKnowAboutTheseWheels(List<List<String>> rows)
    {
        assertThat(rows.get(0)).isEqualTo(List.of("brand", "name"));

        for (List<String> row : rows.subList(1, rows.size()))
        {
            String brand = row.get(0);
            String name = row.get(1);
            domainService.saveWheel(brand, name);
        }
    }

    @Then("we should get a {int} error")
    public void weShouldGetAError(int errorCode)
    {
        assertThat(rest.response().statusCode()).isEqualTo(errorCode);
    }
}
