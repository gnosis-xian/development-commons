package top.agno.gnosis.utils;

import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 〈敏感词过滤工具类〉
 *
 * @author yindy
 * @create 2019/7/30
 * @since 1.0.0
 */
public class SensitiveWordFilterUtil {

    public static Map sensitiveWordMap;

    /**
     * 根据敏感词列表获取敏感词库
     *
     * @param keyWordSet
     * @return 敏感词库
     */
    public static HashMap getSensitiveWordToHashMap(Set<String> keyWordSet) {
        HashMap sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                Character keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
        return sensitiveWordMap;
    }


    /**
     * 校验是否存在敏感词
     *
     * @param txt
     * @return
     */
    public static boolean isContaintSensitiveWord(String txt) {
        // 内容为空默认返回成功
        if (StringUtils.isEmpty(txt)) {
            return false;
        }
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = CheckSensitiveWord(txt, i); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                return true;
            }
        }
        return false;
    }

    /**
     * 敏感词校验
     *
     * @param txt
     * @param beginIndex
     * @return
     */
    public static int CheckSensitiveWord(String txt, int beginIndex) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            char word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    break;
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 获取该字段中存在的所有的敏感词
     *
     * @param txt
     * @return
     */
    public static Set<String> getSensitiveWord(String txt) {
        Set<String> sensitiveWordList = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = CheckSensitiveWord(txt, i);    //判断是否包含敏感字符
            if (length > 0) {    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 敏感词替换为指定的 字符
     *
     * @param txt
     * @param replaceChar
     * @return
     */
    public static String replaceSensitiveWord(String txt, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }
        return resultReplace;
    }
}
