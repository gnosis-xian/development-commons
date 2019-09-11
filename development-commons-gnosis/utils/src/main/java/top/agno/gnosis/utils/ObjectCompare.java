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

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * 对象比较处理类
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class ObjectCompare {

    private ObjectCompare() {}

    /**
     * 判断目标值是否存在与源列表中
     *
     * @param target 源
     * @param source 目标
     * @return 返回是否存在结果 true=存在，false=不存在
     */
    public static boolean isInList(final Object target, final Object... source) {
        if (target == null) {
            return false;
        }

        if (source != null && source.length > 0) {
            for (Object src : source) {
                if (target.equals(src)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断目标值是否存在与源列表中
     * @param target 源
     * @param source 目标
     * @return 返回是否存在结果 true=存在，false=不存在
     */
    public static boolean isInList(final Object target, final String... source) {
        if (target == null) {
            return false;
        }

        if (source != null && source.length > 0) {
            for (String src : source) {
                if (StringUtils.isEmpty(src)) {
                    return false;
                }

                if (target.equals(src.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 正则表达式比较target是否在regExs内
     * @param target 源
     * @param regExs 正则列表
     * @return 返回是否存在结果 true=存在，false=不存在
     */
    public static boolean isInListByRegEx(final String target, final String... regExs) {
        if (StringUtils.isBlank(target)) {
            return false;
        }

        if (regExs != null && regExs.length > 0) {
            for (String regEx : regExs) {
                if (StringUtils.isBlank(regEx)) {
                    continue;
                }

                if (Pattern.compile(regEx).matcher(target).find()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 正则表达式比较target是否在regExs内
     * @param target 源
     * @param regExs 正则列表
     * @return 返回是否存在结果 true=存在，false=不存在
     */
    public static boolean isInListByRegEx(final String target, final Set<String> regExs) {
        if (CollectionUtils.isEmpty(regExs)) {
            return false;
        }

        return isInListByRegEx(target, regExs.toArray(new String[regExs.size()]));
    }

    /**
     * 比较source是否在target结尾
     * @param target 目标
     * @param source 源
     * @return 返回是否存在结果 true=存在，false=不存在
     */
    public static boolean isInEndWiths(final String target, final String... source) {
        if (target == null) {
            return false;
        }

        if (source != null && source.length > 0) {
            for (String suffix : source) {
                if (target.endsWith(suffix)) {
                    return true;
                }
            }
        }

        return false;
    }

}
