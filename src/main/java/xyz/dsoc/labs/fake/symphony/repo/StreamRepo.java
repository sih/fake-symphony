package xyz.dsoc.labs.fake.symphony.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.dsoc.labs.fake.symphony.domain.Stream;

/**
 * @author sih
 */
public interface StreamRepo extends JpaRepository<Stream, Integer> {
}
