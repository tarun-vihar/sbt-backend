package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.model.dto.AbstractEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonService {

    @Autowired
    Validator validator;
    public AbstractEntity updateStatus(AbstractEntity data, String action){

        if(data == null) return data;

        switch (action){
            case "reject":{
                data.setStatus(Constants.REJECTED_STATUS);
                break;
            }
            case "approve":{
                data.setStatus(Constants.APPROVED_STATUS);
                break;
            }
            case "save":
            default:
                data.setStatus(Constants.UNAPPROVED_STATUS);

        }

        return data;
    }

    public <T> List readInputFile(MultipartFile inputFile, String className) throws ClassNotFoundException {
//
//        String fileName = inputFile.getOriginalFilename();
//        try(InputStream is = inputFile.getInputStream();
//
//            Workbook workbook = StreamingBuilder.build()
//                                .rowCacheSize(1000).bufferSize(4096).open(is)){
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            Iterator<Row>  rowIterator = sheet.rowIterator();
//
//            Row  firstRow = rowIterator.next();
//
//            if(workbook == null){
//                throw  new RuntimeException("Invalid Sheet Name");
//            }
//
//            Class<T> clazz = (Class<T>) Class.forName(className);
//            Map<String, Integer> headerMap = getHeaderMap(firstRow);
//            Map<String, Field> fieldMap = new HashMap<>();
//
//            getAllFeilds(clazz, fieldMap);
//
//            List<T> payload = getImportList(rowIterator, headerMap, fieldMap, clazz);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

       return  null;
    }

    private <T> List<T> getImportList(Iterator<Row> rowIterator, Map<String, Integer> headerMap,
                                      Map<String, Field> fieldMap, Class<T> genericClas) throws Exception {

        List<T> importList = new ArrayList<>();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            T object = createObjectFromRow(row, headerMap, fieldMap, genericClas);
            importList.add(object);
        }

        return importList;
    }

    private <T> T createObjectFromRow(Row row, Map<String, Integer> headerMap,
                                      Map<String, Field> fieldMap, Class<T> genericClas) throws Exception {

        T object = genericClas.getDeclaredConstructor().newInstance();
        for(String fieldName : headerMap.keySet()){
            Integer columnIndex = headerMap.get(fieldName);
            Cell cell = row.getCell(columnIndex);

            Field field = fieldMap.get(fieldName);
            field.setAccessible(true);

            Object value = convertStringToFeildValue(cell, field.getType());
            field.set(object, value);
        }

        String error = validateObject(object);

        return  object;
    }

    private <T> String validateObject(T object) {

        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if(!violations.isEmpty()){
            String error = violations.stream()
                    .map(v -> v.getMessage())
                    .collect(Collectors.joining(" - "));

            return  error;
        }

        return  null;

    }

    private Object convertStringToFeildValue(Cell cell, Class<?> feildType) {

        String fieldValue = cell.getStringCellValue();

        try{
            if(feildType == String.class){
                return fieldValue;
            } else if (feildType == Integer.class) {
                return Integer.parseInt(fieldValue);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return fieldValue;
    }

    private <T> void getAllFeilds(Class<T> genericClass, Map<String, Field> fieldMap) {
        if(genericClass == null){
            return;
        }

        Field[] fields = genericClass.getDeclaredFields();
        for(Field field : fields){
            fieldMap.put(field.getName(),field);
        }

        Class<?> superClass = genericClass.getDeclaringClass();
        getAllFeilds(superClass, fieldMap);
    }

    private Map<String, Integer> getHeaderMap(Row row) {
        Map<String,Integer>  headerMap = new HashMap<>();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()){
            Cell cell  = cellIterator.next();
            headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        return  headerMap;
    }
}
