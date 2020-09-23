package com.techm.multiplefilesupload.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadProgressController {

    @RequestMapping(value = "/totalCount", method = RequestMethod.GET)
    public int getTotalCount() {
        return 0;
    }

    @RequestMapping(value = "/successCount", method = RequestMethod.GET)
    public int getSuccessCount() {
        return 0;
    }

    @RequestMapping(value = "/failCount", method = RequestMethod.GET)
    public int getFailCount() {
        return 0;
    }
}
