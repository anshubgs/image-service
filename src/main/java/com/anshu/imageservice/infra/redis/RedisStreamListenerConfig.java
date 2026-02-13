package com.anshu.imageservice.infra.redis;

import com.anshu.imageservice.core.infrastructure.messaging.ImageSavedEventListener;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisStreamListenerConfig {

    private final ImageSavedEventListener imageSavedEventListner;

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamContainer(
            RedisConnectionFactory factory
    ) {

        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(2))
                .build();

        var container = StreamMessageListenerContainer.create(factory, options);

        container.receive(
                Consumer.from("summary-group", "summary-consumer"),
                StreamOffset.create("image.saved.stream", ReadOffset.lastConsumed()),
                imageSavedEventListner
        );

        container.start();
        return container;
    }

    @Bean
    public InitializingBean createConsumerGroups(RedisTemplate<String, String> redisTemplate) {
        return () -> {
            try {
                redisTemplate.opsForStream()
                        .createGroup("image.saved.stream", ReadOffset.latest(), "summary-group");
            } catch (Exception ignored) {}
        };
    }
}
