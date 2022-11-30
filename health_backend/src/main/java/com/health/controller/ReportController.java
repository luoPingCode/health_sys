package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.service.MemberService;
import com.health.service.ReportService;
import com.health.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/11/18 23:32
 */
@RestController
@RequestMapping("report")
public class ReportController {

    @Reference
    MemberService memberService; //注入会员业务层对象

    @Reference
    SetmealService setmealService; //注入套餐业务对象

    @Reference
    ReportService reportService; //注入统计业务层对象

    /**
     * 获取会员数进行统计分析
     *
     * @return
     */
    @GetMapping("getMemberReport")
    public Result getMemberReport() {
        try {
            //        data: {
//            "months":['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
//            "memberCount":"20"
//    }
            //new一个hashMap
            Map<String, Object> map = new HashMap<>();
            //用于封装月份
            List<String> months = new ArrayList<>();
//        获取当前日期
            Calendar calendar = Calendar.getInstance();
//        计算过去12个月
            calendar.add(Calendar.MONTH, -12);
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
//        循环每个月封装成list
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);//一年前开始时间往后逐渐推一个月
                Date date = calendar.getTime();
                months.add(format.format(date));
            }
            map.put("months", months);
            //months1用于查询每个月之前的数据
            List<String> months1 = new ArrayList<>();
            //        获取当前日期
            Calendar calendar1 = Calendar.getInstance();
//        计算过去12个月
            calendar1.add(Calendar.MONTH, -11);
//            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM");
            for (int i = 0; i < 11; i++) {
                calendar1.add(Calendar.MONTH, 1);
                Date time1 = calendar1.getTime();
                months1.add(format.format(time1));
            }
            List<Integer> memberCount = reportService.getMemberReport(months1);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 获取预约饼图
     *
     * @return Result
     */
    @GetMapping("getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> setmealReport = reportService.getSetmealReport();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, setmealReport);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 获取运营数据
     *
     * @return Result
     */
    @GetMapping("getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出运营数据
     *
     * @param request
     */
    @GetMapping("exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) result.get("hotSetmeal");
            //读表格
            //获取导出的模板路径 ,File.separator 根据不同系统生成不同分隔符
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //获取第一个sheet
            XSSFSheet sheet = sheets.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum = 12;
            for (Map<String, Object> map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
//使用输出流进行表格下载,基于浏览器作为客户端下载
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            OutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            outputStream.flush(); //清空输出流
            outputStream.close();
            sheets.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 导出运营数据PDF
     */
    @GetMapping("exportBusinessReport4PDF")
    public void exportBusinessReportPDF(HttpServletRequest request, HttpServletResponse response){
//        1、生成PDF
        try{
            //        1.1模板
            //动态获取模板文件在磁盘的路径
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";
            //编辑模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            //获取数据
            Map<String, Object> reportData = reportService.getBusinessReportData();
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) reportData.get("hotSetmeal");
            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, reportData, new JRBeanCollectionDataSource(hotSetmeal));
            //创建输出流，用于从服务器写数据到客户端浏览器
            //使用输出流进行表格下载,基于浏览器作为客户端下载
            response.setContentType("application/pdf");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");//指定以附件形式进行下载
            ServletOutputStream outputStream = response.getOutputStream();
            //生成文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
