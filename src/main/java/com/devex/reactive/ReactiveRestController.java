package com.devex.reactive;

import java.time.Duration;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveRestController 
{
	
	@GetMapping("/flux")
	public Flux<String> api1()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		return Flux.fromIterable(listA).log();
	}
	
	@GetMapping("/mono")
	public Mono<List<String>> api2()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		return Mono.just(listA).log();
	}
	
	@GetMapping(value = "/stream", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Long> api3()
	{
		
		return Flux
				.interval(Duration.ofSeconds(2))
				.log();
	}
		
}
