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
import quebec.virtualite.backend.TestConstants.BRAND
import quebec.virtualite.backend.TestConstants.NAME
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
                    row["brand"],
                    row["name"]
                )
            }
            .collect(toList())
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
        val actual = DataTable.create(
            listOf(
                listOf("brand", response.brand),
                listOf("name", response.name),
            )
        )
        expected.diff(actual)
    }

    @Then("we get:")
    fun thenWeGetTheWheelsDetails(expected: DataTable)
    {
        assertThat(rest.response().statusCode).isEqualTo(SC_OK)

        val actualContents = ArrayList<List<String>>()
        actualContents.add(listOf("brand", "name"))
        val wheelResponses = rest.response()
            .body
            .`as`(Array<WheelDTO>::class.java)
            .asList()

        for (wheel in wheelResponses)
        {
            actualContents.add(listOf(wheel.brand!!, wheel.name!!))
        }

        val actual = DataTable.create(actualContents)
        expected.diff(actual)
    }

    @Then("we should get a {int} error")
    fun thenWeShouldGetAError(errorCode: Int)
    {
        assertThat(rest.response().statusCode()).isEqualTo(errorCode)
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

    /**
     * Server Unit Test: [RestServerTest.addWheel]
     */
    @When("we add a new wheel:")
    fun weAddWheel(wheel: WheelDefinition)
    {
        rest.put("/wheels", WheelDTO(wheel.brand, wheel.name))
    }

    @When("we add a new wheel")
    fun weAddWheelForLoginTest()
    {
        weAddWheel(WheelDefinition(BRAND, NAME))
    }

    /**
     * Server Unit Test: [RestServerTest.deleteWheel]
     */
    @When("^we delete the (.*)$")
    fun weDeleteWheel(name: String)
    {
        rest.delete("/wheels/{name}", param("name", name))
    }

    @Given("we know about these wheels:")
    fun weKnowAboutTheseWheels(wheels: List<WheelDefinition>)
    {
        wheels.forEach { row ->
            domainService.saveWheel(
                WheelEntity(0, row.brand!!, row.name!!)
            )
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
            "/wheels/{name}",
            WheelDTO(wheel.brand, newName),
            param("name", name)
        )
    }

    @When("^we change the (.*)'s name$")
    fun weUpdateWheelForLoginTest(name: String)
    {
        rest.post(
            "/wheels/{name}",
            WheelDTO(BRAND, name),
            param("name", name)
        )
    }

    private fun getWheel(name: String): WheelDTO
    {
        weAskForDetailsOf(name)
        return rest.response().`as`(WheelDTO::class.java)
    }

    data class WheelDefinition(
        val brand: String?,
        val name: String?
    )
}
