package com.swiftlogistics.logistics_middleware.adapter.wms;

import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.Socket;

@Component
public class WmsAdapter {
    private static final Logger log = LoggerFactory.getLogger(WmsAdapter.class);

    private static final String WMS_HOST = "mock-wms-host.com";
    private static final int WMS_PORT = 1234;

    //Listens for messages on the middleware_queue and process the order

    @RabbitListener(queues = "middleware_queue")
    public void receiveOrderFromQueue(Order order){
        log.info("WMS Adapter received a new order from RabbitMQ: {}", order);

        String wmsMessage = String.format("ORDER|%s|%s", order.getId(), order.getClientName());

        try (Socket socket = new Socket(WMS_HOST, WMS_PORT);
            OutputStream out = socket.getOutputStream()){

            log.info("Attempting to connect to WMS at {}:{}", WMS_HOST, WMS_PORT);

            out.write(wmsMessage.getBytes());
            out.flush();
        }catch (Exception e){
            log.error("Failed to connect or send message to WMS, ERROR: {}", e.getMessage());

        }

    }
}
