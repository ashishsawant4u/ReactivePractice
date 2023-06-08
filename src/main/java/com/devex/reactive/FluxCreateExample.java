package com.devex.reactive;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Component
@Slf4j
public class FluxCreateExample 
{
	@PostConstruct
	public void init()
	{
		 
		 //basicExample();
		//cancelExample();
	}
	
	public void basicExample()
	{
		
		
		MyConsumer myConsumer = new MyConsumer();
		
		Flux<String> teamFlux =  Flux.create(myConsumer).log();
		
		teamFlux.subscribe(t -> log.warn("Received ==> "+t));
		
		myConsumer.doSomething();
		
		
	}
	
	public void cancelExample()
	{
		TeamsConsumer teamsConsumer = new TeamsConsumer();
		
		Flux<String> teamsFlux = Flux.create(teamsConsumer).log();
		
		teamsFlux.take(3).subscribe(t -> log.warn(t));
				
		teamsConsumer.doSomething();
		
	
	}
	
	
}


@Slf4j
class TeamsConsumer implements Consumer<FluxSink<String>>
{
	private FluxSink<String> fluxSink;
	

	@Override
	public void accept(FluxSink<String> t) 
	{
		fluxSink = t;
	}
	
	public FluxSink<String> getFluxSink()
	{
		return this.fluxSink;
	}
	
	public void doSomething()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		for (int i = 0; i < teams.size(); i++) 
		{
			
			if(!this.fluxSink.isCancelled())
			{
				log.warn("emitting ==> "+teams.get(i));
				this.fluxSink.next(teams.get(i));
			}
			
			
		}
		
		this.fluxSink.complete();
	
	}
	
}	

//log.warn("isCancelled ==> "+this.fluxSink.isCancelled());
//if(!this.fluxSink.isCancelled())
//{	
//	log.warn("emitting ==> "+teams.get(i));
//	this.fluxSink.next(teams.get(i));
//}

@Slf4j
class MyConsumer implements Consumer<FluxSink<String>>
{
	
	private FluxSink<String> fluxSink;
	

	@Override
	public void accept(FluxSink<String> t) 
	{
		fluxSink = t;
	}
	
	public void doSomething()
	{
		this.fluxSink.next("mumbai indians");
		this.fluxSink.next("chennai super kings");
		this.fluxSink.complete();
	}
	
}
