package ru.parktikum.yandexreact.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// https://codefile.io/f/Zz3tlEY18l
@RestController
@RequestMapping("/api")
public class ReactController {
	
	private final UserProfileService userProfileService;
	private final OrderService orderService;
	
	@Autowired
	public ReactController(UserProfileService userProfileService, OrderService orderService) {
		this.userProfileService = userProfileService;
		this.orderService = orderService;
	}
	
	@GetMapping("/bad/data")
	public Mono<Void> getAggregatedData() {
		long start = System.currentTimeMillis();
		return Flux.zip(userProfileService.getUserProfileInefficient(), orderService.getOrdersInefficient())
				   .doOnComplete(() -> {
					   long end = System.currentTimeMillis();
					   System.out.println("Request completed in " + (end - start) + " ms");
				   }).then();
	}
	
	@GetMapping("/optimized/data")
	public Mono<Void> getOptimizedAggregatedData() {
		long start = System.currentTimeMillis();
		return Flux.zip(userProfileService.getUserProfileOptimized(), orderService.getOrdersOptimized())
				   .doOnComplete(() -> {
					   long end = System.currentTimeMillis();
					   System.out.println("Request completed in " + (end - start) + " ms");
				   }).then();
	}
}
