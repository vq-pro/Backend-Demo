package quebec.virtualite.backend.services.rest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.utils.RestClient;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
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

    @DataTableType
    public List<WheelDTO> readWheelsFromTable(DataTable table)
    {
        assertThat(table.row(0)).isEqualTo(list("brand", "name"));

        return table.entries().stream()
            .map(row -> new WheelDTO()
                .setBrand(row.get("brand"))
                .setName(row.get("name")))
            .collect(toList());
    }

    /**
     * Server Unit Test: {@link RestServerTest#addWheel()}
     */
    @When("we add a new wheel:")
    public void weAddWheel(WheelDTO wheel)
    {
        rest.put("/wheels", wheel);
    }

    @When("we add a new wheel")
    public void weAddWheel_forLoginTest()
    {
        rest.put("/wheels", WHEEL_DTO);
    }

    @When("we add a new wheel with a blank name")
    public void weAddWheel_withBlankName()
    {
        weAddWheel(WHEEL_DTO.copy().setName(""));
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

    @When("we ask for an empty wheel's details")
    public void weAskForDetailsOf_withEmptyWheel()
    {
        weAskForDetailsOf("");
    }

    /**
     * Server Unit Test: {@link RestServerTest#getWheelDetails()}
     */
    @When("^we ask for the (.*)'s details$")
    public void weAskForDetailsOf(String name)
    {
        rest.get("/wheels/{name}", param("name", name));
    }

    /**
     * Server Unit Test: {@link RestServerTest#getWheelsDetails()}
     */
    @When("we ask for the list of wheels")
    public void weAskForWheels()
    {
        rest.get("/wheels");
    }

    /**
     * Server Unit Test: {@link RestServerTest#updateWheel()}
     */
    @When("^we change the (.*)'s name to (.*)$")
    public void weChangeWheel(String name, String newName)
    {
        WheelDTO wheel = getWheel(name);

        rest.post("/wheels/{name}",
            wheel.setName(newName),
            param("name", name));
    }

    @When("^we change the (.*)'s name$")
    public void weChangeWheel_forLoginTest(String name)
    {
        rest.post("/wheels/{name}", WHEEL_DTO, param("name", name));
    }

    @When("we update an empty wheel")
    public void weChangeWheel_whenEmpty()
    {
        rest.post("/wheels/{name}",
            WHEEL_DTO,
            param("name", ""));
    }

    @When("^we blank the (.*)'s name$")
    public void weChangeWheel_withBlankNewName(String existingName)
    {
        weChangeWheel(existingName, "");
    }

    /**
     * Read there: {@link #readWheelsFromTable(DataTable)}
     */
    @Given("we know about these wheels:")
    public void weKnowAboutTheseWheels(List<WheelDTO> wheels)
    {
        wheels.forEach(wheel ->
            domainService.addWheel(wheel.toEntity(0)));
    }

    /**
     * Server Unit Test: {@link RestServerTest#deleteWheel()}
     */
    @When("^we delete the (.*)$")
    public void weDeleteWheel(String name)
    {
        rest.delete("/wheels/{name}", param("name", name));
    }

    @When("we delete an empty wheel")
    public void weDeleteWhen_whenEmpty()
    {
        weDeleteWheel("");
    }

    @Then("we get the wheel details:")
    public void weGetTheWheelDetails(DataTable expected)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);

        WheelDTO response = rest.response().as(WheelDTO.class);
        DataTable actual = DataTable.create(list(
            list("brand", response.getBrand()),
            list("name", response.getName())));

        expected.diff(actual);
    }

    @Then("we get:")
    public void weGetTheWheelsDetails(DataTable expected)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);

        List<WheelDTO> response = list(rest.response().as(WheelDTO[].class));
        List<List<String>> wheelTable = list(list("brand", "name"));
        response.forEach(wheel ->
            wheelTable.add(list(wheel.getBrand(), wheel.getName())));

        expected.diff(DataTable.create(wheelTable));
    }

    @Then("^we should get a (.*) \\((.*)\\) error$")
    public void weShouldGetAError(String errorStatus, int errorCode)
    {
        try
        {
            assertThat(HttpStatus.valueOf(errorStatus).value())
                .withFailMessage(errorStatus + " does not match code " + errorCode)
                .isEqualTo(errorCode);
            assertThat(rest.response().statusCode()).isEqualTo(errorCode);

        }
        catch (IllegalArgumentException e)
        {
            fail(errorStatus + " is not a valid HttpStatus");
        }
    }

    @Then("the new wheel is added")
    public void wheelIsAdded()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_CREATED);
    }

    @Then("the wheel is deleted")
    public void wheelIsDeleted()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);
    }

    @Then("the wheel is updated")
    public void wheelIsUpdated()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);
    }

    private WheelDTO getWheel(String name)
    {
        weAskForDetailsOf(name);
        return rest.response().as(WheelDTO.class);
    }
}
