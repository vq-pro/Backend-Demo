package quebec.virtualite.backend.services.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
        domainService.addWheel(new WheelEntity()
            .setBrand(wheelDTO.getBrand())
            .setName(wheelDTO.getName()));
    }

    @GetMapping("/wheels/{name}")
    public Mono<WheelDTO> getWheelDetails(@PathVariable String name)
    {
        WheelEntity wheel = wheelFromName(name);

        return Mono.just(
            new WheelDTO()
                .setBrand(wheel.getBrand())
                .setName(wheel.getName()));
    }

    @GetMapping("/wheels")
    public Flux<WheelDTO> getWheelsDetails()
    {
        return Flux.fromIterable(
            domainService.getWheelsDetails()
                .stream()
                .map(wheel ->
                    new WheelDTO()
                        .setBrand(wheel.getBrand())
                        .setName(wheel.getName()))
                .collect(toList()));
    }

    @PostMapping("/wheels/{name}")
    public void updateWheel(
        @PathVariable String name, @RequestBody WheelDTO wheelDTO)
    {
        domainService.updateWheel(wheelFromName(name)
            .setBrand(wheelDTO.getBrand())
            .setName(wheelDTO.getName()));
    }

    private WheelEntity wheelFromName(String name)
    {
        if (name == null || name.isEmpty())
        {
            log.warn("name is not specified");
            throw new ResponseStatusException(BAD_REQUEST);
        }

        return domainService.getWheelDetails(name)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
