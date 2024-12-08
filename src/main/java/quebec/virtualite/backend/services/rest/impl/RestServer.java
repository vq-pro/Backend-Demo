package quebec.virtualite.backend.services.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.CityAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.CityEntity;
import quebec.virtualite.backend.services.rest.CityDTO;
import quebec.virtualite.backend.services.rest.RestServerContract;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static quebec.virtualite.utils.CollectionUtils.transform;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class RestServer implements RestServerContract
{
    private final DomainService domainService;

    @Override
    @PutMapping(URL_ADD_CITY__PUT)
    @ResponseStatus(CREATED)
    public void addCity(@RequestBody CityDTO city)
    {
        domainService.addCity(city.toEntity(0));
    }

    @Override
    @DeleteMapping(URL_DELETE_CITY)
    public void deleteCity(@PathVariable String name)
    {
        domainService.deleteCity(getCity(name));
    }

    @Override
    @GetMapping(URL_GET_CITY)
    public CityDTO getCityDetails(@PathVariable String name)
    {
        return new CityDTO(getCity(name));
    }

    @Override
    @GetMapping(URL_GET_CITIES)
    public List<CityDTO> getCitiesDetails()
    {
        return transform(domainService.getCities(), CityDTO::new);
    }

    @Override
    @PostMapping(URL_UPDATE_CITY__POST)
    public void updateCity(@PathVariable String name, @RequestBody CityDTO city)
    {
        CityEntity existingCity = getCity(name);
        CityEntity updatedCity = city.toEntity(existingCity.id());

        domainService.updateCity(updatedCity);
    }

    @ExceptionHandler(CityAlreadyExistsException.class)
    protected ResponseEntity<String> exceptionHandlerWhenNotFound()
    {
        return new ResponseEntity<>(CONFLICT);
    }

    private CityEntity getCity(String name)
    {
        return domainService.getCity(name)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
