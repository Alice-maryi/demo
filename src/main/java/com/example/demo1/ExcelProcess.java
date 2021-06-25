package com.example.demo1;

import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Author: Y.C
 * @Date: 2021/6/22 5:42 下午
 */
public class ExcelProcess<T> {
    public void exportExcel(String[] headers, Collection<T> dataset, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(fileName);
        sheet.setDefaultColumnWidth(20);
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        try {
            Iterator iterator = dataset.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                index++;
                row = sheet.createRow(index);
                T t = (T) iterator.next();
                Field[] fields = t.getClass().getDeclaredFields();
                for (int i = 0; i < headers.length; i++) {
                    XSSFCell cell = row.createCell(i);
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Class tClass = t.getClass();
                    Method getMethod = tClass.getMethod(methodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    String textValue = null;
                    if (value != null && value != "") {
                        if (fieldName.equals("gender")) {
                            textValue = (Integer)value == 1 ? "男" : "女";
                        } else {
                            textValue = value.toString();
                        }
                    }
                    if (textValue != null) {
                        XSSFRichTextString richTextString = new XSSFRichTextString(textValue);
                        cell.setCellValue(richTextString);
                    }
                }
            }
            getExportFile(workbook, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExportFile(XSSFWorkbook workbook, String name) throws IOException {
        File file = new File("test.xlsx");
        file.createNewFile();
        System.out.println(file.exists());
        workbook.write(new BufferedOutputStream(new FileOutputStream(file)));
    }
}
