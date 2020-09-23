package com.techm.multiplefilesupload.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techm.multiplefilesupload.exception.EmptyFolderException;
import com.techm.multiplefilesupload.model.MetadataProps;
import com.techm.multiplefilesupload.util.FileUploadServiceHelper;
import org.junit.Assert;
import org.junit.Before;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.plaf.PanelUI;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileUploadServiceImplTest {

    @InjectMocks
    private FileUploadServiceImpl fileUploadService;

    @Mock
    private FileUploadServiceHelper helper;

    @Before
    public void setUpMocks()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFileUploadScheduler() {
        List<MetadataProps> sampleList = new ArrayList<>();

        MetadataProps metadataProps = new MetadataProps();
        Map<String,Object> mapDetails = new HashMap<>();
        mapDetails.put("nabw:bodyShape","new");
        mapDetails.put("nabw:noOfCylinders",2);
        mapDetails.put("nabw:vehicleColour","Green");
        mapDetails.put("nabw:dateOnPlate","1999-09-23");
        metadataProps.setDocType("nabw:RDCT_bike");
        metadataProps.setFileData("RDCT_bike.pdf");
        metadataProps.setMetaData(mapDetails);

        sampleList.add(metadataProps);
        when(helper.getJsonAsBean(any(File.class),any(String.class))).thenReturn(sampleList);

        Mockito.doNothing().when(helper).processEachBatchFolder(anyList(),any(File.class),any(String.class));
                fileUploadService.fileUploadScheduler("F:/batch/");
    }

    @Test(expected = EmptyFolderException.class)
    public void testFileUploadSchedulerFail()
    {
        FileUploadServiceImpl fileUploadServiceMock = mock(FileUploadServiceImpl.class);

        doThrow(EmptyFolderException.class)
                .when(fileUploadServiceMock)
                .fileUploadScheduler(anyString());

        fileUploadServiceMock.fileUploadScheduler("F:/src/");
    }

}
