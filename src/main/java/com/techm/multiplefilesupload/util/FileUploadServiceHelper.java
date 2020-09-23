package com.techm.multiplefilesupload.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techm.multiplefilesupload.model.ConfigurationProps;
import com.techm.multiplefilesupload.model.MetadataProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FileUploadServiceHelper {

    @Autowired
    AlfrescoAuthentication auth;
    @Autowired
    ConfigurationProps props;

    public List<MetadataProps> getJsonAsBean(File batchFolder, String basePath) {
        String jsonFileName = batchFolder.getName() + ".json";

        String jsonFilePath = basePath + batchFolder.getName() + "/" + jsonFileName;
        log.info("-----jsonFilePath-------"+jsonFilePath);
        String content = null;
        List<MetadataProps> listMetadataProps = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            ObjectMapper objectMapper = new ObjectMapper();

            listMetadataProps = objectMapper.readValue(content, new TypeReference<List<MetadataProps>>(){});

            log.info("***************"+listMetadataProps.size());

        }catch (Exception e){
            log.error("--------Error in getJsonNode()-------",e);
        }

        return listMetadataProps;
    }


    public void processEachBatchFolder(List<MetadataProps> listMetadataProps,File batchFolder, String basePath)
    {
        int successCount=0;
        try{
            if(!listMetadataProps.isEmpty())
            {
                for (int i = 0; i < listMetadataProps.size(); i++) {

                    MetadataProps metadataProps = listMetadataProps.get(i);

                    String docName = metadataProps.getFileData();


                    String docType = metadataProps.getDocType();
                    log.error("===========docName=========="+docName);

                    Map<String,Object> docProperties = metadataProps.getMetaData();

                    String docFile = basePath+batchFolder.getName()+"/"+docName.replace("\"","");
                    log.error("===========docFile=========="+docFile);
                    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();

                    Resource resource = new FileSystemResource(docFile);
                    //if(!resource.exists() || !resource.isFile() || Files.list(Paths.get(docFile)).count()>1)
                    //    throw new Exception("File not found/Duplicate file found");

                    bodyMap.add("metaData", docProperties);
                    bodyMap.add("docType", docType);
                    bodyMap.add("fileData", resource);

                    if(restTemplateForAlfresco(bodyMap,batchFolder.getName()))
                        successCount++;

                    if(i==listMetadataProps.size()-1)
                    {
                        writeToOutput(getFileCount(i,successCount,listMetadataProps.size(),batchFolder.getName()),batchFolder.getName(),true);
                    }

                }
            }

        }catch (Exception e) {
            log.error("-------error--in processEachBatchFolder---",e);
        }
    }

    public boolean restTemplateForAlfresco(MultiValueMap<String, Object> bodyMap,String batchFolder) throws Exception {
        boolean isSucessful=false;

        HttpHeaders headers = auth.getAuthentication();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;

        try {

            response = restTemplate.postForEntity(props.getUrl(),
                    requestEntity, String.class);

            if(response.getBody()!=null && response.getStatusCodeValue()==200)
            {
                isSucessful = true;
            }

            log.info("response status: " + response.getStatusCode());
            log.info("response body: " + response.getBody());
        }
        catch (HttpClientErrorException e)
        {
            log.error("File(s) Already exists in Alfresco!",e);
            //writeToOutput(e.getResponseBodyAsString(),batchFolder,true);
            //throw new DuplicateDocException("File(s) Already exists in Alfresco!",e);
        }

        writeToOutput(response.getBody(),batchFolder,isSucessful);

        return isSucessful;
    }

    public void writeToOutput(String responseBody,String batchFolderName,boolean isSucessful) throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;

        if(!isSucessful) {
            batchFolderName = batchFolderName + "Error";
        }
        else {
            File batchFolder = new File(props.getFolder() + batchFolderName);
        }

        try {
            File file = new File(props.getLocation() + batchFolderName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.write(responseBody+"\n");
        }
        catch (IOException ex)
        {
            log.error("---------Error in writeToOutput()---------",ex);
        }finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                log.error("------Error while closing FileWriter/BufferedWriter-----",ex);
            }
        }
    }

    public String getFileCount(int currentCount, int successCount, int TotalCount,String batchFolderName)
    {
        String count = "Total File count in "+batchFolderName+ " are:"+TotalCount + "\n"
                +"Successfully uploaded file count:"+successCount + "\n"
                +"Failed file count:"+(currentCount+1-successCount) ;

        return count;
    }

    public long getSizeOfBatchFolderFiles(File batchFolder) {
        long fileSize=0;

        for(File file: batchFolder.listFiles())
        {
            if(!file.getName().contains(".json"))
            {
                fileSize += file.length();
            }
        }
        return fileSize;
    }
}
