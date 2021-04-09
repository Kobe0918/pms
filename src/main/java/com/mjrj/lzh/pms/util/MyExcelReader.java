package com.mjrj.lzh.pms.util;

import com.mjrj.lzh.pms.entity.SysUser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-05-27 19:27
 * @Description: ${Description}
 */
public class MyExcelReader {
    /**
     *      * 
     *      * @author 
     *      * May 30, 2016 5:39:01 PM
     *      * @param beginRow
     *      * @param beanpros
     *      * @param classPathName
     *      * @param results
     *      * @param sheet
     *      * @return
     *      
     */
    public String getDatasByrc(int beginRow, Map<Integer, String> beanpros, String classPathName, List<Object> results, Sheet sheet) {
        try {
            Class clazz = Class.forName(classPathName);
            Set<Integer> set = beanpros.keySet();
            Row row = sheet.getRow(beginRow);
            while (row != null) {
                Object obj = clazz.newInstance();
                for (Integer key : set) {
                    String value = "";
                    if (row != null) {
                        Cell cell = row.getCell(key);
                        if (cell != null) {
                            int type = cell.getCellType();
                            if (type == Cell.CELL_TYPE_STRING) {
                                value = cell.getStringCellValue();
                            } else if (type == Cell.CELL_TYPE_NUMERIC || type == Cell.CELL_TYPE_FORMULA) {
                                value = String.valueOf(cell.getNumericCellValue());
                            } else if (type == Cell.CELL_TYPE_BOOLEAN) {
                                value = String.valueOf(cell.getBooleanCellValue());
                            }
                        }
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(beanpros.get(key), clazz);
                    Method wM = pd.getWriteMethod();
                    wM.invoke(obj, value);
                }
                results.add(obj);
                row = sheet.getRow(++beginRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "用于接受结果的bean不存在";
        }

        return null;
    }

    public static void main(String[] args) {
        MyExcelReader mer = new MyExcelReader();
        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File("F:/ErrorData/test.xls")));
            Sheet sheet = wb.getSheetAt(0);
            List <Object> results = new ArrayList<Object>();
            Map <Integer, String> beanpros = new HashMap<Integer, String>();
            beanpros.put(0, "username");
            beanpros.put(1, "password");
            String s = mer.getDatasByrc(1, beanpros, "com.mjrj.lzh.pms.entity.SysUser", results, sheet);
            if (s == null) {
                for (Object obj : results) {
                    SysUser bean = (SysUser) obj;
                    System.out.println(bean.getUsername() + "---" + bean.getPassword());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}





