package com.in28minutes.microservices.camelmicroservicea.route.queue;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqXmlSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        int timerPeriodMs = 10000;
        from("file:files/xml")
                .log("${body}")
                .to("activemq:my-activemq-xml-queue");
    }
}
