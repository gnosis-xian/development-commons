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

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 名称: JavaBeanUtil
 * 描述: JavaBean处理
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public final class JavaBeanUtil {

    private JavaBeanUtil() {
    }

    /**
     * 高性能map to bean
     *
     * @param bean             转换的bean对象
     * @param map              转换的map
     * @param allowEmptyString string类型中是否允许""赋值
     */
    public static void map2bean(final Object bean, final Map<String, Object> map, final boolean allowEmptyString) {
        map2bean(bean, map, allowEmptyString, DateUtil.HUJIANG_DATE_FORMAT);
    }

    /**
     * 高性能map to bean
     *
     * @param bean             转换的bean对象
     * @param map              转换的map
     * @param allowEmptyString string类型中是否允许""赋值
     * @param dataFormat       如果map中有日期转换为bean中String,需要转为相应格式 如：yyyy-MM-dd HH:mm:ss
     */
    @SuppressWarnings({"rawtypes"})
    public static void map2bean(final Object bean, final Map<String, Object> map, final boolean allowEmptyString,
                                final String dataFormat) {
        Class beanClass = bean.getClass();

        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + "set".length());
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object o = map.get(field);
                    if (o == null) {
                        continue;
                    }
                    if (o instanceof String) {
                        if (o.toString().trim().length() == 0 && allowEmptyString) {
                            continue;
                        }
                        method.invoke(bean, new Object[]{o});
                    } else {
                        method.invoke(bean, new Object[]{o});
                    }
                }
            } catch (final Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 高性能map to bean
     *
     * @param clazz            转换的bean对象
     * @param map              转换的map
     * @param allowEmptyString 是否允许为空字符串
     * @param <T>              返回类型
     * @return bean
     */
    public static <T> T map2bean(final Class<T> clazz, final Map<String, Object> map, final boolean allowEmptyString) {
        return map2bean(clazz, map, allowEmptyString, DateUtil.DEFAULT_DATE_PATTERN);
    }

    /**
     * 高性能map to bean
     *
     * @param <T>              返回类型
     * @param clazz            转换的bean对象
     * @param map              转换的map对象
     * @param allowEmptyString string类型中是否允许""赋值
     * @param dataFormat       如果map中有日期转换为bean中String,需要转为相应格式 如：yyyy-MM-dd HH:mm:ss
     * @return bean
     */
    public static <T> T map2bean(final Class<T> clazz, final Map<String, Object> map, final boolean allowEmptyString,
                                 final String dataFormat) {
        try {
            T object = clazz.newInstance();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                try {
                    if (method.getName().startsWith("set")) {
                        String field = method.getName();
                        field = field.substring(field.indexOf("set") + "set".length());
                        field = field.toLowerCase().charAt(0) + field.substring(1);

                        Object o = map.get(field);
                        if (o == null) {
                            continue;
                        }
                        if (o instanceof String) {
                            if (o.toString().trim().length() == 0 && allowEmptyString) {
                                continue;
                            }
                            method.invoke(object, new Object[]{o});
                        } else {
                            method.invoke(object, new Object[]{o});
                        }
                    }
                } catch (final Exception ex) {
                    throw new RuntimeException(ex.getMessage(), ex);
                }
            }
            return object;
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * bean to map
     *
     * @param bean     转换的bean对象
     * @param mapParam 转换的map对象
     * @return map 如果bean为null 返回null
     */
    public static Map<String, Object> bean2Map(final Map<String, Object> mapParam, final Object bean) {
        Map<String, Object> map = mapParam;
        if (map == null) {
            map = new HashMap<String, Object>();
        }

        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            try {
                if (!"getClass".equals(method.getName()) && method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + "get".length());
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object o = method.invoke(bean, (Object[]) null);
                    if (o == null) {
                        continue;
                    }
                    map.put(field, o);
                }
            } catch (final Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        return map;
    }

    /**
     * bean to map
     *
     * @param bean 转换的bean对象
     * @return map 如果bean为null 返回null
     */
    public static Map<String, Object> bean2Map(final Object bean) {
        Map<String, Object> map = new HashMap<String, Object>();

        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            try {
                if (!"getClass".equals(method.getName()) && method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + "get".length());
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object o = method.invoke(bean, (Object[]) null);
                    if (o == null) {
                        continue;
                    }
                    map.put(field, o);
                }
            } catch (final Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        return map;
    }

    /**
     * bean to map
     *
     * @param bean        转换的bean对象
     * @param isIncludMap 是否读取Map数据
     * @return map 如果bean为null 返回null
     */
    public static Map<String, Object> bean2Map(final Object bean, final boolean isIncludMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            try {
                if (!"getClass".equals(method.getName()) && method.getName().startsWith("get") && (!isIncludMap && method.getReturnType() != Map.class)) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + "get".length());
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object o = method.invoke(bean, (Object[]) null);
                    if (o == null) {
                        continue;
                    }
                    map.put(field, o);
                }
            } catch (final Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        return map;
    }

    /**
     * bean to map
     *
     * @param bean        转换的bean对象
     * @param isIncludMap 是否读取Map数据
     * @return map 如果bean为null 返回null
     */
    public static Map<String, Serializable> bean2Map(final Serializable bean, final boolean isIncludMap) {
        Map<String, Serializable> map = new HashMap<>();

        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            try {
                if (!"getClass".equals(method.getName()) && method.getName().startsWith("get") && (!isIncludMap && method.getReturnType() != Map.class)) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + "get".length());
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Serializable o = (Serializable) method.invoke(bean, (Object[]) null);
                    if (o == null) {
                        continue;
                    }
                    map.put(field, o);
                }
            } catch (final Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        return map;
    }

    /**
     * map转字符串
     * map {key:value,key1:value1...} to key=value&key1=value1
     *
     * @param map map
     * @return 字符串
     */
    public static String map2String(final Map<String, Serializable> map) {
        ArrayList<String> fields = Lists.newArrayList();
        for (Map.Entry<String, Serializable> entry : map.entrySet()) {
            if (EmptyUtils.isNotEmpty(entry.getValue())) {
                fields.add(entry.getKey() + SpecharsUtil.SYMBOL_EQUALS + entry.getValue() + SpecharsUtil.SYMBOL_AMPERSAND);
            }
        }
        int size = fields.size();
        String[] arrayToSort = fields.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
    }
}
