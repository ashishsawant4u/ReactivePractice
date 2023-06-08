package com.devex.reactive.webclient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.devex.reactive.dto.RequestDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wc")
public class WebClientExamples 
{
	@Autowired
	WebClient webClient;
	
	
	@GetMapping("/funMonoWc")
	public Mono<String> funMonoWc()
	{
		return webClient.get().uri("funMono").retrieve().bodyToMono(String.class)
					.map(s-> "Webclient "+s);
	}
	
	@GetMapping("/funFluxWc")
	public Flux<String> funFluxWc()
	{
		return webClient.get().uri("funFlux").retrieve().bodyToFlux(String.class)
					.map(s-> "Webclient "+s);
	}
	
	@GetMapping("/funStreamWc")
	public Flux<String> funStreamWc()
	{
		return webClient.get().uri("funStream").retrieve().bodyToFlux(String.class)
					.map(s-> " Webclient "+s);
	}
	
	@GetMapping("/funPostWc")
	public Mono<String> funPostWc()
	{
		
		RequestDTO req = new RequestDTO("IPL", List.of("MI","CSK","GT"));
		
		return webClient
					.post()
					.uri("funPost")
					.bodyValue(req)
					.retrieve()
					.bodyToMono(String.class)
					.map(s-> " Webclient "+s);
	}
	
	@GetMapping("/funErrorWc")
	public Mono<String> funErrorWc()
	{
		return webClient.get().uri("funError").retrieve().bodyToMono(String.class)
					.map(s-> " Webclient "+s)
					.doOnError(err -> System.out.println("GOT ERROR "+err.getMessage()));
	}
	
	@GetMapping("/funErrorWc2")
	public Mono<String> funErrorWcUsingExchange()
	{
		return webClient.get().uri("funError")
				    .exchangeToMono(clientResponse -> {
				    	
				    	  if(clientResponse.rawStatusCode() == 400)
				    	  {
				    		  return clientResponse.bodyToMono(RuntimeException.class);
				    	  }
				    	  else
				    	  {
				    		  return clientResponse.bodyToMono(String.class);
				    	  }
				    })
					.map(s-> " Webclient "+s)
					.doOnError(err -> System.out.println("GOT ERROR "+err.getMessage()));
	}
	
	@GetMapping("/funReqWc")
	public Mono<String> funReqWc()
	{
		return webClient
				.get()
				.uri("funReq")
				.attribute("usecase", "approvalflow")
				.retrieve()
				.bodyToMono(String.class)
				.map(s-> "Webclient "+s);
	}
}
