package com.techm.multiplefilesupload.util;

import com.techm.multiplefilesupload.model.ConfigurationProps;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class AlfrescoAuthentication {
    @Autowired
    ConfigurationProps props;

    public HttpHeaders getAuthentication()
    {
        HttpHeaders headers = new HttpHeaders();
        String auth = props.getUserName() + ":" + props.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = props.getAuthName() + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }
}
