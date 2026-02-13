package com.anshu.imageservice.core.infrastructure.messaging;

import com.anshu.imageservice.event.ImageSavedEvent;

public interface ImageSavedEventPublisher {
    void publish(ImageSavedEvent event);
}
