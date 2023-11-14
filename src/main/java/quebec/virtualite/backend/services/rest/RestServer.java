package quebec.virtualite.backend.services.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class RestServer
{
    private final DomainService domainService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PutMapping("/wheels")
    @ResponseStatus(CREATED)
    public void addWheel(@RequestBody @Valid WheelDTO wheel)
    {
        try
        {
            domainService.addWheel(convert(wheel));
        }
        catch (WheelAlreadyExistsException exception)
        {
            throw new ResponseStatusException(CONFLICT);
        }
    }

    @DeleteMapping("/wheels/{name}")
    public void deleteWheel(@PathVariable String name)
    {
        validateName(name);

        domainService.deleteWheel(getWheel(name));
    }

    @GetMapping("/wheels/{name}")
    public WheelDTO getWheelDetails(@PathVariable String name)
    {
        validateName(name);

        return convert(getWheel(name));
    }

    @GetMapping("/wheels")
    public List<WheelDTO> getWheelsDetails()
    {
        return domainService.getWheels()
            .stream()
            .map(this::convert)
            .collect(toList());
    }

    @PostMapping("/wheels/{name}")
    public void updateWheel(@PathVariable String name, @RequestBody WheelDTO dto)
    {
        validateName(name);
        validateWheel(dto);

        WheelEntity existingWheel = getWheel(name);
        WheelEntity updatedWheel = convert(dto).setId(existingWheel.getId());

        domainService.saveWheel(updatedWheel);
    }

    private WheelEntity convert(WheelDTO dto)
    {
        return new WheelEntity()
            .setBrand(dto.getBrand())
            .setName(dto.getName());
    }

    private WheelDTO convert(WheelEntity entity)
    {
        return new WheelDTO()
            .setBrand(entity.getBrand())
            .setName(entity.getName());
    }

    private WheelEntity getWheel(String name)
    {
        return domainService.getWheel(name)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    private void validateName(String name)
    {
        if (isNull(name) || isEmpty(name))
        {
            log.warn("name is not specified");
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    private void validateWheel(WheelDTO dto)
    {
        if (isNull(dto)
            || isEmpty(dto.getBrand())
            || isEmpty(dto.getName()))
        {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }
}
