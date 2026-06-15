package ru.parktikum.yandexreact.v1.classic;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassicService {

    public List<String> fetchData() {
        System.out.println("Classic service: Fetching data");
        try {
            // Имитация долгой операции I/O
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return List.of("Data1", "Data2", "Data3");
    }

    public String processData(String data) {
        System.out.println("Classic service: Processing " + data);
        try {
            // Имитация обработки данных
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Processed: " + data;
    }
}
