package quebec.virtualite.backend.services.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import quebec.virtualite.backend.services.utils.AbstractDTOTest;

import static quebec.virtualite.backend.TestConstants.BRAND;
import static quebec.virtualite.backend.TestConstants.EMPTY_BRAND;
import static quebec.virtualite.backend.TestConstants.EMPTY_NAME;
import static quebec.virtualite.backend.TestConstants.NAME;
import static quebec.virtualite.backend.TestConstants.NULL_BRAND;
import static quebec.virtualite.backend.TestConstants.NULL_NAME;

@RunWith(MockitoJUnitRunner.class)
public class WheelDTOTest extends AbstractDTOTest
{
    private static final int NO_ERRORS = 0;
    private static final int ONE_ERROR = 1;
    private static final int TWO_ERRORS = 2;

    @Test
    public void validate()
    {
        validate(BRAND, NAME, NO_ERRORS);

        validate(EMPTY_BRAND, NAME, ONE_ERROR);
        validate(NULL_BRAND, NAME, ONE_ERROR);

        validate(BRAND, EMPTY_NAME, ONE_ERROR);
        validate(BRAND, NULL_NAME, ONE_ERROR);

        validate(EMPTY_BRAND, EMPTY_NAME, TWO_ERRORS);
    }

    private void validate(String brand, String name, int expectedErrors)
    {
        validate(
            new WheelDTO()
                .setBrand(brand)
                .setName(name),
            expectedErrors);
    }
}
