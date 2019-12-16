package com.zl.excel;

import com.zl.entity.ExportEntity;
import com.zl.entity.Status;
import com.zl.excel.style.ExcelExportStylerCustomImpl;
import org.apache.poi.ss.usermodel.Workbook;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 测试类
 */
public class ExcelExportUtilTest {


    public void testExportExcel() {
        System.out.println("开跑");
        Random random = new Random();
        List<ExportEntity> list = new ArrayList<>(100);
        Date date = new Date();
        for (int i = 0; i < 100; i++) {
            ExportEntity entity = new ExportEntity();
            byte b = (byte) i;
            entity.setByteBasic(b);
            entity.setByteWarp(Byte.valueOf(b));
            short s = (short) i;
            entity.setShortBasic(s);
            entity.setShortWarp(Short.valueOf(s));
            entity.setIntBasic(i);
            entity.setIntWarp(Integer.valueOf(i));
            entity.setLongBasic(i);
            entity.setLongWrap(Long.valueOf(i));
            entity.setFloatBasic(random.nextFloat());
            entity.setFloatWrap(random.nextFloat());
            entity.setDoubleBasic(random.nextDouble() * 100);
            entity.setDoubleWrap(random.nextDouble() * 100);
            entity.setBooleanBasic((i & 1) == 0 ? true : false);
            entity.setBooleanWarp((i & 1) != 0 ? Boolean.TRUE : Boolean.FALSE);
            char c = (char) i;
            entity.setCharBasic(c);
            entity.setCharWrap(Character.valueOf(c));
            entity.setString("编号" + i + "号");
            entity.setStatus((i & 1) == 0 ? Status.VALID : Status.INVALID);
            entity.setDate(date);
            entity.setBigDecimal(new BigDecimal(random.nextDouble() * 100));
            list.add(entity);
        }
        ExportParams exportParams = new ExportParams("计算机一班", "名单");
        exportParams.setStyle(ExcelExportStylerCustomImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ExportEntity.class, list);
        File file = new File("E:/exportEntity.xlsx");
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            workbook.write(bufferedOutputStream);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
