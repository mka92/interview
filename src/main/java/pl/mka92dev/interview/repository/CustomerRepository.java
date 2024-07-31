package pl.mka92dev.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mka92dev.interview.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
