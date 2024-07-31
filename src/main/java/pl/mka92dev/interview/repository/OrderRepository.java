package pl.mka92dev.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mka92dev.interview.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
