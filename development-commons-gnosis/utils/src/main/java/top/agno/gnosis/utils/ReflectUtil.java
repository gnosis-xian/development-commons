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
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 名称: ReflectUtil
 * 描述: 反射工具类
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
@Slf4j
public final class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * 获取类属性
     *
     * @param obj       类
     * @param fieldName 属性
     * @return 返回类属性
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        if (obj == null) {
            return null;
        }

        Field targetField = getTargetField(obj.getClass(), fieldName);

        try {
            return FieldUtils.readField(targetField, obj, true);
        } catch (final IllegalAccessException e) {
            log.debug(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取类属性
     *
     * @param targetClass 目标类
     * @param fieldName   属性名
     * @return 返回类属性
     */
    public static Field getTargetField(final Class<?> targetClass, final String fieldName) {
        Field field = null;

        try {
            if (targetClass == null) {
                return field;
            }

            if (Object.class.equals(targetClass)) {
                return field;
            }

            field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
            if (field == null) {
                field = getTargetField(targetClass.getSuperclass(), fieldName);
            }
        } catch (final Exception e) {
            log.debug(e.getMessage(), e);
        }

        return field;
    }

    /**
     * 设置属性值
     *
     * @param obj       目标类
     * @param fieldName 属性名
     * @param value     值
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        if (null == obj) {
            return;
        }
        Field targetField = getTargetField(obj.getClass(), fieldName);
        try {
            FieldUtils.writeField(targetField, obj, value);
        } catch (final IllegalAccessException e) {
            log.debug(e.getMessage(), e);
        }
    }

    /**
     * 查找类中的指定参数的构造方法
     *
     * @param <T>            返回类型
     * @param clazz          类
     * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可
     * @return 构造方法，如果未找到返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {
        if (null == clazz) {
            return null;
        }

        final Constructor<?>[] constructors = clazz.getConstructors();
        Class<?>[] pts;
        for (Constructor<?> constructor : constructors) {
            pts = constructor.getParameterTypes();
            if (isAllAssignableFrom(pts, parameterTypes)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    /**
     * 实例化
     *
     * @param clazz  类型
     * @param params 参数
     * @param <T>    返回类型
     * @return 实例化
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... params) {
        if (EmptyUtils.isEmpty(params)) {
            return newInstance(clazz);
        }

        final Class<?>[] paramTypes = getClasses(params);
        final Constructor<?> constructor = getConstructor(clazz, getClasses(params));
        if (null == constructor) {
            throw new RuntimeException("No Constructor matched for parameter types: [{}]");
        }
        try {
            return getConstructor(clazz, paramTypes).newInstance(params);
        } catch (final Exception e) {
            throw new RuntimeException(MessageFormat.format("Instance class [{}] error!", clazz), e);
        }
    }

    /**
     * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
     *
     * @param types1 类组1
     * @param types2 类组2
     * @return 是否相同、父类或接口
     */
    public static boolean isAllAssignableFrom(final Class<?>[] types1, final Class<?>[] types2) {
        if (EmptyUtils.isEmpty(types1) && EmptyUtils.isEmpty(types2)) {
            return true;
        }
        if (types1.length == types2.length) {
            for (int i = 0; i < types1.length; i++) {
                if (types1[i].isAssignableFrom(types2[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   返回类型
     * @return 对象
     */
    public static <T> T newInstance(final Class<T> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(MessageFormat.format("Instance class [{}] error!", clazz), e);
        }
    }

    /**
     * 获得对象数组的类数组
     *
     * @param objects 对象数组
     * @return 类数组
     */
    public static Class<?>[] getClasses(final Object... objects) {
        Class<?>[] classes = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }

    /**
     * 加载类
     *
     * @param <T>           返回类型
     * @param className     类名
     * @param isInitialized 是否初始化
     * @return 类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className, final boolean isInitialized) {
        Class<T> clazz;
        try {
            clazz = (Class<T>) Class.forName(className, isInitialized, getClassLoader());
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 加载类并初始化
     *
     * @param <T>       返回类型
     * @param className 类名
     * @return 类
     */
    public static <T> Class<T> loadClass(final String className) {
        return loadClass(className, true);
    }

    /**
     * 获得class loader<br>
     * 若当前线程class loader不存在，取当前类的class loader
     *
     * @return 类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = ReflectUtil.class.getClassLoader();
            if (null == classLoader) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }

    /**
     * 获取所有的成员变量(通过GET，SET方法获取)
     *
     * @param <T>   返回类型
     * @param clazz 类型
     * @return 成员变量数组
     */
    public static <T> String[] getFields(final Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        String[] fieldsArray = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fieldsArray[i] = fields[i].getName();
        }

        return fieldsArray;
    }

    /**
     * 获取所有的成员变量,包括父类
     *
     * @param <T>        返回类型
     * @param clazz      类型
     * @param superClass 是否包括父类
     * @return 成员变量数组，包括父类的
     * @throws Exception 异常
     */
    public static <T> Field[] getFields(final Class<T> clazz, final boolean superClass) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = null;
        List<Field> supperFields = new ArrayList<>();
        for (Field f : fields) {
            supperFields.add(f);
        }
        if (superClass) {
            Class<? super T> superClazz = clazz.getSuperclass();
            while (superClazz != null && !(Object.class.getName().equals(superClazz.getName()))) {
                superFields = superClazz.getDeclaredFields();
                if (EmptyUtils.isNotEmpty(superFields)) {
                    for (Field f : superFields) {
                        if (!("serialVersionUID".equals(f.getName()))) {
                            supperFields.add(f);
                        }
                    }
                }
                superClazz = superClazz.getSuperclass();
            }
        }

        Field[] allFields = new Field[supperFields.size()];

        for (int i = 0; i < supperFields.size(); i++) {
            allFields[i] = supperFields.get(i);
        }

        return allFields;
    }

    /**
     * 获取所有的成员变量,包括父类
     *
     * @param <T>   返回类型
     * @param clazz 类型
     * @return 所有的成员变量, 包括父类
     * @throws Exception 异常
     */
    public static <T> Field[] getClassFieldsAndSuperClassFields(final Class<T> clazz) throws Exception {
        Field[] fields = clazz.getDeclaredFields();

        if (clazz.getSuperclass() == null) {
            throw new Exception(clazz.getName() + "没有父类");

        }

        Field[] superFields = clazz.getSuperclass().getDeclaredFields();

        Field[] allFields = new Field[fields.length + superFields.length];

        for (int i = 0; i < fields.length; i++) {
            allFields[i] = fields[i];
        }
        for (int i = 0; i < superFields.length; i++) {
            allFields[fields.length + i] = superFields[i];
        }

        return allFields;
    }

    /**
     * @return 当前线程的class loader
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
