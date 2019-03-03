package com.example.filedemo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.filedemo.domain.WaivedRiskyRules;
import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.property.FileStorageProperties;
import com.example.filedemo.util.ExcelUtil;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    
    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";
    
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    
    public String importFromExcel(File file)
    {
    	Workbook wb = null;
    	
    	List<WaivedRiskyRules> list = new ArrayList();
    	
    	try
    	{
    		if (ExcelUtil.isExcel2007(file.getPath()))
    		{
    			wb = new XSSFWorkbook(new FileInputStream(file));
    		} else {
    			wb = new HSSFWorkbook(new FileInputStream(file));
    		}
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    	
    	
    	Sheet sheet = wb.getSheetAt(0);
    	
    	for (int i = 0; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            
            
            String firewallID= row.getCell(0).getStringCellValue();
            String ruleID= row.getCell(1).getStringCellValue();
            String comment= row.getCell(2).getStringCellValue();
            /*
            int age = row.getCell(1).getNumericCellValue();
            if (age==0 && name==null)
            {
                break;
            }
            */
            
            WaivedRiskyRules rule= new WaivedRiskyRules();
            rule.setFirewallID(firewallID);
            rule.setRuleID(ruleID);
            rule.setComment(comment);
            
            list.add(rule);
        }
    	
    	
    	for(WaivedRiskyRules wr : list)
    	{
    		logger.debug("List : "+wr.toString());
    	}
    	
    	try
        {
            wb.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    	
    	
    	
		return "Success";
    	
    }
    
    public String importFromExcelMultipart(MultipartFile file)
    {
    	String originalFilename = file.getOriginalFilename();
    	
    	Workbook wb = null;
    	
    	List<WaivedRiskyRules> list = new ArrayList();
    	
    	try
    	{
    		if (originalFilename.endsWith(SUFFIX_2003)) {
                wb = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(SUFFIX_2007)) {
                wb = new XSSFWorkbook(file.getInputStream());
            }
    	}
    	catch (IOException e)
    	{
    		logger.info(originalFilename);
    		e.printStackTrace();
    		return null;
    	}
    	
    	if (wb == null) {
           logger.info(originalFilename);
           return "Workbook is null"; 
        }
    	
    	Sheet sheet = wb.getSheetAt(0);
    	int lastRowNum = sheet.getLastRowNum();
    	
    	for (int i = 1; i <= lastRowNum; i++)
        {
            Row row = sheet.getRow(i);
            
            
            String firewallID= row.getCell(0).getStringCellValue();
            String ruleID= row.getCell(1).getStringCellValue();
            String comment= row.getCell(2).getStringCellValue();
            
            WaivedRiskyRules rule= new WaivedRiskyRules();
            rule.setFirewallID(firewallID);
            rule.setRuleID(ruleID);
            rule.setComment(comment);
            
            list.add(rule);
        }
    	
    	
    	for(WaivedRiskyRules wr : list)
    	{
//    		logger.debug("List : "+wr.toString());
    		System.out.println("List : "+wr.toString());
    	}
    	
    	try
        {
            wb.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    	
    	
    	
		return "Success";
    	
    }
    
    
}
