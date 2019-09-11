package top.ango.gnosis.poi.excel.export;


import top.agno.gnosis.annotation.ExcelFiledAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 名称: ExcelSelectParamUtil
 * 描述: 自定义excel导出参数处理工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public class ExcelSelectParamUtil {
    public ExcelSelectParamUtil() {
    }

    public static Map<String, List<ExcelExportSelectDto>> getSelectParam(Object object) {
        Map<String, List<ExcelExportSelectDto>> resultMap = new HashMap(5);
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field[] var4 = fields;
        int var5 = fields.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            ExcelFiledAnnotation annotation = (ExcelFiledAnnotation) field.getAnnotation(ExcelFiledAnnotation.class);
            if (annotation != null && annotation.status()) {
                ExcelExportSelectDto excelExportSelectDto = new ExcelExportSelectDto();
                excelExportSelectDto.setKey(field.getName());
                excelExportSelectDto.setName(annotation.filedName());
                if (resultMap.get(annotation.groupKey()) == null) {
                    List<ExcelExportSelectDto> excelParamList = new ArrayList();
                    excelParamList.add(excelExportSelectDto);
                    resultMap.put(annotation.groupKey(), excelParamList);
                } else {
                    ((List) resultMap.get(annotation.groupKey())).add(excelExportSelectDto);
                }
            }
        }

        return resultMap;
    }
}
