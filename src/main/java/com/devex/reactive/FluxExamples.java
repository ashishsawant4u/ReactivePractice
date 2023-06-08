package com.devex.reactive;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class FluxExamples 
{
	@PostConstruct
	public void init()
	{
		//basicExamples();
		
		//arrayExample();
		
		//listExample();
		
		//streamExample();
		
		//rangeExample();
		
		//customSubscriber();
		
		//fluxVsList();
		
		//fluxInterval();
		
		//fluxFromMono();
		
		//monoFromFlux();
	}
	
	public void basicExamples()
	{
		Flux<String> teams = Flux.just("mumbai indians","chennai super kings","gujrat titans")
								.log();
	
		teams.subscribe(
						team -> log.warn(team),
						err -> log.warn(err.getMessage()),
						() -> log.warn("completed !!"));
	
	}
	
	
	public void arrayExample()
	{
		String teams [] = {"mumbai indians","chennai super kings","gujrat titans"};
	
		Flux<String> teamsFlux = Flux.fromArray(teams).log();
		
		teamsFlux.subscribe(
				team -> log.warn(team),
				err -> log.warn(err.getMessage()),
				() -> log.warn("completed !!"));
	
	}
	
	public void listExample()
	{
		List<String> teams  = Arrays.asList("mumbai indians","chennai super kings","gujrat titans");
	
		Flux<String> teamsFlux = Flux.fromIterable(teams).log();
		
		teamsFlux.subscribe(
				team -> log.warn(team),
				err -> log.warn(err.getMessage()),
				() -> log.warn("completed !!"));
	
	}
	
	
	public void streamExample()
	{
		List<String> teams  = Arrays.asList("mumbai indians","chennai super kings","gujrat titans");
	
		Flux<String> teamsFlux = Flux.fromStream(() -> teams.stream()).log();
		
		teamsFlux.subscribe(
				team -> log.warn(team),
				err -> log.warn(err.getMessage()),
				() -> log.warn("completed subscriber 1 !!"));
		
		teamsFlux.subscribe(
				team -> log.warn(team),
				err -> log.warn(err.getMessage()),
				() -> log.warn("completed subscriber 2 !!"));
	
	}
	
	
	public void rangeExample()
	{
//		Flux.range(11, 5)
//			.subscribe(n -> log.warn(""+n));
		
		List<String> teams  = Arrays.asList("mumbai indians","chennai super kings","gujrat titans");
		
		Flux<Integer> indexFlux = Flux.range(0, 2).log();
		
		indexFlux.subscribe(n -> log.warn(teams.get(n)));
			
	}
	
	public void customSubscriber()
	{
		Flux<Integer> indexFlux = Flux.range(1, 20).log();
		
		
		MySubscriber mySubscriber = new MySubscriber();
		indexFlux.subscribeWith(mySubscriber);
		
		
		
		try 
		{
			mySubscriber.subscriptionRef.request(2);
			Thread.sleep(2000);
			mySubscriber.subscriptionRef.request(2);
			Thread.sleep(2000);
			mySubscriber.subscriptionRef.cancel();
			Thread.sleep(2000);
			mySubscriber.subscriptionRef.request(2);
		} 
		catch (Exception e) 
		{
			log.warn(e.getMessage());
		}
		
	}
	
	
	public void fluxVsList()
	{
		//log.warn("data ==> "+getListData(5).toString());
		
		getFluxData(5).subscribe(i -> log.warn(i+""));
	}
	
	public List<Integer> getListData(int count)
	{
		List<Integer> data = new ArrayList<>();
		
		for (int i = 0; i < count; i++) {
			data.add(i);
		}
		
		try {
			Thread.sleep(3000);
		} catch (Exception e) {}
		
		return data;
	}
	
	public Flux<Integer> getFluxData(int count)
	{
		return Flux.range(0, count)
				.map(i-> {
					
					try {
						Thread.sleep(3000);
					} catch (Exception e) {}
					
					return i;
				});
	}
	
	
	public void fluxInterval()
	{
		
		Consumer<String> threadLogger = (identifier) -> log.warn(Thread.currentThread().getName()+"  ==> "+identifier);
		
		Flux<Long> teamFlux = Flux
								  .interval(Duration.ofSeconds(2))
								  .log();
		
		teamFlux
			.doOnNext(d -> threadLogger.accept("doOnNext "+d))
			.subscribe(i -> log.warn(i+""));
	}
	
	public void fluxFromMono()
	{
		Mono<String> monoRef = Mono.just("mumbai indians");
		
		Flux<String> fluxRef = Flux.from(monoRef).log();
		
		fluxRef.subscribe(t -> log.warn(t));
	}
	
	public void monoFromFlux()
	{
		List<String> teams  = Arrays.asList("mumbai indians","chennai super kings","gujrat titans");
		
		Flux<String> teamsFlux = Flux.fromIterable(teams).log();
		
		Mono<String> teamMono = teamsFlux
								.filter(t -> t.contains("chennai"))
								.next();
		
		teamMono.subscribe(t -> log.warn(t));
									
	}
}



@Slf4j
class MySubscriber implements Subscriber<Integer>
{

	public Subscription subscriptionRef;
	
	@Override
	public void onSubscribe(Subscription s) {
		log.warn("onSubscribe called and got subscription object "+s);
		subscriptionRef = s;
	}

	@Override
	public void onNext(Integer t) {
		log.warn("onNext ==> "+t);
		
	}

	@Override
	public void onError(Throwable t) {
		log.warn("onError ==> "+t.getMessage());
		
	}

	@Override
	public void onComplete() {
		log.warn("onComplete called !!");
		
	}
	
}

