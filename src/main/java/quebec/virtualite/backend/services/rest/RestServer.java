package quebec.virtualite.backend.services.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import quebec.virtualite.backend.services.domain.DomainService;

import static org.h2.util.StringUtils.isNullOrEmpty;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class RestServer
{
    private final DomainService domainService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/wheels/{name}")
    public ResponseEntity<WheelResponse> getWheelDetails(@PathVariable String name)
    {
        if (isNullOrEmpty(name))
        {
            log.warn("name is not specified");
            return ResponseEntity.badRequest().build();
        }

        return domainService.getWheelDetails(name)
            .map(wheel ->
                ResponseEntity.ok().body(
                    new WheelResponse()
                        .setBrand(wheel.getBrand())
                        .setName(wheel.getName())))
            .orElse(ResponseEntity.status(NOT_FOUND).build());
    }
}
