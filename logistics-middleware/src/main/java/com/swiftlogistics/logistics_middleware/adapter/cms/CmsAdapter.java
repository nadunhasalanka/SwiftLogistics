package com.swiftlogistics.logistics_middleware.adapter.cms;

import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class CmsAdapter {
    private static final Logger log = LoggerFactory.getLogger(CmsAdapter.class);

    private static final String CMS_ENDPOINT = "http://mock-cms-host.com:8080/api/orders"; // replace with actual CMS endpoint

    /**
     * Listen to RabbitMQ queue and send Order to CMS as XML
     */
    @RabbitListener(queues = "middleware_queue") // same queue as RosAdapter, or create a separate one
    public void receiveOrderFromQueue(Order order) {
        log.info("CMS Adapter received a new order from RabbitMQ: {}", order);

        try {
            // Convert Order object to XML
            JAXBContext context = JAXBContext.newInstance(Order.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(order, xmlWriter);
            String xmlData = xmlWriter.toString();

            log.info("Converted Order to XML:\n{}", xmlData);

            // Send XML to CMS via HTTP POST
            URL url = new URL(CMS_ENDPOINT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(xmlData.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            log.info("CMS responded with HTTP status: {}", responseCode);

        } catch (Exception e) {
            log.error("Failed to send Order to CMS. Error: {}", e.getMessage(), e);
        }
    }
}
