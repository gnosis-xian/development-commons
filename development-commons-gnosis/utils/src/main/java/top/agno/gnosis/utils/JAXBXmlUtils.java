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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * JAXBXmlUtils 工具类 ： 转换xml和java实体类
 *
 * @author yto.net.cn
 * @since 1.0.1
 */
public final class JAXBXmlUtils {

    /**
     *  如果每次都调用JAXBContext.newInstance方法，会导致性能急剧下降,
     *  所以将JAXBContext的实例化对象用Map缓存起来
     */
    private static Map<String, JAXBContext> JAXB_CONTEXT_MAP = new HashMap<String, JAXBContext>();

    private JAXBXmlUtils() {}

    /**
     * java实体类转xml， 并且格式化生成的xml串
     * @param obj 对象
     * @return xml字符串
     */
    public static String toXML(final Object obj) {
        return toXML(obj, "UTF-8", true, true);
    }

    /**
     * java实体类转xml， 不格式化生成的xml串
     * @param obj 对象
     * @return xml字符串
     */
    public static String toXMLNoFormat(final Object obj) {
        return toXML(obj, "UTF-8", false, true);
    }

    /**
     * java实体类转xml
     * @param obj       对象
     * @param encode    编码格式
     * @param format    是否格式化生成的xml串
     * @param fragment  是否省略xm头声明信息
     * @return xml字符串
     */
    public static String toXML(final Object obj, final String encode, final boolean format, final boolean fragment) {
        try {
            JAXBContext jaxbContext = JAXB_CONTEXT_MAP.get(obj.getClass().getName());
            if (jaxbContext == null) {
                // 如果每次都调用JAXBContext.newInstance方法，会导致性能急剧下降
                jaxbContext = JAXBContext.newInstance(obj.getClass());
                JAXB_CONTEXT_MAP.put(obj.getClass().getName(), jaxbContext);
            }

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encode); // 编码格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format); // 是否格式化生成的xml串
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, fragment); // 是否省略xm头声明信息
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * xml转java实体类
     * @param xml        xml字符串
     * @param valueType  类型
     * @param <T>        泛型
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXML(final String xml, final Class<T> valueType) {
        try {
            JAXBContext jaxbContext = JAXB_CONTEXT_MAP.get(valueType.getName());
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(valueType);
                JAXB_CONTEXT_MAP.put(valueType.getName(), jaxbContext);
            }
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
