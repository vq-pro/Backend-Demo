package quebec.virtualite.backend.services.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quebec.virtualite.backend.services.domain.DomainService;

import static java.lang.String.format;

@RestController
@RequestMapping("/v2")
@Slf4j
public class RestServer
{
    @Autowired
    private DomainService domainService;

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/greetings/{name}")
    public GreetingResponse greet(@PathVariable String name)
    {
        log.warn("Greeting!");

        domainService.recordGreeting(name);

        return new GreetingResponse()
            .setContent(format("Hello %s!", name));
    }
}
