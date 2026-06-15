package ru.parktikum.yandexreact;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// https://codefile.io/f/8FSsYr0bNi
public class ReactiveStressTest {

    private static final int NUM_REQUESTS = 100; // Количество параллельных запросов
    private static final int THREAD_POOL_SIZE = 200; // Ограниченное число потоков
    
//    private static final String TARGET_URL = "http://localhost:8080/api/reactive/data";
//    private static final String TARGET_URL = "http://localhost:8080/api/classic/data";
    
//    private static final String TARGET_URL = "http://localhost:8080/api/bad/data";
    private static final String TARGET_URL = "http://localhost:8080/api/optimized/data";

    public static void main(String[] args) throws InterruptedException {
        WebClient webClient = WebClient.create();
        List<Future<Long>> futures = new ArrayList<>();
        
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("Starting stress test with " + NUM_REQUESTS + " requests...");

        for (int i = 0; i < NUM_REQUESTS; i++) {
            Future<Long> future = executorService.submit(() -> {
                long start = System.currentTimeMillis();
                try {
                    webClient.get()
                            .uri(TARGET_URL)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block(); // Блокирующий вызов в тесте!
                } catch (Exception e) {
                    System.err.println("Request failed: " + e.getMessage());
                }
                return System.currentTimeMillis() - start;
            });
            futures.add(future);
        }

        // Ожидаем завершения всех запросов
        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.MINUTES);

        // Выводим статистику
        analyzeResults(futures);

        System.out.println("Stress test completed.");
    }

    private static void analyzeResults(List<Future<Long>> futures) {
        long totalTime = 0;
        long maxTime = 0;
        int failedRequests = 0;
        List<Long> responseTimes = new ArrayList<>();

        for (Future<Long> future : futures) {
            try {
                long time = future.get();
                responseTimes.add(time);
                totalTime += time;
                maxTime = Math.max(maxTime, time);
            } catch (Exception e) {
                failedRequests++;
            }
        }

        System.out.println("\n=== Stress Test Results ===");
        System.out.println("Total requests: " + futures.size());
        System.out.println("Failed requests: " + failedRequests);
        System.out.println("Average response time: " + (totalTime / futures.size()) + "ms");
        System.out.println("Max response time: " + maxTime + "ms");
        System.out.println("90th percentile: " + calculatePercentile(responseTimes) + "ms");
    }

    private static long calculatePercentile(List<Long> responseTimes) {
        responseTimes.sort(Long::compare);
        int index = (int) Math.ceil(90 / 100.0 * responseTimes.size()) - 1;
        return responseTimes.get(index);
    }
}
