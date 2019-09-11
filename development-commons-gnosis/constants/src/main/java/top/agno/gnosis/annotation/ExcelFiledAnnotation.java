package top.agno.gnosis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 名称: ExcelFiledAnnotation
 * 描述: 自定义excel导出字段注解
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelFiledAnnotation {
    /**
     * 字段名.
     *
     * @return String 字段名
     */
    String filedName() default "";

    /**
     * 字段状态.
     *
     * @return boolean 字段状态
     */
    boolean status() default true;

    /***
     *  导出字段分组名.
     * @return String 分组名
     */
    String groupName() default "";

    /**
     * 导出字段分组Key.
     *
     * @return String
     */
    String groupKey() default "";
}
