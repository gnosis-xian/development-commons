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

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * <pre>
 * 名称: EncryptUtils
 * 描述: 提供部分加密方法
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public final class EncryptUtils {

    /**
     * 盐长度 : 16
     */
    private static final int SALT_BYTE_LENGTH = 16;
    /**
     * 16进制 : 16
     */
    private static final int HEX = 16;
    /**
     * ITERATIONS: 1000
     */
    private static final int ITERATIONS = 1000;
    /**
     * SHA: 0xFF
     */
    private static final int SHA_FF = 0xFF;
    /**
     * SHA: 0x100
     */
    private static final int SHA_100 = 0x100;
    /**
     * PBKDF2 长度: 64 * 8
     */
    private static final int PBKDF2_LENGTH = 64 * 8;

    private EncryptUtils() {
    }

    /**
     * 对字符串进行MD5进行加密处理
     *
     * @param msg 待加密的字符串
     * @return 加密后字符串
     * @deprecated 弃用
     * {@link MD5}
     */
    @Deprecated
    public static String encryptMD5(final String msg) {
        return Hashing.md5().newHasher().putString(msg, Charset.defaultCharset()).hash().toString();
    }

    /**
     * 基本加密处理
     *
     * @param msg  明文
     * @param type 类型
     * @return 密文
     */
    private static String encrypt(final String msg, final String type) {
        MessageDigest md;
        StringBuilder password = new StringBuilder();

        try {
            md = MessageDigest.getInstance("MD5");

            if (StringUtils.isNoneBlank(type)) {
                md.update(type.getBytes());
            } else {
                md.update(msg.getBytes());
            }

            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                String param = Integer.toString((bytes[i] & SHA_FF) + SHA_100, HEX);
                password.append(param.substring(1));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return password.toString();
    }

    /**
     * 盐值的原理非常简单，就是先把密码和盐值指定的内容合并在一起，再使用md5对合并后的内容进行演算，
     * 这样一来，就算密码是一个很常见的字符串，再加上用户名，最后算出来的md5值就没那么容易猜出来了。
     * 因为攻击者不知道盐值的值，也很难反算出密码原文。
     *
     * @param msg 明文
     * @return 加盐密文
     */
    public static String encryptSalt(final String msg) {
        String salt = getSalt();
        return encrypt(msg, salt);
    }

    /**
     * SHA（Secure Hash Algorithm，安全散列算法）是消息摘要算法的一种，被广泛认可的MD5算法的继任者。
     * SHA算法家族目前共有SHA-0、SHA-1、SHA-224、SHA-256、SHA-384和SHA-512五种算法，
     * 通常将后四种算法并称为SHA-2算法
     *
     * @param msg 明文
     * @return 密文
     */
    public static String encryptSHA(final String msg) {
        String salt = getSaltSHA1();
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(msg.getBytes());
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & SHA_FF) + SHA_100, HEX).substring(1));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return sb.toString();
    }

    /**
     * 标准SHA1算法，不加盐
     * sha1 Algorithm without salt
     *
     * @param msg 明文
     * @return 密文
     */
    public static String encryptSHA1(final String msg) {
        return Hashing.sha1().newHasher().putString(msg, Charset.defaultCharset()).hash().toString();
    }

    /**
     * PBKDF2加密
     *
     * @param msg 明文
     * @return 密文
     */
    public static String encryptPBKDF2(final String msg) {
        try {
            int iterations = ITERATIONS;
            char[] chars = msg.toCharArray();
            byte[] salt = getSalt().getBytes();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, PBKDF2_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            return iterations + toHex(salt) + toHex(hash);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 转化十六进制
     *
     * @param array
     * @return
     */
    private static String toHex(final byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(HEX);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * <ul>
     *  <li>SHA-1 (Simplest one – 160 bits Hash)</li>
     *    <li>SHA-256 (Stronger than SHA-1 – 256 bits Hash)</li>
     *    <li>HA-384 (Stronger than SHA-256 – 384 bits Hash)</li>
     *    <li>SHA-512 (Stronger than SHA-384 – 512 bits Hash)</li>
     * </ul>
     *
     * @return
     */
    private static String getSaltSHA1() {
        SecureRandom sr;
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
            return new String(salt);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 盐值的原理非常简单，就是先把密码和盐值指定的内容合并在一起，再使用md5对合并后的内容进行演算，
     * 这样一来，就算密码是一个很常见的字符串，再加上用户名，最后算出来的md5值就没那么容易猜出来了。
     * 因为攻击者不知道盐值的值，也很难反算出密码原文。
     *
     * @return
     */
    private static String getSalt() {
        SecureRandom sr;
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            sr.nextBytes(salt);
            return new String(salt);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
