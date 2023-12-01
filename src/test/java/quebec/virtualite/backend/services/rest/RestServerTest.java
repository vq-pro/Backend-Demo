package quebec.virtualite.backend.services.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static quebec.virtualite.backend.TestConstants.ID;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.TestConstants.WHEEL;
import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
import static quebec.virtualite.backend.TestConstants.WHEEL_WITH_ID;
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
        server.addWheel(WHEEL_DTO);

        // Then
        verify(mockedDomainService).addWheel(WHEEL);
    }

    @Test
    public void deleteWheel()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.of(WHEEL_WITH_ID));

        // When
        server.deleteWheel(NAME);

        // Then
        verify(mockedDomainService).getWheel(NAME);
        verify(mockedDomainService).deleteWheel(WHEEL_WITH_ID);
    }

    @Test
    public void deleteWheel_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            server.deleteWheel(NAME));

        // Then
        verify(mockedDomainService, never()).deleteWheel(any(WheelEntity.class));

        assertStatus(exception, NOT_FOUND);
    }

    @Test
    public void getWheelDetails()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.of(WHEEL_WITH_ID));

        // When
        WheelDTO response = server.getWheelDetails(NAME);

        // Then
        verify(mockedDomainService).getWheel(NAME);

        assertThat(response).isEqualTo(WHEEL_DTO);
    }

    @Test
    public void getWheelDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            server.getWheelDetails(NAME));

        // Then
        assertStatus(exception, NOT_FOUND);
    }

    @Test
    public void getWheelsDetails()
    {
        // Given
        given(mockedDomainService.getWheels())
            .willReturn(list(WHEEL_WITH_ID));

        // When
        List<WheelDTO> response = server.getWheelsDetails();

        // Then
        verify(mockedDomainService).getWheels();

        assertThat(response).isEqualTo(list(WHEEL_DTO));
    }

    @Test
    public void updateWheel()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.of(WHEEL_WITH_ID));

        // When
        server.updateWheel(NAME,
            new WheelDTO(NEW_BRAND, NEW_NAME));

        // Then
        verify(mockedDomainService).getWheel(NAME);
        verify(mockedDomainService).updateWheel(
            new WheelEntity(ID, NEW_BRAND, NEW_NAME));
    }

    @Test
    public void updateWheel_whenNotFound()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            server.updateWheel(NAME, WHEEL_DTO));

        // Then
        assertStatus(exception, NOT_FOUND);
    }

    private static void assertStatus(Throwable exception, HttpStatus expectedStatus)
    {
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", expectedStatus);
    }
}
