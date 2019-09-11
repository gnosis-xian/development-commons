package top.agno.gnosis.utils.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断字段长度
 * 字段值类型注解类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface CheckParams {

    int maxLength() default -1;///限制的最大长,默认值为-1 表示可输入任意长度的字符不作限制

    int mineLiegth() default -1;//限制的最小长度，默认值为-1 表示不对最小值进行校验

    boolean notNull() default false;//判断字段是否为必填字段，输入的为空则不符合要求

    String dataType() default "";//限制字段值输入的类型

    boolean ignore() default false;///出入的值为空时是否进行正则判断，false 为空处理，true 为空不处理

    String errorMsg() default "";///错误信息提示


}
