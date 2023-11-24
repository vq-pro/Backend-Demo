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
    private static final int NO_ERRORS = 0;
    private static final int ONE_ERROR = 1;
    private static final int TWO_ERRORS = 2;

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
        validateDTO(WHEEL_DTO, NO_ERRORS);
        validateDTO(new WheelDTO(), TWO_ERRORS);
    }

    @Test
    public void validateBrand()
    {
        validateDTO(WHEEL_DTO.copy().setBrand(null), ONE_ERROR);
        validateDTO(WHEEL_DTO.copy().setBrand(""), ONE_ERROR);
    }

    @Test
    public void validateName()
    {
        validateDTO(WHEEL_DTO.copy().setName(null), ONE_ERROR);
        validateDTO(WHEEL_DTO.copy().setName(""), ONE_ERROR);
    }
}
