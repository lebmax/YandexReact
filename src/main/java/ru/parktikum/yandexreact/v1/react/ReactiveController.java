package ru.parktikum.yandexreact.v1.react;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

// https://codefile.io/f/bEyvQnLI7m
@RestController
@RequestMapping("/api/reactive")
public class ReactiveController {

    private final ReactiveService reactiveService;

    @Autowired
    public ReactiveController(ReactiveService reactiveService) {
        this.reactiveService = reactiveService;
    }

    @GetMapping("/data")
    public Flux<String> getData() {
        System.out.println("Reactive controller: Starting request processing");
        long start = System.currentTimeMillis();

        List<Flux<String>> arrayOfFlux = new ArrayList<>();

        arrayOfFlux.add(reactiveService.fetchData());
        arrayOfFlux.add(reactiveService.fetchData());
        arrayOfFlux.add(reactiveService.fetchData());

        return Flux.merge(arrayOfFlux)
                .flatMap(reactiveService::processData)
                .doOnComplete(() -> {
                    long end = System.currentTimeMillis();
                    System.out.println("Reactive controller: Request completed in " + (end - start) + " ms");
                });
    }
}

