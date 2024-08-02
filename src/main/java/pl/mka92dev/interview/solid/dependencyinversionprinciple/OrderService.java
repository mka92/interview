package pl.mka92dev.interview.solid.dependencyinversionprinciple;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final PostgreSqlDatabaseAccess postgreSqlDatabaseAccess;

    public OrderService(PostgreSqlDatabaseAccess postgreSqlDatabaseAccess) {
        this.postgreSqlDatabaseAccess = postgreSqlDatabaseAccess;
    }

    public void processOrder(Order order) {
        // logic before save Order
        postgreSqlDatabaseAccess.save(order);
    }

    public void removeOrder(Order order) {
        // logic before remove Order
        postgreSqlDatabaseAccess.remove(order);
    }
}
