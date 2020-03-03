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

import com.google.common.base.Optional;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * 名称: NumberOperate
 * 描述: number 操作final 类
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public class NumberOperate extends AbstractNumberUtil {

    private final Integer reference;

    NumberOperate(final Integer reference) {
        this.reference = reference;
    }

    @Override
    public boolean rang(final Integer number1, final Integer number2) {
        return Range.open(number1, number2).contains(reference);
    }

    @Override
    public boolean rang(final Integer number1, final boolean isContainsLeft, final Integer number2,
                        final boolean isContainsRight) {
        BoundType lowerType = BoundType.OPEN, upperType = BoundType.OPEN;
        if (isContainsLeft) {
            lowerType = BoundType.CLOSED;
        }
        if (isContainsRight) {
            upperType = BoundType.CLOSED;
        }
        return Range.range(number1, lowerType, number2, upperType).contains(reference);
    }

    @Override
    public boolean isEqual(final Integer number) {
        // Integer1,
        // Integer2比较值相等，用equal，在Integer=128之前用==是没问题的，但是在128之后，==就会为false
        return Optional.fromNullable(reference).or(0).equals(Optional.fromNullable(number).or(0));
    }

    @Override
    public boolean contains(final Integer... numbers) {
        if (EmptyUtils.isEmpty(numbers)) {
            return false;
        }
        List<Integer> nums = Arrays.asList(numbers);
        return nums.contains(reference);
    }

    @Override
    public int subtract(final Integer number) {
        return Optional.fromNullable(reference).or(0) - Optional.fromNullable(number).or(0);
    }

    @Override
    public boolean greaterThanOrEqualTo(final Integer number) {
        return Optional.fromNullable(reference).or(0) >= Optional.fromNullable(number).or(0);
    }

    @Override
    public boolean greaterThan(final Integer number) {
        return Optional.fromNullable(reference).or(0) > Optional.fromNullable(number).or(0);
    }

    @Override
    public boolean lessOrEqualTo(final Integer number) {
        return Optional.fromNullable(reference).or(0) <= Optional.fromNullable(number).or(0);
    }

    @Override
    public boolean less(final Integer number) {
        return Optional.fromNullable(reference).or(0) < Optional.fromNullable(number).or(0);
    }
}
