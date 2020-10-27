package work.home.countries.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseCountryServiceTest {

    @Mock
    private CountryRepository repository;

    @Mock
    private CountryListParser parser;

    @Mock
    private TimestampRepository timestampRepository;

    @InjectMocks
    private DatabaseCountryService victim;


    @Test
    public void shouldUpdateDatabaseWhenIsStaleInGetTopCountriesByPopulation() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 123L)));
        when(parser.getCountryList()).thenReturn(countryList);
        when(repository.getTopCountriesByPopulation(anyInt())).thenReturn(countryList);
        victim.getTopCountriesByPopulation(3);
        verify(parser, times(1)).getCountryList();
        verify(repository, times(1)).saveAll(countryList);
        verify(timestampRepository, times(1)).save(any());
    }

    @Test
    public void shouldUpdateDatabaseWhenIsStaleInGetTopCountriesByArea() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 123L)));
        when(parser.getCountryList()).thenReturn(countryList);
        when(repository.getTopCountriesByArea(anyInt())).thenReturn(countryList);
        victim.getTopCountriesByArea(3);
        verify(parser, times(1)).getCountryList();
        verify(repository, times(1)).saveAll(countryList);
        verify(timestampRepository, times(1)).save(any());
    }

    @Test
    public void shouldUpdateDatabaseWhenIsStaleInGetTopCountriesByPopulationDensity() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 123L)));
        when(parser.getCountryList()).thenReturn(countryList);
        when(repository.getTopCountriesByPopulationDensity(anyInt())).thenReturn(countryList);
        victim.getTopCountriesByPopulationDensity(3);
        verify(parser, times(1)).getCountryList();
        verify(repository, times(1)).saveAll(countryList);
        verify(timestampRepository, times(1)).save(any());
    }

    @Test
    public void shouldUpdateDatabaseWhenIsStaleInGetCountriesByPattern() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 123L)));
        when(parser.getCountryList()).thenReturn(countryList);
        when(repository.getCountriesByPattern(anyString())).thenReturn(countryList);
        victim.getCountriesByPattern("asdf");
        verify(parser, times(1)).getCountryList();
        verify(repository, times(1)).saveAll(countryList);
        verify(timestampRepository, times(1)).save(any());
    }


    @Test
    public void shouldNotUpdateDatabaseWhenIsNotStaleInGetTopCountriesByPopulation() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getTopCountriesByPopulation(anyInt())).thenReturn(countryList);
        victim.getTopCountriesByPopulation(3);
        verify(parser, never()).getCountryList();
        verify(repository, never()).saveAll(any());
        verify(timestampRepository, never()).save(any());
    }

    @Test
    public void shouldNotUpdateDatabaseWhenIsNotStaleInGetTopCountriesByArea() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getTopCountriesByArea(anyInt())).thenReturn(countryList);
        victim.getTopCountriesByArea(3);
        verify(parser, never()).getCountryList();
        verify(repository, never()).saveAll(any());
        verify(timestampRepository, never()).save(any());
    }

    @Test
    public void shouldNotUpdateDatabaseWhenIsStaleInGetTopCountriesByPopulationDensity() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getTopCountriesByPopulationDensity(anyInt())).thenReturn(countryList);
        victim.getTopCountriesByPopulationDensity(3);
        verify(parser, never()).getCountryList();
        verify(repository, never()).saveAll(any());
        verify(timestampRepository, never()).save(any());
    }

    @Test
    public void shouldNotUpdateDatabaseWhenIsStaleInGetCountriesByPattern() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getCountriesByPattern(anyString())).thenReturn(countryList);
        victim.getCountriesByPattern("asdf");
        verify(parser, never()).getCountryList();
        verify(repository, never()).saveAll(any());
        verify(timestampRepository, never()).save(any());
    }

    @Test
    public void shouldMapToCountryPopulationResponse() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getTopCountriesByPopulation(3)).thenReturn(countryList);
        List<CountryPopulationResponse> responseList = victim.getTopCountriesByPopulation(3);
        assertEquals(responseList, List.of(
                new CountryPopulationResponse("1.", "Country 1", 1),
                new CountryPopulationResponse("2.", "Country 2", 2),
                new CountryPopulationResponse("3.", "Country 3", 3)
        ));
    }

    @Test
    public void shouldMapToCountryAreaResponse() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getTopCountriesByArea(3)).thenReturn(countryList);
        List<CountryAreaResponse> responseList = victim.getTopCountriesByArea(3);
        assertEquals(responseList, List.of(
                new CountryAreaResponse("1.", "Country 1", 1),
                new CountryAreaResponse("2.", "Country 2", 2),
                new CountryAreaResponse("3.", "Country 3", 3)
        ));
    }

    @Test
    public void shouldMapToCountryPopulationDensityResponse() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getTopCountriesByPopulationDensity(3)).thenReturn(countryList);
        List<CountryDensityResponse> responseList = victim.getTopCountriesByPopulationDensity(3);
        assertEquals(responseList, List.of(
                new CountryDensityResponse("1.", "Country 1", BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP)),
                new CountryDensityResponse("2.", "Country 2", BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP)),
                new CountryDensityResponse("3.", "Country 3", BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP))
        ));
    }

    @Test
    public void shouldMapToCountryPatternResponse() {
        List<Country> countryList = countryList();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getCountriesByPattern("%oun%")).thenReturn(countryList);
        List<CountryPatternResponse> responseList = victim.getCountriesByPattern("*oUn*");
        assertEquals(responseList, List.of(
                new CountryPatternResponse("Country 1"),
                new CountryPatternResponse("Country 2"),
                new CountryPatternResponse("Country 3")
        ));
    }

    @Test
    public void shouldThrowExceptionWhenNotFoundByPattern() {
        List<Country> countryList = new ArrayList<>();
        when(timestampRepository.findById(1)).thenReturn(Optional.of(new Timestamp(1, 9999999999999999L)));
        when(repository.getCountriesByPattern("%zzz%")).thenReturn(countryList);
        assertThatThrownBy(() -> victim.getCountriesByPattern("*zzz*"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No results found for pattern *zzz*");
    }


    private List<Country> countryList() {
        List<Country> testList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Country testCountry = new Country();
            testCountry.setName("Country " + i);
            testCountry.setCapital("Capital");
            testCountry.setPopulation(i);
            testCountry.setArea(i);
            testList.add(testCountry);
        }
        return testList;
    }
}