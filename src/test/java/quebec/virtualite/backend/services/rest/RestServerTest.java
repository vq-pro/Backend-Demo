package quebec.virtualite.backend.services.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static quebec.virtualite.backend.TestConstants.BRAND;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.TestConstants.NULL_NAME;
import static quebec.virtualite.backend.TestConstants.WHEEL;
import static quebec.virtualite.utils.CollectionUtils.list;

@RunWith(MockitoJUnitRunner.class)
public class RestServerTest
{
    private static final String NEW_BRAND = "new brand";
    private static final String NEW_NAME = "new name";

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
    public void addWheel()
    {
        // When
        server.addWheel(new WheelDTO()
            .setBrand(BRAND)
            .setName(NAME));

        // Then
        verify(mockedDomainService).addWheel(WHEEL);
    }

    @Test
    public void getWheelDetails()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        Mono<WheelDTO> response = server.getWheelDetails(NAME);

        // Then
        verify(mockedDomainService).getWheelDetails(NAME);

        assertThat(response.block()).isEqualTo(
            new WheelDTO()
                .setBrand(BRAND)
                .setName(NAME));
    }

    @Test
    public void getWheelsDetails()
    {
        // Given
        given(mockedDomainService.getWheelsDetails())
            .willReturn(list(WHEEL));

        // When
        Flux<WheelDTO> response = server.getWheelsDetails();

        // Then
        verify(mockedDomainService).getWheelsDetails();

        assertThat(response.collectList().block()).isEqualTo(
            list(new WheelDTO()
                .setBrand(BRAND)
                .setName(NAME)));
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

    @Test
    public void updateWheel()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(Optional.of(new WheelEntity()
                .setBrand(BRAND)
                .setName(NAME)));

        // When
        server.updateWheel(NAME,
            new WheelDTO()
                .setBrand(NEW_BRAND)
                .setName(NEW_NAME));

        // Then
        verify(mockedDomainService).getWheelDetails(NAME);
        verify(mockedDomainService).updateWheel(new WheelEntity()
            .setBrand(NEW_BRAND)
            .setName(NEW_NAME));
    }

    @Test
    public void updateWheel_whenNameIsNull_log()
    {
        // When
        Throwable exception = catchThrowable(() ->
            server.updateWheel(NULL_NAME, new WheelDTO()));

        // Then
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", BAD_REQUEST);

        verify(mockedLogger).warn("name is not specified");
    }

    @Test
    public void updateWheel_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheelDetails(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() -> server.updateWheel(NAME, new WheelDTO()));

        // Then
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", NOT_FOUND);
    }
}
