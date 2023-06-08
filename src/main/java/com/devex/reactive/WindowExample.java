package com.devex.reactive;

import java.time.Duration;
import java.util.Random;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class WindowExample 
{
	@PostConstruct
	public void init()
	{
		//basicExample();
	}
	
	public void basicExample()
	{
		
		Consumer<Flux<String>> reader = f -> f.subscribe(bc -> log.warn(""+bc));
		
		Flux<String> ballCommentry = Flux.interval(Duration.ofMillis(100))
				 .map(i -> "ball "+(i+1)+" runs "+new Random().nextInt(6));
		
		ballCommentry
					.window(6)
					.take(3)
					.subscribe(bc -> reader.accept(bc));
	}
}
