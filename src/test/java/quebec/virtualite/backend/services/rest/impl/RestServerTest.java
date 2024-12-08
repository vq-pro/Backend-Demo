package quebec.virtualite.backend.services.rest.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.CityEntity;
import quebec.virtualite.backend.services.rest.CityDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.reflect.Whitebox.setInternalState;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static quebec.virtualite.backend.TestConstants.BAD_CITY_DTO;
import static quebec.virtualite.backend.TestConstants.CITY;
import static quebec.virtualite.backend.TestConstants.CITY_DTO;
import static quebec.virtualite.backend.TestConstants.CITY_WITH_ID;
import static quebec.virtualite.backend.TestConstants.ID;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.services.utils.TestUtils.assertInvalid;
import static quebec.virtualite.backend.services.utils.TestUtils.assertStatus;
import static quebec.virtualite.backend.services.utils.TestUtils.assertValid;

@ExtendWith(MockitoExtension.class)
public class RestServerTest
{
    private static final String NEW_BRAND = "new brand";
    private static final String NEW_NAME = "new name";

    @InjectMocks
    private RestServer server;

    @Mock
    private DomainService mockedDomainService;

    @Mock
    private Logger mockedLog;

    @BeforeEach
    public void before()
    {
        setInternalState(RestServer.class, "log", mockedLog);
    }

    @Test
    public void addCity()
    {
        // When
        server.addCity(CITY_DTO);

        // Then
        verify(mockedDomainService).addCity(CITY);
    }

    @Test
    public void deleteCity()
    {
        // Given
        given(mockedDomainService.getCity(NAME))
            .willReturn(Optional.of(CITY_WITH_ID));

        // When
        server.deleteCity(NAME);

        // Then
        verify(mockedDomainService).getCity(NAME);
        verify(mockedDomainService).deleteCity(CITY_WITH_ID);
    }

    @Test
    public void getCitiesDetails()
    {
        // Given
        given(mockedDomainService.getCities())
            .willReturn(List.of(CITY_WITH_ID));

        // When
        List<CityDTO> response = server.getCitiesDetails();

        // Then
        verify(mockedDomainService).getCities();

        assertThat(response).isEqualTo(List.of(CITY_DTO));
    }

    @Test
    public void getCityDetails()
    {
        // Given
        given(mockedDomainService.getCity(NAME))
            .willReturn(Optional.of(CITY_WITH_ID));

        // When
        CityDTO response = server.getCityDetails(NAME);

        // Then
        verify(mockedDomainService).getCity(NAME);

        assertThat(response).isEqualTo(CITY_DTO);
    }

    @Test
    public void updateCity()
    {
        // Given
        given(mockedDomainService.getCity(NAME))
            .willReturn(Optional.of(CITY_WITH_ID));

        // When
        server.updateCity(NAME, new CityDTO(NEW_BRAND, NEW_NAME));

        // Then
        verify(mockedDomainService).getCity(NAME);
        verify(mockedDomainService).updateCity(
            new CityEntity(ID, NEW_BRAND, NEW_NAME));
    }

    @Test
    void deleteCity_whenNotFound()
    {
        // Given
        given(mockedDomainService.getCity(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            server.deleteCity(NAME));

        // Then
        verify(mockedDomainService, never()).deleteCity(any(CityEntity.class));

        assertStatus(exception, NOT_FOUND);
    }

    @Test
    void getCityDetails_whenNotFound()
    {
        // Given
        given(mockedDomainService.getCity(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            server.getCityDetails(NAME));

        // Then
        assertStatus(exception, NOT_FOUND);
    }

    @Test
    void updateCity_whenNotFound()
    {
        // Given
        given(mockedDomainService.getCity(NAME))
            .willReturn(Optional.empty());

        // When
        Throwable exception = catchThrowable(() ->
            server.updateCity(NAME, CITY_DTO));

        // Then
        assertStatus(exception, NOT_FOUND);
    }

    @Test
    void validate()
    {
        assertValid(server, "addCity", CITY_DTO);
        assertInvalid(server, "addCity", BAD_CITY_DTO);
        assertInvalid(server, "addCity", null);
        assertInvalid(server, "addCity", "");
        assertInvalid(server, "addCity", 10f);

        assertValid(server, "deleteCity", NAME);
        assertInvalid(server, "deleteCity", null);
        assertInvalid(server, "deleteCity", "");
        assertInvalid(server, "deleteCity", 10f);

        assertValid(server, "getCityDetails", NAME);
        assertInvalid(server, "getCityDetails", null);
        assertInvalid(server, "getCityDetails", "");
        assertInvalid(server, "getCityDetails", 10f);

        assertValid(server, "updateCity", NAME, CITY_DTO);
        assertInvalid(server, "updateCity", NAME, BAD_CITY_DTO);
        assertInvalid(server, "updateCity", "", CITY_DTO);
        assertInvalid(server, "updateCity", null, CITY_DTO);
        assertInvalid(server, "updateCity", "", CITY_DTO);
        assertInvalid(server, "updateCity", 10f, CITY_DTO);
    }
}
