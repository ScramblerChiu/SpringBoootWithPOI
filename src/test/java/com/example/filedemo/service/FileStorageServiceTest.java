package com.example.filedemo.service;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.example.filedemo.controller.FileController;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileStorageServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
    @Autowired
    FileStorageService fileStorageService;
	
//	@Test
//	public void contextLoads() {
//	}
	
	@Test
	public void importFromExcel()
	{
		String path = "C:\\Users\\Sam\\Desktop\\waivedRiksyRuleTemplate.xlsx";
		
		File file = new File(path);
		
	    System.out.println("File Name : " + file.getName());
		
		String result = fileStorageService.importFromExcel(file);
		
		Assert.assertEquals("Success", result);
		
	}
	
	
	
	
	

}
