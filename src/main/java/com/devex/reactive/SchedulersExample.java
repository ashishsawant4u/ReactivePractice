package com.devex.reactive;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class SchedulersExample 
{	
	@PostConstruct
	public void init()
	{
		 //basicExample();
		 //multipleSubscriber();
		 //multipleSubscribeOn();
		//singleThreadExample();
		//publishOn();
		//publishOnWithSubscribeOn();
	}
	
	public void basicExample()
	{
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		List<String> teams = Arrays.asList("MI","CSK");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .subscribeOn(Schedulers.boundedElastic())
									  .map(t -> t.toLowerCase())
									  .doOnNext(d -> threadLogger.accept("doOnNext"))
									 // .log()
									  ;
		
		
		
		teamsFlux
				.doFirst(() -> threadLogger.accept("doFirst"))
				//.subscribeOn(Schedulers.boundedElastic())
				.doOnRequest(c -> threadLogger.accept("doOnRequest"))
				.subscribe(t -> threadLogger.accept("received "+t));
	}
	
	public void multipleSubscriber()
	{
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		List<String> teams = Arrays.asList("MI","CSK");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .subscribeOn(Schedulers.boundedElastic())
									  .map(t -> t.toLowerCase())
									  .doOnNext(d -> threadLogger.accept("doOnNext"))
									  ;
		
		
		Runnable runnable = () -> teamsFlux
										.doFirst(() -> threadLogger.accept("doFirst"))
										.doOnRequest(c -> threadLogger.accept("doOnRequest"))
										.subscribe(t -> threadLogger.accept("subscribe "+t));
		
		for (int i = 0; i < 3; i++) 
		{
			new Thread(runnable).start();
		}
		
	}
	
	public void multipleSubscribeOn()
	{
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		List<String> teams = Arrays.asList("MI","CSK");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .subscribeOn(Schedulers.parallel())
									  .map(t -> t.toLowerCase())
									  .doOnNext(d -> threadLogger.accept("doOnNext"))
									  ;
		
		
		
		teamsFlux
				.doFirst(() -> threadLogger.accept("doFirst"))
				.subscribeOn(Schedulers.boundedElastic())
				.doOnRequest(c -> threadLogger.accept("doOnRequest"))
				.subscribe(t -> threadLogger.accept("subscribe "+t));
	}
	
	public void singleThreadExample()
	{
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		List<String> teams = Arrays.asList("MI","CSK");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .map(t -> t.toLowerCase())
									  .doOnNext(d -> threadLogger.accept("doOnNext"));
		
		
		
		teamsFlux
				.doFirst(() -> threadLogger.accept("doFirst"))
				.subscribeOn(Schedulers.single())
				.doOnRequest(c -> threadLogger.accept("doOnRequest"))
				.subscribe(t -> threadLogger.accept("received "+t));
	}
	
	public void publishOn()
	{
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		List<String> teams = Arrays.asList("MI","CSK");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .map(t -> t.toLowerCase())
									  .doOnNext(d -> threadLogger.accept("doOnNext"));
		
		
		
		teamsFlux
				.doFirst(() -> threadLogger.accept("doFirst"))
				.publishOn(Schedulers.boundedElastic())
				.doOnRequest(c -> threadLogger.accept("doOnRequest"))
				.subscribe(t -> threadLogger.accept("received "+t));
	}
	
	public void publishOnWithSubscribeOn()
	{
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		List<String> teams = Arrays.asList("MI","CSK");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .subscribeOn(Schedulers.boundedElastic())
									  .map(t -> t.toLowerCase())
									  .doOnNext(d -> threadLogger.accept("doOnNext"));
		
		
		
		teamsFlux
				.doFirst(() -> threadLogger.accept("doFirst"))
				.publishOn(Schedulers.boundedElastic())
				.doOnRequest(c -> threadLogger.accept("doOnRequest"))
				.subscribe(t -> threadLogger.accept("received "+t));
	}
}
