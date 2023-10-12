package quebec.virtualite.backend.services.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.TestConstants;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class RestServerTest implements TestConstants
{
    @InjectMocks
    private RestServer server;

    @Mock
    private DomainService mockedDomainService;

    @Mock
    private Logger mockedLogger;

    @Before
    public void before()
    {
        setField(server, "log", mockedLogger);
    }

    @Test
    public void getWheelDetails()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(Optional.of(new WheelEntity()
                .setBrand(BRAND)
                .setName(NAME)));

        // When
        WheelResponse response = server.getWheelDetails(NAME).block();

        // Then
        verify(mockedDomainService).getWheelDetails(NAME);

        assertThat(response).isEqualTo(
            new WheelResponse()
                .setBrand(BRAND)
                .setName(NAME));
    }

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @Test
    public void getWheelDetails_whenNameIsNull_log()
    {
        // When
        Throwable exception = catchThrowable(() -> server.getWheelDetails(NULL_NAME));

        // Then
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", BAD_REQUEST);

        verify(mockedLogger).warn("name is not specified");
    }

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @Test
    public void getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() -> server.getWheelDetails(NAME));

        // Then
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", NOT_FOUND);
    }
}
