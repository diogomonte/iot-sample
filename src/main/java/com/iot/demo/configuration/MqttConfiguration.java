package com.iot.demo.configuration;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {

    @Bean
    public IMqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient("tcp://127.0.0.1:1883", MqttAsyncClient.generateClientId());
        client.connect(connectionOptions());
        return client;
    }

    public MqttConnectOptions connectionOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        return options;
    }
}
