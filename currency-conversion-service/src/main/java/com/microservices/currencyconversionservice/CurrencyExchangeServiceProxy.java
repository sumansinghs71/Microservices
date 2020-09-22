package com.microservices.currencyconversionservice;


import org.springframework.cloud.netflix.ribbon.RibbonClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="currency-exchange-service", url="localhost:8000")  
//Enabling feign  
//@FeignClient(name="currency-exchange-service")
// We are trying route throught the gateway
@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
	//@GetMapping("/currency-exchange/from/{from}/to/{to}") -- here syntax @PathVariable("from")  is mandatory
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	// Since using the Zuul we have to add aaplication name before the service
	@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retrieveExchangeValue
		(@PathVariable("from") String from, @PathVariable("to") String to);
}