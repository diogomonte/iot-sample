package com.iot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class StreamService {

    private static final Logger logger = LoggerFactory.getLogger(StreamService.class);

    private final MqttConsumer mqttConsumer;

    public StreamService(MqttConsumer mqttConsumer) {
        this.mqttConsumer = mqttConsumer;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void consumeAndStreamToDifferentSources() {
        this.mqttConsumer.receive()
                .map(String::new)
                .doOnNext(message -> logger.info("Streaming message to different sources. {}", message))
                .subscribe();
    }
}
