package work.home.countries.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import work.home.countries.domain.Country;
import work.home.countries.domain.Currency;
import work.home.countries.domain.Timestamp;
import work.home.countries.exception.NotFoundException;
import work.home.countries.parser.CountryListParser;
import work.home.countries.repository.CountryRepository;
import work.home.countries.repository.CurrencyRepository;
import work.home.countries.repository.TimestampRepository;
import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.response.CountryPopulationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.*;

@Service
@Profile("database")
public class DatabaseCountryService implements CountryService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCountryService.class);
    private CountryRepository countryRepository;
    private CurrencyRepository currencyRepository;
    private TimestampRepository timestampRepository;
    private CountryListParser parser;

    public DatabaseCountryService(CountryRepository repository, CurrencyRepository currencyRepository, TimestampRepository timestampRepository, CountryListParser parser) {
        this.countryRepository = repository;
        this.currencyRepository = currencyRepository;
        this.timestampRepository = timestampRepository;
        this.parser = parser;
    }

    @Override
    public List<CountryPopulationResponse> getTopCountriesByPopulation(int number) {
        if (dbDataIsStale()) {
            updateDatabaseContents();
        }
        List<Country> countries = countryRepository.getTopCountriesByPopulation(number);
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
        List<Country> countries = countryRepository.getTopCountriesByArea(number);
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
        List<Country> countries = countryRepository.getTopCountriesByPopulationDensity(number);
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
        List<Country> countries = countryRepository.getCountriesByPattern(pattern.toLowerCase().replace("*", "%"));
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
        countryRepository.deleteAll();
        List<Country> list = parser.getCountryList();
        for (Country country : list) {
            currencyRepository.saveAll(country.getCurrencies());
        }
//        for (Country country : list) {
//            for (Currency currency : country.getCurrencies()) {
//                currencyRepository.save(currency);
//            }
//        }
        for (Country country : list) {
            List<Currency> currencyList = new ArrayList<>();
            for (Currency currency : country.getCurrencies()) {
                Currency newCurrency = currencyRepository.findById(currency.getName())
                        .orElse(new Currency(currency.getCode(), currency.getName(), currency.getSymbol()));
                currencyList.add(newCurrency);
            };
            country.setCurrencies(currencyList);
            countryRepository.save(country);
        }


        setTimestamp();
        log.info("Database contents updated");
    }
}
