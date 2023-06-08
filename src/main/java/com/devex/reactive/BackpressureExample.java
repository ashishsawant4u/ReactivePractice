package com.devex.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class BackpressureExample 
{
	@PostConstruct
	public void init()
	{
		//bufferDropExample();
		//bufferLatestExample();
		//bufferErrorExample();
		//bufferDropTrackerExample();
	}
	
	public void bufferDropExample()
	{
		
		System.setProperty("reactor.bufferSize.small", "20");
		
		Function<Integer, String> ballCommentry = (ball) -> "ball-"+ball+" runs ==> "+new Random().nextInt(6); 
		
		Flux<Object> commentryFlux =  Flux.create(sink ->{
			
			for (int i = 1; i <= 50; i++) 
			{
				log.warn("emitting ball ==> "+i);
				sink.next(ballCommentry.apply(i));
			}
			sink.complete();
		})
		.onBackpressureDrop();

		commentryFlux
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(d -> {
					
					try 
					{
						Thread.sleep(100);
					} catch (Exception e) {}
				})
				.subscribe(b -> log.warn("received ==> "+b));
		
	}
	
	public void bufferLatestExample()
	{
		
		System.setProperty("reactor.bufferSize.small", "20");
		
		Function<Integer, String> ballCommentry = (ball) -> "ball-"+ball+" runs ==> "+new Random().nextInt(6); 
		
		Flux<Object> commentryFlux =  Flux.create(sink ->{
			
			for (int i = 1; i <= 50; i++) 
			{
				log.warn("emitting ball ==> "+i);
				sink.next(ballCommentry.apply(i));
			}
			
			sink.complete();
		})
		.onBackpressureLatest();
		
		commentryFlux
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(d -> {
					
					try 
					{
						Thread.sleep(100);
					} catch (Exception e) {}
				})
				.subscribe(b -> log.warn("received ==> "+b));
		
	}
	
	public void bufferErrorExample()
	{
		
		System.setProperty("reactor.bufferSize.small", "20");
		
		Function<Integer, String> ballCommentry = (ball) -> "ball-"+ball+" runs ==> "+new Random().nextInt(6); 
		
		Flux<Object> commentryFlux =  Flux.create(sink ->{
			
			for (int i = 1; i <= 50; i++) 
			{
				if(!sink.isCancelled())
				{
					log.warn("emitting ball ==> "+i);
					sink.next(ballCommentry.apply(i));
				}
			}
			
			sink.complete();
		})
		.onBackpressureError();
		
		commentryFlux
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(d -> {
					
					try 
					{
						Thread.sleep(100);
					} catch (Exception e) {}
				})
				.subscribe(b -> log.warn("received ==> "+b));
		
	}
	
	public void bufferDropTrackerExample()
	{
		
		System.setProperty("reactor.bufferSize.small", "20");
		
		Function<Integer, String> ballCommentry = (ball) -> "ball-"+ball+" runs ==> "+new Random().nextInt(6); 
		
		List<String> droppedBalls = new ArrayList<>();
		
		Flux<Object> commentryFlux =  Flux.create(sink ->{
			
			for (int i = 1; i <= 50; i++) 
			{
				log.warn("emitting ball ==> "+i);
				sink.next(ballCommentry.apply(i));
			}
			sink.complete();
		})
		.onBackpressureDrop(dropped -> droppedBalls.add(""+dropped));

		commentryFlux
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(d -> {
					
					try 
					{
						Thread.sleep(100);
					} catch (Exception e) {}
				})
				.subscribe(b -> log.warn("received ==> "+b));
		
		log.warn("dropped balls");
		droppedBalls.forEach(d -> log.warn(d));
	}
}
