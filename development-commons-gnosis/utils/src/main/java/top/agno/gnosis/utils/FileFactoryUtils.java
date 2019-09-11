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

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * 写文件
 *
 * @author yto.net.cn
 * @since 1.0.1
 */
@Slf4j
public final class FileFactoryUtils {

    private FileFactoryUtils() {}

    /**
     * 文件追加内容
     * @param content  内容
     * @param fileName 文件名称
     */
    public static void appendMethodA(final String content, final String fileName) {
        if (StringUtil.isEmpty(fileName) && StringUtil.isEmpty(content)) {
            return;
        }

        RandomAccessFile randomFile = null;
        Long fileLength = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write((content + "\r\n").getBytes());
            randomFile.close();
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (final IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            fileLength = null;
        }
    }

   /**
    * 文件追加内容
    * @param list  内容
    * @param fileName 文件名称
    */
    public static void appendMethodB(final List<String> list, final String fileName) {
        if (list == null || list.size() == 0 || StringUtil.isAlphanumeric(fileName)) {
            return;
        }

        RandomAccessFile randomFile = null;
        Long fileLength = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            fileLength = randomFile.length();
            // 将写文件指针移到文件尾.
            randomFile.seek(fileLength);
            for (String content : list) {
                try {
                    if (content == null || "".equals(content)) {
                        continue;
                    }

                    randomFile.write((content + "\r\n").getBytes());
                } catch (final Exception e) {
                    log.error("{}--{}", content, e.getMessage(), e);
                    continue;
                }
            }
            randomFile.close();
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            randomFile = null;
            fileLength = null;
        }
    }

    @SuppressWarnings("all")
    public static void main(final String[] args) {
        RandomAccessFile randomFile = null;
        try {
            //打开一个随机访问文件流，按读写方式 
            randomFile = new RandomAccessFile("d:\\5.txt", "rw");
            //文件长度，字节数 
            System.out.println(randomFile.length());
            System.out.println(randomFile.read());
            System.out.println(randomFile.readByte());
            System.out.println(randomFile.readChar());

            System.out.println(randomFile.readLine());
            //将写文件指针移到文件尾。 
            randomFile.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (final IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
