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
import quebec.virtualite.backend.TestConstants.BRAND
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.WHEEL_DTO
import quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD
import quebec.virtualite.backend.security.SecurityUsers.TEST_USER
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.utils.RestClient
import quebec.virtualite.backend.utils.RestParam.Companion.param
import quebec.virtualite.utils.CollectionUtils.transform
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
    fun readWheelsFromTable(table: DataTable): List<WheelDTO>
    {
        assertThat(table.row(0)).isEqualTo(listOf("brand", "name"))
        return transform(table.entries()) {
            WheelDTO(it["brand"], it["name"])
        }
    }

    @Then("the new wheel is added")
    fun thenNewWheelIsAdded()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_CREATED)
    }

    @Then("we get the wheel details:")
    fun thenWeGetTheWheelDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_OK)

        val response = rest.response().`as`(WheelDTO::class.java)

        expected.diff(
            DataTable.create(
                listOf(
                    listOf("brand", response.brand),
                    listOf("name", response.name),
                )
            )
        )
    }

    @Then("we get:")
    fun thenWeGetTheWheelsDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_OK)

        val response = rest.response()
            .body
            .`as`(Array<WheelDTO>::class.java)

        expected.diff(
            tableFrom(response, header("brand", "name"))
            { row(it.brand!!, it.name!!) }
        )
    }

    @Then("the wheel is deleted")
    fun thenWheelIsDeleted()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK)
    }

    @Then("the wheel is updated")
    fun thenWheelIsUpdated()
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK)
    }

    /**
     * Server Unit Test: [RestServerTest.addWheel]
     */
    @When("we add a new wheel:")
    fun weAddWheel(wheel: WheelDTO)
    {
        rest.put(URL_ADD_WHEEL__PUT, wheel)
    }

    @When("we add a new wheel")
    fun weAddWheel_forLoginTest()
    {
        weAddWheel(WHEEL_DTO)
    }

    @When("we add a new wheel with a blank name")
    fun weAddWheel_withBlankName()
    {
        weAddWheel(WHEEL_DTO.copy(name = ""))
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
     * Server Unit Test: [RestServerTest.getWheelDetails]
     */
    @When("^we ask for the (.*)'s details$")
    fun weAskForDetailsOf(name: String)
    {
        rest.get(URL_GET_WHEEL, param("name", name))
    }

    /**
     * Server Unit Test: [RestServerTest.getWheelsDetails]
     */
    @When("we ask for the list of wheels")
    fun weAskForWheels()
    {
        rest.get(URL_GET_WHEELS)
    }

    /**
     * Server Unit Test: [RestServerTest.deleteWheel]
     */
    @When("^we delete the (.*)$")
    fun weDeleteWheel(name: String)
    {
        rest.delete(URL_DELETE_WHEEL, param("name", name))
    }

    @When("we delete an empty wheel")
    fun weDeleteWheel_withEmptyName()
    {
        weDeleteWheel("")
    }

    /**
     * Definitions: [RestServerSteps.readWheelsFromTable]
     */
    @Given("we know about these wheels:")
    fun weKnowAboutTheseWheels(wheels: List<WheelDTO>)
    {
        wheels.forEach {
            domainService.addWheel(it.toEntity(0))
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
     * Server Unit Test: [RestServerTest.updateWheel]
     */
    @When("^we change the (.*)'s name to (.*)$")
    fun weUpdateWheel(name: String, newName: String)
    {
        val wheel = getWheel(name)

        rest.post(
            URL_UPDATE_WHEEL__POST,
            WheelDTO(wheel.brand, newName),
            param("name", name)
        )
    }

    @When("^we change the (.*)'s name$")
    fun weUpdateWheel_forLoginTest(name: String)
    {
        rest.post(
            URL_UPDATE_WHEEL__POST,
            WheelDTO(BRAND, name),
            param("name", name)
        )
    }

    @When("we update an empty wheel")
    fun weUpdateWheel_withEmptyName()
    {
        rest.post(
            URL_UPDATE_WHEEL__POST,
            WheelDTO(BRAND, NAME),
            param("name", "")
        )
    }

    @When("^we blank the (.*)'s name$")
    fun weUpdateWheel_withInvalidPayload(name: String)
    {
        weUpdateWheel(name, "")
    }

    private fun getWheel(name: String): WheelDTO
    {
        weAskForDetailsOf(name)
        return rest.response().`as`(WheelDTO::class.java)
    }
}
