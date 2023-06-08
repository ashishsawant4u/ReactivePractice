package com.devex.reactive;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

@Component
@Slf4j
public class FluxGenerate 
{
	@PostConstruct
	public void init()
	{
		//basicExample();
	}
	
	public void basicExample()
	{
		TeamsSyncConsumer teamsSyncConsumer = new  TeamsSyncConsumer();
		
		Flux<String> teamFlux = Flux.generate(teamsSyncConsumer).log();
		
		teamFlux.take(3).subscribe(t -> log.warn(t));
		//teamFlux.subscribe(t -> log.warn(t));
		
		
		
	}
}


@Slf4j
@NoArgsConstructor
class TeamsSyncConsumer implements Consumer<SynchronousSink<String>>
{
	
	public SynchronousSink<String> syncFluxSink;
	
	
	public void doSomething()
	{
		log.warn("emitting ==> ");
		this.syncFluxSink.next("Mumbai Indians");
	}

	@Override
	public void accept(SynchronousSink<String> t) 
	{
		this.syncFluxSink = t;
		doSomething();
		this.syncFluxSink.complete();
	}
	
	
	
}
