package com.in28minutes.microservices.camelmicroserviceb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyCurrencyExchangeProcessor {
    Logger logger = LoggerFactory.getLogger(getClass());

    public void processMessage(CurrencyExchange currencyExchange) {


        logger.info("Do some processing with currencyExchange:getConversionMultiple: {}",
                currencyExchange.getConversionMultiple());
    }
}
