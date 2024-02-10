package com.iot.demo.configuration;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {

    @Value("${mqtt.broker.host}")
    private String mqttBrokerHost;

    @Bean
    public IMqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(String.format("tcp://%s:1883", mqttBrokerHost), MqttAsyncClient.generateClientId());
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
