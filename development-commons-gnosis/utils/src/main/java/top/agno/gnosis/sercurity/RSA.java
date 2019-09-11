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
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * <pre>
 * RSA加密
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class RSA {
    /**
     * 加密算法RSA:RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法:MD5withRSA
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key:RSAPublicKey
     */
    public static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key:RSAPrivateKey
     */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小:117
     */
    public static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小:1024
     */
    public static final int MAX_DECRYPT_BLOCK = 1024;

    /**
     * RSA秘钥长度:512
     */
    public static final int KEY_LENGTH = 512;

    private RSA() {
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return 字节数组
     */
    public static byte[] encrypt(final byte[] data, final byte[] privateKey) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 获取公钥
     *
     * @param modulus        密钥modulus值
     * @param publicExponent 公钥的Exponent值
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey getPublicKey(final String modulus, final String publicExponent) {
        try {
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 获取私钥
     *
     * @param modulus         密钥modulus值
     * @param privateExponent 私钥的Exponent值
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey getPrivateKey(final String modulus, final String privateExponent) {
        try {
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 获取创建密钥的Modulus值
     *
     * @param keyPair 密钥对
     * @return 密钥的Modulus值
     */
    public static String getModulus(final KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getModulus().toString();
    }

    /**
     * 获取创建公钥的Exponent值
     *
     * @param keyPair 密钥对
     * @return 公钥的Exponent值
     */
    public static String getPublicExponent(final KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getPublicExponent().toString();
    }

    /**
     * 获取创建私钥的Exponent值
     *
     * @param keyPair 密钥对
     * @return 私钥的Exponent值
     */
    public static String getPrivateExponent(final KeyPair keyPair) {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return privateKey.getPrivateExponent().toString();
    }

    /**
     * 返回一个密钥对
     *
     * @return 密钥对
     * @throws Exception 异常
     */
    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 密钥位数
            keyPairGen.initialize(KEY_LENGTH);
            // 密钥对
            return keyPairGen.generateKeyPair();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
