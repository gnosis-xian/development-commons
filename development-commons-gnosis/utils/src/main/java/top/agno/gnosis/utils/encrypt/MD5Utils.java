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

package top.agno.gnosis.utils.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import top.agno.gnosis.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * md5工具类
 *
 * @author XieXiCai
 * @since 1.0.1
 */
@Slf4j
public final class MD5Utils {

    private static char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest MESSAGE_DIGEST;

    private static ConcurrentMap<String, String> FILE_MD5_MAP = new ConcurrentHashMap<String, String>();

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            log.error("MD5FileUtil messagedigest初始化失败", e);
        }
    }

    private MD5Utils() {
    }

    /**
     * getMD5Str
     *
     * @param str the str
     * @return String
     */
    @SuppressWarnings("all")
    public static String getMD5Str(final String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (final NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException caught!", e);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                // Integer.toHexString转换成后前面不会添0，所以这里要判断
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    /**
     * md5
     *
     * @param file the file
     * @return String
     */
    public static String getFileMD5String(final File file) {
        FileInputStream in = null;
        FileChannel ch = null;
        String md5 = null;

        try {
            if (FILE_MD5_MAP.get(file.getAbsolutePath()) != null) {
                return FILE_MD5_MAP.get(file.getAbsolutePath());
            }

            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MESSAGE_DIGEST.update(byteBuffer);
            md5 = bufferToHex(MESSAGE_DIGEST.digest());
            FILE_MD5_MAP.put(file.getAbsolutePath(), md5);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (ch != null) {
                    ch.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return md5;
    }

    /**
     * getMD5String
     *
     * @param s the s
     * @return String
     */
    public static String getMD5String(final String s) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        return getMD5String(s.getBytes());
    }

    /**
     * getMD5String
     *
     * @param bytes the bytes
     * @return String
     */
    public static String getMD5String(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        try {
            MESSAGE_DIGEST.update(bytes);
            return bufferToHex(MESSAGE_DIGEST.digest());
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private static String bufferToHex(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(final byte[] bytes, final int m, final int n) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    @SuppressWarnings("all")
    private static void appendHexPair(final byte bt, final StringBuffer stringbuffer) {
        char c0 = HEX_DIGITS[(bt & 0xf0) >> 4];
        char c1 = HEX_DIGITS[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 校验密码
     *
     * @param password  the password
     * @param md5PwdStr the md5PwdStr
     * @return boolean
     */
    public static boolean checkPassword(final String password, final String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * 转换byte为Hex字符串
     *
     * @param value     要转换的byte数据
     * @param minlength 生成hex的最小长度（长度不足时会在前面加0
     * @return string
     */
    @SuppressWarnings("all")
    public static String byteToHex(final byte value, final int minlength) {
        String s = Integer.toHexString(value & 0xff);
        if (s.length() < minlength) {
            for (int i = 0; i < (minlength - s.length()); i++) {
                s = "0" + s;
            }
        }
        return s;
    }

    /**
     * MD5加密字符串
     *
     * @param vaule the value
     * @return byte数组
     */
    @SuppressWarnings("all")
    public static byte[] MD5(final byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value);
            return md.digest();
        } catch (final NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * MD5+BASE64Encoder加密字符串
     *
     * @param src the src
     * @return string
     * @throws NoSuchAlgorithmException e
     */
    @SuppressWarnings("all")
    public static String MD5WithBASE64Encoder(final String src) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] d = md5.digest(src.getBytes()); // 等价 md5.update(value); md5.digest(); 两句
        return new String(Base64.encodeBase64(d)); // base64en.encode(d);
    }

    /**
     * test
     *
     * @param args the args
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @SuppressWarnings("all")
    public static void main(final String[] args) throws IOException, NoSuchAlgorithmException {
        System.out.println(getMD5String("stationcloudmsg"));
        String str = "test";
        System.out.println(MD5WithBASE64Encoder(str));
        ;
    }

}
