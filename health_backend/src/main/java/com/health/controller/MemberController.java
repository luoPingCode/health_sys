package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.utils.DateUtils;
import com.health.utils.TypeConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LuoPing
 * @project IntelliJ IDEA
 * @Package health_sys
 * @Date 2022/12/8 0:48
 */
@RestController
@RequestMapping("member")
@Slf4j
public class MemberController {

    @Reference
    MemberService memberService;

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return memberService.findPage(queryPageBean);
    }

    /**
     * 导出会员情况
     *
     * @param request
     * @return
     */
    @GetMapping("exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取缓存数据
            List<Member> members = TypeConversionUtils.castList(request.getSession().getAttribute("members"), Member.class);
            //获取导出的模板路径 ,File.separator 根据不同系统生成不同分隔符
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "member.xlsx";
            XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //获取第一个sheet
            XSSFSheet sheet = sheets.getSheetAt(0);
            int sheetRow = 1;
            for (Member member : members) {
                log.info("member====: " + member);
                String fileNumber = member.getFileNumber();
                //获取会员的体检预约相关数据
                List<String> memberMap = memberService.getMemberMessage(member.getId());
//                判断
                log.info("memberMap====" + memberMap);
                if (memberMap != null && memberMap.size() > 0) {
                    String setmealName = memberMap.get(0);
                    String addressName = memberMap.get(1);
                    String checkgroupName = memberMap.get(2);
                    String checkitemName = memberMap.get(3);
                    String sex = member.getSex();
                    if ("1".equals(sex)) {
                        sex = "男";
                    } else if ("2".equals(sex)) {
                        sex = "女";
                    } else {
                        sex = "未知";
                    }
                    String regTime = DateUtils.parseDate2String(member.getRegTime());//转换时间
                    sheet.createRow(sheetRow);//创建行，避免空指针
                    XSSFRow row = sheet.getRow(sheetRow);//获取第几行
                    for (int i = 0; i < 11; i++) {
                       //在当前行创建单元格
                        row.createCell(i);
                    }
                    row.getCell(0).setCellValue(fileNumber);
                    row.getCell(1).setCellValue(member.getName());
                    row.getCell(2).setCellValue(sex);
                    row.getCell(3).setCellValue(member.getAge());
                    row.getCell(4).setCellValue(member.getHealthmanager());
                    row.getCell(5).setCellValue(regTime);
                    row.getCell(6).setCellValue(member.getPhoneNumber());
                    row.getCell(7).setCellValue(setmealName);
                    row.getCell(8).setCellValue(addressName);
                    row.getCell(9).setCellValue(checkgroupName);
                    row.getCell(10).setCellValue(checkitemName);
                    sheetRow++;//行加一
                }
            }
//使用输出流进行表格下载,基于浏览器作为客户端下载
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=member.xlsx");//指定以附件形式进行下载
            OutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            outputStream.flush(); //清空输出流
            outputStream.close();
            sheets.close();
//            return new Result(true, MessageConstant.EXPORT_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
//            return new Result(false, MessageConstant.EXPORT_MEMBER_ERROR);
        }
    }
    /**
     * 缓存会员数据 session域
     *
     * @param members
     * @param request
     */
    @PostMapping("findMemberByIds")
    public Result findMemberByIds(@RequestBody List<Member> members, HttpServletRequest request) {
        request.getSession().setAttribute("members", members);
        return new Result(true, "缓存成功");
    }
}
