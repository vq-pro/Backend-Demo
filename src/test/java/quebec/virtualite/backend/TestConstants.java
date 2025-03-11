package quebec.virtualite.backend;

import quebec.virtualite.backend.services.domain.entities.CityEntity;
import quebec.virtualite.backend.services.rest.CityDTO;

public interface TestConstants
{
    Long ID = 111L;
    Long ID2 = 112L;
    String NAME = "Name";
    String NAME2 = "Name2";
    String PROVINCE = "Province";
    String PROVINCE2 = "Province2";

    CityEntity CITY = new CityEntity(ID, NAME, PROVINCE);
    CityEntity CITY2 = new CityEntity(ID2, NAME2, PROVINCE2);
    CityEntity CITY_WITHOUT_ID = new CityEntity(0, NAME, PROVINCE);
    CityDTO CITY_DTO = new CityDTO(NAME, PROVINCE);

    CityDTO BAD_CITY_DTO = new CityDTO("", "");
}
