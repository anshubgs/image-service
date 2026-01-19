package com.anshu.imageservice.event;

/**
 * Abstraction so Kafka/Redis can be swapped
 */
public interface ImageEventPublisher {
    void publish(ImageUploadedEvent event);
}
