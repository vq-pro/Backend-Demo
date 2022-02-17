package quebec.virtualite.backend.utils

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.apache.commons.lang3.StringUtils.isEmpty
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.springframework.stereotype.Component

@Component
class RestClient
{
    private val NON_BREAKING_SPACE = 0x00A0.toChar()
    private val XSRF_TOKEN = "XSRF-TOKEN"
    private val X_XSRF_TOKEN = "X-XSRF-TOKEN"

    private lateinit var response: Response
    private lateinit var username: String
    private lateinit var password: String

    fun connect(serverPort: Int)
    {
        RestAssured.port = serverPort
        clearUser()
    }

    operator fun get(url: String, param: RestParam)
    {
        val urlWithParam = setParam(url, param)
        response = requestForReads()[urlWithParam]
    }

    fun login(username: String, password: String)
    {
        this.username = username
        this.password = password
    }

    fun logout()
    {
        clearUser()
    }

    fun post(url: String, param: RestParam)
    {
        val urlWithParam = setParam(url, param)
        response = requestForWrites()
            .contentType(ContentType.JSON)
            .post(urlWithParam)
    }

    fun put(url: String, param: RestParam)
    {
        val urlWithParam = setParam(url, param)
        response = requestForWrites()
            .contentType(ContentType.JSON)
            .put(urlWithParam)
    }

    fun response(): Response
    {
        return response
    }

    fun trim(json: String): String
    {
        return json
            .replace(" ", "")
            .replace("\r", "")
            .replace("\n", "")
            .replace(NON_BREAKING_SPACE, ' ')
    }

    private fun clearUser()
    {
        username = ""
        password = ""
    }

    private val jSessionID: String
        get() = RestAssured.given()
            .auth().basic(username, password)["/user"]
            .sessionId

    private fun getToken(jSessionID: String): String
    {
        return RestAssured.given()
            .sessionId(jSessionID)
            .contentType(ContentType.JSON)["/user"]
            .cookie(XSRF_TOKEN)
    }

    private fun notIsLoggedIn(): Boolean
    {
        return isEmpty(username) || isEmpty(password)
    }

    private fun requestForReads(): RequestSpecification
    {
        return if (notIsLoggedIn()) RestAssured.given() else RestAssured.given()
            .auth()
            .basic(username, password)
    }

    private fun requestForWrites(): RequestSpecification
    {
        if (notIsLoggedIn())
            return RestAssured.given()

        val jSessionID = jSessionID
        val token = getToken(jSessionID)
        return RestAssured.given()
            .sessionId(jSessionID)
            .header(X_XSRF_TOKEN, token)
    }

    private fun setParam(url: String, param: RestParam): String
    {
        val paramName = "{" + param.key + "}"
        assertThat("Error in URL", url, containsString(paramName))

        return url.replace(paramName, "${param.value}")
    }
}
