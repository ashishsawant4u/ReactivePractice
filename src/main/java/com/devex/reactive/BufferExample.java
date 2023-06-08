package com.devex.reactive;

import java.time.Duration;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class BufferExample 
{
	@PostConstruct
	public void init()
	{
		//bufferExample();
		//bufferWithTimeExample();
		//bufferTimeoutExample();
		//bufferOverlapExample();
	}
	
	public void bufferExample()
	{
		
		Flux<String> ballCommentry = Flux.interval(Duration.ofMillis(100))
										 .map(i -> "ball "+(i+1)+" runs "+new Random().nextInt(6));
		
		ballCommentry
					.buffer(6)
					.take(3)
					.subscribe(bc -> log.warn(""+bc));
	
	
	}
	
	public void bufferWithTimeExample()
	{
		
		Flux<String> ballCommentry = Flux.interval(Duration.ofMillis(100))
										 .map(i -> "ball "+(i+1)+" runs "+new Random().nextInt(6));
		
		ballCommentry
					.buffer(Duration.ofSeconds(1))
					.subscribe(bc -> log.warn(""+bc));
	
	
	}
	
	public void bufferTimeoutExample()
	{
		
		Flux<String> ballCommentry = Flux.interval(Duration.ofMillis(100))
										 .map(i -> "ball "+(i+1)+" runs "+new Random().nextInt(6));
		
		ballCommentry
					.bufferTimeout(6, Duration.ofSeconds(1))
					.subscribe(bc -> log.warn(""+bc));
	
	
	}
	
	public void bufferOverlapExample()
	{
		
		Flux<String> ballCommentry = Flux.interval(Duration.ofMillis(100))
										 .map(i -> "ball "+(i+1)+" runs "+new Random().nextInt(6));
		
		ballCommentry
					.buffer(6,10)
					.take(20)
					.subscribe(bc -> log.warn(""+bc));
	
	
	}
}
