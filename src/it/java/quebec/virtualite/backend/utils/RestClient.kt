package quebec.virtualite.backend.utils

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.apache.commons.lang3.StringUtils.isEmpty
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

    fun delete(url: String, vararg params: RestParam)
    {
        response = requestForWrites()
            .delete(urlWithParams(url, params))
    }

    fun get(url: String, vararg params: RestParam)
    {
        response = requestForReads()[urlWithParams(url, params)]
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

    fun <T> post(url: String, payload: T, vararg params: RestParam)
    {
        response = requestForWrites()
            .contentType(ContentType.JSON)
            .body(payload)
            .post(urlWithParams(url, params))
    }

    fun <T> put(url: String, payload: T)
    {
        response = requestForWrites()
            .contentType(ContentType.JSON)
            .body(payload)
            .put(url)
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

    fun urlWithParams(url: String, params: Array<out RestParam>): String
    {
        var urlWithParam = url
        for (param in params)
        {
            urlWithParam = setParam(urlWithParam, param)
        }
        return urlWithParam
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
        return when
        {
            url.contains(paramName) -> url.replace(paramName, "${param.value}")

            else -> url +
                "${if (url.contains('?')) "&" else "?"}${param.key}=${param.value}"
        }
    }
}
