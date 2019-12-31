package top.agno.gnosis.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static top.agno.gnosis.utils.ExceptionUtils.ExceptionEnum.IS_NOT_ONLY_ONE;
import static top.agno.gnosis.utils.ExceptionUtils.ExceptionEnum.IS_NULL;


/**
 * @author: gaojing [01381583@yto.net.cn]
 */
public class CollectionUtil extends CollectionUtils {

    public static Object getFirstEleIfNotThrowException(List<?> collection) {
        return getFirstEleIfNotThrowException(collection, "");
    }

    public static Object getFirstEleIfNotThrowException(List<?> collection, String tip) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new RuntimeException(tip + IS_NULL.getMsg());
        }
        if (collection.size() != 1) {
            throw new RuntimeException(tip + IS_NOT_ONLY_ONE.getMsg());
        }
        return collection.get(0);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !CollectionUtils.isEmpty(map);
    }

}
