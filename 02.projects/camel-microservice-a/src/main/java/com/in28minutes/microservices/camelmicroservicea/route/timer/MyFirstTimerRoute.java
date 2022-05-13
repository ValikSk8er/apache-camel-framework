package com.in28minutes.microservices.camelmicroservicea.route.timer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
public class MyFirstTimerRoute extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;
    @Autowired
    private LogCurrentTimeBean logCurrentTimeBean;

    @Override
    public void configure() throws Exception {
        // queue - mock by timer
        // processing - doesn't apply changes to income message (void methods)
        // transformation - modify income data
        // database - mock by log

        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        from("timer:first-timer") // null
                .log("body: ${body}")
                .transform().constant("My Constant Message")
                .log("body: ${body}")
//                .transform().constant("Time now is " + LocalDateTime.now()) //time doesn't change
//                .bean("getCurrentTimeBean")
//                .bean(getCurrentTimeBean)
                .bean(getCurrentTimeBean, "getCurrentTime")
                .log("body: ${body}")
                .bean(logCurrentTimeBean)
                .process(new SingleLoggingProcessor())
                .log("body: ${body}")
                .to("log:first-timer");  // database - mock by log

    }
}

@Component
class GetCurrentTimeBean {
    public String getCurrentTime() {
        return "Time now is " + LocalDateTime.now();
    }
}


@Component
class LogCurrentTimeBean {
    public void process() {
        System.out.println("LogCurrentTimeBean processing");
    }
}

class SingleLoggingProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getMessage().getBody());
    }
}
