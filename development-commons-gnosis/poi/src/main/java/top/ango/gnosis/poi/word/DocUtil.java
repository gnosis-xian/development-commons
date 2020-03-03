package top.ango.gnosis.poi.word;

import org.apache.poi.xwpf.usermodel.*;
import top.agno.gnosis.utils.StringUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: gaojing [gaojing1996@vip.qq.com]
 */
public class DocUtil {

//    public static void getBuild(HttpServletResponse response, String tmpFile, Map<String, String> contentMap) {
//
//        //        InputStream inputStream = DocUtil.class.getClassLoader().getResourceAsStream(tmpFile);
////        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(tmpFile);
//        HWPFDocument document = null;
//        try {
//            InputStream inputStream = new FileInputStream(tmpFile);
//            document = new HWPFDocument(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 读取文本内容
//        Range bodyRange = document.getRange();
//        // 替换内容
//        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
//            bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
//        }
//
//        BufferedOutputStream bos = null;
//        ServletOutputStream outputStream = null;
//        BufferedInputStream bis = null;
//        InputStream is = null;
//        //导出到文件
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            document.write(byteArrayOutputStream);
//
//            response.setContentType("multipart/form-data;application/msword;charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("请款单" + ".doc").getBytes(), "iso-8859-1"));
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            outputStream = response.getOutputStream();
//            outputStream.write(byteArrayOutputStream.toByteArray());
////            bos.flush();
////            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            closeStream(is, outputStream, bis, bos);
//        }
//    }

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
//            log.error("close IOException:" + var5.getMessage());
        }
    }

    public static boolean replaceAndGenerateWord(HttpServletResponse response, InputStream inputStream, String destFileName, Map<String, String> map) throws IOException {
        return replaceAndGenerateWord(response, inputStream, destFileName, ".docx", map);
    }

    public static boolean replaceAndGenerateWord(HttpServletResponse response, InputStream inputStream, String destFileName, String destFileSuf, Map<String, String> map) throws IOException {
//        String[] sp = srcPath.split("\\.");

        // 比较文件扩展名
//        if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
        XWPFDocument document = new XWPFDocument(inputStream);
        // 替换段落中的指定文字
        Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
        while (itPara.hasNext()) {
            XWPFParagraph paragraph = itPara.next();
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String oneparaString = run.getText(run.getTextPosition());
                if (oneparaString == null || "".equals(oneparaString)) {
                    continue;
                }
                for (Map.Entry<String, String> entry :
                        map.entrySet()) {
                    oneparaString = oneparaString.replace("${" + entry.getKey() + "}", entry.getValue());
                }
                run.setText(oneparaString, 0);
            }

        }

        // 替换表格中的指定文字
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext()) {
            XWPFTable table = itTable.next();
            int rcount = table.getNumberOfRows();
            for (int i = 0; i < rcount; i++) {
                XWPFTableRow row = table.getRow(i);
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    String cellTextString = cell.getText();
                    if (cellTextString == null || "".equals(cellTextString)) {
                        continue;
                    }
                    for (Map.Entry<String, String> e : map.entrySet()) {
                        String pre = cell.getText();
                        cellTextString = cellTextString.replace("${" + e.getKey() + "}", e.getValue());
                        if (!StringUtil.equals(pre, cellTextString)) {
                            cell.removeParagraph(0);
                            cell.setText(cellTextString);
                            break;
                        }
                    }
                }
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);

        response.setContentType("multipart/form-data;application/msword");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((destFileName + destFileSuf).getBytes(), "iso-8859-1"));
        response.setHeader("Access-Control-Allow-Origin", "*");
        ServletOutputStream outputStream = response.getOutputStream();
//            outputStream.write(byteArrayOutputStream.toByteArray());
        document.write(outputStream);

        InputStream in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        return true;
//        }

//        return false;

    }

}