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

package top.agno.gnosis.sercurity;

import java.security.MessageDigest;

/**
 * <pre>
 * MD5加密
 * </pre>
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class MD5 {

    /** START: 8 */
    public static final int START = 8;
    /** END: 24 */
    public static final int END = 24;
    /** md5长度：16 */
    public static final int MIN_MD5_LENGTH = 16;
    /** md5长度：32 */
    public static final int MAX_MD5_LENGTH = 32;
    /** HEX: 0x0f */
    private static final int HEX = 0x0f;
    /** RIGHT_MOVE: 4 */
    private static final int RIGHT_MOVE = 4;

    private MD5() {}

    /**
     * 对字符串进行MD5进行加密处理
     *
     * @param msg 明文
     * @return md5密文
     */
    public static String encryptMD5(final String msg) {
        return encryptMD5(msg, MAX_MD5_LENGTH);
    }

    /**
     * 支持16、32位长度加密
     *
     * @param msg    明文
     * @param length 长度
     * @return 密文
     */
    public static String encryptMD5(final String msg, final int length) {
        if (length != MIN_MD5_LENGTH && length != MAX_MD5_LENGTH) {
            throw new RuntimeException("长度只支持16、32位两种模式");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(msg.getBytes("utf-8"));
            String result = toHex(bytes);
            if (length == MIN_MD5_LENGTH) {
                return result.substring(START, END);
            }
            return result;
        } catch (final Exception e) {
            throw new RuntimeException("MD5加密出现错误");
        }
    }

    private static String toHex(final byte[] bytes) {
        final char[] hexDigits = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(hexDigits[(bytes[i] >> RIGHT_MOVE) & HEX]);
            ret.append(hexDigits[bytes[i] & HEX]);
        }
        return ret.toString();
    }

}
