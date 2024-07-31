package pl.mka92dev.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.mka92dev.interview.model.Customer;
import pl.mka92dev.interview.model.Order;
import pl.mka92dev.interview.model.OrderStatus;
import pl.mka92dev.interview.repository.CustomerRepository;
import pl.mka92dev.interview.repository.OrderRepository;
import pl.mka92dev.interview.kafka.KafkaProducerService;
import pl.mka92dev.interview.kafka.KafkaProducerServiceMock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaProducerServiceMock kafkaProducerServiceMock;

    public void processOrder(Long orderId, OrderStatus orderStatus) {
        String env = environment.getProperty("spring.profiles.active");
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Customer customer = order.getCustomer();

        if ("prod".equals(env)) {
            System.out.println("Processing order in PROD environment");
            order.setStatus(orderStatus);
            if (orderStatus == OrderStatus.REMOVED && customer.getOrders().contains(order)) {
                customer.getOrders().remove(order);
            }
            orderRepository.save(order);
            kafkaProducerService.sendMessage(customer);
        } else if ("test".equals(env)) {
            System.out.println("Processing order in TEST environment");
            order.setStatus(orderStatus);
            if (orderStatus == OrderStatus.REMOVED) {
                customer.getOrders().removeIf(o -> o.getId().equals(order.getId()));
            }
            orderRepository.save(order);
            kafkaProducerServiceMock.sendMessage(customer);
        } else {
            System.out.println("Skipping logic for other environment");
        }
    }

    public void addCustomerOrder(Long customerId, Set<Long> orderIdSet) {
        updateCustomerOrder(customerId, orderIdSet, true);
    }

    public void removeCustomerOrder(Long customerId, Set<Long> orderIdSet) {
        updateCustomerOrder(customerId, orderIdSet, false);
    }

    private void updateCustomerOrder(Long customerId, Set<Long> orderIdSet, boolean add) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        deleteDuplicates(orderIdSet);

        for (Long orderId : orderIdSet) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            if (add) {
                if (!customer.getOrders().contains(order)) {
                    customer.getOrders().add(order);
                }
            } else {
                customer.getOrders().remove(order);
            }
        }
        customerRepository.save(customer);
    }

    public void deleteDuplicates(Set<Long> orderIdSet) {
        List<Long> distinctOrderIds = orderIdSet.stream().distinct().collect(Collectors.toList());
        orderIdSet.clear();
        orderIdSet.addAll(distinctOrderIds);
        System.out.println("Distinct order IDs: " + distinctOrderIds);
    }
}
