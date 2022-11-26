package com.sabindangi.springboot;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesProducer.class);
    private String topicName="wikimedia_recentchange";

    private KafkaTemplate<String,String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {

        //LOGGER.info(String.format("Message sent -> %s", message));

        // kafkaTemplate.send(topicName, message);
        EventHandler eventHandler = new WikimediaChangesHandler(topicName,kafkaTemplate) ;

        String url="https://stream.wikimedia.org/v2/stream/recentchange";

        EventSource.Builder builder=new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource=builder.build();

        eventSource.start();
        TimeUnit.MINUTES.sleep(10);
    }
}
