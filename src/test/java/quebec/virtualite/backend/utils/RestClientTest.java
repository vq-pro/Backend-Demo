package quebec.virtualite.backend.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static quebec.virtualite.backend.utils.RestParam.param;

@ExtendWith(MockitoExtension.class)
class RestClientTest
{
    @InjectMocks
    private RestClient client;

    @Test
    void urlWithParams()
    {
        urlWithParams(
            "/a/b={b}", List.of(param("b", 1)),
            "/a/b=1");

        urlWithParams(
            "/a", List.of(param("b", 1)),
            "/a?b=1");

        urlWithParams(
            "/a", List.of(param("b", 1), param("c", 2)),
            "/a?b=1&c=2");

        urlWithParams(
            "/a/b={b}", List.of(param("b", 1), param("c", 2), param("d", 3), param("e", 4)),
            "/a/b=1?c=2&d=3&e=4");
    }

    private void urlWithParams(String urlStart, List<RestParam> params, String urlResult)
    {
        // When
        val url = client.urlWithParams(urlStart, params.toArray(new RestParam[0]));

        // Then
        assertThat(url).isEqualTo(urlResult);
    }
}