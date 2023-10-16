package quebec.virtualite.backend.services.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import quebec.virtualite.backend.TestConstants;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static quebec.virtualite.utils.CollectionUtils.list;

@RunWith(MockitoJUnitRunner.class)
public class RestServerTest implements TestConstants
{
    private static final String NULL_NAME = null;

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
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.of(new WheelEntity()
                .setBrand(BRAND)
                .setName(NAME)));

        // When
        ResponseEntity<WheelResponse> response = server.getWheelDetails(NAME);

        // Then
        verify(mockedDomainService).getWheel(NAME);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(
            new WheelResponse()
                .setBrand(BRAND)
                .setName(NAME));
    }

    @Test
    public void getWheelDetails_whenNameIsNull_log()
    {
        // When
        ResponseEntity<WheelResponse> response = server.getWheelDetails(NULL_NAME);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        verify(mockedLogger).warn("name is not specified");
    }

    @Test
    public void getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.empty());

        // When
        ResponseEntity<WheelResponse> response = server.getWheelDetails(NAME);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void getWheelsDetails()
    {
        // Given
        given(mockedDomainService.getWheels())
            .willReturn(list(new WheelEntity()
                .setBrand(BRAND)
                .setName(NAME)));

        // When
        ResponseEntity<List<WheelResponse>> response =
            server.getWheelsDetails();

        // Then
        verify(mockedDomainService).getWheels();

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(
            list(new WheelResponse()
                .setBrand(BRAND)
                .setName(NAME)));
    }
}
