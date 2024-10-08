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
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.rest.RestServerContract;
import quebec.virtualite.backend.services.rest.WheelDTO;

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
    @PutMapping(URL_ADD_WHEEL__PUT)
    @ResponseStatus(CREATED)
    public void addWheel(@RequestBody WheelDTO wheel)
    {
        domainService.addWheel(wheel.toEntity(0));
    }

    @Override
    @DeleteMapping(URL_DELETE_WHEEL)
    public void deleteWheel(@PathVariable String name)
    {
        domainService.deleteWheel(getWheel(name));
    }

    @Override
    @GetMapping(URL_GET_WHEEL)
    public WheelDTO getWheelDetails(@PathVariable String name)
    {
        return new WheelDTO(getWheel(name));
    }

    @Override
    @GetMapping(URL_GET_WHEELS)
    public List<WheelDTO> getWheelsDetails()
    {
        return transform(domainService.getWheels(),
            WheelDTO::new);
    }

    @Override
    @PostMapping(URL_UPDATE_WHEEL__POST)
    public void updateWheel(@PathVariable String name, @RequestBody WheelDTO wheel)
    {
        WheelEntity existingWheel = getWheel(name);
        WheelEntity updatedWheel = wheel.toEntity(existingWheel.id());

        domainService.updateWheel(updatedWheel);
    }

    @ExceptionHandler(WheelAlreadyExistsException.class)
    protected ResponseEntity<String> exceptionHandler()
    {
        return new ResponseEntity<>(CONFLICT);
    }

    private WheelEntity getWheel(String name)
    {
        return domainService.getWheel(name)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
