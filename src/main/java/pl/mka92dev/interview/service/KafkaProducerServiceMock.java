package pl.mka92dev.interview.kafka;

import org.springframework.stereotype.Service;
import pl.mka92dev.interview.model.Customer;

@Service
public class KafkaProducerServiceMock {

    public void sendMessage(Customer customer) {
        // Mockowa implementacja wysyłania wiadomości
        System.out.println("Mock sending message: " + customer);
    }
}
