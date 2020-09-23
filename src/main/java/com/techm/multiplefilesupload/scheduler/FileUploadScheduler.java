package com.techm.multiplefilesupload.scheduler;

import com.techm.multiplefilesupload.controller.FileUploadController;
import com.techm.multiplefilesupload.model.ConfigurationProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Slf4j
public class FileUploadScheduler {

    @Autowired
    FileUploadController fileUploadController;
    @Autowired
    ConfigurationProps props;

    @Scheduled(fixedDelay = 20000, initialDelay = 2000)
    public void runScheduler() {
       try {
           log.info("---------in Scheduler-------");
            fileUploadController.fileUploadScheduler(props.getFolder());
           log.info("---------end Scheduler-------");
         }catch (Exception e)
        {
            log.error("Error in runScheduler",e);
        }
    }
}
