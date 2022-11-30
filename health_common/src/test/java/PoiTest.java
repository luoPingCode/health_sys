import com.health.utils.POIUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

/**
 * @author LuoPing
 * @date 2022/11/1 14:54
 */

public class PoiTest {
//    @Test
//    public void readExcel() throws Exception {
////        获取文件sheet
//        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(new File("D:\\luopwork\\test.xlsx")));
//        XSSFSheet sheetAt = sheets.getSheetAt(0);
////        遍历每一行数据
//        for (Row row : sheetAt) {
//            for (Cell cell : row) {
//                cell.setCellType(Cell.CELL_TYPE_STRING);
//                System.out.println(cell.getStringCellValue());
//            }
//            System.out.println("--------");
//        }
////        关闭资源
//        sheets.close();
//    }
//    @Test
//    public void readExcel2() throws Exception {
//        //        获取文件sheet
//        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("D:\\luopwork\\test.xlsx")));
//        XSSFSheet sheetAt = workbook.getSheetAt(0);
//        int lastRowNum = sheetAt.getLastRowNum();//获取行数
//        System.out.println(lastRowNum + "lastRowNum");
//        for (int i = 0; i <= lastRowNum; i++) {
//            Row row = sheetAt.getRow(i);
//            short lastCellNum = row.getLastCellNum();//获取列数
//            System.out.println(lastCellNum + "lastCellNum");
//            for (int j = 0; j < lastCellNum; j++) {
//                Cell cell = row.getCell(j);
//                cell.setCellType(Cell.CELL_TYPE_STRING);
//                System.out.println(cell.getStringCellValue());
//            }
//        }
//        workbook.close();
//    }

    @Test
    public void writeToFile() throws Exception {
        XSSFWorkbook sheets = new XSSFWorkbook();
        XSSFSheet sheet = sheets.createSheet("代码完成情况");
//        插入第一行 标题
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("年龄");
        row.createCell(2).setCellValue("性别");

        for (int i = 0; i < 2; i++) {
            XSSFRow row1 = sheet.createRow(i+1);
            row1.createCell(0).setCellValue("张三");
            row1.createCell(1).setCellValue(12);
            row1.createCell(2).setCellValue("男");
        }
//        FileOutputStream stream = new FileOutputStream(new File("D:\\hello.xlsx"));
//        sheets.write(stream);//写出
//        stream.flush();//刷新
        sheets.close();
    }
}
