package work.home.countries.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import work.home.countries.domain.Country;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

    @Query(value = "SELECT * FROM country ORDER BY population DESC LIMIT :limit", nativeQuery = true)
    List<Country> getTopCountriesByPopulation(@Param("limit") int limit);

    @Query(value = "SELECT * FROM country ORDER BY area DESC LIMIT :limit", nativeQuery = true)
    List<Country> getTopCountriesByArea(@Param("limit") int limit);

    @Query(value = "SELECT * FROM country WHERE area != 0 ORDER BY population/area DESC LIMIT :limit", nativeQuery = true)
    List<Country> getTopCountriesByPopulationDensity(@Param("limit") int limit);

    @Query(value = "SELECT * FROM country WHERE lower(name) LIKE :pattern", nativeQuery = true)
    List<Country> getCountriesByPattern(@Param("pattern") String pattern);

//    @Query(value = "INSERT INTO country (area, capital, name, population) VALUES (:area, :capital, :name, :population)", nativeQuery = true)
//    void saveCountry(@Param("area") double area, @Param("capital") String capital, @Param("name") String name, @Param("population") long population);


}


