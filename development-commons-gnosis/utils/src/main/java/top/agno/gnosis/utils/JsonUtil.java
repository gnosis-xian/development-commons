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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 名称: JsonUtil
 * 描述: Json工具类处理
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public final class JsonUtil implements java.io.Serializable {

    private static final long serialVersionUID = -8872078079583695100L;

    static {
        JSON.DEFFAULT_DATE_FORMAT = DateUtil.HUJIANG_DATE_FORMAT;
    }

    private JsonUtil() {
    }

    /**
     * 对象转json
     *
     * @param obj               对象
     * @param serializerFeature 序列化特性
     * @return String str
     */
    public static String object2JSON(final Object obj, final SerializerFeature... serializerFeature) {
        if (obj == null) {
            return "{}";
        }
        return JSON.toJSONString(obj, serializerFeature);
    }

    /**
     * 对象转json
     *
     * @param obj               对象
     * @param serializeConfig   序列化配置
     * @param serializerFeature 序列化特性
     * @return String str
     */
    public static String object2JSON(final Object obj, final SerializeConfig serializeConfig,
                                     final SerializerFeature... serializerFeature) {
        if (obj == null) {
            return "{}";
        }
        return JSON.toJSONString(obj, serializeConfig, serializerFeature);
    }

    /**
     * 对象转json
     *
     * @param obj 对象
     * @return String str
     */
    public static String object2JSON(final Object obj) {
        if (obj == null) {
            return "{}";
        }
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
    }

    /**
     * json转对象
     *
     * @param json  json字符串
     * @param clazz 类型
     * @param <T>   泛型
     * @return T 对象实例
     */
    public static <T> T json2Object(final String json, final Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    /**
     * json转对象
     *
     * @param json      json字符串
     * @param reference 类型
     * @param <T>       泛型
     * @return T 对象实例
     */
    public static <T> T json2Reference(final String json, final TypeReference<T> reference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, reference);
    }

    /**
     * json转对象
     *
     * @param json     json字符串
     * @param type     类型
     * @param features 特性
     * @param <T>      泛型
     * @return T 对象实例
     */
    public static <T> T json2Reference(final String json, final Type type, final Feature... features) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, type, features);
    }

    /**
     * json转Map
     *
     * @param json     json字符串
     * @param features 特性
     * @return Map实例
     */
    public static Map<String, Object> json2Map(final String json, final Feature... features) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        }, features);
    }


    /**
     * json转对象
     *
     * @param json      json字符串
     * @param reference 类型
     * @param features  特性
     * @param <T>       泛型
     * @return T 对象实例
     */
    public static <T> T json2Reference(final String json, final TypeReference<T> reference, final Feature... features) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSON.parseObject(json, reference, features);
    }

    /**
     * 字节反序列化
     *
     * @param bytes 字节数组
     * @param <T>   泛型
     * @return T 对象实例
     */
    public static <T> T deserialize(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return JSON.parseObject(bytes, new TypeReference<T>() {
        }.getType());
    }

    /**
     * 对象序列化
     *
     * @param t   对象实例
     * @param <T> 泛型
     * @return byte[]字节数据
     */
    public static <T> byte[] serialize(final T t) {
        if (t == null) {
            return null;
        }
        return JSON.toJSONBytes(t, SerializerFeature.WriteClassName);
    }

    /**
     * fastjson 转成实体类
     *
     * @param cls        类型
     * @param jsonString json字符串
     * @param <T>        集合类型
     * @return T 泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromFastjson(final String jsonString, final Class<?> cls) {
        return (T) JSON.parseObject(jsonString, cls);
    }

    /**
     * fastjson 将实体类转成json
     *
     * @param obj 对象
     * @return String json字符串
     */
    public static String toFastjson(final Object obj) {
        return JSON.toJSONString(obj);
    }

    private static final SerializeConfig config;
    private static final SerializerFeature[] features = {
            /**
             *  输出空置字段
            */
            SerializerFeature.WriteMapNullValue,
            /**
             * list字段如果为null，输出为[]，而不是null
            */
            SerializerFeature.WriteNullListAsEmpty,
            /**
             * 数值字段如果为null，输出为0，而不是null
            */
            SerializerFeature.WriteNullNumberAsZero,
            /**
             * Boolean字段如果为null，输出为false，而不是null
            */
            SerializerFeature.WriteNullBooleanAsFalse,
            /**
             * 字符类型字段如果为null，输出为""，而不是null
            */
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteDateUseDateFormat
            //SerializerFeature.DisableCircularReferenceDetect
    };

    static {
        config = new SerializeConfig();
        /**
         * 使用和json-lib兼容的日期输出格式
         */
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        /**
         * 使用和json-lib兼容的日期输出格式
         */
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    /**
     * 将对象转换为Json字符串
     *
     * @param t 对象
     * @return String 转换后的Json字符串
     */
    public static <T> String toString(T t) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        return toString(t, dateFormat);
    }

    /**
     * 将对象转换为Json字符串
     *
     * @param t          对象
     * @param dateFormat 日期类型转换格式
     * @param <T>
     * @return String 转换后的Json字符串
     */
    public static <T> String toString(T t, String dateFormat) {
        JSON.DEFFAULT_DATE_FORMAT = dateFormat;
        return JSON.toJSONString(t, features);
    }

    /**
     * 解析Json字符串为对象
     *
     * @param json  Json字符串
     * @param clazz 目标对象类型
     * @return T 目标对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 解析Json字符串为对象列表
     *
     * @param json  Json字符串
     * @param clazz 目标对象类型
     * @return List<T> 目标对象列表
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 解析Json字符串为MAP
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(String json) {
        return json != null && !json.isEmpty() ? (Map) JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        }, new Feature[0]) : null;
    }


    public static JSONObject parseObject2(String json) {
        JSONObject result = new JSONObject();
        return result;
    }

    /**
     * 判断字符串是否可以转化为json对象
     *
     * @param content 待判断内容
     * @return
     */
    public static boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if (StringUtils.isBlank(content)) {
            return false;
        }
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
