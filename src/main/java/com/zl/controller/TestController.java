package com.zl.controller;

import com.zl.entity.Status;
import com.zl.entity.User;
import com.zl.excel.ExcelExportUtil;
import com.zl.excel.ExportParams;
import com.zl.excel.style.ExcelExportStylerCustomImpl;
import com.zl.util.ConcurrentDateUtil;
import com.zl.util.PoiMergeCellUtil;
import com.zl.util.PublicUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class TestController {


    @GetMapping(value = "/test")
    @ResponseBody
    public String test(HttpServletResponse response, HttpServletRequest request) {

        List<User> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            User user = new User(i, "用户" + i + "号");
            user.setStatus(i % 2 == 0 ? Status.VALID : Status.INVALID);
            Date date = new Date();
            user.setBirthday(date);
            user.setUpdateTime(date);
            user.setPrice(new BigDecimal(100 * i * random.nextDouble()));
            list.add(user);
        }


        ExportParams exportParams = new ExportParams("计算机一班", "名单");
        exportParams.setStyle(ExcelExportStylerCustomImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, list);
        ExcelUtils.exportExcel(workbook, "名单列表.xlsx", response, request);
        return "SUCCESS";
        //maiqun123a
    }

    @GetMapping(value = "/show")
    public void show(HttpServletResponse response, HttpServletRequest request) {
        System.out.println("show");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet("整体概况");
        Row row = sheet1.createRow(0);
        XSSFRichTextString xssfRichTextString = new XSSFRichTextString("58(行销推广部) 得分概览\n排名为前台分组中的排名，前台分组包括：行销推广部");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
//        cell.setCellStyle(cellStyle);
        for (int i = 0; i < 6; i++) {
            Cell cell1 = row.createCell(i);
            cell1.setCellStyle(cellStyle);
        }
        Cell cell = row.getCell(0);
        cell.setCellValue("58(行销推广部) 得分概览\n排名为前台分组中的排名，前台分组包括：行销推广部");
        row.setHeightInPoints(2 * sheet1.getDefaultRowHeightInPoints());
        PoiMergeCellUtil.addMergedRegion(sheet1, 0, 0, 0, 5);
        ExcelUtils.exportExcel(workbook, "鼠标列表.xlsx", response, request);
    }

    @RequestMapping(value = "/")
    public String index() {
        return "show";
    }

}
