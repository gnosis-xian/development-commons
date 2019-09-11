package top.agno.gnosis.utils.file.oss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description:
 * @author: YT-ZXY
 * @Date: 2019/6/11 17:07
 */
public class FileDownLoadUtil {

    public static HttpServletResponse downLoadFiles(HttpServletRequest req, List<Map<String, Object>> files, HttpServletResponse response) throws Exception {

        try {
            // List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
            // 创建一个临时压缩文件
            // 临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，最好是放在服务器上，方法最后有删除临时文件的步骤

            String zipBasePath = req.getSession().getServletContext().getRealPath("/");
            String zipName = "tradeInvoice.zip";
            String zipFilename = zipBasePath + File.separator + zipName;
            File file = new File(zipFilename);
            file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            // response.getWriter()
            // 创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(req, file, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 把接受的全部文件打成压缩包
     */
    public static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> file = (Map<String, Object>) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    public static void zipFile(Map<String, Object> inputFile, ZipOutputStream ouputStream) {
        try {
            InputStream in = (InputStream) inputFile.get("file");
            BufferedInputStream bins = new BufferedInputStream(in, 512);
            ZipEntry entry = new ZipEntry(inputFile.get("fileName").toString());
            ouputStream.putNextEntry(entry);
            // 向压缩文件中输出数据
            int nNumber;
            byte[] buffer = new byte[512];
            while ((nNumber = bins.read(buffer)) != -1) {
                ouputStream.write(buffer, 0, nNumber);
            }
            // 关闭创建的流对象
            bins.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServletResponse downloadZip(HttpServletRequest req, File file, HttpServletResponse response) {
        if (file.exists() == false) {
            System.out.println("待压缩的文件目录：" + file + "不存在.");
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                String origin = req.getHeader("Origin");
                response.setHeader("Access-Control-Allow-Origin", origin);
                // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
}
