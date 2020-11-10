package work.home.countries.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import work.home.countries.domain.Currency;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, String> {
}
