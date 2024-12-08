package quebec.virtualite.backend.services.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface RestServerContract
{
    // --------------------------------------------------
    String URL_ADD_CITY__PUT = "/cities";

    void addCity(@Valid CityDTO city);

    // --------------------------------------------------
    String URL_DELETE_CITY = "/cities/{name}";

    void deleteCity(@NotBlank String name);

    // --------------------------------------------------
    String URL_GET_CITY = "/cities/{name}";

    CityDTO getCityDetails(@NotBlank String name);

    // --------------------------------------------------
    String URL_GET_CITIES = "/cities";

    List<CityDTO> getCitiesDetails();

    // --------------------------------------------------
    String URL_UPDATE_CITY__POST = "/cities/{name}";

    void updateCity(@NotBlank String name, @Valid CityDTO city);
}
