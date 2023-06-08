package com.devex.reactive;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

public class StepVarifierDemo 
{
	@Test
	public void fluxTestExample()
	{
		Flux<String> teamsFlux = Flux.just("MI","CSK","GT");
		
		StepVerifier.create(teamsFlux)
			.expectNext("MI")
			.expectNext("CSK")
			.expectNext("GT")
			.expectComplete()
			.verify();
	}
	
	@Test
	public void fluxTestExample2()
	{
		Flux<String> teamsFlux = Flux.just("MI","CSK","GT");
		
		StepVerifier.create(teamsFlux)
			.expectNext("MI","CSK","GT")
			.expectComplete()
			.verify();
	}
	
	@Test
	public void fluxTestErrorExample()
	{
		Flux<Object> dataFlux = Flux.just(100,200);
		Flux<Object> errorFlux = Flux.error(new RuntimeException("ERROR"));
		
		
		Flux<Object> commonFlux =  Flux.concat(dataFlux,errorFlux);
		
		StepVerifier.create(commonFlux)
			.expectNext(100,200)
			.expectError()
			.verify();
	}
	
	@Test
	public void fluxTestCountExample()
	{
		Flux<String> teamsFlux = Flux.just("MI","CSK","GT");
		
		StepVerifier.create(teamsFlux)
			.expectNextCount(3)
			.expectComplete()
			.verify();
	}
	
	@Test
	public void fluxTestConsumeWhileExample()
	{
		Flux<Integer> dataFlux = Flux.just(100,200);
		
		StepVerifier.create(dataFlux)
			.thenConsumeWhile(n -> n >= 100)
			.expectComplete()
			.verify();
	}
	
	
	
	@Test
	public void fluxTestAssertExample()
	{
		List<Player> players =  List.of(
											new Player("rohit",9000)
										);
		
		
		
		Flux<Player> dataFlux = Flux.fromIterable(players);
		
		StepVerifier.create(dataFlux)
			.assertNext(p -> Assertions.assertNotNull(p.getName()))
			.expectComplete()
			.verify();
	}
	
	@Test
	public void fluxTestDelayExample()
	{
		Flux<String> teamsFlux = Flux.just("MI","CSK","GT")
				                     .delayElements(Duration.ofSeconds(3));
		
		StepVerifier.create(teamsFlux)
			.expectNextCount(3)
			.expectComplete()
			.verify(Duration.ofSeconds(2));
	}
	
	@Test
	public void fluxTestScenarioNameExample()
	{
		Flux<String> teamsFlux = Flux.just("MI","CSK","GT");
		
		
		StepVerifierOptions options = StepVerifierOptions.create().scenarioName("teams-testcase");
		
		StepVerifier.create(teamsFlux,options)
			.expectNextCount(2)
			.expectComplete()
			.verify(Duration.ofSeconds(2));
	}
	
	@Test
	public void fluxTestCaseNameExample()
	{
		Flux<String> teamsFlux = Flux.just("MI","CSK","GT");
		
		StepVerifier.create(teamsFlux)
			.expectNext("MI")
			.as("MI-testcase")
			.expectNext("CSK")
			.as("CSK-testcase")
			.expectNext("RR")
			.as("RR-testcase")
			.expectComplete()
			.verify();
	}
	
	
	@Test
	public void fluxTestContextExample()
	{
		Flux<String> teamsFlux = Flux.deferContextual(context -> {
			
			if(context.hasKey("auth"))
			{
				return Flux.just("MI","CSK","GT");
			}
			else
			{
				return Flux.error(new RuntimeException("401:UnAuthorized"));
			}
			
		});
									  
		
		
		StepVerifierOptions options = StepVerifierOptions.create().withInitialContext(Context.of("auth", "randomtoken"));
		
		StepVerifier.create(teamsFlux,options)
			.expectNextCount(3)
			.expectComplete()
			.verify();
	}
	
	
	
}

@Data
@AllArgsConstructor
class Player
{
	private String name;
	private Integer runs;
}

