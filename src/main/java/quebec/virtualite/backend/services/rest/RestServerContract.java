package quebec.virtualite.backend.services.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface RestServerContract
{
    String URL_ADD_CITY__PUT = "/cities";
    String URL_DELETE_CITY = "/cities/{name}";
    String URL_DELETE_CITY_WITHOUT_NAME = "/cities/";
    String URL_GET_CITY = "/cities/{name}";
    String URL_GET_CITIES = "/cities";
    String URL_UPDATE_CITY__POST = "/cities/{name}";
    String URL_UPDATE_CITY__POST_WITHOUT_NAME = "/cities/";

    void addCity(@Valid CityDTO city);

    void deleteCity(@NotBlank String name);

    List<CityDTO> getCitiesDetails();

    CityDTO getCityDetails(@NotBlank String name);

    void updateCity(@NotBlank String name, @Valid CityDTO city);
}
