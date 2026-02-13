package com.anshu.imageservice.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.integration.AckMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@Configuration
public class PubSubSubscriberConfig {

    @Value("${pubsub.subscription.device-registered}")
    private String subscriptionName;

    @Bean
    public PubSubInboundChannelAdapter pubSubInboundChannelAdapter(
            PubSubTemplate pubSubTemplate,
            MessageChannel pubsubInputChannel) {

        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);

        // 🔥 MANUAL ACK (because we are calling ack() / nack())
        adapter.setAckMode(AckMode.MANUAL);

        adapter.setOutputChannel(pubsubInputChannel);

        return adapter;
    }
}
