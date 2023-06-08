package com.devex.reactive;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class OperatorsExamples 
{
	
	@PostConstruct
	public void init()
	{
		//filterExample();
		//mapExample();
		//handleExample();
		//doCallbacksExample();
		//limitRateExample();
		//onErrorExample();
		//timeoutExample();
		//defaultIfEmptyExample();
		//switchIfEmptyExample();
		//transformExample();
		//switchOnFirst();
		//flatMapExample();
		
		
	}
	
	public void filterExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .filter(t -> t.endsWith("K"));
		
		teamsFlux.subscribe(t -> log.warn(t));
	}
	
	public void mapExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .map(t -> t.toLowerCase());
		
		teamsFlux.subscribe(t -> log.warn(t));
	}
	
	public void handleExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									.handle(
											(data,sink) ->{
												
												if(data.endsWith("K"))
												{
													sink.next(data);
												}
												if(data.equals("GT"))
												{
													sink.complete();
												}
											}
											);
		teamsFlux.subscribe(t -> log.warn(t));
	}
	
	public void doCallbacksExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
				.doAfterTerminate(() -> log.warn("doAfterTerminate"))
				.doFinally(signal -> log.warn("doFinally "+signal))
				.doFirst(() -> log.warn("doFirst"))
				.doOnCancel(() -> log.warn("doOnCancel"))
				.doOnComplete(() -> log.warn("doOnComplete"))
				.doOnDiscard(String.class, t -> log.warn("doOnDiscard "+t))
				.doOnEach(t -> log.warn("doOnEach "+t))
				.doOnError(error -> log.warn("doOnError "+error.getMessage()))
				.doOnRequest(request -> log.warn("doOnRequest "+request))
				.doOnSubscribe(subscription -> log.warn("doOnSubscribe "+subscription))
				.doOnTerminate(() -> log.warn("doOnTerminate"))
				.doOnNext(t -> log.warn("doOnNext "+t));
	
		teamsFlux.subscribe(t -> log.warn(t));
	}
	
	public void limitRateExample()
	{
		
		List<String> playersList = new ArrayList<>();
		
		for (int i = 0; i < 20; i++) 
		{
			playersList.add("Player-"+i);
		}
		
		Flux<String> teamsFlux = Flux.fromIterable(playersList)
										 .log()
										 .limitRate(5,2);
										
		teamsFlux.subscribe(t -> log.warn(t));		
	}
	
	public void onErrorExample()
	{
		List<String> playersList = new ArrayList<>();
		
		for (int i = 0; i < 50; i++) 
		{
			playersList.add("Player-"+i);
		}
		
		
		Flux<Object> teamsFlux = Flux.fromIterable(playersList)
					.handle(
							(data,sink) ->{
								if(data.endsWith("9"))
								{
									sink.error(new RuntimeException("CODE FATA #### "));
								}
								else
								{
									sink.next(data);
								}
							}
							)
//					.onErrorReturn("Anything");
//					.onErrorResume(
//							err -> {
//								log.warn(err.getMessage());
//								return Flux.just("Anything");
//							});
							
						.onErrorContinue(
								(err,data) ->{
									log.warn("error ==> "+err.getMessage());
									log.warn("data caused error ==> "+data);
								}
								);
					
		
		teamsFlux.subscribe(
				t -> log.warn(t+""),
				err -> log.warn(err.getMessage())
				);	
	}
	
	public void timeoutExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		
		List<String> fallbackTeams = Arrays.asList("Ind","Aus","Pak","SA","Eng","Nz","Ban");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams)
									  .delayElements(Duration.ofSeconds(3));
		
		Flux<String> fallbackTeamsFlux = Flux.fromIterable(fallbackTeams)
											.delayElements(Duration.ofMillis(300));
		
		teamsFlux
				.timeout(Duration.ofSeconds(1), fallbackTeamsFlux)
				.subscribe(t -> log.warn(t));
	}
	
	public void defaultIfEmptyExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams);
		
		teamsFlux
				.filter(t -> t.equals("GL"))
				.defaultIfEmpty("BACKUP_TEAM")
				.subscribe(t -> log.warn(t));
		
	}
	
	public void switchIfEmptyExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		List<String> fallbackTeams = Arrays.asList("Ind","Aus","Pak","SA","Eng","Nz","Ban");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams);
		Flux<String> fallbackTeamsFlux = Flux.fromIterable(fallbackTeams);
		
		teamsFlux
				.filter(t -> t.equals("GL"))
				.switchIfEmpty(fallbackTeamsFlux)
				.subscribe(t -> log.warn(t));
	
	}
	
	public void transformExample()
	{
		List<String> teams = Arrays.asList("MI","CSK","GT","RR","PK","RCB","SRH","DC","KKR");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams);
		
		
		Function<Flux<String>, Flux<String>> transformHandler = (f) ->
																	f
																	.filter(t -> t.contains("K"))
																	.map(t -> t.toLowerCase()+"-IPL");
	
																	
		teamsFlux
				.transform(transformHandler)
				.subscribe(t -> log.warn(t));														
	}
	
	public void switchOnFirst()
	{
		List<String> ranks = Arrays.asList("1","2","3","4","5");
		
		Flux<String> ranksFlux = Flux.fromIterable(ranks);
		
		Function<Flux<String>, Flux<Integer>> transformHandler = (f) ->
								f.map(t -> Integer.parseInt(t));
		
		ranksFlux
				.switchOnFirst(
						(signal,flux) ->{
							log.warn("switchOnFirst");
							if(signal.isOnNext() && signal.get() instanceof String)
							{
								log.warn("if");
								return transformHandler.apply(flux);
							}
							else
							{
								log.warn("else");
								return flux;
							}
						}
						)
				.subscribe(t -> log.warn(""+t));
		
	}
	
	public void flatMapExample()
	{
		List<String> leagues = List.of("IPL","PSL","BBL");
		
		List<String> iplTeams = List.of("Mumbai Indians","Chennai Super Kings","Gujrat Titans");
		List<String> pslTeams = List.of("Islamabad United","Multan Sultans","Peshawar Zalmi");
		List<String> bblTeams = List.of("Perth Scorchers","Melbourne Stars","Adelaide Strikers");
		
		Map<String, Flux<String>> leagueTeams = Map.of(
					"IPL",Flux.fromIterable(iplTeams),
					"PSL",Flux.fromIterable(pslTeams),
					"BBL",Flux.fromIterable(bblTeams)
				);
		
		
		Flux<String> leaguesFlux = Flux.fromIterable(leagues);
		
		leaguesFlux
				//.map(l -> leagueTeams.get(l))
				.flatMap(l -> leagueTeams.get(l))
				.subscribe(t -> log.warn(""+t));
		
	}
}
