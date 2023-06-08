package com.devex.reactive.functioanlendpoints;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.devex.reactive.dto.RequestDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component("functionalService")
public class FunctionalService 
{
	 public Mono<ServerResponse> monoExample(ServerRequest request)
	 {
		 Mono<String> mono = Mono.just("Hello Functional");
		 return ServerResponse.ok().body(mono,String.class);
	 }
	 
	 public Mono<ServerResponse> fluxExample(ServerRequest request)
	 {
		 Flux<String> flux = Flux.just("MI","CSK","GT");
		 return ServerResponse.ok().body(flux,String.class);
	 }
	 
	 public Mono<ServerResponse> streamExample(ServerRequest request)
	 {
		 Flux<String> flux = Flux.just("MI","CSK","GT");
		 return ServerResponse
				    .ok()
				    .contentType(MediaType.TEXT_EVENT_STREAM)
				 	.body(flux,String.class);
	 }
	 
	 public Mono<ServerResponse> postExample(ServerRequest serverRequest)
	 {
		 Mono<RequestDTO> request = serverRequest.bodyToMono(RequestDTO.class);
		 
		 Mono<String> mono = request.map(l -> l.getLeagueName()+" saved");
		 return ServerResponse.ok().body(mono,String.class);
	 }
	 
	 public Mono<ServerResponse> errorExample(ServerRequest request)
	 {
		 return Mono.error(new RuntimeException());
	 }
	 
	 public Mono<ServerResponse> requestUtilsExample(ServerRequest serverRequest)
	 {
		
		 System.out.println("HEADERS ==> "+serverRequest.headers());
		 System.out.println("QUERYPARAM ==> "+serverRequest.queryParams());
		 
		 
		 return ServerResponse.ok().bodyValue("ACCEPTED");
	 }
}
