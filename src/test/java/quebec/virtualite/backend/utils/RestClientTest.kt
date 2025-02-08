package quebec.virtualite.backend.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
@Tag("UnitTest")
class RestClientTest
{
    @InjectMocks
    private lateinit var client: RestClient

    @Test
    fun urlWithParams()
    {
        urlWithParams(
            "/a/b={b}",
            arrayOf(RestParam("b", 1)),
            "/a/b=1"
        )

        urlWithParams(
            "/a",
            arrayOf(RestParam("b", 1)),
            "/a?b=1"
        )

        urlWithParams(
            "/a",
            arrayOf(RestParam("b", 1), RestParam("c", 2)),
            "/a?b=1&c=2"
        )

        urlWithParams(
            "/a/b={b}",
            arrayOf(RestParam("b", 1), RestParam("c", 2), RestParam("d", 3), RestParam("e", 4)),
            "/a/b=1?c=2&d=3&e=4"
        )
    }

    private fun urlWithParams(urlStart: String, params: Array<RestParam>, urlResult: String)
    {
        // When
        val url = client.urlWithParams(urlStart, params)

        // Then
        assertThat(url).isEqualTo(urlResult)
    }
}
