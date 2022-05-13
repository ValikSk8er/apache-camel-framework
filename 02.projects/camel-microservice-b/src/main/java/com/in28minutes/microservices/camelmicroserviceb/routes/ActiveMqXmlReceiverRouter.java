package com.in28minutes.microservices.camelmicroserviceb.routes;

import com.in28minutes.microservices.camelmicroserviceb.CurrencyExchange;
import com.in28minutes.microservices.camelmicroserviceb.MyCurrencyExchangeProcessor;
import com.in28minutes.microservices.camelmicroserviceb.MyCurrencyExchangeTransformer;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//@Component
public class ActiveMqXmlReceiverRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("activemq:my-activemq-xml-queue")
                .unmarshal()
                .jacksonxml(CurrencyExchange.class)
                .to("log:received-message-from-active-mq-xml");
    }
}
