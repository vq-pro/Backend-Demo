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
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import quebec.virtualite.backend.security.SecurityUsers.TEST_PASSWORD
import quebec.virtualite.backend.security.SecurityUsers.TEST_USER
import quebec.virtualite.backend.services.domain.DomainService
import quebec.virtualite.backend.services.domain.entities.WheelEntity
import quebec.virtualite.backend.utils.RestClient
import quebec.virtualite.backend.utils.RestParam.Companion.param
import java.util.*
import java.util.stream.Collectors.toList

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

    data class WheelDefinition(
        val brand: String,
        val name: String
    )

    @Before
    fun beforeEachScenario()
    {
        domainService.deleteAll()
    }

    @DataTableType
    fun readWheelsFromTable(table: DataTable): List<WheelDefinition>
    {
        assertThat(table.row(0)).isEqualTo(listOf("brand", "name"))
        return table.entries().stream()
            .map { row ->
                WheelDefinition(
                    row["brand"]!!,
                    row["name"]!!
                )
            }
            .collect(toList())
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
        rest.get("/wheels/{name}", param("name", name))
    }

    /**
     * Server Unit Test: [RestServerTest.getAllWheelDetails]
     */
    @When("we ask for the list of wheels")
    fun weAskForWheels()
    {
        rest.get("/wheels")
    }

    @Then("we get:")
    fun thenWeGetTheWheelDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_CREATED)

        val actualContents = ArrayList<List<String>>()
        actualContents.add(listOf("brand", "name"))
        val wheelResponses = rest.response()
            .body
            .`as`(Array<WheelResponse>::class.java)
            .asList()

        for (wheel in wheelResponses)
        {
            actualContents.add(listOf(wheel.brand, wheel.name))
        }

        val actual = DataTable.create(actualContents)
        expected.diff(actual)
    }

    @Then("we get the wheel details:")
    fun weGetTheWheelDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_OK)
        val response = rest.response().`as`(WheelResponse::class.java)
        val actual = DataTable.create(
            listOf(
                listOf("brand", response.brand),
                listOf("name", response.name)
            )
        )
        expected.diff(actual)
    }

    @Given("we know about these wheels:")
    fun weKnowAboutTheseWheels(wheels: List<WheelDefinition>)
    {
        wheels.forEach { row ->
            domainService.saveWheel(
                WheelEntity(0, row.brand, row.name)
            )
        }
    }

    @Then("we should get a {int} error")
    fun weShouldGetAError(errorCode: Int)
    {
        assertThat(rest.response().statusCode()).isEqualTo(errorCode)
    }
}
