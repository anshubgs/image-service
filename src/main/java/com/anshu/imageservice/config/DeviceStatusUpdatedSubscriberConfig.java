package com.anshu.imageservice.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class DeviceStatusUpdatedSubscriberConfig {

    @Value("${pubsub.subscription.device-status-updated}")
    private String deviceStatusUpdatedSub;

    // 🔹 Channel (NEW)
    @Bean
    public MessageChannel deviceStatusUpdatedInputChannel() {
        return new DirectChannel();
    }

    // 🔹 Adapter (NEW)
    @Bean
    public PubSubInboundChannelAdapter deviceStatusUpdatedInboundAdapter(
            PubSubTemplate pubSubTemplate,
            MessageChannel deviceStatusUpdatedInputChannel) {

        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, deviceStatusUpdatedSub);

        adapter.setAckMode(AckMode.MANUAL);
        adapter.setOutputChannel(deviceStatusUpdatedInputChannel);

        return adapter;
    }
}
