package quebec.virtualite.backend.services.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface RestServerContract
{
    // --------------------------------------------------
    String URL_ADD_WHEEL__PUT = "/wheels";

    void addWheel(@Valid WheelDTO wheel);

    // --------------------------------------------------
    String URL_DELETE_WHEEL = "/wheels/{name}";

    void deleteWheel(@NotBlank String name);

    // --------------------------------------------------
    String URL_GET_WHEEL = "/wheels/{name}";

    WheelDTO getWheelDetails(@NotBlank String name);

    // --------------------------------------------------
    String URL_GET_WHEELS = "/wheels";

    List<WheelDTO> getWheelsDetails();

    // --------------------------------------------------
    String URL_UPDATE_WHEEL__POST = "/wheels/{name}";

    void updateWheel(@NotBlank String name, @Valid WheelDTO wheel);
}
