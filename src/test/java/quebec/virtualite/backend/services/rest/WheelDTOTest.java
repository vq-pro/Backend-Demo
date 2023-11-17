package quebec.virtualite.backend.services.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static quebec.virtualite.backend.TestConstants.EMPTY_BRAND;
import static quebec.virtualite.backend.TestConstants.EMPTY_NAME;
import static quebec.virtualite.backend.TestConstants.NULL_BRAND;
import static quebec.virtualite.backend.TestConstants.NULL_NAME;
import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
import static quebec.virtualite.backend.services.utils.TestUtils.validateDTO;

@RunWith(MockitoJUnitRunner.class)
public class WheelDTOTest
{
    private static final int NO_ERRORS = 0;
    private static final int ONE_ERROR = 1;
    private static final int TWO_ERRORS = 2;

    @Test
    public void validate()
    {
        validateDTO(WHEEL_DTO, NO_ERRORS);
        validateDTO(new WheelDTO(EMPTY_BRAND, EMPTY_NAME), TWO_ERRORS);
    }

    @Test
    public void validateBrand()
    {
        validateDTO(WHEEL_DTO.withBrand(EMPTY_BRAND), ONE_ERROR);
        validateDTO(WHEEL_DTO.withBrand(NULL_BRAND), ONE_ERROR);
    }

    @Test
    public void validateName()
    {
        validateDTO(WHEEL_DTO.withName(EMPTY_NAME), ONE_ERROR);
        validateDTO(WHEEL_DTO.withName(NULL_NAME), ONE_ERROR);
    }
}
