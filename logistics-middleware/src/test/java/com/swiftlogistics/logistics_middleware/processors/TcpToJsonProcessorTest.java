package com.swiftlogistics.logistics_middleware.processors;

import org.apache.camel.Exchange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TcpToJsonProcessorTest extends CamelTestSupport{
    @Test
    public void testTcpToJsonTransformation() throws Exception {
        //Create a new camel object
        Exchange exchange = createExchangeWithBody("SHIPMENT-001");

        //Create an instance for the Processor
        TcpToJsonProecessor processor = new TcpToJsonProcessor();

        //Process the Exchange object
        processor.process(exchange);

        //Get the transformed ody from the Exchange
        String transformedBody = exchange.getIn().getBody(String.class);

        //Assert that the transformed body matches the exepted JSON string
        assertEquals("{\"shipmentId\": \"SHIPMENT-001\"}", transformedBody);

    }
}
