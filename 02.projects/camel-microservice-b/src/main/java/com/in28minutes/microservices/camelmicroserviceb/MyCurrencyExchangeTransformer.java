package com.in28minutes.microservices.camelmicroserviceb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCurrencyExchangeTransformer {

    public CurrencyExchange transformMessage(CurrencyExchange currencyExchange) {
        currencyExchange.setConversionMultiple(
                currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN)
        );

        return currencyExchange;

    }
}
