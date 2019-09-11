package top.agno.gnosis.aspects;

/**
 * @author: gaojing [gaojing1996@vip.qq.com]
 */
public class IsProcessUtil {

    public static boolean isProdEnv(String profilesActive) {
        return "prod".equals(profilesActive) || "pro".equals(profilesActive);
    }

    public static boolean isRequire(String require) {
        return require != null && "false".equals(require.toLowerCase());
    }
}
