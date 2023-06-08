package com.devex.reactive.webclient;

import java.util.Map;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfiguration 
{
	Map<String, String> queryparams = Map.of(
				
				"paramA","AAAA",
				"paramB","BBBB"
			
			);
			
	Consumer<HttpHeaders> headersConsumer = h -> {
	
		h.set("headerX", "XXXXXX");
		h.set("headerY", "YYYYYY");
	};
	
	
	ExchangeFilterFunction filter = (clientRequest , next) ->{
	
		if(clientRequest.attribute("usecase").isPresent())
		{
			ClientRequest newReq =  ClientRequest
					     .from(clientRequest)
						 .headers(h-> h.set("usecase", clientRequest.attribute("usecase").get().toString()))
						 .build();
			return next.exchange(newReq);
		}
		return next.exchange(clientRequest);
		
	};
	
	
	@Bean
	public WebClient webClient()
	{
		
		String queryString = "?paramA={paramA}&paramB={paramB}";
		
		return WebClient.builder()
						//.baseUrl("http://localhost:7037")
						.baseUrl("http://localhost:7037"+queryString)
						.defaultHeaders(headersConsumer)
						.defaultUriVariables(queryparams)
						.filter(filter)
						.build();
	}
	
	
	
	
	
	
	
}
