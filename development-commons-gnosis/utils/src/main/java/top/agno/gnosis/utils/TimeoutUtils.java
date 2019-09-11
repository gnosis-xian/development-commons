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

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 名称: TimeoutUtils
 * 描述: 超时工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class TimeoutUtils {

    private TimeoutUtils() {
    }

    /**
     * Converts the given timeout to seconds.
     * <p>
     * Since a 0 timeout blocks some Redis ops indefinitely, this method will return 1 if the original value is greater
     * than 0 but is truncated to 0 on conversion.
     *
     * @param timeout The timeout to convert
     * @param unit    The timeout's unit
     * @return The converted timeout
     */
    public static long toSeconds(final long timeout, final TimeUnit unit) {
        return roundUpIfNecessary(timeout, unit.toSeconds(timeout));
    }

    /**
     * Converts the given timeout to milliseconds.
     * <p>
     * Since a 0 timeout blocks some Redis ops indefinitely, this method will return 1 if the original value is greater
     * than 0 but is truncated to 0 on conversion.
     *
     * @param timeout The timeout to convert
     * @param unit    The timeout's unit
     * @return The converted timeout
     */
    public static long toMillis(final long timeout, final TimeUnit unit) {
        return roundUpIfNecessary(timeout, unit.toMillis(timeout));
    }

    private static long roundUpIfNecessary(final long timeout, final long convertedTimeout) {
        // A 0 timeout blocks some Redis ops indefinitely, round up if that's
        // not the intention
        if (timeout > 0 && convertedTimeout == 0) {
            return 1;
        }
        return convertedTimeout;
    }
}
