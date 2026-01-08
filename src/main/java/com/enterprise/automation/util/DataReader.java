package com.enterprise.automation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Multi-format test data reader supporting Excel, CSV, and JSON
 */
public class DataReader {
    private static final Logger logger = LoggerFactory.getLogger(DataReader.class);
    private static final String TEST_DATA_DIR = "src/test/resources/testdata";

    private DataReader() {}

    /**
     * Read data from Excel file
     */
    public static List<Map<String, String>> readExcelData(String fileName, String sheetName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        String filePath = TEST_DATA_DIR + "/excel/" + fileName;
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warn("Sheet not found: {}", sheetName);
                return data;
            }
            
            Row headerRow = sheet.getRow(0);
            List<String> headers = getHeaderList(headerRow);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, String> rowData = new LinkedHashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.put(headers.get(j), getCellValue(cell));
                    }
                    data.add(rowData);
                }
            }
            logger.info("Read {} rows from Excel sheet: {}", data.size(), sheetName);
        }
        
        return data;
    }

    /**
     * Read data from CSV file
     */
    public static List<Map<String, String>> readCsvData(String fileName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        String filePath = TEST_DATA_DIR + "/csv/" + fileName;
        
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (org.apache.commons.csv.CSVRecord record : csvParser) {
                data.add(record.toMap());
            }
            logger.info("Read {} rows from CSV file: {}", data.size(), fileName);
        }
        
        return data;
    }

    /**
     * Read data from JSON file
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> readJsonData(String fileName) throws IOException {
        String filePath = TEST_DATA_DIR + "/json/" + fileName;
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> data = mapper.readValue(new File(filePath), List.class);
        logger.info("Read {} records from JSON file: {}", data.size(), fileName);
        return data;
    }

    /**
     * Get header list from Excel row
     */
    private static List<String> getHeaderList(Row headerRow) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.getStringCellValue());
        }
        return headers;
    }

    /**
     * Get cell value handling different data types
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
