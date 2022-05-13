package com.in28minutes.microservices.camelmicroserviceb.routes;

import com.in28minutes.microservices.camelmicroserviceb.CurrencyExchange;
import com.in28minutes.microservices.camelmicroserviceb.MyCurrencyExchangeProcessor;
import com.in28minutes.microservices.camelmicroserviceb.MyCurrencyExchangeTransformer;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.crypto.CryptoDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;


//@Component
public class ActiveMqReceiverRouter extends RouteBuilder {


    @Autowired
    private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;
    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;
    @Override
    public void configure() throws Exception {
//        from("activemq:my-activemq-queue")
//                .unmarshal()
//                .json(JsonLibrary.Jackson, CurrencyExchange.class)
//                .bean(myCurrencyExchangeProcessor)
//                .bean(myCurrencyExchangeTransformer)
//                .to("log:received-message-from-active-mq");

        // with decryption - key should be copied to resource
        from("activemq:my-activemq-queue")
                .unmarshal(createEncryptor())
                .to("log:received-message-from-active-mq");
    }


    private CryptoDataFormat createEncryptor() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        ClassLoader classLoader = getClass().getClassLoader();
        keyStore.load(classLoader.getResourceAsStream("myDesKey.jceks"), "someKeystorePassword".toCharArray());
        Key sharedKey = keyStore.getKey("myDesKey", "someKeyPassword".toCharArray());

        CryptoDataFormat sharedKeyCrypto = new CryptoDataFormat("DES", sharedKey);
        return sharedKeyCrypto;
    }

}
