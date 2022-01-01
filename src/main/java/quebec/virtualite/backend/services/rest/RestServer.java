package quebec.virtualite.backend.services.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import quebec.virtualite.backend.services.domain.DomainService;
import quebec.virtualite.backend.services.domain.entities.WheelEntity;

import static java.lang.String.format;
import static org.h2.util.StringUtils.isNullOrEmpty;

@RestController
public class RestServer
{
    private static final Logger log = LoggerFactory.getLogger(RestServer.class);

    private final DomainService domainService;

    public RestServer(DomainService domainService)
    {
        this.domainService = domainService;
    }

    @GetMapping("/wheels/{name}")
    public ResponseEntity<WheelResponse> getWheelDetails(@PathVariable String name)
    {
        if (isNullOrEmpty(name))
        {
            log.warn("name is not specified");
            return ResponseEntity.badRequest().body(null);
        }

        WheelEntity wheel = domainService.getWheelDetails(name);

        return ResponseEntity.ok(
            new WheelResponse()
                .setMessage("Hello " + wheel.getBrand() + " " + wheel.getName() + "!"));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/v2/greetings/{name}")
    public GreetingResponse greet(@PathVariable String name)
    {
        log.warn("Greeting!");

        domainService.recordGreeting(name);

        return new GreetingResponse()
            .setContent(format("Hello %s!", name));
    }
}
