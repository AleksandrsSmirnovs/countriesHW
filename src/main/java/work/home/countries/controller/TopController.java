package work.home.countries.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPopulationResponse;
import work.home.countries.service.CountryService;
import work.home.countries.service.DatabaseCountryService;

import java.util.List;

@RestController
@RequestMapping("/countries/top")
public class TopController {

    private static final Logger log = LoggerFactory.getLogger(TopController.class);
    private final CountryService service;

    public TopController(CountryService service) {
        this.service = service;
    }

    @GetMapping("/population/{number}")
    public List<CountryPopulationResponse> getTopCountriesByPopulation(@PathVariable int number) {
        log.info(String.format("Received request: find top %d countries by population", number));
        return service.getTopCountriesByPopulation(number);
    }

    @GetMapping("/area/{number}")
    public List<CountryAreaResponse> getTopCountriesByArea(@PathVariable int number) {
        log.info(String.format("Received request: find top %d countries by area", number));
        return service.getTopCountriesByArea(number);
    }

    @GetMapping("/density/{number}")
    public List<CountryDensityResponse> getTopCountriesByPopulationDensity(@PathVariable int number) {
        log.info(String.format("Received request: find top %d countries by population density", number));
        return service.getTopCountriesByPopulationDensity(number);
    }


}
