package com.zl.controller;

import com.zl.entity.Status;
import com.zl.entity.User;
import com.zl.excel.ExcelExportUtil;
import com.zl.excel.ExcelImportUtil;
import com.zl.excel.ExportParams;
import com.zl.excel.ImportParams;
import com.zl.excel.ImportResult;
import com.zl.excel.style.ExcelExportStylerCustomImpl;
import com.zl.util.PoiMergeCellUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Controller
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @GetMapping(value = "/test")
    @ResponseBody
    public String test(HttpServletResponse response, HttpServletRequest request) {
        List<User> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i < 20000; i++) {
            User user = new User(i, "用户" + i + "号");
            user.setStatus(i % 2 == 0 ? Status.VALID : Status.INVALID);
            Date date = new Date();
            user.setBirthday(date);
            user.setUpdateTime(date);
            user.setPrice(new BigDecimal(100 * i * random.nextDouble()));
            user.setaDouble(random.nextDouble() * i);
            user.setaFloat(random.nextFloat() * i);
            user.setaLong(random.nextLong());
            user.setaBoolean(Boolean.valueOf("true"));
            user.setbDouble(random.nextDouble() * i);
            user.setbFloat(random.nextFloat() * i);
            user.setbLong(random.nextLong());
            user.setbInt(random.nextInt() * i);
            user.setbBoolean(false);
            list.add(user);
        }

        long start = System.currentTimeMillis();
        ExportParams exportParams = new ExportParams("计算机一班", "名单");
        exportParams.setStyle(ExcelExportStylerCustomImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, list);
        long end = System.currentTimeMillis();
        System.out.println("解析时间：" + (end - start) + "毫秒");

        ExcelUtils.exportExcel(workbook, "名单列表.xlsx", response, request);
        return "SUCCESS";
        //maiqun123a
    }

    @GetMapping(value = "/show")
    public void show(HttpServletResponse response, HttpServletRequest request) {
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


    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("uploadFile") MultipartFile file) {
        if (file.isEmpty()) {
            logger.info("文件为空");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);

        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);

        // 文件上传路径
        String filePath = "E:/";

        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;

        File dest = new File(filePath + fileName);

        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
            logger.info("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    @RequestMapping(value = "/excelUpload")
    @ResponseBody
    public String excelTest(@RequestParam("uploadFile") MultipartFile file) {


        try {
            InputStream inputStream = file.getInputStream();
            ImportParams importParams = new ImportParams();
            importParams.setExcelVerifyHandler(new VerifyHandler());
            ImportResult<User> objectImportResult = ExcelImportUtil.importExcel(inputStream, User.class, importParams);
            if (CollectionUtils.isNotEmpty(objectImportResult.getList())) {
                System.out.println("数据：" + objectImportResult.getList().size());
                System.out.println(objectImportResult.getList());
            }
            System.out.println("错误：" + objectImportResult.getVerifyMsg());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    @RequestMapping(value = "/showHtml")
    public String show() {
        return "show";
    }


    @RequestMapping(value = "/testAjax")
    public String testAjax() {
        return "ajax";
    }

    @RequestMapping(value = "/testRequest")
    @ResponseBody
    public User testRequest(User user) {
        System.out.println(user);
        return user;
    }

    @RequestMapping(value = "/test1")
    @ResponseBody
    public Object test1(@RequestParam("key") String param) {
        return param;
    }


    @RequestMapping(value = "/test2")
    @ResponseBody
    public User test2(@RequestBody User user) {
        System.out.println(user);
        return user;
    }
}
