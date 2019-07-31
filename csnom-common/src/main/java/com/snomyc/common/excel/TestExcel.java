package com.snomyc.common.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.List;

public class TestExcel {

    public static void main(String[] args) throws Exception {
        File fi = new File("F://conch_data.xlsx");
        InputStream ins = new FileInputStream(fi);
        Workbook wb = WorkbookFactory.create(ins);
        ins.close();
        // 得到excel工作表对象
        Sheet sheet = wb.getSheetAt(0);

        //总行数
        int sumRow = sheet.getPhysicalNumberOfRows();
        //从第二行开始读取
        for (int i = 1; i < sumRow; i++) {
            Row row = sheet.getRow(i);
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            //如果该列没有订单编号则继续往下读取
            if(row == null || row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            //获取第一列imei号
            String imei = row.getCell(0).getStringCellValue();
            System.out.println(imei);
            //写入第二列设备号
            row.createCell(1).setCellValue("1");
            //写入第三列卡号
            row.createCell(2).setCellValue("2");
        }

        try {
            FileOutputStream out = new FileOutputStream("F:/conch_data.xlsx");
            wb.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
