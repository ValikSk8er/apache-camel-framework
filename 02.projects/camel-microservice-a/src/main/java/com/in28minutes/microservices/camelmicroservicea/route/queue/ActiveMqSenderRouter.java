package com.in28minutes.microservices.camelmicroservicea.route.queue;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.crypto.CryptoDataFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        int timerPeriodMs = 10000;

        //timer
//        from("timer:active-mq-timer?period=" + timerPeriodMs)
//                .transform().constant("My message for Active MQ")
//                .log("${body}")

//        //queue
//                .to("activemq:my-activemq-queue");

//        from("file:files/json")
//                .log("${body}")
//                .to("activemq:my-activemq-queue");

        //encrypt - the encrypt key should be copied to resource folder
        from("timer:active-mq-timer?period=" + timerPeriodMs)
                .transform().constant("My message for Active MQ")
                .marshal(createEncryptor())
                .to("activemq:my-activemq-queue");
    }

    private CryptoDataFormat createEncryptor() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        ClassLoader classLoader = getClass().getClassLoader();
        keyStore.load(classLoader.getResourceAsStream("myDesKey.jceks"),
                "someKeystorePassword".toCharArray());
        Key sharedKey = keyStore.getKey("myDesKey", "someKeyPassword".toCharArray());

        CryptoDataFormat sharedKeyCrypto = new CryptoDataFormat("DES", sharedKey);
        return sharedKeyCrypto;
    }

}
