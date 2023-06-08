package com.devex.reactive;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
public class SinksExample 
{
	@PostConstruct
	public void init()
	{
		//sinkMonoExample();
		//sinkFluxUnicastExample();
		//sinkFluxMulticastExample();
		//sinkFluxMulticastNoHistoryExample();
		//sinkFluxMulticastFullHistoryExample();
	}
	
	
	public void sinkMonoExample()
	{
		
		  Sinks.One<String> teamSink =   Sinks.one();
		  
		  teamSink.tryEmitValue("Mumbai Indians");
		  
		  Mono<String> teamMono =  teamSink.asMono();
		  
		  teamMono.subscribe(t -> log.warn(""+t));
		
	}
	
	public void sinkFluxUnicastExample()
	{
		
		  Sinks.Many<String> teamSink =  Sinks.many().unicast().onBackpressureBuffer();
		  
		  teamSink.tryEmitNext("MI");
		  
		  Flux<String> teamFlux =  teamSink.asFlux();
		  
		  teamFlux.subscribe(t -> log.warn(""+t));
		
		  teamSink.tryEmitNext("CSK");
		  
		 // teamFlux.subscribe(t -> log.warn(""+t));
	}
	
	public void sinkFluxMulticastExample()
	{
		
		  Sinks.Many<String> teamSink =  Sinks.many().multicast().onBackpressureBuffer();
		  
		  teamSink.tryEmitNext("MI");
		  
		  Flux<String> teamFlux =  teamSink.asFlux();
		  
		  teamFlux.subscribe(t -> log.warn("subscriber1 ==> "+t));
		
		  teamSink.tryEmitNext("CSK");
		  
		  teamFlux.subscribe(t -> log.warn("subscriber2 ==> "+t));
		  
		  teamSink.tryEmitNext("GT");
	}
	
	public void sinkFluxMulticastNoHistoryExample()
	{
		
		  Sinks.Many<String> teamSink =  Sinks.many().multicast().directAllOrNothing();
		  
		  teamSink.tryEmitNext("MI");
		  
		  Flux<String> teamFlux =  teamSink.asFlux();
		  
		  teamFlux.subscribe(t -> log.warn("subscriber1 ==> "+t));
		
		  teamSink.tryEmitNext("CSK");
		  
		  teamFlux.subscribe(t -> log.warn("subscriber2 ==> "+t));
		  
		  teamSink.tryEmitNext("GT");
	}
	
	public void sinkFluxMulticastFullHistoryExample()
	{
		
		  Sinks.Many<String> teamSink =  Sinks.many().replay().all();
		  
		  teamSink.tryEmitNext("MI");
		  
		  Flux<String> teamFlux =  teamSink.asFlux();
		  
		  teamFlux.subscribe(t -> log.warn("subscriber1 ==> "+t));
		
		  teamSink.tryEmitNext("CSK");
		  
		  teamFlux.subscribe(t -> log.warn("subscriber2 ==> "+t));
		  
		  teamSink.tryEmitNext("GT");
	}
}
