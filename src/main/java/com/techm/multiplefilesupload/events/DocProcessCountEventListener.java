package com.techm.multiplefilesupload.events;

import com.techm.multiplefilesupload.model.ConfigurationProps;
import com.techm.multiplefilesupload.serviceImpl.FileUploadServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocProcessCountEventListener implements ApplicationListener<DocProcessCountEvent> {
    @Autowired
    FileUploadServiceImpl fileUploadService;
    @Autowired
    ConfigurationProps props;

    @Override
    public void onApplicationEvent(DocProcessCountEvent event) {
        Object source = event.getSource();
        if(source instanceof FileUploadServiceImpl)
        {
            fileUploadService = (FileUploadServiceImpl) source;




        }
    }
}
