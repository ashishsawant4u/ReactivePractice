package com.devex.reactive;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class DeafultController 
{
	
	
	@GetMapping("/m1")
	@ResponseBody
	public String method1()
	{
		
		monoBasicExample().subscribe(d->d.forEach(e->log.info(e)));
		
		
		return "Reactive Programming";
	}
	
	
	public Mono<List<String>> monoBasicExample()
	{
		return Mono.just(List.of("MI","CSK","PK")).log();
	}
	
	
	
	
	
	private Flux<String> multipleElements()
	{
		
		return Flux.fromIterable(List.of("aaaa","bbbbb","ccccc","dddd")).log();
		
		//using map
//		return Flux.fromIterable(List.of("aaaa","bbbbb","ccccc","dddd"))
//				.map(s -> s.toUpperCase())
//				.log();
		
		//using filter
//		return Flux.fromIterable(List.of("aaaa","bbbbb","ccccc","dddd"))
//				.filter(s->s.startsWith("a"))
//				.log();
		
		
		//using flatMap
		//List<String> listA = List.of("A","B","C");
		//List<String> listB = List.of("X","Y","Z");
		
//		return Flux.fromIterable(List.of(listA,listB))
//					.flatMap(s-> Flux.fromIterable(s.stream().collect(Collectors.toList())));
		
		
		//Async flatmap
		//flatMap does not preserve order
//		return Flux.fromIterable(List.of(listA,listB))
//				.flatMap(s-> {
//					return Flux.fromIterable(s.stream().collect(Collectors.toList()))
//								.delayElements(Duration.ofSeconds(2));
//				});
		
		//concatMap preserve order
//		return Flux.fromIterable(List.of(listA,listB))
//				.concatMap(s-> {
//					return Flux.fromIterable(s.stream().collect(Collectors.toList()))
//								.delayElements(Duration.ofSeconds(2));
//				});
		
	}
	
	private Mono<List<String>> singleElements()
	{
		return Mono.just(List.of("A","B","C")).log();
	}
	
}
