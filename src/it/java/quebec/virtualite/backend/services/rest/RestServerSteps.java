package quebec.virtualite.backend.services.rest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.utils.RestClient;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static quebec.virtualite.backend.security.TestUserDefinitions.TEST_PASSWORD;
import static quebec.virtualite.backend.security.TestUserDefinitions.TEST_USER;
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
    public List<WheelDefinition> readWheelsFromTable(DataTable table)
    {
        assertThat(table.row(0)).isEqualTo(list("brand", "name"));

        return table.entries().stream()
            .map(row -> new WheelDefinition()
                .setBrand(row.get("brand"))
                .setName(row.get("name")))
            .collect(toList());
    }

    @Then("the new wheel is added")
    public void newWheelIsAdded()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_CREATED);
    }

    /**
     * Server Unit Test: {@link RestServerTest#deleteWheel()}
     */
    @When("^we delete the (.*)$")
    public void weDeleteWheel(String name)
    {
        rest.delete("/wheels/{name}", param("name", name));
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

    @When("we add a new wheel")
    public void weAddWheelLoginTest()
    {
        rest.put("/wheels", new WheelDTO());
    }

    /**
     * Server Unit Test: {@link RestServerTest#addWheel()}
     */
    @When("we add a new wheel:")
    public void weAddWheel(WheelDefinition newWheel)
    {
        rest.put("/wheels", new WheelDTO()
            .setBrand(newWheel.brand)
            .setName(newWheel.name));
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
    public void weAskForListOfWheels()
    {
        rest.get("/wheels");
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

    @Then("the wheel is deleted")
    public void wheelIsDeleted()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);
    }

    @Then("we get:")
    public void weGetTheWheelsDetails(DataTable expected)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);

        List<WheelDTO> response = list(rest.response().as(WheelDTO[].class));
        List<List<String>> list = list(
            list("brand", "name"));
        for (WheelDTO wheel : response)
        {
            list.add(list(wheel.getBrand(), wheel.getName()));
        }
        expected.diff(DataTable.create(list));
    }

    @When("^we change the (.*)'s name$")
    public void weUpdateWheelLoginTest(String name)
    {
        rest.post("/wheels/{name}", new WheelDTO(), param("name", name));
    }

    /**
     * Server Unit Test: {@link RestServerTest#updateWheel()}
     */
    @When("^we change the (.*)'s name to (.*)$")
    public void weUpdateWheel(String name, String newName)
    {
        WheelDTO existingWheel = getWheel(name);

        rest.post("/wheels/{name}",
            new WheelDTO()
                .setBrand(existingWheel.getBrand())
                .setName(newName),
            param("name", name));
    }

    @Given("we know about these wheels:")
    public void weKnowAboutTheseWheels(List<WheelDefinition> wheels)
    {
        wheels.forEach(row ->
            domainService.saveWheel(
                new WheelEntity()
                    .setBrand(row.getBrand())
                    .setName(row.getName())));
    }

    @Then("we should get a {int} error")
    public void weShouldGetAError(int errorCode)
    {
        assertThat(rest.response().statusCode()).isEqualTo(errorCode);
    }

    private WheelDTO getWheel(String name)
    {
        weAskForDetailsOf(name);
        return rest.response().as(WheelDTO.class);
    }

    @Data
    @Accessors(chain = true)
    public static class WheelDefinition
    {
        String brand;
        String name;
    }
}
