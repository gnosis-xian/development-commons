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

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 * 名称: DateUtil
 * 描述: 判空工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class EmptyUtils {

    private EmptyUtils() {
    }

    /**
     * 判断集合是否为空 coll->null->true coll-> coll.size() == 0 -> true
     *
     * @param <T>  类型
     * @param coll 集合
     * @return 空返回true, 否者返回flase
     */
    public static <T> boolean isEmpty(final Collection<T> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断集合是否不为空
     *
     * @param <T>  类型
     * @param coll 集合
     * @return 非空返回true, 否者返回flase
     */
    public static <T> boolean isNotEmpty(final Collection<T> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断map是否为空
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @param map map
     * @return 空返回true, 否者返回flase
     */
    public static <K, V> boolean isEmpty(final Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断map是否不为空
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @param map map
     * @return 非空返回true, 否者返回flase
     */
    public static <K, V> boolean isNotEmpty(final Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * 判断一个对象是否为空
     *
     * @param <T> 类型
     * @param t   对象实例
     * @return 空返回true, 否者返回flase
     */
    public static <T> boolean isEmpty(final T t) {
        if (t == null) {
            return true;
        }
        if (StringUtils.isEmpty(t.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否不为空
     *
     * @param <T>   类型
     * @param datas 数组实例
     * @return 非空返回true, 否者返回flase
     */
    public static <T> boolean isNotEmpty(final T[] datas) {
        return !isEmpty(datas);
    }

    /**
     * 判断数组是否不为空
     *
     * @param <T>   类型
     * @param datas 数组实例
     * @return 空返回true, 否者返回flase
     */
    public static <T> boolean isEmpty(final T[] datas) {
        return ObjectUtils.isEmpty(datas);
    }

    /**
     * 判断一个对象是否不为空
     *
     * @param <T> 类型
     * @param t   对象实例
     * @return 非空返回true, 否者返回flase
     */
    public static <T> boolean isNotEmpty(final T t) {
        return !isEmpty(t);
    }

    /**
     * 判断多个T是否存在空对象，只判断null不判断空
     * 可用于多参数简化代码：
     * 如： if(parameter1==null || parameter2==null || parameter3==null)
     * 可以简化为：
     * if (EmptyUtils.hasNull(parameter1, parameter2,parameter3))
     *
     * @param <T>   类型
     * @param datas 数组
     * @return 包含null返回true, 否者返回flase
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasNull(final T... datas) {
        for (T t : datas) {
            if (t == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个Map是否存在空对象
     *
     * @param datas 数组
     * @param <K>   键
     * @param <V>   值
     * @return 存在空对象，返回true, 否者返回false
     */
    @SuppressWarnings("unchecked")
    public static <K, V> boolean hasEmpty(final Map<K, V>... datas) {
        for (Map<K, V> data : datas) {
            if (isEmpty(data)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个Collection是否存在空对象
     *
     * @param datas 数组
     * @param <T>   键
     * @return 存在空对象，返回true, 否者返回false
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasEmpty(final Collection<T>... datas) {
        for (Collection<T> data : datas) {
            if (isEmpty(data)) {
                return true;
            }
        }
        return false;
    }

}
