package com.powernode.crm.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

import static com.powernode.crm.commons.utils.HSSFUtils.getCellValueForStr;

/**
 * @author LXM
 * @create 2022-05-16 12:03
 */
public class ParseExcelTest {
    public static void main(String[] args) throws Exception{
        // 根据指定的excel文件来生成HSSFWorkBook对象
        FileInputStream inputStream = new FileInputStream("C:\\Users\\10727\\Desktop\\SelectedActivity.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        // 获取页
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 获取row对象
        HSSFRow row = null;
        HSSFCell cell = null;
        // 最后一行号码，返回的是最后一行的下标
        for (int i = 0; i <= sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            // 返回的是最后一列的下标加一
            for (int j = 0; j < row.getLastCellNum(); j++){
                cell = row.getCell(j);
                System.out.println(getCellValueForStr(cell));
            }
            // 换行
            System.out.println();
        }
    }



}
