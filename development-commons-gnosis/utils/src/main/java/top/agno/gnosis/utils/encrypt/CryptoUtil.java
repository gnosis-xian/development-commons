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
import top.agno.gnosis.sercurity.MD5;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <pre>
 * 名称: BeanUtil
 * 描述: 加密工具
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
@Slf4j
public final class CryptoUtil {

    private static final String AES_ALG = "AES";
    private static final String AES_CBC_ALG = "AES/CBC/PKCS5Padding";
    private static final String HMACSHA1_ALG = "HmacSHA1";

    private static final int DEFAULT_HMACSHA1_KEYSIZE = 160; // RFC2401
    private static final int DEFAULT_AES_KEYSIZE = 128;
    private static final int DEFAULT_IVSIZE = 16;
    private static final int H_0XFF = 0xff;

    private CryptoUtil() {
    }

    /**
     * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
     *
     * @param input 原始输入字符数组
     * @param key   HMAC-SHA1密钥
     * @return 字符数组
     */
    public static byte[] hmacSha1(final byte[] input, final byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, HMACSHA1_ALG);
            Mac mac = Mac.getInstance(HMACSHA1_ALG);
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (final GeneralSecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 校验HMAC-SHA1签名是否正确.
     *
     * @param expected 已存在的签名
     * @param input    原始输入字符串
     * @param key      密钥
     * @return 正确返回true，否则返回false
     */
    public static boolean isMacValid(final byte[] expected, final byte[] input, final byte[] key) {
        byte[] actual = hmacSha1(input, key);
        return Arrays.equals(expected, actual);
    }

    /**
     * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求,
     * RFC2401建议最少长度为160位(20字节).
     *
     * @return 返回字节数组
     */
    public static byte[] generateHmacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1_ALG);
            keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (final GeneralSecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 使用AES加密原始字符串.
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥
     * @return 字节数组
     */
    public static byte[] aesEncrypt(final byte[] input, final byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 使用AES加密原始字符串.
     *
     * @param input 原始输入字符数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @return 字节数组
     */
    public static byte[] aesEncrypt(final byte[] input, final byte[] key, final byte[] iv) {
        return aes(input, key, iv, Cipher.ENCRYPT_MODE);
    }

    /**
     * 使用AES解密字符串, 返回原始字符串.
     *
     * @param input Hex编码的加密字符串
     * @param key   符合AES要求的密钥
     * @return 明文
     */
    public static String aesDecrypt(final byte[] input, final byte[] key) {
        byte[] decryptResult = aes(input, key, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES解密字符串, 返回原始字符串.
     *
     * @param input Hex编码的加密字符串
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @return 明文
     */
    public static String aesDecrypt(final byte[] input, final byte[] key, final byte[] iv) {
        byte[] decryptResult = aes(input, key, iv, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 明文
     */
    private static byte[] aes(final byte[] input, final byte[] key, final int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
            Cipher cipher = Cipher.getInstance(AES_ALG);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (final GeneralSecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 明文
     */
    private static byte[] aes(final byte[] input, final byte[] key, final byte[] iv, final int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(AES_CBC_ALG);
            cipher.init(mode, secretKey, ivSpec);
            return cipher.doFinal(input);
        } catch (final GeneralSecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
     *
     * @return 字节数组
     */
    public static byte[] generateAesKey() {
        return generateAesKey(DEFAULT_AES_KEYSIZE);
    }

    /**
     * 生成AES密钥,可选长度为128,192,256位.
     *
     * @param keysize 长度
     * @return AES秘钥
     */
    public static byte[] generateAesKey(final int keysize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALG);
            keyGenerator.init(keysize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (final GeneralSecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 32位小写MD5
     *
     * @param str 字符串
     * @return 32位小写MD5
     */
    public static String parseStrToMd5L32(final String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & H_0XFF;
                if (bt < DEFAULT_IVSIZE) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (final NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return reStr;
    }

    /**
     * 32位大写MD5
     *
     * @param str 字符串
     * @return 32位大写MD5
     */
    public static String parseStrToMd5U32(final String str) {
        String reStr = parseStrToMd5L32(str);
        if (reStr != null) {
            reStr = reStr.toUpperCase();
        }
        return reStr;
    }

    /**
     * 16位大写MD5
     *
     * @param str 字符串
     * @return 16位大写MD5
     */
    public static String parseStrToMd5U16(final String str) {
        String reStr = parseStrToMd5L32(str);
        if (reStr != null) {
            reStr = reStr.toUpperCase().substring(MD5.START, MD5.START);
        }
        return reStr;
    }

    /**
     * 16位小写MD5
     *
     * @param str 字符串
     * @return 16位小写MD5
     */
    public static String parseStrToMd5L16(final String str) {
        String reStr = parseStrToMd5L32(str);
        if (reStr != null) {
            reStr = reStr.substring(MD5.START, MD5.START);
        }
        return reStr;
    }
}
