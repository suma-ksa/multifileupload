package com.techm.multiplefilesupload.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class DocProcessCountEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void publish(DocProcessCountEvent event)
    {
        this.publisher.publishEvent(event);
    }
}

