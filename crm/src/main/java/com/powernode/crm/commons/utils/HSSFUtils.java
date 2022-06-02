package com.powernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 关于excel的工具类
 * @author LXM
 * @create 2022-05-16 13:34
 */
public class HSSFUtils {
    /**
     * 从指定的HSSF对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String res = "";
        if (cell.getCellType() ==  HSSFCell.CELL_TYPE_STRING){
            res += cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            res += cell.getNumericCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
            res += cell.getBooleanCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
            res += cell.getCellFormula();
        }
        return res;
    }
}
