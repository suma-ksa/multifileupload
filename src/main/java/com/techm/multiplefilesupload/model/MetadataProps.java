package com.techm.multiplefilesupload.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
@Data
public class MetadataProps {
    private String fileData;
    private String docType;
    private Map<String,Object> metaData;
}
