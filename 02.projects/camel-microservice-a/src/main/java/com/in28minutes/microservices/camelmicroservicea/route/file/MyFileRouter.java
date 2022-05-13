package com.in28minutes.microservices.camelmicroservicea.route.file;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class MyFileRouter extends RouteBuilder {
    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {
        String reusableEndpoint = "direct:log-file-values";

        from("file:files/input")
                .routeId("Files-Input-Route")
                .transform().body(String.class)
                .choice()
//                    .when(simple("${file:ext} ends with 'xml'"))
                .when(method(deciderBean))
                        .log("XML FILE")
                    .when(simple("${body} contains USD")) //works if body is string, need transformation
                        .log("Not an XML FILE but contains USD")
                    .otherwise()
                        .log("Not an XML FILE")
                .end()
                .log("${body}")
                .log("${messageHistory}")
                .to("file:files/output")
                .to(reusableEndpoint);

        from(reusableEndpoint)
                .log("${messageHistory} ${file:absolute.path}")
                .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
                .log("${file:onlyname.noext} ${file:parent} ${file:path} ${file:absolute}")
                .log("${file:size} ${file:modified}")
                .log("${routeId} ${camelId} ${body}");

    }

}

@Component
class DeciderBean {

    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(@Body String body,
                                      @Headers Map<String,String> headers,
                                      @ExchangeProperties Map<String,String> exchangeProperties) {

        logger.info("DeciderBean {} {} {}", body, headers, exchangeProperties);
        return true;
    }
}