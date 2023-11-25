package quebec.virtualite.backend.services.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
import static quebec.virtualite.backend.services.utils.TestUtils.validateDTO;

@RunWith(MockitoJUnitRunner.class)
public class WheelDTOTest
{
    @Test
    public void copy()
    {
        // When
        WheelDTO result = WHEEL_DTO.copy();

        // Then
        assertThat(result).isEqualTo(WHEEL_DTO);
    }

    @Test
    public void validate()
    {
        validateDTO(0, WHEEL_DTO);

        validateDTO(1, WHEEL_DTO.copy().setBrand(null));
        validateDTO(1, WHEEL_DTO.copy().setBrand(""));

        validateDTO(1, WHEEL_DTO.copy().setName(null));
        validateDTO(1, WHEEL_DTO.copy().setName(""));

        validateDTO(2, new WheelDTO());
    }
}
