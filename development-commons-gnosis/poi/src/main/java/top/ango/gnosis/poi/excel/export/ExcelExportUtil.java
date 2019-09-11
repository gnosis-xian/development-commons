package top.ango.gnosis.poi.excel.export;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具类
 *
 * @author : zhangbolong
 * @since : 2019-8-21
 */
@Slf4j
public class ExcelExportUtil {


    /**
     * 日志常量
     */
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);
    public static String NO_DEFINE = "no_define";
    /**
     * 时间格式
     */
    public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认列宽
     */
    public static int DEFAULT_COLUMN_WIDTH = 17;

    public ExcelExportUtil() {
    }

    /**
     * excel自定义导出.
     *
     * @param sheetName  sheet页名称
     * @param titleName  标题
     * @param headerList 列头
     * @param dataList   数据
     * @return OutputStream
     */
    public static <T> OutputStream exportBasicExcel(String sheetName, String titleName, List<ExcelHeaderDto> headerList, List<T> dataList) throws Exception {
        checkParams(sheetName, headerList, dataList);
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        workbook.setCompressTempFiles(true);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setAlignment((short) 2);
        cellStyle.setVerticalAlignment((short) 1);
        Font cellFont = workbook.createFont();
        cellFont.setBoldweight((short) 400);
        cellStyle.setFont(cellFont);
        SXSSFSheet sheet = null;
        int rowIndex = 0;
        if (dataList != null && dataList.size() > 0) {
            Iterator var9 = dataList.iterator();

            label148:
            while (true) {
                while (true) {
                    if (!var9.hasNext()) {
                        break label148;
                    }

                    Object dataObject = var9.next();
                    if (rowIndex % '\uffff' == 0) {
                        sheet = createSheet(sheetName, titleName, headerList, workbook);
                        rowIndex = sheet.getLastRowNum() + 1;
                    }

                    if (null == sheet) {
                        ++rowIndex;
                    } else {
                        SXSSFRow dataRow = sheet.createRow(rowIndex++);
                        int cellIndex = 0;
                        Iterator var13 = headerList.iterator();

                        while (var13.hasNext()) {
                            ExcelHeaderDto headerDto = (ExcelHeaderDto) var13.next();
                            String fieldName = headerDto.getFieldName();
                            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                            try {
                                Method method = dataObject.getClass().getMethod(methodName);
                                Object obj = method.invoke(dataObject);
                                setCellValue(cellStyle, dataRow, cellIndex++, obj);
                            } catch (NoSuchMethodException var32) {
                                var32.printStackTrace();
                            } catch (InvocationTargetException var33) {
                                var33.printStackTrace();
                            } catch (IllegalAccessException var34) {
                                var34.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            createSheet(sheetName, titleName, headerList, workbook);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            workbook.write(out);
        } catch (IOException var30) {
            logger.error("导出Excel出错{}", var30);
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                    workbook.dispose();
                }
            } catch (IOException var29) {
                logger.error("导出Excel出错{}", var29);
            }

        }

        return out;
    }

    public static <T> void exportMoreExcel(String sheetName, String titleName, List<ExcelHeaderDto> headerList,
                                           List<T> dataList, ByteArrayOutputStream out) throws Exception {
        checkParams(sheetName, headerList, dataList);
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        workbook.setCompressTempFiles(true);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setAlignment((short) 2);
        cellStyle.setVerticalAlignment((short) 1);
        Font cellFont = workbook.createFont();
        cellFont.setBoldweight((short) 400);
        cellStyle.setFont(cellFont);
        SXSSFSheet sheet = null;
        int rowIndex = 0;
        if (dataList != null && dataList.size() > 0) {
            Iterator var9 = dataList.iterator();

            label148:
            while (true) {
                while (true) {
                    if (!var9.hasNext()) {
                        break label148;
                    }

                    Object dataObject = var9.next();
                    if (rowIndex % '\uffff' == 0) {
                        sheet = createSheet(sheetName, titleName, headerList, workbook);
                        rowIndex = sheet.getLastRowNum() + 1;
                    }

                    if (null == sheet) {
                        ++rowIndex;
                    } else {
                        SXSSFRow dataRow = sheet.createRow(rowIndex++);
                        int cellIndex = 0;
                        Iterator var13 = headerList.iterator();

                        while (var13.hasNext()) {
                            ExcelHeaderDto headerDto = (ExcelHeaderDto) var13.next();
                            String fieldName = headerDto.getFieldName();
                            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                            try {
                                Method method = dataObject.getClass().getMethod(methodName);
                                Object obj = method.invoke(dataObject);
                                setCellValue(cellStyle, dataRow, cellIndex++, obj);
                            } catch (NoSuchMethodException var32) {
                                var32.printStackTrace();
                            } catch (InvocationTargetException var33) {
                                var33.printStackTrace();
                            } catch (IllegalAccessException var34) {
                                var34.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            createSheet(sheetName, titleName, headerList, workbook);
        }

//        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            workbook.write(out);
        } catch (IOException var30) {
            logger.error("导出Excel出错{}", var30);
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                    workbook.dispose();
                }
            } catch (IOException var29) {
                logger.error("导出Excel出错{}", var29);
            }

        }

    }

    private static SXSSFSheet createSheet(String sheetName, String titleName, List<ExcelHeaderDto> headerList, SXSSFWorkbook workbook) {
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment((short) 2);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom((short) 1);
        headerStyle.setBorderLeft((short) 1);
        headerStyle.setBorderRight((short) 1);
        headerStyle.setBorderTop((short) 1);
        headerStyle.setAlignment((short) 2);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight((short) 700);
        headerStyle.setFont(headerFont);
        int[] arrColWidth = new int[headerList.size()];
        int cellIndex = 0;

        int i;
        for (Iterator var11 = headerList.iterator(); var11.hasNext(); ++cellIndex) {
            ExcelHeaderDto head = (ExcelHeaderDto) var11.next();
            i = head.getHeaderName().getBytes().length;
            Integer columnWidth = head.getColumnWidth() == null ? DEFAULT_COLUMN_WIDTH : head.getColumnWidth();
            arrColWidth[cellIndex] = i < columnWidth ? columnWidth : i;
            sheet.setColumnWidth(cellIndex, arrColWidth[cellIndex] * 256);
        }

        int rowNum = 0;
        SXSSFRow headerRow;
        if (!StringUtils.isEmpty(titleName)) {
            headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue(titleName);
            headerRow.getCell(0).setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerList.size() - 1));
        }

        headerRow = sheet.createRow(rowNum++);

        for (i = 0; i < headerList.size(); ++i) {
            headerRow.createCell(i).setCellValue(((ExcelHeaderDto) headerList.get(i)).getHeaderName());
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        return sheet;
    }

    private static <T> void checkParams(String sheetName, List<ExcelHeaderDto> headerList, List<T> dataList) throws Exception {
        if (null == sheetName) {
            throw new Exception("sheet名称不能为空");
        } else if (CollectionUtils.isEmpty(headerList)) {
            throw new Exception("标题对象集合不能为空");
        } else {
            Iterator var3 = headerList.iterator();

            ExcelHeaderDto dto;
            do {
                if (!var3.hasNext()) {
                    return;
                }

                dto = (ExcelHeaderDto) var3.next();
                if (StringUtils.isEmpty(dto.getHeaderName())) {
                    throw new Exception("标题名称不能为空");
                }
            } while (!StringUtils.isEmpty(dto.getFieldName()));

            throw new Exception("字段属性名不能为空");
        }
    }

    private static void setCellValue(CellStyle cellStyle, SXSSFRow dataRow, int cellIndex, Object obj) {
        SXSSFCell newCell = dataRow.createCell(cellIndex++);
        String cellValue = "";
        if (obj == null) {
            cellValue = "";
        } else if (obj instanceof Date) {
            cellValue = (new SimpleDateFormat(DEFAULT_DATE_PATTERN)).format(obj);
        } else if (!(obj instanceof Float) && !(obj instanceof Double) && !(obj instanceof BigDecimal)) {
            cellValue = obj.toString();
        } else {
            cellValue = (new BigDecimal(obj.toString())).setScale(2, 4).toString();
        }

        newCell.setCellValue(cellValue);
        newCell.setCellStyle(cellStyle);
    }

    public static <T> void genExcelOutStream(HttpServletResponse response, String fileName, String title, String sheetName, List<ExcelHeaderDto> headerList, List<T> dataList) {
        ServletOutputStream outputStream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;

        try {
            os = (ByteArrayOutputStream) exportBasicExcel(sheetName, title, headerList, dataList);
            byte[] content = os.toByteArray();
            is = new ByteArrayInputStream(content);
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentLength(content.length);
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[content.length + 1024];

            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

            bos.flush();
        } catch (Exception var17) {
            logger.error("导出出错{}", var17);
        } finally {
            closeStream(is, outputStream, bis, bos);
        }

    }


    public static <T> void genMoreSheetsExcelOutStream(HttpServletResponse response, String fileName, String title, String sheetName, List<ExcelHeaderDto> headerList, Map<String, List<T>> map) {
        ServletOutputStream outputStream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            for (String key : map.keySet()) {

                exportMoreExcel(key, title, headerList, map.get(key), os);
            }
            byte[] content = os.toByteArray();
            is = new ByteArrayInputStream(content);
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentLength(content.length);
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[content.length + 1024];

            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

            bos.flush();
        } catch (Exception var17) {
            logger.error("导出出错{}", var17);
        } finally {
            closeStream(is, outputStream, bis, bos);
        }

    }

    private static void closeStream(InputStream is, ServletOutputStream outputStream, BufferedInputStream bis, BufferedOutputStream bos) {
        try {
            if (is != null) {
                is.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        } catch (IOException var5) {
            logger.debug("close IOException:" + var5.getMessage());
        }

    }

    /**
     * 获取列头
     *
     * @param headList 请求参数
     * @return ExcelExportDto
     */
    public static List<ExcelHeaderDto> getExcelHeaderDtos(List<ExcelExportSelectDto> headList) {
        List<ExcelHeaderDto> newHeadList = new ArrayList<ExcelHeaderDto>();
        for (ExcelExportSelectDto excelHeadList : headList) {
            ExcelHeaderDto excelHeaderDto = new ExcelHeaderDto();
            excelHeaderDto.setHeaderName(excelHeadList.getName());
            excelHeaderDto.setFieldName(excelHeadList.getKey());
            newHeadList.add(excelHeaderDto);
        }
        return newHeadList;
    }

}
