package com.devex.reactive;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class ContextExample 
{
	@PostConstruct
	public void init()
	{
		//contextExample();
	}
	
	public void contextExample()
	{
		
		Flux<String> teamsFlux = Flux.deferContextual(context ->{
			
			if(context.hasKey("authorization"))
			{
				return Flux.just("MI","CSK");
			}
			else
			{
				return Flux.error(new RuntimeException("401:Unathorized"));
			}
			
		});
		
		teamsFlux
			.contextWrite(context -> context.put("authorization", "baisc_nc934c3c4jm94c4j30jc4"))
			.subscribe(t -> log.warn("subscriber received ==> "+t));	
		
		
		
	}
}
