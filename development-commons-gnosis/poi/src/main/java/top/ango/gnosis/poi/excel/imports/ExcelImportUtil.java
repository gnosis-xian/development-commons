package top.ango.gnosis.poi.excel.imports;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具类
 *
 * @author : zhangbolong
 * @since : 2019-8-21
 */
public class ExcelImportUtil {

    /**
     * 读取文件流
     *
     * @param param
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readXlsPart(ExcelImportParams param) throws Exception {

        InputStream is = new FileInputStream(param.getFilePath());

        Set keySet = param.getMap().keySet();// 返回键的集合

        /** 反射用 **/
        Class<?> demo = null;
        Object obj = null;
        /** 反射用 **/

        List<Object> list = new ArrayList<Object>();
        demo = Class.forName(param.getClassPath());
        String fileType = param.getFilePath().substring(param.getFilePath().lastIndexOf(".") + 1, param.getFilePath().length());

        Workbook wb = null;

        if (ExcelTypeEnum.EXCEL_THREE.getText().equals(fileType)) {
            wb = new HSSFWorkbook(is);
        } else if (ExcelTypeEnum.EXCEL_SEVEN.getText().equals(fileType)) {
            wb = new XSSFWorkbook(is);
        } else {
            throw new Exception("您输入的excel格式不正确");
        }
        int startSheetNum = 0;
        int endSheetNum = 1;
        if (null != param.getSheetIndex()) {
            startSheetNum = param.getSheetIndex() - 1;
            endSheetNum = param.getSheetIndex();
        }
        for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {// 获取每个Sheet表

            int rowNum_x = -1;// 记录第x行为表头
            Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
            List<String> headlist = new ArrayList();// 存放所有的表头字段信息

            Sheet hssfSheet = wb.getSheetAt(sheetNum);

            // 设置默认最大行为2w行
            if (hssfSheet != null && hssfSheet.getLastRowNum() > 50000) {
                throw new Exception("Excel 数据超过60000行,请检查是否有空行,或分批导入");
            }

            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

                if (param.getRowNumIndex() != null && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
                    Row hssfRow = hssfSheet.getRow(param.getRowNumIndex());
                    if (hssfRow == null) {
                        throw new RuntimeException("指定的行为空，请检查");
                    }
                    rowNum = param.getRowNumIndex() - 1;
                }
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                    if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }

                if (rowNum_x == -1) {
                    // 循环列Cell
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

                        Cell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }

                        String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

                        tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
                        tempCellValue = tempCellValue.trim();

                        headlist.add(tempCellValue);

                        Iterator it = keySet.iterator();

                        while (it.hasNext()) {
                            Object key = it.next();
                            if (StringUtils.isNotBlank(tempCellValue)
                                    && StringUtils.equals(tempCellValue, key.toString())) {
                                rowNum_x = rowNum;
                                cellmap.put(param.getMap().get(key).toString(), cellNum);
                            }
                        }
                        if (rowNum_x == -1) {
                            throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
                        }
                    }

                } else {
                    obj = demo.newInstance();
                    Iterator it = keySet.iterator();
                    while (it.hasNext()) {
                        Object key = it.next();
                        Integer cellNum_x = cellmap.get(param.getMap().get(key).toString());
                        if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
                            continue;
                        }
                        String attr = param.getMap().get(key).toString();// 得到属性

                        Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[]{obj.getClass()});

                        Cell cell = hssfRow.getCell(cellNum_x);
                        getValue(cell, obj, attr, attrType, rowNum, cellNum_x, key);

                    }
                    list.add(obj);
                }

            }
        }
        is.close();
        // wb.close();
        return (List<T>) list;
    }


    /**
     * 读取网络流
     *
     * @param param
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readURLExcel(ExcelImportParams param) throws Exception {

        Set keySet = param.getMap().keySet();// 返回键的集合

        /** 反射用 **/
        Class<?> demo = null;
        Object obj = null;
        /** 反射用 **/

        List<Object> list = new ArrayList<Object>();
        demo = Class.forName(param.getClassPath());
        String fileType = param.getFilePath().substring(param.getFilePath().lastIndexOf(".") + 1,
                param.getFilePath().length());
        //InputStream is = new FileInputStream(param.getFilePath());


        InputStream is = param.getInputStream();

        Workbook wb = null;

        if (ExcelTypeEnum.EXCEL_THREE.getText().equals(fileType)) {
            wb = new HSSFWorkbook(is);
        } else if (ExcelTypeEnum.EXCEL_SEVEN.getText().equals(fileType)) {
            wb = new XSSFWorkbook(is);
        } else {
            throw new Exception("您输入的excel格式不正确");
        }
        int startSheetNum = 0;
        int endSheetNum = 1;
        if (null != param.getSheetIndex()) {
            startSheetNum = param.getSheetIndex() - 1;
            endSheetNum = param.getSheetIndex();
        }
        for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {// 获取每个Sheet表

            int rowNum_x = -1;// 记录第x行为表头
            Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
            List<String> headlist = new ArrayList();// 存放所有的表头字段信息

            Sheet hssfSheet = wb.getSheetAt(sheetNum);

            // 设置默认最大行为2w行
            if (hssfSheet != null && hssfSheet.getLastRowNum() > 60000) {
                throw new Exception("Excel 数据超过60000行,请检查是否有空行,或分批导入");
            }

            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

                if (param.getRowNumIndex() != null && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
                    Row hssfRow = hssfSheet.getRow(param.getRowNumIndex());
                    if (hssfRow == null) {
                        throw new RuntimeException("指定的行为空，请检查");
                    }
                    rowNum = param.getRowNumIndex() - 1;
                }
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                    if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }

                if (rowNum_x == -1) {
                    // 循环列Cell
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

                        Cell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }

                        String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

                        tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
                        tempCellValue = tempCellValue.trim();

                        headlist.add(tempCellValue);

                        Iterator it = keySet.iterator();

                        while (it.hasNext()) {
                            Object key = it.next();
                            if (StringUtils.isNotBlank(tempCellValue)
                                    && StringUtils.equals(tempCellValue, key.toString())) {
                                rowNum_x = rowNum;
                                cellmap.put(param.getMap().get(key).toString(), cellNum);
                            }
                        }
                        if (rowNum_x == -1) {
                            throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
                        }
                    }

                } else {
                    obj = demo.newInstance();
                    Iterator it = keySet.iterator();
                    while (it.hasNext()) {
                        Object key = it.next();
                        Integer cellNum_x = cellmap.get(param.getMap().get(key).toString());
                        if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
                            continue;
                        }
                        String attr = param.getMap().get(key).toString();// 得到属性

                        Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[]{obj.getClass()});

                        Cell cell = hssfRow.getCell(cellNum_x);
                        getValue(cell, obj, attr, attrType, rowNum, cellNum_x, key);

                    }
                    list.add(obj);
                }

            }
        }
        is.close();
        // wb.close();
        return (List<T>) list;
    }


    /**
     * 读取流文件
     *
     * @param param
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readInputStreamExcel(ExcelImportParams param) throws Exception {

        Set keySet = param.getMap().keySet();// 返回键的集合

        /** 反射用 **/
        Class<?> demo = null;
        Object obj = null;
        /** 反射用 **/

        List<Object> list = new ArrayList<Object>();
        demo = Class.forName(param.getClassPath());
        String fileType = param.getFilePath().substring(param.getFilePath().lastIndexOf(".") + 1,
                param.getFilePath().length());
        //InputStream is = new FileInputStream(param.getFilePath());

        URL url = new URL(param.getFilePath());
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();


        Workbook wb = null;

        if (ExcelTypeEnum.EXCEL_THREE.getText().equals(fileType)) {
            wb = new HSSFWorkbook(is);
        } else if (ExcelTypeEnum.EXCEL_SEVEN.getText().equals(fileType)) {
            wb = new XSSFWorkbook(is);
        } else {
            throw new Exception("您输入的excel格式不正确");
        }
        int startSheetNum = 0;
        int endSheetNum = 1;
        if (null != param.getSheetIndex()) {
            startSheetNum = param.getSheetIndex() - 1;
            endSheetNum = param.getSheetIndex();
        }
        for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {// 获取每个Sheet表

            int rowNum_x = -1;// 记录第x行为表头
            Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
            List<String> headlist = new ArrayList();// 存放所有的表头字段信息

            Sheet hssfSheet = wb.getSheetAt(sheetNum);

            // 设置默认最大行为2w行
            if (hssfSheet != null && hssfSheet.getLastRowNum() > 60000) {
                throw new Exception("Excel 数据超过60000行,请检查是否有空行,或分批导入");
            }

            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

                if (param.getRowNumIndex() != null && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
                    Row hssfRow = hssfSheet.getRow(param.getRowNumIndex());
                    if (hssfRow == null) {
                        throw new RuntimeException("指定的行为空，请检查");
                    }
                    rowNum = param.getRowNumIndex() - 1;
                }
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                    if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }

                if (rowNum_x == -1) {
                    // 循环列Cell
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

                        Cell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }

                        String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

                        tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
                        tempCellValue = tempCellValue.trim();

                        headlist.add(tempCellValue);

                        Iterator it = keySet.iterator();

                        while (it.hasNext()) {
                            Object key = it.next();
                            if (StringUtils.isNotBlank(tempCellValue)
                                    && StringUtils.equals(tempCellValue, key.toString())) {
                                rowNum_x = rowNum;
                                cellmap.put(param.getMap().get(key).toString(), cellNum);
                            }
                        }
                        if (rowNum_x == -1) {
                            throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
                        }
                    }

                } else {
                    obj = demo.newInstance();
                    Iterator it = keySet.iterator();
                    while (it.hasNext()) {
                        Object key = it.next();
                        Integer cellNum_x = cellmap.get(param.getMap().get(key).toString());
                        if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
                            continue;
                        }
                        String attr = param.getMap().get(key).toString();// 得到属性

                        Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[]{obj.getClass()});

                        Cell cell = hssfRow.getCell(cellNum_x);
                        getValue(cell, obj, attr, attrType, rowNum, cellNum_x, key);

                    }
                    list.add(obj);
                }

            }
        }
        is.close();
        // wb.close();
        return (List<T>) list;
    }


    /**
     * 得到Excel列的值
     *
     * @param cell
     * @param obj
     * @param attr
     * @param attrType
     * @param row
     * @param col
     * @param key
     * @throws Exception
     */
    public static void getValue(Cell cell, Object obj, String attr, Class attrType, int row, int col, Object key)
            throws Exception {
        Object val = null;
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            val = cell.getBooleanCellValue();

        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    if (attrType == String.class) {
                        val = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    } else {
                        val = dateConvertFormat(sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())));
                    }
                } catch (ParseException e) {
                    throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 日期格式转换错误  ");
                }
            } else {
                if (attrType == String.class) {
                    cell.setCellType(CellType.STRING);
                    val = cell.getStringCellValue();
                } else if (attrType == BigDecimal.class) {
                    val = new BigDecimal(cell.getNumericCellValue());
                } else if (attrType == long.class) {
                    val = (long) cell.getNumericCellValue();
                } else if (attrType == Double.class) {
                    val = cell.getNumericCellValue();
                } else if (attrType == Float.class) {
                    val = (float) cell.getNumericCellValue();
                } else if (attrType == int.class || attrType == Integer.class) {
                    val = (int) cell.getNumericCellValue();
                } else if (attrType == Short.class) {
                    val = (short) cell.getNumericCellValue();
                } else {
                    val = cell.getNumericCellValue();
                }
            }

        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            if (attrType.equals(double.class) || attrType.equals(Double.class)) {
                val = Double.parseDouble(cell.getStringCellValue());
            } else {
                val = cell.getStringCellValue();
            }

        }

        setter(obj, attr, val, attrType, row, col, key);
    }


    /**
     * 反射的set方法给属性赋值
     *
     * @param obj   具体的类
     * @param att   类的属性
     * @param value 赋予属性的值
     * @param type  属性是哪种类型 比如:String double boolean等类型
     * @param row
     * @param col
     * @param key
     * @throws Exception
     */
    public static void setter(Object obj, String att, Object value, Class<?> type, int row, int col, Object key)
            throws Exception {
        try {
            Method method = obj.getClass().getMethod("set" + toUpperCaseFirstOne(att), type);
            method.invoke(obj, value);
        } catch (Exception e) {
            throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 赋值异常  " + e);
        }

    }

    /**
     * 将传进来的表头和表头对应的属性存进Map集合，表头字段为key,属性为value
     *
     * @param keyValue 把传进指定格式的字符串解析到Map中
     *                 形如: String keyValue = "手机名称:phoneName,颜色:color,售价:price";
     * @return
     */
    public static Map<String, String> getMap(String keyValue) {
        Map<String, String> map = new HashMap<String, String>();
        if (keyValue != null) {
            String[] str = keyValue.split(",");
            for (String element : str) {
                String[] str2 = element.split(":");
                map.put(str2[0], str2[1]);
            }
        }
        return map;
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder())
                    .append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * String类型日期转为Date类型
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date dateConvertFormat(String dateStr) throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = format.parse(dateStr);
        return date;
    }
}
