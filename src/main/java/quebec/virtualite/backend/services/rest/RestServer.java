package quebec.virtualite.backend.services.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import quebec.virtualite.backend.services.domain.DomainService;
import reactor.core.publisher.Mono;

import static org.h2.util.StringUtils.isNullOrEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class RestServer
{
    private final DomainService domainService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/wheels/{name}")
    public Mono<WheelResponse> getWheelDetails(@PathVariable String name)
    {
        if (isNullOrEmpty(name))
        {
            log.warn("name is not specified");
            throw new ResponseStatusException(BAD_REQUEST);
        }

        return Mono.just(domainService.getWheelDetails(name)
            .map(wheel ->
                new WheelResponse()
                    .setBrand(wheel.getBrand())
                    .setName(wheel.getName()))
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }
}
