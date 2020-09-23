package com.techm.multiplefilesupload.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.techm.multiplefilesupload.exception.CSVFileReadException;
import com.techm.multiplefilesupload.exception.DuplicateDocException;
import com.techm.multiplefilesupload.exception.EmptyFolderException;
import com.techm.multiplefilesupload.model.ConfigurationProps;
import com.techm.multiplefilesupload.model.MetadataProps;
import com.techm.multiplefilesupload.service.FileUploadService;
import com.techm.multiplefilesupload.util.AlfrescoAuthentication;
import com.techm.multiplefilesupload.util.FileUploadServiceHelper;
import com.techm.multiplefilesupload.util.MultipartFileToFile;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    FileUploadServiceHelper helper;

    @Override
    public void fileUploadScheduler(String baseFolderName) {

        File baseFolder = new File(baseFolderName);
        if(baseFolder.list().length==0) {
            throw new EmptyFolderException(baseFolderName+" is Empty");
        }

        String[] filesList = baseFolder.list();
        long fileSize=0;
        for(String batchFolderName: filesList)
        {
           File batchFolder = new  File(baseFolder+File.separator+batchFolderName);

           fileSize += helper.getSizeOfBatchFolderFiles(batchFolder);
           List<MetadataProps> listMetadataProps = helper.getJsonAsBean(batchFolder,baseFolderName);
           helper.processEachBatchFolder(listMetadataProps,batchFolder,baseFolderName);
        }


        DecimalFormat df = new DecimalFormat("0.00");
        log.info("Total Size of the documents in MB: "+df.format((double)fileSize/(1024*1024)));
    }


//    public JsonNode getJsonNode(File baseFolder, String basePath) {
//        String jsonFileName = baseFolder.getName() + ".json";
//
//        String jsonFilePath = basePath + baseFolder.getName() + "/" + jsonFileName;
//
//        String content = null;
//
//        JsonNode jsonnode = null;
//        try {
//            content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            jsonnode = objectMapper.readTree(content);
//
//        }catch (Exception e){
//            log.error("--------Error in getJsonNode()-------",e);
//        }
//
//        return jsonnode;
//    }
//
//    public void processEachBatchFolderOld(JsonNode jsonnode,File batchFolder, String basePath)
//    {
//        try{
//            if(jsonnode.isArray())
//            {
//                ArrayNode arrayNode = (ArrayNode) jsonnode;
//                for (int i = 0; i < arrayNode.size(); i++) {
//
//                    JsonNode docJsonNode = arrayNode.get(i);
//
//                    String docName = docJsonNode.get("name").toString();
//
//                    JsonNode docPropertiesJsonNode = docJsonNode.get("properties");
//
//                    String docFile = basePath+batchFolder.getName()+"/"+docName.replace("\"","");
//
//                    Iterator<String> fieldNames=docPropertiesJsonNode.fieldNames();
//
//                    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
//                    while(fieldNames.hasNext()) {
//                        String paramName = fieldNames.next();
//                        String paramValue = docPropertiesJsonNode.get(paramName).toString();
//                        bodyMap.add(paramName,paramValue.replace("\"",""));
//                    }
//
//                    Resource resource = new FileSystemResource(docFile);
//                    if(!resource.exists() || !resource.isFile() || Files.list(Paths.get(docFile)).count()>1)
//                        throw new Exception("File not found/Duplicate file found");
//
//                    bodyMap.add("filedata", resource);
//                    //ResponseEntity<String> response = restTemplateForAlfresco(bodyMap,batchFolder.getName());
//
//                    //String responseBody = response.getBody();
//
//                    //writeToOutput(responseBody,batchFolder.getName());
//
//                    /*RequestBody requestBody = createRequestBody(fieldNames,docPropertiesJsonNode,docFile);
//                    Response response = restServiceForAlfresco(requestBody);
//                    */
//                }
//            }
//
//        }catch (Exception e) {
//            log.error("-------error--in processEachBatchFolder---",e);
//        }
//    }
//
//
//    public RequestBody createRequestBody(Iterator<String> params,JsonNode docPropertiesJsonNode,String docFile){
//        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
//        multipartBuilder.setType(MultipartBody.FORM);
//        while(params.hasNext()) {
//            String paramName = params.next();
//            String paramValue = docPropertiesJsonNode.get(paramName).toString();
//            multipartBuilder.addFormDataPart(paramName,paramValue.replace("\"",""));
//        }
//
//        multipartBuilder.addFormDataPart("filedata",docFile,
//                RequestBody.create(MediaType.parse("application/octet-stream"),
//                        new File(docFile)));
//
//        return multipartBuilder.build();
//    }
//
//    public Response restServiceForAlfresco(RequestBody body) throws IOException {
//        String credential = Credentials.basic("admin", "admin");
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/4fcb7bdc-557f-4b62-ac5f-929500ae7023/children")
//                //.method("POST",body)
//                .post(body)
//                .addHeader("Authorization",credential)
//                .build();
//        log.info("------request--------"+request);
//        Response response = client.newCall(request).execute();
//
//        return response;
//    }
}

