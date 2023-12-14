package com.example.exceldemopractice.service;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class UploadService {

    private static final Set<String> TODDLER_RANGES = Set.of("0-1 Y","1-2 Y", "2-3 Y", "3-4 Y", "4-5 Y", "5-6 Y");

    public void parentMapping() throws IOException{
        FileInputStream fileInput = new FileInputStream("products_export_2.xlsx");

        Workbook workbook = WorkbookFactory.create(fileInput);
        Sheet sheet = workbook.getSheetAt(0);

        //  handle is 0th column
        int handleIndex = 0;

        // title is 1st column (parent is title)
        int titleIndex = 1;

        // tags is 6th column(where we have to add gender)
        int tagsIndex = 6;

        // column 10(based on 0 indexing) is name of option2 i.e. size and column 11 is its value eg. 1-2 Y
        int sizeValueIndex = 11;

        // <Handle,RowIndex>
        Map<String,Integer> mp = getParentProducts(sheet, handleIndex, titleIndex);

        // now we have a map with handle and rowIndex
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if(Objects.nonNull(row)) {

                Cell handleCell = row.getCell(handleIndex);
                Cell sizeValueCell = row.getCell(sizeValueIndex);
                Cell titleCell = row.getCell(titleIndex);

                // getting parent row
                Integer parentIndex = mp.get(handleCell.getStringCellValue());
                if(parentIndex != null){
                    Row parentRow = sheet.getRow(parentIndex);

                    if (Objects.nonNull(titleCell)
                            && Objects.nonNull(sizeValueCell)
                            && Objects.nonNull(handleCell)
                            && Objects.nonNull(parentRow)) {

                        String titleCellString = titleCell.getStringCellValue();
                        String sizeValueString = sizeValueCell.getStringCellValue();

                        Cell parentTagCell = parentRow.getCell(tagsIndex);

                        if(Objects.nonNull(parentTagCell) && StringUtils.isNotBlank(parentTagCell.getStringCellValue())) {
                            String parentTag = parentTagCell.getStringCellValue();

                            // GETTING GENDER
                            String gender = getGenderFromTitle(titleCellString);

                            if(StringUtils.isNotBlank(gender)){
                                String tagPrefix = getTagPrefixByAge(sizeValueString);
                                String tag = tagPrefix.concat(gender);

                                if(StringUtils.isBlank(parentTag))
                                    parentTagCell.setCellValue(tag);
                                else
                                    parentTagCell.setCellValue(parentTag.concat(",").concat(tag));

                                System.out.println(parentTagCell.getStringCellValue());
                            }
                        }
                    }
                }

            }
        }

        FileOutputStream fileOut = new FileOutputStream("updated_sheet.xlsx");
        // Write the content of the workbook to the file
        workbook.write(fileOut);
        // Close the FileOutputStream to release resources
        fileOut.close();
        // Close the workbook to release resources
        workbook.close();
        return;
    }


    private String getGenderFromTitle(String titleCellString) {
        String gender = "";
        if (StringUtils.containsIgnoreCase(titleCellString, "boy")) {
            gender = "BOYS";
        } else if (StringUtils.containsIgnoreCase(titleCellString, "girl")) {
            gender = "GIRLS";
        }

        return gender;
    }

    private Map<String, Integer> getParentProducts(Sheet sheet, int handleIndex, int titleIndex) {

        Map<String, Integer> map = new HashMap<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            // to see if row is not null
            if(row != null){
                Cell handleCell = row.getCell(handleIndex);
                Cell titleCell = row.getCell(titleIndex);
                // checking if titleCell isn't null and value in titleCell isn't blank, same for handleCell
                if(titleCell != null && StringUtils.isNotBlank(titleCell.getStringCellValue())
                        && Objects.nonNull(handleCell) && StringUtils.isNotBlank(handleCell.getStringCellValue())){
                    map.put(handleCell.getStringCellValue(),rowIndex);
                }
            }
        }

        return  map;
    }

    private String getTagPrefixByAge(String age) {
            if (age == null) {
                return StringUtils.EMPTY;
            }

            if (TODDLER_RANGES.contains(age)) {
                return " TODDLER ";
            } else {
                return " ";
            }

//        switch (age){
//            case "1-2 Y":
//            case "2-3 Y":
//            case "3-4 Y":
//            case "4-5 Y":
//            case "5-6 Y": return " TODDLER ";
//            default: return StringUtils.EMPTY;
//        }

    }

//
//    public List<List<String>> readExcelFile(MultipartFile file) throws IOException {
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//
//        List<List<String>> excelData = new ArrayList<>();
//
//
//        Iterator<Row> rowIterator = sheet.iterator();
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            List<String> rowData = new ArrayList<>();
//
//            Iterator<Cell> cellIterator = row.cellIterator();
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//                rowData.add(getCellValueAsString(cell));
//            }
//
//            excelData.add(rowData);
//        }
//
//        return excelData;
//    }
//
//    private String getCellValueAsString(Cell cell) {
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                return String.valueOf(cell.getNumericCellValue());
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            case BLANK:
//                return "";
//            default:
//                return "";
//        }
//    }
//
//    public List<String> getTitleListFromExcelFile(MultipartFile file) throws IOException {
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        List<String> titleList = new ArrayList<>();
//        // the title column is in the first column of the first sheet
//        Sheet sheet = workbook.getSheetAt(0);
//
//        // the title column is the first column
//        int titleIndex = 1;
//
//        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
//            Row row = sheet.getRow(rowIndex);
//
//            if (row != null) {
//                Cell titleCell = row.getCell(titleIndex);
//                // titleCell means the title String eg. "Army Print Elasticated Jogger with Cross Pocket"
//                if (titleCell != null) {
//                    titleList.add(titleCell.getStringCellValue());
//                }
//            }
//        }
//
//        return titleList;
//    }
//
//    // to get back MODIFIED EXCEL
//    public byte[] generateModifiedExcel() throws IOException {
//        // Create a new workbook
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // Create a new sheet
//            Sheet sheet = workbook.createSheet("ModifiedSheet");
//
//            // Create a row and add data
//            Row row = sheet.createRow(0);
//            Cell cell = row.createCell(0);
//            cell.setCellValue("Modified Data");
//
//            // Save the modified workbook to a byte array
//            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
//                workbook.write(byteArrayOutputStream);
//                return byteArrayOutputStream.toByteArray();
//            }
//        }
//    }

}
