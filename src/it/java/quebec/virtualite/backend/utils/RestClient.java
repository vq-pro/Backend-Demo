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

    private Response response;

    private String username;
    private String password;

    public void connect(int serverPort)
    {
        RestAssured.port = serverPort;
        clearUser();
    }

    public void get(String url, RestParam param)
    {
        url = setParam(url, param);

        response = requestForReads()
            .get(url);
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

    public void post(String url, RestParam param)
    {
        url = setParam(url, param);

        response = requestForWrites()
            .contentType(JSON)
            .post(url);
    }

    public void put(String url, RestParam param)
    {
        url = setParam(url, param);

        response = requestForWrites()
            .contentType(JSON)
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

        return given()
            .sessionId(getJSessionID());
    }

    private String setParam(String url, RestParam param)
    {
        String paramName = "{" + param.key + "}";
        assertThat("Error in URL", url, containsString(paramName));

        return url.replace(paramName, String.valueOf(param.value));
    }
}
