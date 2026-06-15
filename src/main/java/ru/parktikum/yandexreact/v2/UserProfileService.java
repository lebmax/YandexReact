package ru.parktikum.yandexreact.v2;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

// https://codefile.io/f/LExUj8CojH
@Service
public class UserProfileService {

    public Mono<UserProfile> getUserProfileInefficient() {
        Mono<String> userMono = getUserById();

        return userMono.flatMap(user -> {
            Mono<String> addressMono = getStubbedAddress();
            Mono<List<String>> ordersMono = getStubbedOrders();

            return ordersMono.flatMap(orders ->
                    addressMono.map(address ->
                            new UserProfile(user, address, orders)
                    )
            );
        });
    }

    public Mono<UserProfile> getUserProfileOptimized() {
        Mono<String> userMono = getUserById();
        Mono<String> addressMono = userMono.flatMap(user -> getStubbedAddress());
        Mono<List<String>> ordersMono = userMono.flatMap(user -> getStubbedOrders());

        return Mono.zip(userMono, addressMono, ordersMono)
                .map(tuple -> new UserProfile(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    private Mono<String> getUserById() {
        return Mono.just("U-1001")
                .delayElement(Duration.ofMillis(1000)); // Simulating network latency
    }

    private Mono<String> getStubbedAddress() {
        return Mono.just("123 Reactive Stm Spring City")
                .delayElement(Duration.ofMillis(1000)); // Simulating network latency
    }

    private Mono<List<String>> getStubbedOrders() {
        return Mono.just(List.of(
                "O-1001",
                "O-1002"
        )).delayElement(Duration.ofMillis(1000)); // Simulating network latency
    }

    public record UserProfile(String user, String address, List<String> orders) {}
}
