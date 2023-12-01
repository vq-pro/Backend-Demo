package quebec.virtualite.backend.services.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface RestServerContract
{
    // ----------------------------------
    String URL_PUT_ADD_WHEEL = "/wheels";
    // ----------------------------------
    String URL_DELETE_WHEEL = "/wheels/{name}";
    // ----------------------------------
    String URL_GET_WHEEL = "/wheels/{name}";
    // ----------------------------------
    String URL_GET_ALL_WHEELS = "/wheels";
    // ----------------------------------
    String URL_POST_UPDATE_WHEEL = "/wheels/{name}";

    void addWheel(@Valid WheelDTO wheel);

    void deleteWheel(@NotBlank String name);

    WheelDTO getWheelDetails(@NotBlank String name);

    List<WheelDTO> getWheelsDetails();

    void updateWheel(
        @NotBlank String name,
        @Valid WheelDTO wheel);
}
