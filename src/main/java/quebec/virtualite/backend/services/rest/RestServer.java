package quebec.virtualite.backend.services.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelAlreadyExistsException;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import quebec.virtualite.backend.services.domain.entities.WheelInvalidException;

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
    public void addWheel(@RequestBody WheelDTO wheelDTO)
    {
        try
        {
            domainService.addWheel(convert(wheelDTO));
        }
        catch (WheelAlreadyExistsException exception)
        {
            throw new ResponseStatusException(CONFLICT);
        }
        catch (WheelInvalidException exception)
        {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @DeleteMapping("/wheels/{name}")
    public void deleteWheel(@PathVariable String name)
    {
        domainService.deleteWheel(getWheel(name));
    }

    @GetMapping("/wheels/{name}")
    public WheelDTO getWheelDetails(@PathVariable String name)
    {
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

    private WheelEntity convert(WheelDTO dto)
    {
        return new WheelEntity()
            .setBrand(dto.getBrand())
            .setName(dto.getName());
    }

    private WheelDTO convert(WheelEntity wheel)
    {
        return new WheelDTO()
            .setBrand(wheel.getBrand())
            .setName(wheel.getName());
    }

    private WheelEntity getWheel(String name)
    {
        if (isNull(name) || isEmpty(name))
        {
            log.warn("name is not specified");
            throw new ResponseStatusException(BAD_REQUEST);
        }

        return domainService.getWheel(name)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
