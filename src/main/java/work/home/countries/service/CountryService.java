package work.home.countries.service;

import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.response.CountryPopulationResponse;

import java.util.List;

public interface CountryService {

    List<CountryPopulationResponse> getTopCountriesByPopulation(int number);

    List<CountryAreaResponse> getTopCountriesByArea(int number);

    List<CountryDensityResponse> getTopCountriesByPopulationDensity(int number);

    List<CountryPatternResponse> getCountriesByPattern(String pattern);
}
