package com.in28minutes.microservices.camelmicroservicea.route.patterns;

import com.in28minutes.microservices.camelmicroserviceb.CurrencyExchange;
import org.apache.camel.Body;
import org.apache.camel.DynamicRouter;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Map;

@Component
public class EipPatternsRouter extends RouteBuilder {
    @Autowired
    private Splitter splitter;
    @Autowired
    private DynamicRouterBean dynamicRouterBean;

    @Override
    public void configure() throws Exception {

        //enable tracing for debuging issues
        getContext().setTracing(true);
        //if error handle fails - send to dead-letter
        errorHandler(deadLetterChannel("activemq:dead-letter-queue"));

        // Patterns:
        //Pipeline - default
        //Content Based Routing: when choice()
        //Multicast: when many routes in to()
        //Split
        //Aggregate: convert list of messages to one and redirect to endpoint
        //routing slip
        //dynamic routing

//        //Multicast
//        from("timer:rest-api-consumer?period=10000")
//                .multicast()
//                .to("log:something1", "log:something2");

        //Split
//        from("file:files/csv")
//                .unmarshal()
//                    .csv()//if not provide, result will be just full file without parsing
//                .split(body())
//                .to("log:split-files");

        //Split 2
        //message, message2, message3
//        from("file:files/csv")
//                .convertBodyTo(String.class)
//                .split(body(), ",") //split by comma
//                .to("log:split-files");

        //Split 3
//        from("file:files/csv")
//                .convertBodyTo(String.class)
//                .split(method(splitter))
//                .to("log:split-files");

        //Aggregate
//        from("file:files/aggregate-json")
//                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
//                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
//                .completionSize(3)
////                .completionTimeout(HIGHEST)
//                .to("log:aggregate-json");

        String routingSlip = "direct:endpoint1,direct:endpoint2";
        //routing slip
        // timePeriod took from property file
        from("timer:routingSlip&period={{timePeriod}}")
                .transform().constant("My message is hardcoded")
                .routingSlip(simple(routingSlip));

        from("direct:endpoint1")
                .to("{{endpoint-for-logging}}"); //took from property files
        from("direct:endpoint2")
                .to("log:direct-endpoint2");
        from("direct:endpoint3")
                .to("log:direct-endpoint3");

        //dynamic routing
        from("timer:dynamicRouting&period=10000")
                .transform().constant("My message is hardcoded")
                .dynamicRouter(method(dynamicRouterBean));

    }
}

@Component
class Splitter {
    public List<String> split(String body) {
        return null;
    }
}

@Component
class DynamicRouterBean {
    private int invocations;

    public String decideTheNextEndpoint(
            @ExchangeProperties Map<String, String> properties,
            @Headers Map<String, String> headers,
            @Body String body) {
        invocations++;

        String endpoint = null;

        if (invocations % 3 == 0) {
            endpoint = "direct:endpoint1";
        }
        if (invocations % 3 == 1) {
            endpoint = "direct:endpoint2,direct:endpoint3";
        }

        return endpoint;
    }
}