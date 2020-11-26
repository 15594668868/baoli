package utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 读取excel所有sheet数据
 */
public class FilesUtils {
    /**
     * 读取excel所有sheet页所有用例
     * @param file 文件名称
     * @return
     */
    public static ArrayList<HashMap<String,String>> readExcel(String file) {
        //后续使用dataformatter格式化单元格为字符串
        DataFormatter dataFormatter = new DataFormatter();
        //获取文件地址
        String dir = System.getProperty("user.dir") + File.separator + "src/main/resources/testcase";
        //存储获取到的文件
        ArrayList result = new ArrayList<HashMap<String, String>>();
        if(file.endsWith(".xlsx")) {
            try {
                //创建book
                XSSFWorkbook book = new XSSFWorkbook(new File(dir + File.separator +file));
                //循环拿到所有sheet
                for(int x = 0; x < book.getNumberOfSheets(); x++) {
                    //拿到当前sheet页
                    XSSFSheet sheet = book.getSheetAt(x);
                    HashMap<String, String> hm;
                    //sheet页第一行也就是表头
                    Row titles = sheet.getRow(0);
                    //sheet页中只有第一行有数据则返回0
                    if(sheet.getLastRowNum() != 0){
                        Row row;
                        //拿到所有的行数循环
                        for(int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                            hm = new HashMap<String, String>();
                            //拿到当前行
                            row = sheet.getRow(i);
                            //循环拿到的表头
                            for(int j = 0; j < titles.getLastCellNum(); j++){
                                //给key为excel首行的标题，value为拿到当前行的值
                                hm.put(dataFormatter.formatCellValue(titles.getCell(j)),dataFormatter.formatCellValue(row.getCell(j)));
                            }
                            //将拿到的当前行存进list
                            result.add(hm);
                        }
                    } else {
                        if(StringUtil.isNotEmpty(titles)){
                            //如果当前只有标题行，则循环标题行给空值
                            hm = new HashMap<String, String>();
                            for(int i = 0; i < titles.getLastCellNum(); i++) {
                                hm.put(titles.getCell(i).getStringCellValue(),"");
                            }
                            result.add(hm);
                        }

                    }
                }
                book.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                HSSFWorkbook book = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(new File(dir + File.separator + file))));
                for(int x = 0; x < book.getNumberOfSheets(); x++) {
                    HSSFSheet sheet = book.getSheetAt(x);
                    HashMap<String, String> hm;
                    Row titles = sheet.getRow(0);
                    if (sheet.getLastRowNum() != 0) {
                        Row row;
                        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                            hm = new HashMap<String, String>();
                            row = sheet.getRow(i);
                            for (int j = 0; j < titles.getLastCellNum(); j++) {
                                hm.put(dataFormatter.formatCellValue(titles.getCell(j)), dataFormatter.formatCellValue(row.getCell(j)));
                            }
                            result.add(hm);
                        }
                    } else {
                        if(StringUtil.isNotEmpty(titles)) {
                            hm = new HashMap<String, String>();
                            for (int i = 0; i < titles.getLastCellNum(); i++) {
                                hm.put(titles.getCell(i).getStringCellValue(), "");
                            }
                            result.add(hm);
                        }
                    }
                }
                book.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取某一行数据
     * @param file 文件名称
     * @param rowName 用例的MethodName
     * @return
     */
    public static HashMap<String,String> readExcelGetRow(String file, String rowName) {
        //后续使用dataformatter格式化单元格为字符串
        DataFormatter dataFormatter = new DataFormatter();
        //获取文件地址
        String dir = System.getProperty("user.dir") + File.separator + "src/test/resources/testfile";
        //存储获取到的文件
        HashMap<String,String> result = new HashMap<String, String>();
        if(file.endsWith(".xlsx")) {
            try {
                //创建book
                XSSFWorkbook book = new XSSFWorkbook(new File(dir + File.separator +file));
                //循环拿到所有sheet
                for(int x = 0; x < book.getNumberOfSheets(); x++) {
                    //拿到当前sheet页
                    XSSFSheet sheet = book.getSheetAt(x);
                    //sheet页第一行也就是表头
                    Row titles = sheet.getRow(0);
                    //sheet页中只有第一行有数据则返回0
                    if(sheet.getLastRowNum() != 0){
                        Row row;
                        //拿到所有的行数循环
                        for(int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                            //拿到当前行
                            row = sheet.getRow(i);
                            //判断传入的rowName是否与当前行第一列相同，相同就拿值，不同就跳出
                            if(StringUtil.equals(rowName, dataFormatter.formatCellValue(row.getCell(0)))) {
                                //循环拿到的表头
                                for(int j = 0; j < titles.getLastCellNum(); j++){
                                    //给key为excel首行的标题，value为拿到当前行的值
                                    result.put(dataFormatter.formatCellValue(titles.getCell(j)),dataFormatter.formatCellValue(row.getCell(j)));
                                }
                                return result;
                            } else {
                                continue;
                            }
                        }
                    } else {
                        if(StringUtil.isNotEmpty(titles)){
                            //如果当前只有标题行，则循环标题行给空值
                            for(int i = 0; i < titles.getLastCellNum(); i++) {
                                result.put(titles.getCell(i).getStringCellValue(),"");
                            }
                        }
                    }
                }
                book.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                HSSFWorkbook book = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(new File(dir + File.separator + file))));
                for(int x = 0; x < book.getNumberOfSheets(); x++) {
                    HSSFSheet sheet = book.getSheetAt(x);
                    Row titles = sheet.getRow(0);
                    if (sheet.getLastRowNum() != 0) {
                        Row row;
                        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                            row = sheet.getRow(i);
                            //判断传入的rowName是否与当前行第一列相同，相同就拿值，不同就跳出
                            String s = dataFormatter.formatCellValue(row.getCell(0));
                            if(StringUtil.equals(rowName, s)) {
                                for (int j = 0; j < titles.getLastCellNum(); j++) {
                                    result.put(dataFormatter.formatCellValue(titles.getCell(j)), dataFormatter.formatCellValue(row.getCell(j)));
                                }
                                return result;
                            } else {
                                continue;
                            }
                        }
                    } else {
                        if(StringUtil.isNotEmpty(titles)){
                            for (int i = 0; i < titles.getLastCellNum(); i++) {
                                result.put(titles.getCell(i).getStringCellValue(), "");
                            }
                        }
                    }
                }
                book.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }



}
