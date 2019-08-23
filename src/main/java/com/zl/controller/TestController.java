package com.zl.controller;

import com.zl.entity.Status;
import com.zl.entity.User;
import com.zl.excel.ExcelExportUtil;
import com.zl.excel.ExportParams;
import com.zl.excel.style.ExcelExportStylerCustomImpl;
import com.zl.util.ConcurrentDateUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TestController {


    @GetMapping(value = "/test")
    public String test(HttpServletResponse response, HttpServletRequest request) {

        List<User> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User(i, "用户" + i + "号");
            user.setStatus(Status.VALID);
            user.setBirthday(new Date());
            list.add(user);
        }


        ExportParams exportParams = new ExportParams("计算机一班", "名单");
        exportParams.setStyle(ExcelExportStylerCustomImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, list);
        ExcelUtils.exportExcel(workbook, "名单列表.xlsx", response, request);
        return "SUCCESS";
    }

    public static void main(String[] args) {
        String format1 = "yyyy-MM-dd";
        String format2 = "yyyy-MM-dd HH:mm:ss";

    }
}
