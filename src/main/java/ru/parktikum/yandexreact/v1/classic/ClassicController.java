package ru.parktikum.yandexreact.v1.classic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// https://codefile.io/f/beZgdIgJlD
@RestController
@RequestMapping("/api/classic")
public class ClassicController {

    private final ClassicService classicService;

    @Autowired
    public ClassicController(ClassicService classicService) {
        this.classicService = classicService;
    }

    @GetMapping("/data")
    public List<String> getData() {
        System.out.println("Classic controller: Starting request processing");
        long start = System.currentTimeMillis();

        List<String> dataToProcess = new ArrayList<>();
        dataToProcess.addAll(classicService.fetchData());
        dataToProcess.addAll(classicService.fetchData());
        dataToProcess.addAll(classicService.fetchData());

        // Последовательно получаем и обрабатываем данные
        List<String> result = dataToProcess
                .stream()
                .map(classicService::processData)
                .toList();

        long end = System.currentTimeMillis();
        System.out.println("Classic controller: Request completed in " + (end - start) + " ms");
        return result;
    }
}
