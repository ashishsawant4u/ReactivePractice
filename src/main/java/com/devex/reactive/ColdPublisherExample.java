package com.devex.reactive;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class ColdPublisherExample 
{
	@PostConstruct
	public void init()
	{
		//matchHighlightsBallByBall();
	}
	
	public void matchHighlightsBallByBall()
	{
		
		Flux<Object> ballCommentryFlux = Flux.create(fluxSync ->{
			
			for (int i = 1; i <= 120; i++) 
			{
				fluxSync.next(" ball "+i+" ==> "+ new Random().nextInt(6));
			}
			fluxSync.complete();
		})
		.delayElements(Duration.ofSeconds(2));
		
		ballCommentryFlux.subscribe(b->log.warn("[Spectator 1 watching]  "+b));
		
		try 
		{
			Thread.sleep(10000);
		} catch (Exception e) {}
		
		ballCommentryFlux.subscribe(b->log.warn("[Spectator 2 watching]  "+b));
		
	}
}