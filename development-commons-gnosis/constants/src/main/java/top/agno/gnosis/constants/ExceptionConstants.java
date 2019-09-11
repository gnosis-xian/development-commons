package top.agno.gnosis.constants;

/**
 * @author: gaojing [gaojing1996@vip.qq.com]
 */
public interface ExceptionConstants {

    /** 系统错误: 50000 */
    int SYS_ERROR_CODE = 50000;

    /** 数据库错误: 5000 */
    int DB_ERROR_CODE = 5000;

    /** 服务器配置错误 */
    int CONFIGURATION_ERROR_CODE = -50001;

    /** 认证错误 */
    int AUTHENTICATION_ERROR_CODE = -40100;

    /** 请求频率超过了限制*/
    int CALLRATE_ERROR_CODE = -42900;

    /** 操作禁止 */
    int OPERATION_ERROR_CODE = -40300;

    /** 一般请求错误 */
    int REQUEST_ERROR_CODE = -40000;

    /** 资源不存在 */
    int RESOURCE_ERROR_CODE = -40400;

    /** 一般服务器错误 */
    int SERVER_ERROR_CODE = -50000;

    /** 参数格式错误 */
    int INVALID_ARGUMENT_ERROR_CODE = -40001;

    /** 操作不容许 */
    int INVALID_OPERATION_ERROR_CODE = -40301;

    /** 没有足够权限 */
    int PERMISSION_ERROR_CODE = -40302;

    /** 参数缺失 */
    int MISSING_ARGUMETN_ERROR_CODE = -40002;

    /** 所请求数据不存在 */
    int MISSING_DATA_ERROR_CODE = -40402;

    /** 所请求方法不存在 */
    int MISSING_METHOD_ERROR_CODE = -40401;

    /** 服务器处理超时 */
    int SERVER_TIMEOUT_ERROR_CODE = -50400;

    /** NOT_FOUND错误 */
    int NOT_FOUND_ERROR_CODE = -40404;

    /** SECRET错误 */
    int SECRET_ERROR_CODE = -50100;

    /** SQL错误 */
    int SQL_ERROR_CODE = -50200;
}
