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

package top.agno.gnosis.utils.random;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * <pre>
 * 名称: RandomUtils
 * 描述: 随机数工具类
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public final class RandomUtils {

    /**
     * ASCIL码随机数起始位
     */
    public static final int ASCII_MIN = 32;
    /**
     * ASCIL码随机数最后位
     */
    public static final int ASCII_MAX = 127;
    /**
     * 整数最大数
     */
    public static final int INTEGER_RANGE = 128;

    private static final int NUM_55296 = 55296;
    private static final int NUM_56320 = 56320;
    private static final int NUM_57343 = 57343;
    private static final int NUM_56191 = 56191;
    private static final int NUM_56192 = 56192;
    private static final int NUM_56319 = 56319;

    /**
     * <p>Random object used by random method. This has to be not local
     * to the random method so as to not return the same value in the
     * same millisecond.</p>
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * <p>{@code RandomUtils} instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * {@code RandomUtils.random(5);}.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    private RandomUtils() {
        super();
    }

    // Random
    //-----------------------------------------------------------------------

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of all characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String random(final int count) {
        return random(count, false, false);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of characters whose
     * ASCII value is between {@code 32} and {@code 126} (inclusive).</p>
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAscii(final int count) {
        return random(count, ASCII_MIN, ASCII_MAX, false, false);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alphabetic
     * characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAlphabetic(final int count) {
        return random(count, true, false);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAlphanumeric(final int count) {
        return random(count, true, true);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of numeric
     * characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomNumeric(final int count) {
        return random(count, false, true);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters as indicated by the arguments.</p>
     *
     * @param count   the length of random string to create
     * @param letters if {@code true}, generated string may include
     *                alphabetic characters
     * @param numbers if {@code true}, generated string may include
     *                numeric characters
     * @return the random string
     */
    public static String random(final int count, final boolean letters, final boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters as indicated by the arguments.</p>
     *
     * @param count   the length of random string to create
     * @param start   the position in set of chars to start at
     * @param end     the position in set of chars to end before
     * @param letters if {@code true}, generated string may include
     *                alphabetic characters
     * @param numbers if {@code true}, generated string may include
     *                numeric characters
     * @return the random string
     */
    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    /**
     * <p>Creates a random string based on a variety of options, using
     * default source of randomness.</p>
     *
     * <p>This method has exactly the same semantics as
     * {@link #random(int, int, int, boolean, boolean, char[], Random)}, but
     * instead of using an externally supplied source of randomness, it uses
     * the internal static {@link Random} instance.</p>
     *
     * @param count   the length of random string to create
     * @param start   the position in set of chars to start at
     * @param end     the position in set of chars to end before
     * @param letters only allow letters?
     * @param numbers only allow numbers?
     * @param chars   the set of chars to choose randoms from.
     *                If {@code null}, then it will use the set of all chars.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *                                        {@code (end - start) + 1} characters in the set array.
     */
    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers, final char... chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    /**
     * <p>Creates a random string based on a variety of options, using
     * supplied source of randomness.</p>
     *
     * <p>If start and end are both {@code 0}, start and end are set
     * to {@code ' '} and {@code 'z'}, the ASCII printable
     * characters, will be used, unless letters and numbers are both
     * {@code false}, in which case, start and end are set to
     * {@code 0} and {@code Integer.MAX_VALUE}.
     *
     * <p>If set is not {@code null}, characters between start and
     * end are chosen.</p>
     *
     * <p>This method accepts a user-supplied {@link Random}
     * instance to use as a source of randomness. By seeding a single
     * {@link Random} instance with a fixed seed and using it for each call,
     * the same random sequence of strings can be generated repeatedly
     * and predictably.</p>
     *
     * @param count   the length of random string to create
     * @param start   the position in set of chars to start at
     * @param end     the position in set of chars to end before
     * @param letters only allow letters?
     * @param numbers only allow numbers?
     * @param chars   the set of chars to choose randoms from, must not be empty.
     *                If {@code null}, then it will use the set of all chars.
     * @param random  a source of randomness.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *                                        {@code (end - start) + 1} characters in the set array.
     * @throws IllegalArgumentException       if {@code count} &lt; 0 or the provided chars array is empty.
     * @since 2.0
     */
    public static String random(final int count, final int start, final int end, final boolean letters,
                                final boolean numbers, final char[] chars, final SecureRandom random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        int endP = end;
        int startP = start;
        if (startP == 0 && endP == 0) {
            if (chars != null) {
                endP = chars.length;
            } else {
                if (!letters && !numbers) {
                    endP = Integer.MAX_VALUE;
                } else {
                    endP = 'z' + 1;
                    startP = ' ';
                }
            }
        } else {
            if (endP <= startP) {
                throw new IllegalArgumentException(
                        "Parameter end (" + endP + ") must be greater than start (" + startP + ")");
            }
        }

        return new String(getBuffer(count, startP, endP - startP, letters, numbers, chars, random));
    }

    private static char[] getBuffer(final int count, final int start, final int gap, final boolean letters,
                                    final boolean numbers, final char[] chars, final SecureRandom random) {
        int countT = count;
        final char[] buffer = new char[countT];
        while (countT-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                if (ch >= NUM_56320 && ch <= NUM_57343) {
                    if (countT == 0) {
                        countT++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[countT] = ch;
                        countT--;
                        buffer[countT] = (char) (NUM_55296 + random.nextInt(INTEGER_RANGE));
                    }
                } else if (ch >= NUM_55296 && ch <= NUM_56191) {
                    if (countT == 0) {
                        countT++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[countT] = (char) (NUM_56320 + random.nextInt(INTEGER_RANGE));
                        countT--;
                        buffer[countT] = ch;
                    }
                } else if (ch >= NUM_56192 && ch <= NUM_56319) {
                    // private high surrogate, no effing clue, so skip it
                    countT++;
                } else {
                    buffer[countT] = ch;
                }
            } else {
                countT++;
            }
        }
        return buffer;
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of characters
     * specified by the string, must not be empty.
     * If null, the set of all characters is used.</p>
     *
     * @param count the length of random string to create
     * @param chars the String containing the set of characters to use,
     *              may be null, but must not be empty
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0 or the string is empty.
     */
    public static String random(final int count, final String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, chars.toCharArray());
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of characters specified.</p>
     *
     * @param count the length of random string to create
     * @param chars the character array containing the set of characters to use,
     *              may be null
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String random(final int count, final char... chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RANDOM);
    }

    /**
     * 生成指定长度的随机数字符串.
     *
     * @param length 随机数长度
     * @return String 随机数
     */
    public static String generateRandomNumber(int length) {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        StringBuffer code = new StringBuffer(String.valueOf(hashCodeV));
        //如果hashcode位数不够，则在后面拼随机数
        String number = (new BigDecimal(((Math.random() * 9 + 1) * Math.pow(10, length - code.length() - 1)))).toString().substring(0, length - code.length());
        code.append(number);
        return code.toString();
    }


    /**
     * 生成指定长度的随机数字符串.
     *
     * @param length 随机数长度
     * @param prefix 前缀
     * @return String 随机数
     */
    public static String generateRandomNumber(int length, String prefix) {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        StringBuffer code = new StringBuffer(prefix).append(String.valueOf(hashCodeV));
        //如果hashcode位数不够，则在后面拼随机数
        String number = (new BigDecimal(((Math.random() * 9 + 1) * Math.pow(10, length - code.length() - 1)))).toString().substring(0, length - code.length());
        code.append(number);
        return code.toString();
    }

    /**
     * 获取随机TOKEN信息
     *
     * @return token
     */
    public static String getToken() {
        UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String token = uuid.toString();
        // 转换为大写
        token = token.toUpperCase();
        // 替换 “-”变成空格
        token = token.replaceAll("-", "");
        return token;
    }
}
