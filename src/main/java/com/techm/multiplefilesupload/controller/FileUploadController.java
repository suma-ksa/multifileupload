package com.techm.multiplefilesupload.controller;

import com.techm.multiplefilesupload.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;

    public void fileUploadScheduler(String filepath)
    {
        fileUploadService.fileUploadScheduler(filepath);
    }
}
