/*
 * Copyright © 2015-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.agno.gnosis.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.*;

/**
 * <pre>
 * 名称: ZipUtils
 * 描述: 字符串压缩、解压操作工具类
 * </pre>
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class ZipUtils {

    private static final int BYTE_LENGTH = 1024;

    private ZipUtils() {}

    /**
     * 使用gzip进行压缩
     * @param primStr 压缩串
     * @return 压缩后字符串
     */
    public static String gzip(final String primStr) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(primStr.getBytes(Charset.defaultCharset()));
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

   /**
    * 使用gzip进行解压缩
    * @param compressedStr 需要解压的文本
    * @return 解压后的文本
    */
    public static String gunzip(final String compressedStr) {
        byte[] compressed = Base64.getDecoder().decode(compressedStr);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ByteArrayInputStream in = new ByteArrayInputStream(compressed);
                GZIPInputStream ginzip = new GZIPInputStream(in)) {

            byte[] buffer = new byte[BYTE_LENGTH];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return out.toString();
    }

    /**
     * 使用zip进行压缩
     * @param str 压缩前的文本
     * @return 返回压缩后的文本
     */
    public static String zip(final String str) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (ZipOutputStream zout = new ZipOutputStream(out)) {
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /**
     * 使用zip进行解压缩
     * @param compressedStr 压缩后的文本
     * @return 解压后的字符串
     */
    public static String unzip(final String compressedStr) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] compressed = Base64.getDecoder().decode(compressedStr);
        try (ByteArrayInputStream in = new ByteArrayInputStream(compressed);
                ZipInputStream zin = new ZipInputStream(in)) {
            zin.getNextEntry();
            byte[] buffer = new byte[BYTE_LENGTH];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return out.toString();
    }
}
