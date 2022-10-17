package com.retrospecsoptometrists.service.authentication.services;

import com.generate.avroschema.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
public class Producer {

    @Value("${topic.user}")
    private String userTopic;

    @Autowired
    private KafkaTemplate<String, User> userKafkaTemplate;

    public void sendToUserCreateUpdateEventStream(User user) {
        final ProducerRecord<String, User> producerRecord = new ProducerRecord<>(userTopic, user);
        userKafkaTemplate.send(producerRecord);
        log.info("Successfully sent User Profile to User Update Event Stream");
    }
}
