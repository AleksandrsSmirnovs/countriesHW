package work.home.countries.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import work.home.countries.domain.Country;
import work.home.countries.domain.Timestamp;
import work.home.countries.exception.NotFoundException;
import work.home.countries.parser.CountryListParser;
import work.home.countries.repository.CountryRepository;
import work.home.countries.repository.TimestampRepository;
import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.response.CountryPopulationResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.*;

@Service
@Profile("database")
public class DatabaseCountryService implements CountryService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCountryService.class);
    private CountryRepository repository;
    private TimestampRepository timestampRepository;
    private CountryListParser parser;

    public DatabaseCountryService(CountryRepository repository, TimestampRepository timestampRepository, CountryListParser parser) {
        this.repository = repository;
        this.timestampRepository = timestampRepository;
        this.parser = parser;
    }

    @Override
    public List<CountryPopulationResponse> getTopCountriesByPopulation(int number) {
        if (dbDataIsStale()) {
            updateDatabaseContents();
        }
        List<Country> countries = repository.getTopCountriesByPopulation(number);
        AtomicInteger counter = new AtomicInteger(1);
        return countries.stream()
                .map(country -> new CountryPopulationResponse(counter.getAndIncrement() + ".",
                        country.getName(),
                        country.getPopulation()))
                .collect(toList());
    }

    @Override
    public List<CountryAreaResponse> getTopCountriesByArea(int number) {
        if (dbDataIsStale()) {
            updateDatabaseContents();
        }
        List<Country> countries = repository.getTopCountriesByArea(number);
        AtomicInteger counter = new AtomicInteger(1);
        return countries.stream()
                .map(country -> new CountryAreaResponse(
                        counter.getAndIncrement() + ".",
                        country.getName(),
                        country.getArea()
                ))
                .collect(toList());
    }

    @Override
    public List<CountryDensityResponse> getTopCountriesByPopulationDensity(int number) {
        if (dbDataIsStale()) {
            updateDatabaseContents();
        }
        List<Country> countries = repository.getTopCountriesByPopulationDensity(number);
        AtomicInteger counter = new AtomicInteger(1);
        return countries.stream()
                .map(country -> new CountryDensityResponse(
                        counter.getAndIncrement() + ".",
                        country.getName(),
                        country.getPopulationDensity()
                ))
                .collect(toList());
    }

    @Override
    public List<CountryPatternResponse> getCountriesByPattern(String pattern) {
        if (dbDataIsStale()) {
            updateDatabaseContents();
        }
        List<Country> countries = repository.getCountriesByPattern(pattern.toLowerCase().replace("*", "%"));
        if (countries.isEmpty()) {
            throw new NotFoundException(String.format("No results found for pattern %s", pattern));
        }
        return countries.stream()
                .map(country -> new CountryPatternResponse(
                        country.getName()
                ))
                .collect(toList());
    }

    private boolean dbDataIsStale() {
        Timestamp dbTimestamp = timestampRepository.findById(1).orElse(new Timestamp(1, 0L));
        return System.currentTimeMillis() - dbTimestamp.getTimestamp() > 500;
    }

    private void setTimestamp() {
        timestampRepository.save(new Timestamp(1, System.currentTimeMillis()));
    }

    private void updateDatabaseContents() {
        repository.deleteAll();
        List<Country> list = parser.getCountryList();
        repository.saveAll(list);
        setTimestamp();
        log.info("Database contents updated");
    }
}
