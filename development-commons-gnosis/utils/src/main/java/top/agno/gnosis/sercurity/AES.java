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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * <pre>
 * AES加密
 * </pre>
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class AES {

    private AES() {}

    /**
     * AES加密
     * @param content 明文
     * @param key     秘钥
     * @param ivStr   the iv字符串
     * @return 密文
     */
    public static String encrypt(final String content, final String key, final String ivStr) {
        try {
            byte[] contentAES = content.getBytes(Charset.defaultCharset());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = key.getBytes(Charset.defaultCharset());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(contentAES);
            // 此处使用BASE64做转码。
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * AES解密
     * @param content 密文
     * @param key     秘钥
     * @param ivStr   the iv字符串
     * @return 明文
     */
    public static String decrypt(final String content, final String key, final String ivStr) {
        try {
            byte[] raw = key.getBytes(Charset.defaultCharset());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(Charset.defaultCharset()));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] enContent = Base64.getDecoder().decode(content);
            byte[] original = cipher.doFinal(enContent);
            return new String(original, Charset.defaultCharset());
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
