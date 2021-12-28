package quebec.virtualite.backend.services.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import quebec.virtualite.backend.services.domain.DomainService;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RestServerTest
{
    private static final String NAME = "name";

    @Mock
    private DomainService mockedDomainService;

    @InjectMocks
    private RestServer server;

    @Test
    public void greet()
    {
        // When
        GreetingResponse response = server.greet(NAME);

        // Then
        verify(mockedDomainService).recordGreeting(NAME);

        assertThat(response.getContent(), is("Hello " + NAME + "!"));
    }
}
