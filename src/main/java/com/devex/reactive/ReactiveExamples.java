package com.devex.reactive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReactiveExamples
{
	@PostConstruct
	public void callReactive()
	{
		//monoBasicExample().subscribe(d->d.forEach(e->log.warn(e)));
		
		//fluxBasicExample().subscribe(d -> log.warn(d.toString()));
		
		//monoMapExample().subscribe(d -> log.warn(d));
		
		//fluxMapExample().subscribe(d -> log.warn(d));
		
		//monoMapExample2().subscribe(d -> d.forEach(e -> log.warn(e)));
		
		//fluxMapExample2() 
		
		//monoFilterExample().subscribe(d -> d.forEach(e -> log.warn(e)));
		
		//fluxFilterExample().subscribe(d -> d.forEach(e -> log.warn(e)));
		
		//monoFlatMapExample().subscribe(d -> d.forEach(e -> log.warn(e)));
		
		//fluxFlatMapExample().subscribe(d -> d.forEach(e -> log.warn(e)));
	}
	
	public Flux<List<String>> fluxFlatMapExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		List<String>  listB = List.of("KKR","GT","RR");
		
		
		
	
		
	
		return Flux.fromIterable(List.of(listA,listB))
				.log();
	}
	
	List<String> C2SFun(String s)
	{
		char[] cArr = s.toCharArray();
		
		List<String> output = new ArrayList<>();
		
		for (int i = 0; i < cArr.length; i++) {
			output.add(String.valueOf(cArr[i]));
		}
		
		return output;
	}
	
	
	
	public Mono<List<String>> monoFlatMapExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		Function<List<String>,List<String>> fun2 = list -> list.stream()
																	.flatMap(e -> C2SFun(e).stream())
																	.collect(Collectors.toList());
		
		
		return Mono.just(listA)
			.flatMap(l -> Mono.just( fun2.apply(l)     )  )
			.log();
	}
	
	public Mono<List<String>> monoFilterExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		return Mono.just(listA)
				.filter(e -> e.contains("CSK"))
				.log();
	}
	
	public Flux<List<String>> fluxFilterExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		List<String>  listB = List.of("KKR","GT","RR");
		
	
		return Flux.fromIterable(List.of(listA,listB))
				.filter( e -> e.contains("GT"))
				.log();
	}
	 
	
	public Flux<List<String>> fluxMapExample2()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		List<String>  listB = List.of("KKR","GT","RR");
		
		Function<List<String>, List<String>> convert = (s) -> s.stream().map(e->e.toLowerCase()).collect(Collectors.toList()); 
	
		return Flux.fromIterable(List.of(listA,listB))
				.map(d -> convert.apply(d))
				.log();
	}
	
	
	public Mono<List<String>> monoMapExample2()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		Function<List<String>, List<String>> convert = (s) -> s.stream().map(e->e.toLowerCase()).collect(Collectors.toList()); 
		
		return Mono.just(listA)
				.map(e -> convert.apply(e)  )
				.log();
	}
	
	public Mono<String> monoMapExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		return Mono.just(listA)
				.map(e -> e.toString().toLowerCase())
				.log();
	}
	
	public Flux<String> fluxMapExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		List<String>  listB = List.of("KKR","GT","RR");
		
		return Flux.fromIterable(List.of(listA,listB))
				.map(d -> d.get(0))
				.log();
	}
	
	
	//data in the mono is emitted in one go
	public Mono<List<String>> monoBasicExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		
		return Mono.just(listA).log();
	}
	
	//data in the flux emitted individually over onNext() event
	public Flux<List<String>> fluxBasicExample()
	{
		List<String>  listA = List.of("MI","CSK","PK");
		List<String>  listB = List.of("KKR","GT","RR");
		
		return Flux.fromIterable(List.of(listA,listB)).log();
	}
}
