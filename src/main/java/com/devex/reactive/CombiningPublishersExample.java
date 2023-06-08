package com.devex.reactive;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple3;

@Component
@Slf4j
public class CombiningPublishersExample 
{
	@PostConstruct
	public void init()
	{
		//startsWithExample();
		//concatExample();
		//concatDelayErrorExample();
		//mergeExample();
		//zipExample();
		//combineLatestExample();
	}
	
	public void startsWithExample()
	{
		
		
		Flux<String> top4teams = Flux.just("MI","CSK","GT","LSG");
		Flux<String> next4teams = Flux.just("RR","RCB","PK","SRH")
									 .startWith(top4teams);
		
		
		next4teams
				//.filter(t -> t.startsWith("M"))
				.filter(t -> t.endsWith("K"))
				//.take(1)
				.subscribe(t -> log.warn(t));				
				
	}
	
	public void concatExample()
	{
		
		Flux<String> top4teams = Flux.just("MI","CSK","GT","LSG");
		Flux<String> next4teams = Flux.just("RR","RCB","PK","SRH");
									 			
		Flux<String> allteams = Flux.concat(top4teams,next4teams);	
		
		allteams.subscribe(t -> log.warn(t));	
		
	}
	
	public void concatDelayErrorExample()
	{
		
		Flux<String> top4teams = Flux.just("MI","CSK","GT","LSG");
		Flux<String> errorTeams = Flux.error(new RuntimeException("wrong teams error"));
		Flux<String> next4teams = Flux.just("RR","RCB","PK","SRH");
									 			
		Flux<String> allteams = Flux.concatDelayError(top4teams,errorTeams,next4teams);	
		
		allteams.subscribe(t -> log.warn(t));	
		
	}
	
	public void mergeExample()
	{
		Flux<String> top4teams = Flux.just("MI","CSK","GT","LSG").delayElements(Duration.ofMillis(1));
		Flux<String> next4teams = Flux.just("RR","RCB","PK","SRH").delayElements(Duration.ofMillis(2));
									 			
		Flux<String> allteams = Flux.merge(top4teams,next4teams);	

		allteams.subscribe(t -> log.warn(t));	
		
	}
	
	public void zipExample()
	{
		Flux<String> wicketkeeper = Flux.range(0, 75).map(i -> "wicketkeeper");
	//	Flux<String> wicketkeeper = Flux.empty();
		
		Flux<String> batsmans = Flux.range(0, 100).map(i -> "batsmans");
		
		Flux<String> bowlers = Flux.range(0, 100).map(i -> "bowlers");
		
		Flux<Tuple3<String, String, String>> team = Flux.zip(wicketkeeper,batsmans,bowlers);	
		
		team
			.take(8)
			//.doOnNext(p ->  log.warn(p.getT1()))
			.subscribe(t -> log.warn(""+t));
	}
	
	public void combineLatestExample()
	{
		Flux<String> batsmans = Flux.just("rohit sharma","ishan kishan","suryakumar yadav","camroon green","tim david","tilak verma")
									.delayElements(Duration.ofMillis(20));
		
		Flux<String> bowlers =  Flux.just("trent boult","sandeep sharma","yuzi chahal","ravi ashwin","navdeep saini")
									.delayElements(Duration.ofMillis(50));;
		
		Flux<String> combineLatest = Flux.combineLatest(batsmans,bowlers, (bat,bowl) -> bat+" facing "+bowl);
		
		combineLatest
				.subscribe(t -> log.warn(""+t));
		
	}
}