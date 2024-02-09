package com.iot.demo;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class MqttConsumer {

    private final IMqttClient mqttClient;

    public MqttConsumer(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public Flux<byte[]> receive() {
        Sinks.Many<byte[]> sink = Sinks.many().unicast().onBackpressureBuffer();
        try {
            mqttClient.subscribe("topic/sample", (topic, message) -> sink.tryEmitNext(message.getPayload()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return sink.asFlux();
    }
}
