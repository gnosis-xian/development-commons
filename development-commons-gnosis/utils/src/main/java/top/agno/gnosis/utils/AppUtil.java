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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * <pre>
 * 系统工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
@Slf4j
public final class AppUtil {

    private AppUtil() {
    }

    /**
     * 获取os的cpu核数
     *
     * @return cpu核数
     */
    public static int getSystemProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }


    /**
     * 自动重试.
     *
     * @param retryFun Redis重试回调
     * @param maxRetry 最大重试次数（配置项）
     * @param <V>      返回值类型
     * @return 返回V
     */
    public static <V> V retry(final Callable<V> retryFun, final Integer maxRetry) {
        V value = null;
        Integer maxRetryTimes = maxRetry;
        while (value == null && maxRetryTimes-- > 0) {
            try {
                long start = System.currentTimeMillis();
                value = retryFun.call();
                log.info("appUtil auto retry cost time [{}] ms ", System.currentTimeMillis() - start);
            } catch (final RuntimeException appEx) {
                throw appEx;
            } catch (final Exception ex) {
                log.error("appUtil retryFun error:{}", ex.getMessage());
            }
        }
        return value;
    }

}
