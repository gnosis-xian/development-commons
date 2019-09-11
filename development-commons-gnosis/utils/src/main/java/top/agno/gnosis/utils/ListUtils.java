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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * 名称: ListUtils
 * 描述: List工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ListUtils {

    private ListUtils() {
    }

    /**
     * 求ls对ls2的差集,即ls中有，但ls2中没有的
     *
     * @param ls  集合1
     * @param ls2 集合2
     * @param <T> 数据类型
     * @return 集合1对集合2的差集
     */
    public static <T> List<T> diff(final List<T> ls, final List<T> ls2) {
        List<T> list = new ArrayList(Arrays.asList(new Object[ls.size()]));
        Collections.copy(list, ls);
        list.removeAll(ls2);
        return list;
    }

    /**
     * 求2个集合的交集
     *
     * @param ls  集合1
     * @param ls2 集合2
     * @param <T> 数据类型
     * @return 两集合的交集
     */
    public static <T> List<T> intersect(final List<T> ls, final List<T> ls2) {
        List list = new ArrayList(Arrays.asList(new Object[ls.size()]));
        Collections.copy(list, ls);
        list.retainAll(ls2);
        return list;
    }

    /**
     * 求2个集合的并集
     *
     * @param ls  集合1
     * @param ls2 集合2
     * @param <T> 数据类型
     * @return 两集合的并集
     */
    public static <T> List<T> union(final List<T> ls, final List<T> ls2) {
        List<T> list = new ArrayList(Arrays.asList(new Object[ls.size()]));
        // 将ls的值拷贝一份到list中
        Collections.copy(list, ls);
        list.removeAll(ls2);
        list.addAll(ls2);
        return list;
    }
}
