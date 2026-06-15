package ru.parktikum.yandexreact.v1.react;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ReactiveService {

    public Flux<String> fetchData() {
        return Flux.defer(() -> {
            System.out.println("Reactive service: Fetching data");
            return Flux.just("Data1", "Data2", "Data3")
                    .delayElements(Duration.ofMillis(1000));
        });
    }

    public Mono<String> processData(String data) {
        return Mono.defer(() -> {
            System.out.println("Reactive service: Processing " + data);
            return Mono.just("Processed: " + data)
                    .delayElement(Duration.ofMillis(500));
        });
    }
}
