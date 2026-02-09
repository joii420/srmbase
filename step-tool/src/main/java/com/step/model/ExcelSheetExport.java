package com.step.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2024/7/18  13:26
 */
@Data
public class ExcelSheetExport {

    private String sheet;
    private List<ExcelTitle> titles;
    private List<Map<String,Object>> data;
}
