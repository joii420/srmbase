package com.step.tool.utils;

import com.step.logger.LOGGER;
import com.step.model.ExcelSheetExport;
import com.step.model.ExcelTitle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class ExcelUtil {

    public static byte[] exportSheets(List<ExcelSheetExport> sheets) {
        if (CollectionUtil.isEmpty(sheets)) {
            return new byte[0];
        }
        try (Workbook workbook = new XSSFWorkbook()) {
            for (ExcelSheetExport exportSheet : sheets) {
                String sheetName = exportSheet.getSheet();
                List<Map<String, Object>> dataList = exportSheet.getData();
                List<ExcelTitle> titles = exportSheet.getTitles();
                if (CollectionUtil.isEmpty(titles) && CollectionUtil.isEmpty(dataList)) {
                    continue;
                }
                Sheet sheet = workbook.createSheet(sheetName);
                CellStyle dateCellStyle = workbook.createCellStyle();
                CreationHelper creationHelper = workbook.getCreationHelper();
                dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                // Create header row
                Row headerRow = sheet.createRow(0);
                if (CollectionUtil.isNotEmpty(titles)) {
                    for (int i = 0; i < titles.size(); i++) {
                        ExcelTitle excelTitle = titles.get(i);
                        Cell cell = headerRow.createCell(i);
                        addRowData(cell, excelTitle.getTitle());
                    }
                } else {
                    Map<String, Object> dataExample = dataList.get(0);
                    Set<String> keys = dataExample.keySet();
                    int i = 0;
                    for (String key : keys) {
                        Cell cell = headerRow.createCell(i++);
                        addRowData(cell, key);
                    }
                }
                // ... Add more header cells as needed

                // Populate data rows
                int rowNum = 1;
                for (Map<String, Object> data : dataList) {
                    Row row = sheet.createRow(rowNum++);
                    if (CollectionUtil.isNotEmpty(titles)) {
                        for (int i = 0; i < titles.size(); i++) {
                            ExcelTitle excelTitle = titles.get(i);
                            Object rowData = data.get(excelTitle.getField());
                            Cell cell = row.createCell(i);
                            addRowData(cell, rowData);
                            if (rowData instanceof Date || rowData instanceof LocalDateTime) {
                                cell.setCellStyle(dateCellStyle);
                            }
                        }
                    } else {
                        Set<String> dataKeys = data.keySet();
                        int d = 0;
                        for (String dataKey : dataKeys) {
                            Object rowData = data.get(dataKey);
                            Cell cell = row.createCell(d++);
                            addRowData(cell, rowData);
                        }
                    }
                    // ... Set other cell values based on your data object
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void addRowData(Cell cell, Object rowData) {
        if (rowData == null) {
            return;
        }
        if (rowData instanceof String value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Integer value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Long value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Date value) {
            cell.setCellValue(value);
        } else if (rowData instanceof LocalDateTime value) {
            cell.setCellValue(value);
        } else if (rowData instanceof LocalDate value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Calendar value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Boolean value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Short value) {
            cell.setCellValue(value);
        } else if (rowData instanceof RichTextString value) {
            cell.setCellValue(value);
        } else if (rowData instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            System.out.println("EXCEL 不支持的类型: " + rowData.getClass().getSimpleName());
            cell.setCellValue("" + rowData);
        }
    }

    public static byte[] export(List<Map<String, Object>> dataList) {
        if (CollectionUtil.isEmpty(dataList)) {
            return new byte[0];
        }
        ExcelSheetExport excelSheetExport = new ExcelSheetExport();
        excelSheetExport.setSheet(DateUtil.formatDate(new Date()));
        excelSheetExport.setData(dataList);
        return exportSheets(List.of(excelSheetExport));
    }


    public static List<Map<String, Object>> read(String filePath, String sheetName) {
        List<Map<String, Object>> result = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fileInputStream)) {
            Sheet sheet = workbook.getSheet(sheetName); // 获取第一个工作表
            if (sheet == null) {
                LOGGER.warn("sheet not fount: {}", sheetName);
                return null;
            }
            readSheetResult(result, sheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, Object>> read(String filePath, int sheetIndex) {
        List<Map<String, Object>> result = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(sheetIndex); // 获取第一个工作表
            readSheetResult(result, sheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void readSheetResult(List<Map<String, Object>> result, Sheet sheet) {
        // 遍历工作表的每一行
        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            // 如果行为空，跳过
            if (row == null) {
                continue;
            }

            // 遍历每一行的每一个单元格
            for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                // 获取合并单元格的值
                String cellValue = getMergedCellValue(cell, sheet);

                // 填充每一行的数据
                Cell firstCellInMergedRegion = getFirstCellInMergedRegion(cell, sheet);
                if (firstCellInMergedRegion != null) {
                    row.createCell(firstCellInMergedRegion.getColumnIndex(), CellType.STRING)
                            .setCellValue(cellValue);
                }
            }
        }

        // 打印处理后的数据
        for (Row row : sheet) {
            int column = 1;
            Map<String, Object> rowMap = new LinkedHashMap<>();
            for (Cell cell : row) {
                String cellValue = getCellValue(cell);
                rowMap.put("v" + column++, cellValue);
//                    System.out.print(getCellValue(cell) + "\t");
            }
            if (rowMap.size() > 0) {
                result.add(rowMap);
            }
        }
    }

    public static List<Map<String, Object>> read(String filePath) {
        return read(filePath, 0);
    }

    // 获取合并单元格的值
    private static String getMergedCellValue(Cell cell, Sheet sheet) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();

        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (rowIndex >= mergedRegion.getFirstRow() && rowIndex <= mergedRegion.getLastRow() &&
                    colIndex >= mergedRegion.getFirstColumn() && colIndex <= mergedRegion.getLastColumn()) {
                Row firstRow = sheet.getRow(mergedRegion.getFirstRow());
                Cell firstCell = firstRow.getCell(mergedRegion.getFirstColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                return getCellValue(firstCell);
            }
        }
        return getCellValue(cell);

    }

    // 获取合并单元格区域的第一个单元格
    private static Cell getFirstCellInMergedRegion(Cell cell, Sheet sheet) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();

        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (rowIndex >= mergedRegion.getFirstRow() && rowIndex <= mergedRegion.getLastRow() &&
                    colIndex >= mergedRegion.getFirstColumn() && colIndex <= mergedRegion.getLastColumn()) {
                Row firstRow = sheet.getRow(mergedRegion.getFirstRow());
                return firstRow.getCell(mergedRegion.getFirstColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            }
        }
        return null;
    }

    private static String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        String value = null;
        switch (cellType) {
            case STRING -> value = cell.getStringCellValue();
            case NUMERIC -> {
                double numericCellValue = cell.getNumericCellValue();
                value = "" + BigDecimal.valueOf(numericCellValue).toBigInteger().toString();
            }
            case BOOLEAN -> value = "" + cell.getBooleanCellValue();
            default -> {

            }

        }
        return value;
    }
}
