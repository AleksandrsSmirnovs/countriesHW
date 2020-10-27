package work.home.countries.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import work.home.countries.domain.Country;
import work.home.countries.exception.NotFoundException;
import work.home.countries.parser.CountryListParser;
import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.response.CountryPopulationResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Service
@Profile("noDatabase")
public class NoDatabaseCountryService implements CountryService {

    private final CountryListParser countryListParser;

    public NoDatabaseCountryService(CountryListParser countryListParser) {
        this.countryListParser = countryListParser;
    }

    @Override
    public List<CountryPopulationResponse> getTopCountriesByPopulation(int number) {
        List<Country> countries = countryListParser.getCountryList();
        if (number > countries.size()) {
            number = countries.size();
        }
        AtomicInteger counter = new AtomicInteger(1);
        return countries.stream()
                .sorted(comparing(Country::getPopulation).reversed())
                .limit(number)
                .map(country -> new CountryPopulationResponse(counter.getAndIncrement() + ".",
                        country.getName(),
                        country.getPopulation()))
                .collect(toList());
    }

    @Override
    public List<CountryAreaResponse> getTopCountriesByArea(int number) {
        List<Country> countries = countryListParser.getCountryList();
        if (number > countries.size()) {
            number = countries.size();
        }
        AtomicInteger counter = new AtomicInteger(1);
        return countries.stream()
                .sorted(comparing(Country::getArea).reversed())
                .limit(number)
                .map(country -> new CountryAreaResponse(
                        counter.getAndIncrement() + ".",
                        country.getName(),
                        country.getArea()
                ))
                .collect(toList());
    }

    @Override
    public List<CountryDensityResponse> getTopCountriesByPopulationDensity(int number) {
        List<Country> countries = countryListParser.getCountryList();
        if (number > countries.size()) {
            number = countries.size();
        }
        countries.sort(comparing(country -> country.getPopulation() / country.getArea()));
        AtomicInteger counter = new AtomicInteger(1);
        return countries.stream()
                .filter(country -> country.getArea() != 0)
                .sorted(comparing(Country::getPopulationDensity).reversed())
                .limit(number)
                .map(country -> new CountryDensityResponse(
                        counter.getAndIncrement() + ".",
                        country.getName(),
                        country.getPopulationDensity()
                ))
                .collect(toList());
    }

    @Override
    public List<CountryPatternResponse> getCountriesByPattern(String pattern) {
        List<Country> countries = countryListParser.getCountryList();
        List<CountryPatternResponse> response = countries.stream()
                .filter(country -> country.getName().toLowerCase().matches(pattern.toLowerCase().replace("*", ".*")))
                .map(country -> new CountryPatternResponse(
                        country.getName()
                ))
                .collect(toList());
        if (response.isEmpty()) {
            throw new NotFoundException(String.format("No results found for pattern %s", pattern));
        }
        return response;
    }
}

