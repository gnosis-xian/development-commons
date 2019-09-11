package top.agno.gnosis.utils.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断实体元素值长度是否大于数据表限制长度
 * 及判断字段值是否符合限制输入的类型
 */
@Component
@Slf4j
public class CheckParamUtil {
    public String errorMsg = "";//错误信息
    public Map<String, String> map;

    public CheckParamUtil() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("*", "[\\w\\W]+");
        map.put("n", "\\d+$");
        map.put("s", "[\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w\\.\\s]+$");
        map.put("p", "[0-9]{6}$");
        map.put("m", "13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$");
//        map.put("e", "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

        map.put("e", "^(?=\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$).{6,30}$");
        map.put("url", "(\\w+:\\/\\/)?\\w+(\\.\\w+)+.*$");
        map.put("IDCard", "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
        map.put("d", "^(-?\\d+)(\\.\\d+)?$");///浮点型正则
        map.put("y", "[A-Za-z0-9]+$");///不含特殊符号的非中文字符
        map.put("zh", "[\u4e00-\u9fa5]+");///只能输入中文字符
        this.map = map;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }


    /**
     * 执行判断
     *
     * @param obj
     * @return 返回true 表示实体内所有值在限制之内，否则返回false
     */
    public boolean checkParam(Object obj) {
        boolean chenkRS = true;// 检测结果
        String missMsg = "";
        int error = 0;
        Field fileds[] = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fileds.length; i++) {
                Field field = fileds[i];
                CheckParams col = field.getAnnotation(CheckParams.class);
                if (col != null) {
                    String fieldname = field.getName();
                    StringBuffer getMethodName = new StringBuffer("get");
                    getMethodName.append(fieldname.substring(0, 1).toUpperCase());
                    getMethodName.append(fieldname.substring(1));
                    Method getMethod = obj.getClass().getMethod(
                            getMethodName.toString(), new Class[]{});
                    Object str = getMethod.invoke(obj, new Object[]{});
                    if (isNotEmpty(str)) {
                        str = str.toString().trim();
                    }
                    //判断必填字段输入的字是否为空
                    if (col.notNull() && !isNotEmpty(str)) {
                        missMsg += col.errorMsg() + "\n";
                        error++;
                        continue;
                    }
                    //判断最大限制长度,不受限制输入的最小长度的作用
                    if (col.maxLength() > 0 && col.mineLiegth() == -1) {
                        if (isNotEmpty(str)) {
                            if (str.toString().length() > col.maxLength()) {
                                missMsg += col.errorMsg() + "\n";
                                error++;
                                continue;
                            }
                        }
                    } else if (col.maxLength() > 0 && col.mineLiegth() > -1) {
                        if (isNotEmpty(str)) {
                            if (str.toString().length() > col.maxLength() || str.toString().length() < col.mineLiegth()) {
                                missMsg += col.errorMsg() + "\n";
                                error++;
                                continue;
                            }
                        } else {
                            missMsg += col.errorMsg() + "\n";
                            error++;
                            continue;
                        }
                    } else if (col.maxLength() == -1 && col.mineLiegth() > -1) {
                        if (isNotEmpty(str)) {
                            if (str.toString().length() < col.mineLiegth()) {
                                missMsg += col.errorMsg() + "\n";
                                error++;
                                continue;
                            }
                        } else {
                            missMsg += col.errorMsg() + "\n";
                            error++;
                            continue;
                        }
                    }

                    if (isNotEmpty(col.dataType())) {
                        String reg = createRegex(col.dataType());
                        boolean rs = true;
                        if (isNotEmpty(col.dataType()) && col.ignore()) {
                            if (isNotEmpty(str)) {
                                if (isNotEmpty(reg)) {
                                    rs = isMatch(reg, str.toString());
                                } else {
                                    rs = isMatch(col.dataType(), str.toString());
                                }
                                if (!rs) {
                                    missMsg += col.errorMsg() + "\n";
                                    error++;
                                    continue;
                                }
                            }
                        } else if (isNotEmpty(col.dataType()) && !col.ignore()) {
                            if (isNotEmpty(str)) {
                                if (isNotEmpty(reg)) {

                                    rs = isMatch(reg, str.toString());
                                } else {
                                    rs = isMatch(col.dataType(), str.toString());
                                }
                            }
                            if (!rs || !isNotEmpty(str)) {
                                missMsg += col.errorMsg() + "\n";
                                error++;
                                continue;
                            }
                        }
                    }
                }
            }
            if (error > 0) {
                chenkRS = false;
                this.setErrorMsg(missMsg);
            }
        } catch (Exception ex) {
            log.error("参数解析异常信息:{}", ex.getMessage());
        }
        return chenkRS;

    }

    /**
     * 判断字段值是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        boolean flag = true;
        if ((str != null) && (!str.equals(""))) {
            if (str.toString().length() > 0)
                flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断字符串是否满足正则
     *
     * @param regex 正则
     * @param str   内容
     * @return 符合则返回true
     */
    public boolean isMatch(String regex, String str) {
        boolean b = true;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * @param dataType 正则校验类型
     * @return
     */
    public String createRegex(String dataType) {
        Map<String, String> map = this.getMap();///正则校验 map
        String rs = map.get(dataType);
        Pattern p = Pattern.compile("(zh(.*?)|y(.*?)|n(.*?)|s(.*?)|\\*(.*?))");///获取datatype 的校验类型，返还 zh,y,n,s,*
        Matcher m = p.matcher(dataType);//正则校验
        while (m.find()) {
            String reg = map.get(m.group(0));///m.group(0) 返还的值为 zh,y,n,s,* 中的一个，通过返回值从map中得到相应的相应的正则
            if (this.isNotEmpty(reg)) {
                String newReg = m.group(0);
                if (newReg.indexOf("*") > -1) {
                    newReg = "\\" + newReg;
                }
                Pattern p1 = Pattern.compile(newReg + "(.*?);");///组装正则 获取  zh,y,n,s,* 以上类型后面的附带条件，如获取zh1-5（只能输入1-5位的中文字符）的附带信息为“1-5”
                Matcher m1 = p1.matcher(dataType + ";");
                while (m1.find()) {
                    String type = m1.group(1);//返回的为zh1-5 中1-5
                    ///从新组装 从map 获取到的正则表达式 开始


                    if (this.isNotEmpty(type) && type.indexOf("-") > -1) {
                        type = type.replaceAll("-", ",");
                        type = "{" + type + "}";
                        type = type.replaceAll("\\$", "RDS_CHAR_DOLLAR");
                        reg = reg.replaceAll("\\+", type);
                        reg = reg.replaceAll("RDS_CHAR_DOLLAR", "\\$");
                    } else if (this.isNotEmpty(type) && type.indexOf("-") == -1) {
                        type = "{" + type + "}";
                        type = type.replaceAll("\\$", "RDS_CHAR_DOLLAR");
                        reg = reg.replaceAll("\\+", type);
                        reg = reg.replaceAll("RDS_CHAR_DOLLAR", "\\$");
                    }
                    /// 结束
                    rs = reg;
                }
            }
        }
        return rs;

    }


}
