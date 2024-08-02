package pl.mka92dev.interview.codereview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mka92dev.interview.codereview.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
