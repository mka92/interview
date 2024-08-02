package pl.mka92dev.interview.codereview.service;

import org.springframework.stereotype.Service;
import pl.mka92dev.interview.codereview.model.Customer;

@Service
public class KafkaProducerService {

    public void sendMessage(Customer customer) {
        // Implementacja wysyłania wiadomości do Kafki
        System.out.println("Sending message to Kafka: " + customer);
    }
}
