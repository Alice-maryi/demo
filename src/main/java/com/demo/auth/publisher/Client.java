package com.demo.auth.publisher;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Client {

    private static final AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) throws Exception{
        File[] listFiles = new File("F:\\农信2022数据").listFiles();
        Map<String, Map<String, OutExcelData>> areaDataMap = new HashMap<>();
        for (File listFile : listFiles) {
            if (!listFile.isDirectory()) {
                continue;
            }
            String absolutePath = listFile.getAbsolutePath();
            List<String> pathList = getFilePathList(absolutePath);
            Map<Integer, List<ExcelData>> listMap = buildData(pathList);
            List<ExcelData> list = getStoreData(listMap);
            Map<String, OutExcelData> outExcelDataMap = convertData(listMap, list);
            areaDataMap.put(absolutePath.substring(absolutePath.lastIndexOf("\\") + 1), outExcelDataMap);
        }
        outputExcele(areaDataMap);
    }

    private static void outputExcele(Map<String, Map<String, OutExcelData>> areaDataMap) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
//        XSSFWorkbook wb = new XSSFWorkbook("F:\\农信2022数据\\ttt.xlsx");
//        XSSFSheet sheet = wb.cloneSheet(1, "test");
        XSSFSheet sheet = wb.createSheet("test");
        int rowIndex = 2;
        for (Map.Entry<String, Map<String, OutExcelData>> entry : areaDataMap.entrySet()) {
            List<OutExcelData> outData = new ArrayList<>(entry.getValue().values());
            output(outData, rowIndex, sheet, entry.getKey());
            rowIndex += outData.size();
        }
        FileOutputStream outputStream = new FileOutputStream("F:\\农信2022数据\\test.xlsx");
        wb.write(outputStream);

    }

//    public static void main(String[] args) throws Exception {
//        List<String> pathList = getFilePathList("F:\\农信2022数据\\怀集");
//        // 一年的数据，key是月份，值是单月的数据
//        Map<Integer, List<ExcelData>> listMap = buildData(pathList);
//        List<ExcelData> list = getStoreData(listMap);
//        // 结果数据，每一项是每个商户的统计数据
//        // 具体的结构是，每个商户下每年包括每月的统计数据
//        Map<String, OutExcelData> outExcelDataMap = convertData(listMap, list);
//        // 这是一个样例
//        // {"allYearChange":866,"allYearQuality":221272.61,"department":"广东怀集农村商业银行股份有限公司","monthDataMap":{1:{"count":187,"quality":52277.87},5:{"count":223,"quality":48729.69},6:{"count":366,"quality":111693.41},7:{"count":71,"quality":1156.69},8:{"count":19,"quality":7414.95}},"storeName":"怀集县怀城街道范先生鱼粉店","storeNumber":"8000441224001704"}
//        outputExcel(outExcelDataMap);
//    }


    private static void output(List<OutExcelData> dataList, int index, XSSFSheet sheet, String area){
        for (int i = 0; i < dataList.size(); i++) {
            OutExcelData excelData = dataList.get(i);
            int ii = i + index;
            fillRow(sheet.createRow(ii), excelData, area);
        }
    }

    private static void fillRow(XSSFRow row, OutExcelData excelData, String area) {
        row.createCell(0).setCellValue(count.getAndAdd(1));
        row.createCell(1).setCellValue(area);
        row.createCell(2).setCellValue(excelData.getStoreName());
        row.createCell(3).setCellValue(excelData.getAllYearChange());
        row.createCell(4).setCellValue(excelData.getAllYearQuality().toEngineeringString());
        for (int j = 0; j < 12; j++) {
            int index = j + 5;
            OutMonthExcelData v = excelData.getMonthDataMap().get(j + 1);
            row.createCell(index).setCellValue(v != null ? v.getCount() : 0);
            row.createCell(index + 1).setCellValue(v != null ? v.getQuality().toEngineeringString() : "0");
        }
    }

    private static void outputExcel(Map<String, OutExcelData> outExcelDataMap) throws Exception {
        List<OutExcelData> outData = new ArrayList<>(outExcelDataMap.values());
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("test");
        try {
            for (int i = 0; i < outData.size(); i++) {
                OutExcelData excelData = outData.get(i);
                XSSFRow row = sheet.createRow(3 + i);
                row.createCell(2).setCellValue(excelData.getStoreName());
                row.createCell(3).setCellValue(excelData.getAllYearChange());
                row.createCell(4).setCellValue(excelData.getAllYearQuality().toEngineeringString());
                for (int j = 0; j < 12; j++) {
                    int index = j + 5;
                    OutMonthExcelData v = excelData.getMonthDataMap().get(j + 1);
                    row.createCell(index).setCellValue(v != null ? v.getCount() : 0);
                    row.createCell(index + 1).setCellValue(v != null ? v.getQuality().toEngineeringString() : "0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream outputStream = new FileOutputStream("F:\\农信2022数据\\test.xlsx");
        wb.write(outputStream);
    }

    private static List<ExcelData> getStoreData(Map<Integer, List<ExcelData>> listMap) {
        List<ExcelData> res = new ArrayList<>();
        listMap.forEach((k, v) -> {
            res.add(v.get(0));
        });
        return res;
    }

    private static Map<String, OutExcelData> convertData(Map<Integer, List<ExcelData>> dataList, List<ExcelData> list) {
        // k：商户，v：汇总
        Map<String, OutExcelData> res = new HashMap<>();
        for (int i = 1; i < 13; i++) {
            // 单月的数据
            List<ExcelData> dataListItem = dataList.get(i);
            if (dataListItem == null) {
                break;
            }
            // key：每个商户，v：每个商户单月的数据，比如现在i的值是1，那就是这个商户1月的数据
            Map<String, List<ExcelData>> storeMap = dataListItem.stream().collect(Collectors.groupingBy(ExcelData::getStoreNumber));
            int finalI = i;
            storeMap.forEach((k, v) -> {
                OutExcelData outExcelData = new OutExcelData();
                ExcelData data = v.get(0);
                outExcelData.setDepartment(data.getDepartment());
                outExcelData.setStoreName(data.getStoreName());
                res.compute(k, (k1, v1) -> {
                    if (v1 == null) {
                        v1 = outExcelData;
                    }
                    if (v1.getMonthDataMap() == null) {
                        v1.setMonthDataMap(new HashMap<>());
                    }
                    OutMonthExcelData monthData = new OutMonthExcelData();
                    BigDecimal monthQuality = BigDecimal.ZERO;
                    Integer monthCount = 0;
                    for (ExcelData excelData : v) {
                        monthQuality = monthQuality.add(excelData.getChangeQuality());
                        monthCount += excelData.getChangeNumber();
                    }
                    v1.getMonthDataMap().put(finalI, monthData);
                    monthData.setCount(monthCount);
                    monthData.setQuality(monthQuality);
                    return v1;
                });
            });
        }
        res.forEach((k, v) -> {
            list.stream().filter(item -> item.getStoreNumber().equals(k)).findFirst().ifPresent(a -> {
                v.setStoreName(a.getStoreName());
                v.setDepartment(a.getDepartment());
            });
            v.setStoreNumber(k);
            Integer yearCount = 0;
            BigDecimal yearQuality = BigDecimal.ZERO;
            for (OutMonthExcelData value : v.getMonthDataMap().values()) {
                yearCount += value.getCount();
                yearQuality = yearQuality.add(value.getQuality());
            }
            v.setAllYearChange(yearCount);
            v.setAllYearQuality(yearQuality);
        });
        return res;
    }

    private static Map<Integer, List<ExcelData>> buildData(List<String> pathList) throws Exception {
        Map<Integer, List<ExcelData>> monthDataMap = new HashMap<>();
        for (int i = 0; i < pathList.size(); i++)  {
            String path = pathList.get(i);
            List<ExcelData> dataList = new ArrayList<>();
            monthDataMap.put(i + 1, dataList);
            FileInputStream fileInputStream = new FileInputStream(path);//开启文件读取流
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);//读取文件
            //获取sheet
            XSSFSheet sheet = sheets.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            for (int j = 1; j < rows; j++) {
                //获取列数
                XSSFRow row = sheet.getRow(j);
                if (row.getCell(0) == null) {
                    break;
                }
                ExcelData excelData = new ExcelData();

                XSSFCell departmentCell = row.getCell(0);
                excelData.setDepartment(departmentCell.getStringCellValue());

                XSSFCell storeNameCell = row.getCell(1);
                excelData.setStoreName(storeNameCell.getStringCellValue());

                XSSFCell storeNumCell = row.getCell(2);
                excelData.setStoreNumber(storeNumCell.getStringCellValue());

                XSSFCell payTypeCell = row.getCell(3);
                excelData.setPayType(payTypeCell.getStringCellValue());

                XSSFCell changeQualityCell = row.getCell(4);
                excelData.setChangeQuality(new BigDecimal(changeQualityCell.getRawValue()));

                XSSFCell changeNumCell = row.getCell(5);
                excelData.setChangeNumber(((Double) changeNumCell.getNumericCellValue()).intValue());

                XSSFCell currentChangeCell = row.getCell(8);
                excelData.setCurrentChangeQuality(new BigDecimal(currentChangeCell.getRawValue()));

                dataList.add(excelData);
            }
        }
        return monthDataMap;
    }

    private static List<String> getFilePathList(String rootPath) {
        File file = new File(rootPath);
        List<String> list = new ArrayList<>();
        for (File item : Objects.requireNonNull(file.listFiles())) {
            list.add(item.getAbsolutePath());
        }
        return list;
    }


}
