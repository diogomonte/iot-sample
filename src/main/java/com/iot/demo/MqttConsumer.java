package com.iot.demo;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Service
public class MqttConsumer {

    private static final String MQTT_EVENT_TOPIC = "thing/+/event";

    private static Map<String, Flux<byte[]>> subscriptions = new HashMap<>();
    private final IMqttClient mqttClient;

    public MqttConsumer(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public Flux<byte[]> receive() {
        if (subscriptions.containsKey(MQTT_EVENT_TOPIC)) {
            return subscriptions.get(MQTT_EVENT_TOPIC);
        } else {
            return createNewSinkForEventTopic();
        }
    }

    private Flux<byte[]> createNewSinkForEventTopic() {
        try {
            Sinks.Many<byte[]> sink = Sinks.many().multicast().onBackpressureBuffer();
            mqttClient.subscribe(MQTT_EVENT_TOPIC, (topic, message) -> sink.tryEmitNext(message.getPayload()));
            subscriptions.put(MQTT_EVENT_TOPIC, sink.asFlux());
            return sink.asFlux();
        } catch (MqttException e) {
            throw new RuntimeException("Error subscribing to mqtt topic", e);
        }
    }
}
