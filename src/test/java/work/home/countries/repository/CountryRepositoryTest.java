package work.home.countries.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import work.home.countries.domain.Country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository victim;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldFindTopByPopulation() {
        countryList(1).forEach(c -> entityManager.persistAndFlush(c));
        List<Country> result = victim.getTopCountriesByPopulation(3);
        List<Country> expected = countryList(8);
        Collections.reverse(expected);
        assertEquals(result, expected);
    }

    @Test
    public void shouldFindTopByArea() {
        countryList(1).forEach(c -> entityManager.persistAndFlush(c));
        List<Country> result = victim.getTopCountriesByArea(3);
        List<Country> expected = countryList(8);
        Collections.reverse(expected);
        assertEquals(result, expected);
    }

    @Test
    public void shouldFindTopByDensity() {
        countryList(1).forEach(c -> entityManager.persistAndFlush(c));
        List<Country> result = victim.getTopCountriesByPopulationDensity(3);
        List<Country> expected = countryList(8);
        Collections.reverse(expected);
        assertEquals(result, expected);
    }

    @Test
    public void shouldFindByPattern() {
        countryList(1).forEach(c -> entityManager.persistAndFlush(c));
        List<Country> result = victim.getCountriesByPattern("%ry 1%");
        List<Country> expected = List.of(countryList(1).get(0), countryList(1).get(9));
        assertEquals(result, expected);
    }

    @Test
    public void shouldReturnEmptyListWhenNotFoundByPattern() {
        countryList(1).forEach(c -> entityManager.persistAndFlush(c));
        List<Country> result = victim.getCountriesByPattern("%zazaza%");
        List<Country> expected = new ArrayList<>();
        assertEquals(result, expected);
    }


    private List<Country> countryList(int min) {
        List<Country> testList = new ArrayList<>();
        for (int i = min; i <= 10; i++) {
            Country testCountry = new Country();
            testCountry.setName("Country " + i);
            testCountry.setCapital("Capital");
            testCountry.setPopulation(i * 2);
            testCountry.setArea(i + 1);
            testList.add(testCountry);
        }
        return testList;
    }

}