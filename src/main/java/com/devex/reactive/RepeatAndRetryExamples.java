package com.devex.reactive;

import java.time.Duration;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

@Component
@Slf4j
public class RepeatAndRetryExamples 
{

	@PostConstruct
	public void init()
	{
		//repeatExample();
		//retryExample();
		//retryWithDelayExample();
		//retrySpecExample();
	}
	
	public void repeatExample()
	{
		
		Flux<String> last3Winners = Flux.just("MI","GT","CSK")
			    						.doOnComplete(() -> log.warn("completed...."));
		
		last3Winners.repeat(2)
					.subscribe(t -> log.warn(t));
		
		
	}
	
	
	public void retryExample()
	{
		
		Flux<String> last3Winners = Flux.just("MI","GT","CSK")
										.map(t -> String.valueOf(t.length()/0))
										.doOnError(err -> log.warn("Error "+err.getMessage()))
			    						.doOnComplete(() -> log.warn("completed...."));
		
		last3Winners.retry(2)
					.subscribe(t -> log.warn(""+t));
		
		
	}
	
	public void retryWithDelayExample()
	{
		Flux<String> last3Winners = Flux.just("MI","GT","CSK")
										.map(t -> String.valueOf(t.length()/0))
										.doOnError(err -> log.warn("Error "+err.getMessage()))
										.doOnComplete(() -> log.warn("completed...."));

		last3Winners.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(5)))
					.subscribe(t -> log.warn(""+t));
	}
	
	public void retrySpecExample()
	{
		Flux<Integer> numbersFlux = Flux.create(sink ->{
			
			int num = new Random().nextInt(20);
			
			System.out.println("produced ==> "+num);
			
			if(num < 5)
			{
				sink.error(new RuntimeException("ErrorX"));
			}
			else if(num < 12)
			{
				sink.error(new RuntimeException("ErrorY"));
			}
			else
			{
				sink.next(num);
			}
			
		});
		
		
		numbersFlux.retryWhen(Retry.from( flux -> 
								 flux
								.handle((signal, syncsink) ->{
									if(signal.failure().getMessage().equals("ErrorX"))
									{
										syncsink.next(1);
									}
									else
									{
										syncsink.error(signal.failure());
									}
								})
								.doOnError(err -> log.warn(err.getMessage()))
						
						
						
					))
					.subscribe(n -> log.warn("received ==> "+n));
	}
}
