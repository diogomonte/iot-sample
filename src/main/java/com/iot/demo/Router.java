package com.iot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {

    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    @Bean
    public RouterFunction<ServerResponse> offerMim(MqttConsumer mqttConsumer){
        return route(GET("/live-data"), request -> ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromProducer(mqttConsumer.receive()
                        .map(String::new)
                        .doOnNext(message -> logger.info("Live data api: {}", message)), String.class)));
    }
}
