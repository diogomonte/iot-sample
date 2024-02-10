package com.iot.demo.thing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.demo.MessageBody;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
public class ThingMockService {

    private static final List<Thing> things = List.of(
            new Thing("temperature-sensor", "temperature-1"),
            new Thing("temperature-sensor", "temperature-2"),
            new Thing("humidity-sensor", "humidity-1"),
            new Thing("light-sensor", "light-1")
    );

    private final ObjectMapper objectMapper = new ObjectMapper();


    private final IMqttClient iMqttClient;

    public ThingMockService(IMqttClient iMqttClient) {
        this.iMqttClient = iMqttClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        Flux.interval(Duration.ofSeconds(1))
                .map(i -> things.get(i.intValue() % things.size()))
                .flatMap(thing -> {
                    int randomNumber = new Random().nextInt(91) + 10;
                    try {
                        final var messageByteArray = objectMapper.writeValueAsBytes(new MessageBody(thing.thingType(), randomNumber));
                        return publishMessage("thing/" + thing.thingId() + "/event", messageByteArray);
                    } catch (JsonProcessingException e) {
                        return Flux.error(new RuntimeException(e));
                    }
                }).subscribe(
                        result -> System.out.println("Message published successfully"),
                        error -> System.err.println("Error occurred while publishing message: " + error),
                        () -> System.out.println("Publishing completed")
                );
    }

    private Mono<Void> publishMessage(String topic, byte[] message) {
        return Mono.create(sink -> {
            try {
                iMqttClient.publish(topic, new MqttMessage(message));
                sink.success();
            } catch (MqttException e) {
                sink.error(e);
            }
        });
    }
}
