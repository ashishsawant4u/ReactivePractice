package com.devex.reactive;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MonoExamples 
{
	@PostConstruct
	public void init()
	{
		//basicExamples();
		//emptyOrError();
		//lazyExample();
	}
	
	public void basicExamples()
	{
		
		String payload = "Mumbai Indians";
		
		
		//Publisher
		Mono<String> topTeam = Mono.just(payload)
								//.map(team -> team.length()/0+"")
								.log();
		
		//topTeam.subscribe();
		
		//topTeam.subscribe(team -> log.warn(team));
		
		topTeam.subscribe(
				team -> log.warn(team),
				err -> log.warn(err.getMessage()),
				() -> log.warn("complete !!")
				);
	}
	
	public void emptyOrError()
	{
		Mono<Double> teamsNRR = Mono.just(0.45).log();
		Mono<Double> nrrGt20 = Mono.empty();
		Mono<Double> wrongNRR = Mono.error(new RuntimeException("wrong net run rate"));
		
		//teamsNRR.subscribe(n -> log.warn("data => "+n));
		
//		nrrGt20.subscribe(
//					n -> log.warn("data => "+n),
//					err -> log.warn(err.getMessage()),
//					()  -> log.warn("completed !!")
//					);
		
		
		wrongNRR.subscribe(
				n -> log.warn("data => "+n),
				err -> log.warn(err.getMessage()),
				() -> log.warn("complete !!")
				);
	
	
	}
	
	public void lazyExample()
	{
		//getData() gets invoked even if we have not subscribing Mono
		//Mono<String> team = Mono.just(getData());
		
		//bez of fromSupplier getData() gets invoked only after subscribe
		Mono<String> teamLazy = Mono.fromSupplier(() -> getData());
		teamLazy.subscribe(t-> log.warn(t));
	}
	
	public String getData()
	{
		log.warn("preparing data....");
		
		return "Mumbai Indians";
	}
	
	
}
