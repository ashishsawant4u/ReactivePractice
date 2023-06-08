package com.devex.reactive.functioanlendpoints;

import java.util.function.BiFunction;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig 
{
	
	@Resource(name = "functionalService")
	FunctionalService functionalService;
	
	
	@Bean
	public RouterFunction<ServerResponse> routes()
	{
		return RouterFunctions
					.route()
					.GET("/funMono",functionalService::monoExample)
					.GET("/funFlux",functionalService::fluxExample)
					.GET("/funStream",functionalService::streamExample)
					.POST("/funPost",functionalService::postExample)
					.GET("/funError",functionalService::errorExample)
					.onError(RuntimeException.class, runtimeExHandler)
					.GET("/funReq",functionalService::requestUtilsExample)
					.build();
	}
	
	
	BiFunction<Throwable, ServerRequest, Mono<ServerResponse>>  runtimeExHandler =
			(error, request) ->{
			
				return ServerResponse.badRequest().bodyValue("Something Went Wrong!!");
			};
			
			
	
	@Bean
	public RouterFunction<ServerResponse> routes2()
	{
		return RouterFunctions
					.route()
					.path("controllerA", this::routes)
					.build();
	}
}
