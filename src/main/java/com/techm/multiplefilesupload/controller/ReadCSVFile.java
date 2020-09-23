package com.techm.multiplefilesupload.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.techm.multiplefilesupload.events.DocProcessCountEvent;
import com.techm.multiplefilesupload.events.DocProcessCountEventPublisher;
import com.techm.multiplefilesupload.exception.CSVFileReadException;
import com.techm.multiplefilesupload.scheduler.FileUploadScheduler;
import com.techm.multiplefilesupload.serviceImpl.FileUploadServiceImpl;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ReadCSVFile implements CommandLineRunner {

    @Autowired
    //UserControler userControler;
    FileUploadController fileUploadController;
    @Autowired
    FileUploadScheduler fileUploadScheduler;

    @Autowired
    DocProcessCountEventPublisher publisher;


    public void readCSVFile(String file) {
        String fileExtention = file.substring(file.indexOf(".")+1, file.length());

        if (!fileExtention.equals("csv"))
        {
            throw new CSVFileReadException("Please Provide only csv File!");
        }

        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));

                StringBuilder sb=new StringBuilder(" ");
                for (String str: values) {
                   sb.append(str);
                   sb.append(" ");
                }
                log.info(String.valueOf(sb));
            }
        } catch (FileNotFoundException e) {
            throw new CSVFileReadException("File Not Found!",e);
        } catch (IOException  | CsvValidationException e) {
            log.error("IO Exception in ReadCsv():",e);
        }
    }

    public void readingFile(String filepath) {

        File file = new File(filepath);
        try (FileInputStream input = new FileInputStream(file);) {

            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                  "application/octet-stream", input);

            //userControler.uploadFile(multipartFile);
            //fileUploadController.fileUploadClient(multipartFile);
        }
        catch (FileNotFoundException e)
        {
            throw new CSVFileReadException("File Not Found!",e);
        } catch (IOException e) {
            log.error("IO Exception in ReadCsv():",e);
        }
    }

    @Override
    public void run(String... args) {
        StopWatch watch = new StopWatch();
        watch.start();

        for (int i = 0; i < args.length; ++i) {

            try{
                //readingFile(args[i]);

            }catch (CSVFileReadException e)
            {
                log.error(e.getMessage());
            }
        }

        fileUploadScheduler.runScheduler();

        watch.stop();
        log.info(" "+watch.prettyPrint());
        //log.info("Time Elapsed: " + watch.getTotalTimeMillis()+ " milliseconds");

        //publisher.publish(new DocProcessCountEvent(new FileUploadServiceImpl()));
    }
}
