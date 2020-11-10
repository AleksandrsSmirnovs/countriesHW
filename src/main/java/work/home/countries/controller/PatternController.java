package work.home.countries.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/countries/pattern")
public class PatternController {

    private static final Logger log = LoggerFactory.getLogger(PatternController.class);
    private final CountryService service;

    public PatternController(CountryService service) {
        this.service = service;
    }

    @GetMapping("{pattern}")
    public List<CountryPatternResponse> getCountriesByPattern(@PathVariable String pattern) {
        log.info("Received request: find countries matching pattern \"{}\"", pattern);
        return service.getCountriesByPattern(pattern);
    }
}
