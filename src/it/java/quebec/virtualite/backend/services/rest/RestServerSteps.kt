package quebec.virtualite.backend.services.rest

import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_OK
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.HttpStatus
import quebec.virtualite.backend.TestConstants.CITY_DTO
import quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD
import quebec.virtualite.backend.security.SecurityUsers.TEST_USER
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.utils.RestClient
import quebec.virtualite.backend.utils.RestParam.Companion.param
import quebec.virtualite.utils.CollectionUtils.map
import quebec.virtualite.utils.CucumberUtils.header
import quebec.virtualite.utils.CucumberUtils.row
import quebec.virtualite.utils.CucumberUtils.tableFrom
import java.util.*

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
class RestServerSteps(
    private val domainService: DomainService,
    private val rest: RestClient,
    @Value("\${local.server.port}") serverPort: Int
)
{
    init
    {
        rest.connect(serverPort)
    }

    @Before
    fun beforeEachScenario()
    {
        domainService.deleteAll()
    }

    @DataTableType
    fun readCitiesFromTable(table: DataTable): List<CityDTO>
    {
        assertThat(table.row(0)).isEqualTo(listOf("name", "province"))
        return map(table.entries()) { row ->
            CityDTO(row["name"], row["province"])
        }
    }

    @Then("the new city is added")
    fun thenNewCityIsAdded()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_CREATED)
    }

    @Then("we get the city details:")
    fun thenWeGetTheCityDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_OK)

        val response = rest.response().`as`(CityDTO::class.java)

        expected.diff(
            DataTable.create(
                listOf(
                    listOf("name", response.name),
                    listOf("province", response.province),
                )
            )
        )
    }

    @Then("we get:")
    fun thenWeGetTheCitiesDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_OK)

        expected.diff(
            tableFrom(
                rest.response().body.`as`(Array<CityDTO>::class.java),
                header("name", "province")
            )
            { row(it.name!!, it.province!!) }
        )
    }

    @Then("the city is deleted")
    fun thenCityIsDeleted()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK)
    }

    @Then("the city is updated")
    fun thenCityIsUpdated()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK)
    }

    /**
     * Server Unit Test: [RestServerTest.addCity]
     */
    @When("we add a new city:")
    fun weAddCity(city: CityDTO)
    {
        rest.put(URL_ADD_CITY__PUT, city)
    }

    @When("we add a new city")
    fun weAddCity_forLoginTest()
    {
        weAddCity(CITY_DTO)
    }

    @When("we add a new city with a blank name")
    fun weAddCity_withBlankName()
    {
        weAddCity(CITY_DTO.copy(name = ""))
    }

    @Given("^we are logged in$")
    fun weAreLoggedIn()
    {
        rest.login(TEST_USER, TEST_PASSWORD)
    }

    @Given("^we are not logged in$")
    fun weAreNotLoggedIn()
    {
        // Nothing to do here
    }

    /**
     * Server Unit Test: [RestServerTest.getCityDetails]
     */
    @When("^we ask for (.*)'s details$")
    fun weAskForDetailsOf(name: String)
    {
        rest.get(URL_GET_CITY, param("name", name))
    }

    /**
     * Server Unit Test: [RestServerTest.getCitiesDetails]
     */
    @When("we ask for the list of cities")
    fun weAskForCities()
    {
        rest.get(URL_GET_CITIES)
    }

    /**
     * Server Unit Test: [RestServerTest.deleteCity]
     */
    @When("^we delete (.*)$")
    fun weDeleteCity(name: String)
    {
        rest.delete(URL_DELETE_CITY, param("name", checkForEmpty(name)))
    }

    /**
     * Definitions: [RestServerSteps.readCitiesFromTable]
     */
    @Given("we know about these cities:")
    fun weKnowAboutTheseCities(cities: List<CityDTO>)
    {
        cities.forEach {
            domainService.addCity(it.toEntity(0))
        }
    }

    @Then("^we should get a (.*) \\((.*)\\) error$")
    fun weShouldGetAError(errorStatus: String, errorCode: Int)
    {
        try
        {
            assertThat(HttpStatus.valueOf(errorStatus).value())
                .withFailMessage("$errorStatus does not match code $errorCode")
                .isEqualTo(errorCode)
            assertThat(rest.response().statusCode()).isEqualTo(errorCode)

        } catch (e: IllegalArgumentException)
        {
            fail<Any>("$errorStatus is not a valid HttpStatus")
        }
    }

    /**
     * Server Unit Test: [RestServerTest.updateCity]
     */
    @When("^we change the name of (.*) to (.*)$")
    fun weUpdateCity(name: String, newName: String)
    {
        val city = getCity(name)

        rest.post(
            URL_UPDATE_CITY__POST,
            CityDTO(newName, city.province), param("name", name)
        )
    }

    @When("^we update (.*)$")
    fun weUpdateCity_forLoginTest(name: String)
    {
        rest.post(
            URL_UPDATE_CITY__POST,
            CITY_DTO, param("name", checkForEmpty(name))
        )
    }

    @When("^we blank the name of (.*)$")
    fun weUpdateCity_withInvalidPayload(name: String)
    {
        weUpdateCity(name, "")
    }

    private fun checkForEmpty(name: String): String
    {
        return if ("an empty city".equals(name)) "" else name
    }

    private fun getCity(name: String): CityDTO
    {
        weAskForDetailsOf(name)
        return rest.response().`as`(CityDTO::class.java)
    }
}
