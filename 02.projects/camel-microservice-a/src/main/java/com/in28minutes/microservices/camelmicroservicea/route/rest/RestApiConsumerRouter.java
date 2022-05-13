package com.in28minutes.microservices.camelmicroservicea.route.rest;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestApiConsumerRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .host("localhost")
                .port(8000);

        from("timer:rest-api-consumer?period=10000")
                .log("${body}")
                .setHeader("from", () -> "EUR")
                .setHeader("to", () -> "USD")
                .to("rest:get:/currency-exchange/{from}/to/{to}")
                .log("${body}");
    }
}
