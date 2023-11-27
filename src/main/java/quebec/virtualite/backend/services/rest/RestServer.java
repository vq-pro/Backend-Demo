package quebec.virtualite.backend.services.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static quebec.virtualite.backend.services.rest.WheelDTO.toWheelDTO;

@RestController
@RequiredArgsConstructor
@Validated
public class RestServer
{
    private final DomainService domainService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PutMapping("/wheels")
    @ResponseStatus(CREATED)
    public void addWheel(@RequestBody @Valid WheelDTO wheel)
    {
        domainService.addWheel(wheel.toEntity());
    }

    @DeleteMapping("/wheels/{name}")
    public void deleteWheel(@PathVariable @NotBlank String name)
    {
        domainService.deleteWheel(getWheel(name));
    }

    @GetMapping("/wheels/{name}")
    public WheelDTO getWheelDetails(@PathVariable @NotBlank String name)
    {
        return toWheelDTO(getWheel(name));
    }

    @GetMapping("/wheels")
    public List<WheelDTO> getWheelsDetails()
    {
        return domainService.getWheels()
            .stream()
            .map(WheelDTO::toWheelDTO)
            .collect(toList());
    }

    @PostMapping("/wheels/{name}")
    public void updateWheel(
        @PathVariable @NotBlank String name,
        @RequestBody @Valid WheelDTO wheel)
    {
        WheelEntity existingWheel = getWheel(name);
        WheelEntity updatedWheel = wheel
            .toEntity()
            .setId(existingWheel.getId());

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
