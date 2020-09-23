package com.techm.multiplefilesupload.events;

import org.springframework.context.ApplicationEvent;

public class DocProcessCountEvent extends ApplicationEvent {
    public DocProcessCountEvent(Object source) {
        super(source);
    }

    public String toString(){
        return "My Custom Event";
    }
}
