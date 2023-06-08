package com.devex.reactive;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

@Component
@Slf4j
public class FluxGenerateWithState 
{
	@PostConstruct
	public void init()
	{
		//basicExample2();
		//basicExample();
	}
	
	public void basicExample2()
	{
		Flux.generate(
				() -> 1,
				(state,sink) ->{
					log.warn("state "+state);
					if(state < 3)
					{
						sink.next("Mumbai Indians");
					}
					else
					{
						sink.complete();
					}
					return state + 1;
				}
				)
				.log()
				.subscribe(t -> log.warn(t+""));
				
		
		
	}
	
	public Callable<Integer> getState()
	{
		return () -> 0;
	}
	
	public BiFunction<Integer, SynchronousSink<String>, Integer> doSomething()
	{
		return (count,sink) ->{
			
			if(count < 5)
			{
				sink.next("Indian Premier League");
			}
			else
			{
				sink.complete();
			}
			return count + 1;
		};
	}
	
	public void basicExample()
	{
		Flux.generate(getState(),doSomething())
			.log()
			//.take(2)
			.subscribe(t -> log.warn(t));
	}
}



