package com.glasssix.dubbo.utils;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: g6
 * @description: 导出
 * @author: wenjiang
 * @create: 2020-08-31 08:46
 **/
public class ExportSupport {

    public static void export(Object obj, Class className, String fileName, String[] excelHeaders, String[] fields) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        //导出数据为空,直接返回
        List query = (List) obj;
        if (query.size() == 0) {
            return;
        }
        //获取导出数据的总条数
        int countColumnNum = query.size();
        //创建XSSFWorkbook文件对象
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.createSheet(fileName);
        SXSSFSheet sheet = workbook.getSheetAt(0);
        // 获取表的第一行
        SXSSFRow firstRow = sheet.createRow(0);
        //创建表的第一行的每列的说明

        SXSSFCell[] firstCells = new SXSSFCell[excelHeaders.length];
        //给表的第一行的每一列赋值
        for (int j = 0; j < excelHeaders.length; j++) {
            firstCells[j] = firstRow.createCell(j);
            firstCells[j].setCellValue(new XSSFRichTextString(excelHeaders[j]));
        }
        //反射从对象中取出方法对象
        List<Method> methodList = new ArrayList<>();
        for (String method : fields) {
            methodList.add(className.getMethod("get" + firstUpper(method)));
        }

        //引入依赖包，创建线程池

        //把表的第一列写好后,接下来从表的第二列开始把对应的值写入到文件里
        Object data;
        for (int i = 0; i < countColumnNum; i++) {
            //给execl创建一行
            SXSSFRow row = sheet.createRow(i + 1);
            //取出数据
            Object object = query.get(i);
            //循环给列赋值
            for (int column = 0; column < excelHeaders.length; column++) {
                //确认每一列对应的表的列
                for (int method = 0; method < methodList.size(); method++) {
                    SXSSFCell cell = row.createCell(method);
                    data = methodList.get(method).invoke(object);
                    cell.setCellValue(data == null ? "" : String.valueOf(data));
                }
            }
        }
        //写一个try catch捕捉异常(response获取输出流)
        OutputStream os = null;
        try {
            //防止导出的Excel文件名中文乱码
            String filename = new String(fileName.getBytes("utf-8"), "ISO_8859_1");
            //文件名
//            if (ObjectUtils.isNotEmpty(ContactQo.fileName)){
                response.setHeader("fileName", URLEncoder.encode(fileName,"UTF-8"));
//            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");
//            response.setContentType("application");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            os = response.getOutputStream();
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO流异常");
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("关闭IO流异常");
            }
        }
    }

    /**
     * POI导出公共方法
     *
     * @param obj
     * @param className
     * @param fileName
     * @param excelHeaders
     * @param fields
     * @throws Exception
     */
    public static void exportTemplate(Object obj, Class className, String fileName, String[] excelHeaders, String[]
            fields) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        //导出数据为空,直接返回
        List query = (List) obj;
        if (query.size() == 0) {
            return;
        }
        //获取导出数据的总条数
        int countColumnNum = query.size();
        //创建XSSFWorkbook文件对象
        XSSFWorkbook book = null;
        //管理员导出数据
        book = new XSSFWorkbook();
        //创建一个Name的新表
        XSSFSheet sheet = book.createSheet(fileName);
        // 获取表的第一行
        XSSFRow firstRow = sheet.createRow(0);
        //创建表的第一行的每列的说明
        XSSFCell[] firstCells = new XSSFCell[excelHeaders.length];
        //给表的第一行的每一列赋值
        for (int j = 0; j < excelHeaders.length; j++) {
            firstCells[j] = firstRow.createCell(j);
            firstCells[j].setCellValue(new XSSFRichTextString(excelHeaders[j]));
        }
        //反射从对象中取出方法对象
        List<Method> methodList = new ArrayList<>();
        for (String method : fields) {
            methodList.add(className.getMethod("get" + firstUpper(method)));
        }
        //把表的第一列写好后,接下来从表的第二列开始把对应的值写入到文件里
        for (int i = 0; i < countColumnNum; i++) {
            //给execl创建一行
            XSSFRow row = sheet.createRow(i + 1);
            //取出数据
            Object object = query.get(i);
            //循环给列赋值
            for (int column = 0; column < excelHeaders.length; column++) {
                //确认每一列对应的表的列
                for (int method = 0; method < methodList.size(); method++) {
                    XSSFCell cell = row.createCell(method);
                    cell.setCellValue(methodList.get(method).invoke(object) == null ? "" : methodList.get(method).invoke(object).toString());
                }
            }
        }
        //写一个try catch捕捉异常(response获取输出流)
        OutputStream os = null;
        try {
            //防止导出的Excel文件名中文乱码
            String filename = new String(fileName.getBytes("utf-8"), "ISO_8859_1");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");
            response.setContentType("application");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            os = response.getOutputStream();
            book.write(os);
        } catch (IOException e) {
            System.out.println("IO流异常");
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                System.out.println("关闭IO流异常");
            }
        }
    }


    /**
     * 将字符串的第一个字母转换成大写
     */
    public static String firstUpper(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }
}

