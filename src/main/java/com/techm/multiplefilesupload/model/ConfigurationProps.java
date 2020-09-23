package com.techm.multiplefilesupload.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
@EnableConfigurationProperties
@Data
public class ConfigurationProps {
    private String userName;
    private String password;
    private String authName;
    private String location;
    private String url;
    private String folder;
}
