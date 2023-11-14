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
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static quebec.virtualite.backend.TestConstants.BRAND;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.TestConstants.NULL_BRAND;
import static quebec.virtualite.backend.TestConstants.NULL_NAME;
import static quebec.virtualite.backend.TestConstants.WHEEL;
import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
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
    public void addWheel_withDuplicate_conflict()
    {
        // Given
        doThrow(WheelAlreadyExistsException.class)
            .when(mockedDomainService)
            .addWheel(WHEEL);

        // When
        Throwable exception = catchThrowable(() ->
            server.addWheel(WHEEL_DTO));

        // Then
        assertStatus(exception, CONFLICT);
    }

    @Test
    public void deleteWheel()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        server.deleteWheel(NAME);

        // Then
        verify(mockedDomainService).getWheel(NAME);
        verify(mockedDomainService).deleteWheel(WHEEL);
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
        verify(mockedDomainService, never()).deleteWheel(WHEEL);

        assertStatus(exception, NOT_FOUND);
    }

    @Test
    public void getWheelDetails()
    {
        // Given
        given(mockedDomainService.getWheel(NAME))
            .willReturn(Optional.of(WHEEL));

        // When
        WheelDTO response = server.getWheelDetails(NAME);

        // Then
        verify(mockedDomainService).getWheel(NAME);

        assertThat(response).isEqualTo(WHEEL_DTO);
    }

    @Test
    public void getWheelDetails_whenNameIsNull_log()
    {
        // When
        Throwable exception = catchThrowable(() ->
            server.getWheelDetails(NULL_NAME));

        // Then
        verify(mockedLogger).warn("name is not specified");

        assertStatus(exception, BAD_REQUEST);
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
            .willReturn(list(WHEEL));

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
            .willReturn(Optional.of(new WheelEntity()
                .setBrand(BRAND)
                .setName(NAME)));

        // When
        server.updateWheel(NAME, new WheelDTO()
            .setBrand(NEW_BRAND)
            .setName(NEW_NAME));

        // Then
        verify(mockedDomainService).getWheel(NAME);
        verify(mockedDomainService).saveWheel(
            new WheelEntity()
                .setBrand(NEW_BRAND)
                .setName(NEW_NAME));
    }

    @Test
    public void updateWheel_whenNameIsNull_log()
    {
        // When
        Throwable exception = catchThrowable(() ->
            server.updateWheel(NULL_NAME, WHEEL_DTO));

        // Then
        verify(mockedLogger).warn("name is not specified");

        assertStatus(exception, BAD_REQUEST);
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

    @Test
    public void updateWheel_withNullField_exception()
    {
        updateWheel_withNullField(NULL_BRAND, NAME);
        updateWheel_withNullField(BRAND, NULL_NAME);
        updateWheel_withNullField(NULL_BRAND, NULL_NAME);
    }

    @Test
    public void updateWheel_withNullPayload_exception()
    {
        // When
        Throwable exception = catchThrowable(() ->
            server.updateWheel(NAME, null));

        // Then
        verify(mockedDomainService, never()).saveWheel(any(WheelEntity.class));

        assertStatus(exception, BAD_REQUEST);
    }

    private static void assertStatus(Throwable exception, HttpStatus expectedStatus)
    {
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", expectedStatus);
    }

    private void updateWheel_withNullField(String brand, String name)
    {
        // Given
        WheelDTO wheel = new WheelDTO()
            .setBrand(brand)
            .setName(name);

        // When
        Throwable exception = catchThrowable(() ->
            server.updateWheel(NAME, wheel));

        // Then
        verify(mockedDomainService, never()).saveWheel(any(WheelEntity.class));

        assertStatus(exception, BAD_REQUEST);
    }
}
