package work.home.countries.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import work.home.countries.domain.Timestamp;

@Repository
public interface TimestampRepository extends CrudRepository<Timestamp, Integer> {
}
