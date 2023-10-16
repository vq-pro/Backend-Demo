package quebec.virtualite.backend.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@Component
public class RestClient
{
    private static final char NON_BREAKING_SPACE = (char) 0x00A0;
    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";

    private Response response;

    private String username;
    private String password;

    public void connect(int serverPort)
    {
        RestAssured.port = serverPort;
        clearUser();
    }

    public void delete(String url, RestParam... params)
    {
        response = requestForWrites()
            .delete(urlWithParams(url, params));
    }

    public void get(String url, RestParam... params)
    {
        response = requestForReads()
            .get(urlWithParams(url, params));
    }

    public void login(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public void logout()
    {
        clearUser();
    }

    public void post(String url, Object dto, RestParam... params)
    {
        response = requestForWrites()
            .contentType(JSON)
            .body(dto)
            .post(urlWithParams(url, params));
    }

    public void put(String url, Object dto)
    {
        response = requestForWrites()
            .contentType(JSON)
            .body(dto)
            .put(url);
    }

    public Response response()
    {
        return response;
    }

    public String trim(String json)
    {
        return json
            .replace(" ", "")
            .replace("\r", "")
            .replace("\n", "")
            .replace(NON_BREAKING_SPACE, ' ');
    }

    private void clearUser()
    {
        username = "";
        password = "";
    }

    private String getJSessionID()
    {
        return given()
            .auth().basic(username, password)
            .get("/user")
            .getSessionId();
    }

    private String getToken(String jSessionID)
    {
        return given()
            .sessionId(jSessionID)
            .contentType(JSON)
            .get("/user")
            .cookie(XSRF_TOKEN);
    }

    private boolean notIsLoggedIn()
    {
        return isEmpty(username) || isEmpty(password);
    }

    private RequestSpecification requestForReads()
    {
        if (notIsLoggedIn())
            return given();

        return given()
            .auth()
            .basic(username, password);
    }

    private RequestSpecification requestForWrites()
    {
        if (notIsLoggedIn())
            return given();

        String jSessionID = getJSessionID();
        String token = getToken(jSessionID);

        return given()
            .sessionId(jSessionID)
            .header(X_XSRF_TOKEN, token);
    }

    private String setParam(String url, RestParam param)
    {
        String paramName = "{" + param.key + "}";
        assertThat("Error in URL", url, containsString(paramName));

        return url.replace(paramName, String.valueOf(param.value));
    }

    private String urlWithParams(String url, RestParam[] params)
    {
        for (RestParam param : params)
        {
            url = setParam(url, param);
        }
        return url;
    }
}
