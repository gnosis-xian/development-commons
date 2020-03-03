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

import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 名称: BeanUtil
 * 描述: 基于cglib进行Bean Copy
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public final class BeanUtil extends BeanUtils {

    private BeanUtil() {
    }

    /**
     * 基于cglib进行对象复制
     *
     * @param source 被复制的对象
     * @param clazz  复制对象类型
     * @param <T>    返回类型
     * @return t
     */
    public static <T> T copy(final Object source, final Class<T> clazz) {
        if (EmptyUtils.isEmpty(source)) {
            return null;
        }
        T target = instantiate(clazz);
        BeanCopier copier = BeanCopier.create(source.getClass(), clazz, false);
        copier.copy(source, target, null);
        return target;
    }

    /**
     * 基于cglib进行对象复制
     *
     * @param source 被复制的对象
     * @param target 复制对象
     * @return
     */
    public static void copy(final Object source, final Object target) {
        if (EmptyUtils.isEmpty(source) || EmptyUtils.isEmpty(target)) {
            return;
        }
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map  map
     * @param bean javabean
     * @param <T>  返回类型
     * @return bean
     */
    public static <T> T mapToBean(final Map<String, Object> map, final T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将对象装换为map
     *
     * @param bean 对象
     * @param <T>  返回类型
     * @return map
     */
    public static <T> Map<String, Object> beanToMap(final T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将对象装换为map
     *
     * @param bean      对象
     * @param ignoreMap 是否忽略Map
     * @param <T>       返回类型
     * @return map
     */
    public static <T> Map<String, Object> beanToMap(final T bean, final Boolean ignoreMap) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (null == beanMap.get(key)
                        || (ignoreMap && Map.class.isAssignableFrom(beanMap.get(key).getClass()))) {
                    continue;
                }
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 基于cglib进行对象组复制
     *
     * @param datas 被复制的对象数组
     * @param clazz 复制对象
     * @param <T>   返回类型
     * @return list
     */
    public static <T> List<T> copyByList(final List<?> datas, final Class<T> clazz) {
        if (datas == null) {
            return null;
        }
        if (EmptyUtils.isEmpty(datas)) {
            return new ArrayList<>(0);
        }
        List<T> result = new ArrayList<>(datas.size());
        for (Object data : datas) {
            result.add(copy(data, clazz));
        }
        return result;
    }

    /**
     * 通过class实例化对象
     *
     * @param clazz 类型
     * @param <T>   数据类型
     * @return class实例
     * @throws RuntimeException 运行时异常
     */
    public static <T> T instantiate(final Class<T> clazz) throws RuntimeException {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException ex) {
            throw new RuntimeException(clazz + ":Is it an abstract class?", ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(clazz + ":Is the constructor accessible?", ex);
        }
    }
}
