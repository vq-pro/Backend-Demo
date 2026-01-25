package quebec.virtualite.backend.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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

    public void delete(String url, RestParam... params)
    {
        response = securedRequest()
            .delete(urlWithParams(url, params));
    }

    public void get(String url, RestParam... params)
    {
        response = securedRequest()
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
        response = securedRequest()
            .contentType(JSON)
            .body(dto)
            .post(urlWithParams(url, params));
    }

    public void put(String url, Object dto)
    {
        response = securedRequest()
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

    protected String urlWithParams(String url, RestParam[] params)
    {
        for (RestParam param : params)
        {
            url = setParam(url, param);
        }

        return url;
    }

    private void clearUser()
    {
        username = "";
        password = "";
    }

    private boolean notIsLoggedIn()
    {
        return isEmpty(username) || isEmpty(password);
    }

    private RequestSpecification securedRequest()
    {
        if (notIsLoggedIn())
            return given();

        return given()
            .auth()
            .basic(username, password);
    }

    private String setParam(String url, RestParam param)
    {
        String paramName = "{" + param.key + "}";
        String paramValue = String.valueOf(param.value);
        char separator = url.contains("?")
                         ? '&'
                         : '?';

        return url.contains(paramName)
               ? url.replace(paramName, paramValue)
               : String.format("%s%c%s=%s", url, separator, param.key, paramValue);
    }
}
