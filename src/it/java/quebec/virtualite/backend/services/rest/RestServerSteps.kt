package quebec.virtualite.backend.services.rest

import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
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
import quebec.virtualite.backend.utils.RestParam.param
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
    fun weAskForDetailsOf(name: String?)
    {
        rest["/wheels/{name}", param("name", name)]
    }

    @Then("we get the wheel details:")
    fun weGetTheWheelDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode()).isEqualTo(SC_OK)
        val response = rest.response().`as`(WheelResponse::class.java)
        val actual = DataTable.create(
            listOf(
                listOf("Brand", response.brand),
                listOf("Name", response.name)
            )
        )
        expected.diff(actual)
    }

    @Given("we know about these wheels:")
    fun weKnowAboutTheseWheels(wheels: List<WheelDefinition>)
    {
        wheels.forEach { row ->
            domainService.saveWheel(
                WheelEntity()
                    .setBrand(row.brand)
                    .setName(row.name)
            )
        }
    }

    @Then("we should get a {int} error")
    fun weShouldGetAError(errorCode: Int)
    {
        assertThat(rest.response().statusCode()).isEqualTo(errorCode)
    }
}
