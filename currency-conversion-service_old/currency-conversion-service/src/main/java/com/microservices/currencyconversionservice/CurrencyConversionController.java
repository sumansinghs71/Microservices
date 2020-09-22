package com.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){
		// Feign Problem-1, here we have to write lot of code, instead we use convertCurrencyfeign as an alternative
		Map<String,String> uriVariables=new HashMap<>();
		uriVariables.put("from",from);
		uriVariables.put("to",to);
		ResponseEntity<CurrencyConversionBean> response = new RestTemplate().getForEntity("http://localhost:8001/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean responsebean=response.getBody();
		return new CurrencyConversionBean(responsebean.getId(),from,to,responsebean.getConversionMultiple(),quantity,quantity.multiply(responsebean.getConversionMultiple()),responsebean.getPort());
	}

	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyfeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){
		
		CurrencyConversionBean responsebean=currencyExchangeServiceProxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversionBean(responsebean.getId(),from,to,responsebean.getConversionMultiple(),quantity,quantity.multiply(responsebean.getConversionMultiple()),responsebean.getPort());
	
	}
}