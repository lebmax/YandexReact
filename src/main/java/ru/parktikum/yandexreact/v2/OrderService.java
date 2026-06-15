package ru.parktikum.yandexreact.v2;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

// https://codefile.io/f/wJstG69Loj
@Service
public class OrderService {

    public Mono<String> getOrdersInefficient() {
        Mono<String> userMono = getUserById();

        String order = getBlockingOrderData();

        return userMono.map(user -> user +  order);
    }

    public Mono<String> getOrdersOptimized() {
        Mono<String> userMono = getUserById();
        Mono<String> orderMono = Mono.fromCallable(this::getBlockingOrderData)
                .subscribeOn(Schedulers.boundedElastic()); // Offloading blocking call

        return userMono.flatMap(user -> orderMono.map(order -> user + order));
    }

    private Mono<String> getUserById() {
        return Mono.just("John Doe")
                .delayElement(Duration.ofMillis(1000)); // Simulating network latency
    }

    private String getBlockingOrderData() {
        try {
            Thread.sleep(3000); // Simulating a slow database or external service call
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "ORD";
    }
}
