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

import org.apache.commons.lang3.math.NumberUtils;

/**
 * <pre>
 * 名称: NumberUtils
 * 描述: number 工具类
 * </pre>
 * @author yto.net.cn
 * @since 1.0.0
 */
public abstract class AbstractNumberUtil extends NumberUtils {

    /**
     * 在两数范围.
     *  <pre>（ [number1..number2) | [number1..number2] | (number1..number2) | (number1..number2]）</pre>
     *
     * @param number1 数字1
     * @param isContainsLeft 是否包含number1
     * @param number2 数字2
     * @param isContainsRight 是否包含number2
     * @return 在范围内返回true, 否则返回flase
     */
    public abstract boolean rang(Integer number1, boolean isContainsLeft, Integer number2, boolean isContainsRight);

    /**
     * 在两数范围 : a < x < c.
     *
     * @param number1 数字1
     * @param number2 数字2
     * @return 在范围内返回true, 否则返回flase
     */
    public abstract boolean rang(Integer number1, Integer number2);

    /**
     * 两数相等
     * <pre>
     *   NumberUtils.fromNullable(null).equal(null) = true
     *   NumberUtils.fromNullable(null).equal(0)   = true
     *   NumberUtils.fromNullable(0).equal(null)  = true
     *   NumberUtils.fromNullable(0).equal(0)  = true
     *   NumberUtils.fromNullable(1).equal(null)  = false
     *   NumberUtils.fromNullable(null).equal(1)  = false
     *   NumberUtils.fromNullable(1).equal(2)  = false
     * </pre>
     * @param number 比较的数字
     * @return 相等返回true, 否则返回flase
     */
    public abstract boolean isEqual(Integer number);

    /**
     * 包含某个数
     *
     * @param numbers 比较的多个数字
     * @return 包含返回true, 否则返回flase
     */
    public abstract boolean contains(Integer... numbers);

    /**
     * 两数比较 小于 <
     * <pre>
     *   NumberUtils.fromNullable(null).less(null) = false
     *   NumberUtils.fromNullable(null).less(0)   = false
     *   NumberUtils.fromNullable(0).less(null)  = false
     *   NumberUtils.fromNullable(0).less(0)  = false
     *   NumberUtils.fromNullable(1).less(null)  = false
     *   NumberUtils.fromNullable(null).less(1)  = true
     *   NumberUtils.fromNullable(1).less(2)  = true
     * </pre>
     * @param number 比较的数字
     * @return 小于返回true, 否则返回flase
     */
    public abstract boolean less(Integer number);

    /**
     * 两数比较 小于等于 <=
     * <pre>
     *   NumberUtils.fromNullable(null).lessOrEqualTo(null) = true
     *   NumberUtils.fromNullable(null).lessOrEqualTo(0)   = true
     *   NumberUtils.fromNullable(0).lessOrEqualTo(null)  = true
     *   NumberUtils.fromNullable(0).lessOrEqualTo(0)  = true
     *   NumberUtils.fromNullable(1).lessOrEqualTo(null)  = false
     *   NumberUtils.fromNullable(null).lessOrEqualTo(1)  = true
     *   NumberUtils.fromNullable(1).lessOrEqualTo(2)  = true
     * </pre>
     * @param number 比较的数字
     * @return 小于等于返回true, 否则返回flase
     */
    public abstract boolean lessOrEqualTo(Integer number);

    /**
     * 两数比较 大于 >
     * <pre>
     *   NumberUtils.fromNullable(null).greaterThan(0) = false
     *   NumberUtils.fromNullable(null).greaterThan(0)   = false
     *   NumberUtils.fromNullable(0).greaterThan(null)  = false
     *   NumberUtils.fromNullable(0).greaterThan(0)  = false
     *   NumberUtils.fromNullable(1).greaterThan(null)  = true
     *   NumberUtils.fromNullable(null).greaterThan(1)  = false
     *   NumberUtils.fromNullable(1).greaterThan(2) = false
     * </pre>
     * @param number 比较的数字
     * @return 大于返回true, 否则返回flase
     */
    public abstract boolean greaterThan(Integer number);

    /**
     * 两数比较 大于等于 >=
     * <pre>
     *  NumberUtils.fromNullable(null).greaterThanOrEqualTo(null) = true
     *  NumberUtils.fromNullable(null).greaterThanOrEqualTo(0) = true
     *  NumberUtils.fromNullable(0).greaterThanOrEqualTo(null) = true
     *  NumberUtils.fromNullable(0).greaterThanOrEqualTo(0) = true
     *  NumberUtils.fromNullable(1).greaterThanOrEqualTo(null) = true
     *  NumberUtils.fromNullable(null).greaterThanOrEqualTo(1) = flase
     *  NumberUtils.fromNullable(1).greaterThanOrEqualTo(2) = flase
     * </pre>
     * @param number 比较的数字
     * @return 大于等于返回true, 否则返回flase
     */
    public abstract boolean greaterThanOrEqualTo(Integer number);

    /**
     * 两数相减
     * <pre>
     *  NumberUtils.fromNullable(null).subtract(null) = 0
     *  NumberUtils.fromNullable(null).subtract(0) = 0
     *  NumberUtils.fromNullable(0).subtract(null) = 0
     *  NumberUtils.fromNullable(0).subtract(0) = 0
     *  NumberUtils.fromNullable(1).subtract(null) = 1
     *  NumberUtils.fromNullable(null).subtract(1) = -1
     *  NumberUtils.fromNullable(1).subtract(2) = -1
     * </pre>
     * @param number 比较的数字
     * @return 相减后的结果
     */
    public abstract int subtract(Integer number);

    /**
     * 空值转换成数字
     * @param number 数字
     * @return 转换结果
     */
    public static NumberOperate fromNullable(final Integer number) {
        return (number == null) ? new NumberOperate(0) : new NumberOperate(number);
    }

}
