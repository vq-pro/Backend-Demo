package quebec.virtualite.backend.services.rest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.rest.impl.RestServerTest;
import quebec.virtualite.backend.utils.RestClient;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static quebec.virtualite.backend.TestConstants.CITY_DTO;
import static quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD;
import static quebec.virtualite.backend.security.SecurityUsers.TEST_USER;
import static quebec.virtualite.backend.services.rest.RestServerContract.URL_ADD_CITY__PUT;
import static quebec.virtualite.backend.services.rest.RestServerContract.URL_DELETE_CITY;
import static quebec.virtualite.backend.services.rest.RestServerContract.URL_GET_CITIES;
import static quebec.virtualite.backend.services.rest.RestServerContract.URL_GET_CITY;
import static quebec.virtualite.backend.services.rest.RestServerContract.URL_UPDATE_CITY__POST;
import static quebec.virtualite.backend.utils.RestParam.param;
import static quebec.virtualite.utils.CollectionUtils.map;
import static quebec.virtualite.utils.CucumberUtils.header;
import static quebec.virtualite.utils.CucumberUtils.row;
import static quebec.virtualite.utils.CucumberUtils.tableFrom;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@CucumberContextConfiguration
@RequiredArgsConstructor
public class RestServerSteps
{
    private final DomainService domainService;
    private final RestClient rest;

    @Value("${local.server.port}")
    private Integer serverPort;

    @Before
    public void beforeEachScenario()
    {
        rest.connect(serverPort);
        domainService.deleteAll();
    }

    @Then("the new city is added")
    public void cityIsAdded()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_CREATED);
    }

    @Then("the city is deleted")
    public void cityIsDeleted()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);
    }

    @Then("the city is updated")
    public void cityIsUpdated()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);
    }

    @DataTableType
    public List<CityDTO> readCitiesFromTable(DataTable table)
    {
        assertThat(table.row(0))
            .isEqualTo(header("name", "province"));

        return map(table.entries(),
            row -> new CityDTO(
                row.get("name"),
                row.get("province")));
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
     * Server Unit Test: {@link RestServerTest#addCity()}
     */
    @When("we add a new city:")
    public void weAddCity(CityDTO city)
    {
        rest.put(URL_ADD_CITY__PUT, city);
    }

    @When("we add a new city")
    public void weAddCity_forLoginTest()
    {
        rest.put(URL_ADD_CITY__PUT, CITY_DTO);
    }

    @When("we add a new city with a blank name")
    public void weAddCity_withBlankName()
    {
        weAddCity(CITY_DTO.withName(""));
    }

    /**
     * Server Unit Test: {@link RestServerTest#getCitiesDetails()}
     */
    @When("we ask for the list of cities")
    public void weAskForCities()
    {
        rest.get(URL_GET_CITIES);
    }

    /**
     * Server Unit Test: {@link RestServerTest#getCityDetails()}
     */
    @When("^we ask for (.*)'s details$")
    public void weAskForDetailsOf(String name)
    {
        name = checkForEmpty(name);

        rest.get(URL_GET_CITY, param("name", name));
    }

    /**
     * Server Unit Test: {@link RestServerTest#updateCity()}
     */
    @When("^we change the name of (.*) to (.*)$")
    public void weChangeCity(String name, String newName)
    {
        CityDTO city = getCity(name);

        rest.post(URL_UPDATE_CITY__POST,
            city.withName(newName),
            param("name", name));
    }

    /**
     * Server Unit Test: {@link RestServerTest#deleteCity()}
     */
    @When("^we delete (.*)$")
    public void weDeleteCity(String name)
    {
        name = checkForEmpty(name);

        rest.delete(URL_DELETE_CITY, param("name", name));
    }

    @Then("we get:")
    public void weGetTheCitiesDetails(DataTable expected)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);

        expected.diff(
            tableFrom(List.of(rest.response().as(CityDTO[].class)),
                header("name", "province"),
                city -> row(city.getName(), city.getProvince())));
    }

    @Then("we get the city details:")
    public void weGetTheCityDetails(DataTable expected)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK);

        CityDTO response = rest.response().as(CityDTO.class);

        expected.diff(
            tableFrom(
                row("name", response.getName()),
                row("province", response.getProvince())));
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

    /**
     * Read there: {@link #readCitiesFromTable(DataTable)}
     */
    @Given("we know about these cities:")
    public void weKnowAboutTheseCities(List<CityDTO> cities)
    {
        cities.forEach(city ->
            domainService.addCity(city.toEntity(0)));
    }

    @When("^we update (.*)$")
    public void weUpdateCity_forLoginTest(String name)
    {
        name = checkForEmpty(name);

        rest.post(URL_UPDATE_CITY__POST, CITY_DTO, param("name", name));
    }

    @When("^we blank the name of (.*)$")
    public void weUpdateCity_withBlankNewName(String existingName)
    {
        weChangeCity(existingName, "");
    }

    private String checkForEmpty(String name)
    {
        return "an empty city".equals(name)
               ? ""
               : name;
    }

    private CityDTO getCity(String name)
    {
        weAskForDetailsOf(name);
        return rest.response().as(CityDTO.class);
    }
}
