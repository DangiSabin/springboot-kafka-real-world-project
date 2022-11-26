package com.sabindangi.springboot;

import com.sabindangi.springboot.entity.WikimediaData;
import com.sabindangi.springboot.repository.WikimediaDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDatabaseConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
    private WikimediaDataRepository wikimediaDataRepository;

    public KafkaDatabaseConsumer(WikimediaDataRepository wikimediaDataRepository) {
        this.wikimediaDataRepository = wikimediaDataRepository;
    }

    @KafkaListener(topics="wikimedia_recentchange",groupId="myGroup")
    public void consume(String message){
        LOGGER.info(String.format("Message received -> %s", message));

        WikimediaData wikimediaData = new WikimediaData();
        wikimediaData.setWikiEventData(message);

        wikimediaDataRepository.save(wikimediaData);
    }

}
