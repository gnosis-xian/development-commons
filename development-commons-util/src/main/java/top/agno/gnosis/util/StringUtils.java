package top.agno.gnosis.util;

/**
 * Description:
 * Author: gaojing [01381583@yto.net.cn]
 * Date: Created in 9:18 2019/9/11
 * Modified:
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        if ("".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
