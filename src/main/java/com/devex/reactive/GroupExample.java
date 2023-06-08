package com.devex.reactive;

import java.util.Random;
import java.util.function.BiConsumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class GroupExample 
{
	@PostConstruct
	public void init()
	{
		//basicExample();
	}
	
	public void basicExample()
	{
		
		BiConsumer<Flux<String>, Boolean> reader = (f,key) -> f.subscribe(bc -> {
			log.warn("isBoundry "+key+" ==> "+bc);
			
		});
		
		Flux<String> ballCommentry = Flux
				 .range(1, 12)
				 .map(i -> "ball "+(i+1)+" runs "+new Random().nextInt(6));
		
		
		
		
		ballCommentry
					.groupBy(b -> Integer.parseInt(b.split(" ")[3]) >= 4)
					.subscribe(bc -> reader.accept(bc,bc.key()));
	}
}
