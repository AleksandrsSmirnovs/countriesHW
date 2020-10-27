package work.home.countries.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import work.home.countries.domain.Country;
import work.home.countries.exception.NotFoundException;
import work.home.countries.parser.CountryListParser;
import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.response.CountryPopulationResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoDatabaseCountryServiceTest {

    @Mock
    private CountryListParser countryListParser;

    @InjectMocks
    private NoDatabaseCountryService victim;

    @Test
    public void shouldReturnTopCountriesByPopulation() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        List<CountryPopulationResponse> responseList = victim.getTopCountriesByPopulation(3);
        assertEquals(responseList, List.of(
                new CountryPopulationResponse("1.", "Country 15", 150),
                new CountryPopulationResponse("2.", "Country 14", 140),
                new CountryPopulationResponse("3.", "Country 13", 130)
        ));
    }

    @Test
    public void shouldReturnTopCountriesByArea() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        List<CountryAreaResponse> responseList = victim.getTopCountriesByArea(3);
        assertEquals(responseList, List.of(
                new CountryAreaResponse("1.", "Country 15", 16),
                new CountryAreaResponse("2.", "Country 14", 15),
                new CountryAreaResponse("3.", "Country 13", 14)
        ));
    }

    @Test
    public void shouldReturnTopCountriesByPopulationDensity() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        List<CountryDensityResponse> responseList = victim.getTopCountriesByPopulationDensity(3);
        assertEquals(responseList, List.of(
                new CountryDensityResponse("1.", "Country 15", BigDecimal.valueOf(9.38)),
                new CountryDensityResponse("2.", "Country 14", BigDecimal.valueOf(9.33)),
                new CountryDensityResponse("3.", "Country 13", BigDecimal.valueOf(9.29))
        ));
    }

    @Test
    public void shouldReturnCountriesByPattern() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        List<CountryPatternResponse> responseList = victim.getCountriesByPattern("*rY 1*");
        assertEquals(responseList, List.of(
                new CountryPatternResponse("Country 1"),
                new CountryPatternResponse("Country 10"),
                new CountryPatternResponse("Country 11"),
                new CountryPatternResponse("Country 12"),
                new CountryPatternResponse("Country 13"),
                new CountryPatternResponse("Country 14"),
                new CountryPatternResponse("Country 15")
        ));
    }

    @Test
    public void shouldThrowExceptionWhenNotFoundByPattern() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        assertThatThrownBy(() -> victim.getCountriesByPattern("*zazaza*"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No results found for pattern *zazaza*");
    }

    @Test
    public void shouldReturnMaximumPossibleWhenRequestedNumberByDensityIsGreater() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        assertThatCode(() -> victim.getTopCountriesByPopulationDensity(18)).doesNotThrowAnyException();
        assertEquals(15, victim.getTopCountriesByPopulationDensity(18).size());
    }

    @Test
    public void shouldReturnMaximumPossibleWhenRequestedNumberByPopulationIsGreater() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        assertThatCode(() -> victim.getTopCountriesByPopulation(18)).doesNotThrowAnyException();
        assertEquals(15, victim.getTopCountriesByPopulation(18).size());
    }

    @Test
    public void shouldReturnMaximumPossibleWhenRequestedNumberByAreaIsGreater() {
        when(countryListParser.getCountryList()).thenReturn(countryList());
        assertThatCode(() -> victim.getTopCountriesByArea(18)).doesNotThrowAnyException();
        assertEquals(15, victim.getTopCountriesByArea(18).size());
    }

    @Test
    public void shouldIgnoreCountriesWithNullOrZeroAreaWhenRequestedTopByDensity() {
        List<Country> countryList = countryList();
        Country crazyCountry1 = new Country();
        Country crazyCountry2 = new Country();
        crazyCountry1.setName("crazyCountry 1");
        crazyCountry1.setPopulation(123);
        crazyCountry1.setArea(0);
        crazyCountry2.setName("crazyCountry 2");
        crazyCountry1.setPopulation(123);
        countryList.add(crazyCountry1);
        countryList.add(crazyCountry2);
        when(countryListParser.getCountryList()).thenReturn(countryList);
        assertThatCode(() -> victim.getTopCountriesByPopulationDensity(3)).doesNotThrowAnyException();
        List<CountryDensityResponse> responseList = victim.getTopCountriesByPopulationDensity(3);
        assertEquals(responseList, List.of(
                new CountryDensityResponse("1.", "Country 15", BigDecimal.valueOf(9.38)),
                new CountryDensityResponse("2.", "Country 14", BigDecimal.valueOf(9.33)),
                new CountryDensityResponse("3.", "Country 13", BigDecimal.valueOf(9.29))
        ));
    }


    private List<Country> countryList() {
        List<Country> testList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Country testCountry = new Country();
            testCountry.setName("Country " + i);
            testCountry.setCapital("Capital " + i);
            testCountry.setPopulation(i * 10);
            testCountry.setArea(i + 1);
            testList.add(testCountry);
        }
        return testList;
    }


}