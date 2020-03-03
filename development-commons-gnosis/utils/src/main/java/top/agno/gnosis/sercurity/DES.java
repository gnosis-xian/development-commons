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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * <pre>
 * Des加密
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public final class DES {

    /**
     * yeshjcom
     */
    private static final String CRYPT_KEY = "yeshjcom";
    /**
     * 16
     */
    private static final int HEX = 16;
    /**
     * 0xFF
     */
    private static final int BY_HEX = 0xFF;
    /**
     * CHARSET
     */
    private static final Charset CHARSET = Charset.defaultCharset();

    private DES() {
    }

    /**
     * 解密
     *
     * @param message 密文
     * @return 明文
     */
    public static String decrypt(final String message) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(CRYPT_KEY.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(CRYPT_KEY.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(hex2byte(message)));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * des3 cbc解密
     *
     * @param key   秘钥
     * @param keyiv ivkey
     * @param data  密文
     * @return 明文
     */
    public static String des3DecodeCBC(final String key, final String keyiv, final String data) {
        try {
            byte[] outs = decryptCBC(Base64.getDecoder().decode(key), Base64.getDecoder().decode(keyiv),
                    Base64.getDecoder().decode(data));
            return new String(outs, CHARSET);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * des3 cbc加密
     *
     * @param key   秘钥
     * @param keyiv ivkey
     * @param data  明文
     * @return 密文
     */
    public static String des3EncodeCBC(final String key, final String keyiv, final String data) {
        try {
            byte[] outs = encryptCBC(Base64.getDecoder().decode(key), Base64.getDecoder().decode(keyiv),
                    data.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(outs);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Des CBC加密
     */
    private static byte[] encryptCBC(final byte[] key, final byte[] keyiv, final byte[] data) {
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESEDE");
            SecretKey deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DESEDE/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            return cipher.doFinal(data);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Des CBC解密
     */
    private static byte[] decryptCBC(final byte[] key, final byte[] keyiv, final byte[] data) {
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESEDE");
            SecretKey deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DESEDE/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            return cipher.doFinal(data);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 加密
     *
     * @param message 明文
     * @return 密文
     */
    public static String encrypt(final String message) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(CRYPT_KEY.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(CRYPT_KEY.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return byte2hex(cipher.doFinal(message.getBytes(CHARSET)));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 16进制转字节数组
     *
     * @param ss 16进制
     * @return 字节数组
     */
    public static byte[] hex2byte(final String ss) {
        byte[] digest = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, HEX);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    private static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & BY_HEX));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }
}
