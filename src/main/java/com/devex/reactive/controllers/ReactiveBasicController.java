package com.devex.reactive.controllers;

import java.time.Duration;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.devex.reactive.dto.LiveScoreDto;
import com.devex.reactive.dto.RequestDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactx")
public class ReactiveBasicController 
{
	
	 @GetMapping("/playerruns")
	 public Mono<Integer> playerRuns()
	 {
		 return Mono.fromSupplier(() -> new Random().nextInt(10000));
	 }
	 
	 @GetMapping(value="/livescore",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	 public Flux<LiveScoreDto> liveScore()
	 {
		return  Flux.create(sink -> {
			
			 for (int i = 1; i <= 120; i++) 
			 {
				 sink.next(new LiveScoreDto(i, new Random().nextInt(6)));
			 }
			 
			 sink.complete();
			 
		 })
		 .delayElements(Duration.ofSeconds(2))
		 .cast(LiveScoreDto.class)
		 .log();
	 }
	 
	 @PostMapping("/saveleague")
	 public Mono<String> teamForLeague(@RequestBody Mono<RequestDTO> request)
	 {
		 return request.map(l -> l.getLeagueName()+" saved");
	 }
	
	 
	 @PostMapping("/saveleagueError")
	 public Mono<ResponseEntity<String>> teamForLeague2(@RequestBody Mono<RequestDTO> request)
	 {
		 return Mono.just(new ResponseEntity<String>("something went wrong",HttpStatus.BAD_REQUEST));
		 //return Mono.just(ResponseEntity.badRequest().body("something went wrong"));
	 }
	 
}
