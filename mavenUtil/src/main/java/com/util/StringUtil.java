package com.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String arrTest[] = {"[br]", "[/b]", "[/i]", "[/u]", "[/size]", "[/color]", "[/align]", "[/url]", "[/email]", "[/img]"};
    public static final String arrParam[] = {"\\[br\\]", "\\[b\\](.+?)\\[/b\\]", "\\[i\\](.+?)\\[/i\\]", "\\[u\\](.+?)\\[/u\\]", "\\[size=(.+?)\\](.+?)\\[/size\\]", "\\[color=(.+?)\\](.+?)\\[/color\\]", "\\[align=(.+?)\\](.+?)\\[/align\\]", "\\[url=(.+?)\\](.+?)\\[/url\\]", "\\[email=(.+?)\\](.+?)\\[/email\\]," + "\\[img=(.+?)\\](.+?)\\[/img\\]"};
    public static final String arrCode[] = {"<br>", "<b>$1</b>", "<i>$1</i>", "<u>$1</u>", "<font size=\"$1\">$2</font>", "<font color=\"$1\">$2</font>", "<div align=\"$1\">$2</div>", "<a href=\"$1\" target=\"_blank\">$2</a>", "<a href=\"email:$1\">$2</a>", "<img src=\"$1\" border=0>$2</img>"};

    /**
     * 转换int类型
     * @param content
     * @return
     */
    public static int getInt(String content) {
        int intContent;
        try {
            intContent = Integer.parseInt(content);
        } catch (Exception e) {
            intContent = 0;
        }
        return intContent;
    }

    /**
     * 转换long类型
     * @param content
     * @return
     */
    public static long getLong(String content) {
        long lngContent;
        try {
            lngContent = Long.parseLong(content);
        } catch (Exception e) {
            lngContent = 0L;
        }
        return lngContent;
    }

    /**
     * @param str    原字符串
     * @param length 字符串达到多长才截取
     * @return
     */
    public static String subStringToPoint(String str, int length, String more) {
        if (str == null)
            return "";
        String reStr = "";
        if (str.length() * 2 - 1 > length) {
            int reInt = 0;
            char[] tempChar = str.toCharArray();
            for (int kk = 0; (kk < tempChar.length && length > reInt); kk++) {
                String s1 = String.valueOf(tempChar[kk]);
                byte[] b = s1.getBytes();
                reInt += b.length;
                reStr += tempChar[kk];
            }
            if (length == reInt || (length == reInt - 1)) {
                if (!reStr.equals(str)) {
                    reStr += more;
                }
            }
        } else {
            reStr = str;
        }
        return reStr;
    }

    /**
     * 转换字符,用于替换提交的数据中存在非法数据:"'"
     *
     * @param Content
     * @return
     */
    public static String replaceChar(String content) {
        String newstr = "";
        newstr = content.replaceAll("\'", "''");
        return newstr;
    }

    /**
     * 对标题""转换为中文“”采用对应转换
     *
     * @param Content
     * @return
     */
    public static String replaceSymbol(String content) {
        int intPlaceNum = 0;
        int Num = 0;
        String strContent = content;
        while (true) {
            // 判断是否还存在"
            intPlaceNum = strContent.indexOf("\"");
            if (intPlaceNum < 0) {
                break;
            } else {
                if (Num % 2 == 0) {
                    strContent = strContent.replaceFirst("\"", "“");
                } else {
                    strContent = strContent.replaceFirst("\"", "”");
                }
                Num = Num + 1;
            }
        }
        return strContent;
    }

    /**
     * 替换HTML标记
     *
     * @param Content
     * @return
     */
    public static String replaceCharToHtml(String content) {
        String strContent = content;
        strContent = strContent.replaceAll("<", "&lt;");
        strContent = strContent.replaceAll(">", "&gt;");
        strContent = strContent.replaceAll("\"", "&quot;");
        return strContent;
    }

    /**
     * char转换为html标记
     * @param content
     * @return
     */
    public static String replaceHtmlToChar(String content) {
        String strContent = content;
        strContent = strContent.replaceAll("&lt;", "<");
        strContent = strContent.replaceAll("&gt;", ">");
        strContent = strContent.replaceAll("&quot;", "\"");
        return strContent;
    }

    /**
     *  数据库替换
     */
    public static String replaceCharToSql(String content) {
        String strContent = content;
        strContent = strContent.replaceAll("%", "\\\\%");
        return strContent;
    }

    /**
     * 格式化为html识别字符
     * @param value
     * @return
     */
    public static String toHtmlValue(String value) {
        if (null == value) {
            return null;
        }
        char a = 0;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            a = value.charAt(i);
            switch (a) {
                // 双引号
                case 34:
                    buf.append("&#034;");
                    break;
                // &号
                case 38:
                    buf.append("&amp;");
                    break;
                // 单引号
                case 39:
                    buf.append("&#039;");
                    break;
                // 小于号
                case 60:
                    buf.append("&lt;");
                    break;
                // 大于号
                case 62:
                    buf.append("&gt;");
                    break;
                default:
                    buf.append(a);
                    break;
            }
        }
        return buf.toString();
    }

    /**
     * 清除掉所有特殊字符  只允许字母,数字,中文   
     *
     * @param Content
     * @return
     */
    public static String replaceIllegalChar(String content) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return m.replaceAll("").trim();
    }

    /**
     * 标题中含有特殊字符替换 如:●▲@◎※ 主要在标题中使用
     *
     * @param Content
     * @return
     */
    public static String replaceSign(String content) {
        String strContent = "";
        strContent = content.replaceAll("\\*", "");
        strContent = strContent.replaceAll("\\$", "");
        strContent = strContent.replaceAll("\\+", "");
        String arrStr[] = {":", "：", "●", "▲", "■", "@", "＠", "◎", "★", "※", "＃", "〓", "＼", "§", "☆", "○", "◇", "◆", "□", "△", "＆", "＾", "￣", "＿", "♂", "♀", "Ю", "┭", "①", "「", "」", "≮", "§", "￡", "∑", "『", "』", "⊙", "∷", "Θ", "の", "↓", "↑", "Ф", "~", "Ⅱ", "∈", "┣", "┫", "╋", "┇", "┋", "→", "←", "!", "Ж", "#"};
        for (int i = 0; i < arrStr.length; i++) {
            if ((strContent.indexOf(arrStr[i])) >= 0) {
                strContent = strContent.replaceAll(arrStr[i], "");
            }
        }
        return strContent;
    }

    /**
     * 替换所有英文字母
     *
     * @param Content
     * @return
     */
    public static String replaceLetter(String content) {
        String strMark = "[^[A-Za-z]+$]";
        String strContent = "";
        strContent = content.replaceAll(strMark, "");
        return strContent;
    }

    /**
     * 替换所有数字
     *
     * @param Content
     * @return
     */
    public static String replaceNumber(String content) {
        String strMark = "[^[0-9]+$]";
        String strContent = "";
        strContent = content.replaceAll(strMark, "");
        return strContent;
    }

    /**
     * 将/n转换成为html<br>
     * ,空格转为&nbsp;
     *
     * @param Content
     * @return
     */
    public static String replaceBr(String content) {
        if (content == null) {
            return "";
        }
        String strContent = "";
        strContent = content.replaceAll("\n\r\t", "<br/>");
        strContent = strContent.replaceAll("\n\r", "<br/>");
        strContent = strContent.replaceAll("\r\n", "<br/>");
        strContent = strContent.replaceAll("\n", "<br/>");
        strContent = strContent.replaceAll("\r", "<br/>");
        return strContent;
    }

    /**
     * 清除所有<>标记符号 主要在搜索中显示文字内容 而不显示样式
     *
     * @param Content
     * @return
     */
    public static String replaceMark(String content) {
        String strContent = "";
        String strMark = "<\\s*[^>]*>";
        strContent = content.trim();
        strContent = strContent.replaceAll("\"", "");
        strContent = strContent.replaceAll("\'", "");
        // 删除所有<>标记
        strContent = strContent.replaceAll(strMark, "");
        strContent = strContent.replaceAll("&nbsp;", "");
        strContent = strContent.replaceAll(" ", "");
        strContent = strContent.replaceAll("　", "");
        strContent = strContent.replaceAll("\r", "");
        strContent = strContent.replaceAll("\n", "");
        strContent = strContent.replaceAll("\r\n", "");
        return strContent;
    }

    /**
     * 清楚Word垃圾代码
     *
     * @param Content
     * @return
     */
    public static String clearWord(String content) {
        String strContent = "";
        strContent = content.trim();
        strContent = strContent.replaceAll("x:str", "");
        // Remove Style attributes
        strContent = strContent.replaceAll("<(\\w[^>]*) style=\"([^\"]*)\"", "<$1");
        // Remove all SPAN tags
        strContent = strContent.replaceAll("<\\/?SPAN[^>]*>", "");
        // Remove Lang attributes
        strContent = strContent.replaceAll("<(\\w[^>]*) lang=([^ |>]*)([^>]*)", "<$1$3");
        // Remove Class attributes
        strContent = strContent.replaceAll("<(\\w[^>]*) class=([^ |>]*)([^>]*)", "<$1$3");
        // Remove XML elements and declarations
        strContent = strContent.replaceAll("<\\\\?\\?xml[^>]*>", "");
        // Remove Tags with XML namespace declarations: <o:p></o:p>
        strContent = strContent.replaceAll("<\\/?\\w+:[^>]*>", "");
        return strContent;
    }


    /**
     * 判断对象是否为空
     *
     * @param obj Object
     * @return boolean 空返回true,非空返回false
     */
    public static boolean isNull(Object obj) {
        return (null == obj) ? true : false;
    }

    /**
     * 判断字符串是否为:null,空串,'null'
     *
     * @param s
     * @return boolean 空返回true,非空返回false
     */
    public static boolean isEmpty(String s) {
        if (s == null || "".equals(s.trim()) || "null".equals(s.trim())) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否为:null,空串,'null'
     *
     * @param s
     * @return boolean 空返回true,非空返回false
     */
    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }

        if ("".equals(s.toString().trim()) || "null".equals(s.toString().trim())) {
            return true;
        }

        return false;
    }

    /**
     * 获取百分比
     *
     * @param p1
     * @param p2
     * @return
     */
    public static String percent(double p1, double p2) {
        if (p2 == 0) {
            return "0.00%";
        }
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        str = nf.format(p3);
        return str;
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String oldCharset, String newCharset) {
        try {
            if (str != null) {
                // 用旧的字符编码解码字符串。解码可能会出现异常。
                byte[] bs = str.getBytes(oldCharset);
                // 用新的字符编码生成字符串
                return new String(bs, newCharset);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public String changeCharset(String str, String newCharset) {
        try {
            if (str != null) {
                // 用默认字符编码解码字符串。
                byte[] bs = str.getBytes();
                // 用新的字符编码生成字符串
                return new String(bs, newCharset);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解析html中的参数信息
     *
     * @param elementStr
     * @return
     */
    public static Map<String, String> getConfigValue(String elementStr) {
        try {
            elementStr = java.net.URLDecoder.decode(elementStr, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int start = elementStr.indexOf("configvalue");
        Map<String, String> map = null; // 参数的键值对
        if (start != -1) {
            map = new HashMap<String, String>();
            start = elementStr.indexOf("\"", start);
            int end = elementStr.lastIndexOf("||");
            if (start < 0 || end < 0) {
                return null;
            }
            String configValue = elementStr.substring(start + 1, end);
            String[] values = configValue.split("\\|\\|");

            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (value != null && value.trim().length() > 1) {
                    int de = value.indexOf("=");
                    if (de > 0) {
                        String name = value.substring(0, de);
                        String v = value.substring(de + 1);
                        map.put(name, v);
                    }
                }
            }
        }
        return map;
    }


    /**
     * 替换左侧字符内容
     * StringUtils.ltrim(null, *) = null 
     * StringUtils.ltrim("", *) = "" 
     * StringUtils.ltrim("abc", "") = "abc" 
     * StringUtils.ltrim("abc", null) = "abc" 
     * StringUtils.ltrim("  abc", null) = "abc" 
     * StringUtils.ltrim("abc  ", null) = "abc  " 
     * StringUtils.ltrim(" abc ", null) = "abc " 
     * StringUtils.ltrim("yxabc  ", "xyz") = "abc  "
     * 
     * @param str
     * @param trimChars
     * @return
     */
    public static String lTrim(String str, String trimChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (trimChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (trimChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (trimChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }
        return str.substring(start);
    }

    /**
     * 替换右侧字符串内容
     * StringUtils.rtrim(null, *) = null StringUtils.rtrim("", *) = "" StringUtils.rtrim("abc", "") = "abc" StringUtils.rtrim("abc", null) = "abc" StringUtils.rtrim("  abc", null) = "  abc" StringUtils.rtrim("abc  ", null) = "abc" StringUtils.rtrim(" abc ", null) = " abc" StringUtils.rtrim("  abcyx", "xyz") = "  abc" StringUtils.rtrim("120.00", ".0") = "12"
     *
     * @param str
     * @param trimChars
     * @return
     */
    public static String rTrim(String str, String trimChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (trimChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (trimChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (trimChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    /**
     * 左右内容都替换
     * StringUtils.lrtrim(null, *) = null StringUtils.lrtrim("", *) = "" StringUtils.lrtrim("abc", null) = "abc" StringUtils.lrtrim("  abc", null) = "abc" StringUtils.lrtrim("abc  ", null) = "abc" StringUtils.lrtrim(" abc ", null) = "abc" StringUtils.lrtrim("  abcyx", "xyz") = "  abc"
     *
     * @param str
     * @param trimChars
     * @return
     */
    public static String lrTrim(String str, String trimChars) {
        if (StringUtil.isEmpty(str)) {
            return str;
        }
        str = lTrim(str, trimChars);
        return rTrim(str, trimChars);
    }


    /**
     * 从指定开始位置start以最大长度maxLength取子串,不抛出异常
     *
     * @param str
     * @param start
     * @param maxLength
     * @return
     */
    public static String substring(String str, int start, int maxLength) {
        if (str != null) {
            int realLength = str.length();
            if (start >= realLength) {
                return null;
            }
            str = str.substring(start);
            if (str.length() > maxLength) {
                return str.substring(0, maxLength);
            }
            return str;
        }
        return str;
    }

    /**
     * 判断str1中包含str2的个数
     *
     * @param str1
     * @param str2
     * @return counter 包含的个数
     */
    public static int countStr(String str1, String str2) {
        int counter = 0;
        if (str1.indexOf(str2) == -1) {
            return 0;
        } else if (str1.indexOf(str2) != -1) {
            counter++;
            countStr(str1.substring(str1.indexOf(str2) + str2.length()), str2);
            return counter;
        }
        return counter;
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * 将小写的人民币转化成大写
     *
     * @param number
     * @return String 返回
     * @author lihuihui
     */
    public static String convertToChineseNumber(double number) {
        StringBuffer chineseNumber = new StringBuffer();
        String[] num = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[] unit = {"分", "角", "圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万"};
        String tempNumber = String.valueOf(Math.round((number * 100)));
        int tempNumberLength = tempNumber.length();
        if ("0".equals(tempNumber)) {
            return "零圆整";
        }
        if (tempNumberLength > 15) {
            try {
                throw new Exception("超出转化范围.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean preReadZero = true; // 前面的字符是否读零
        for (int i = tempNumberLength; i > 0; i--) {
            if ((tempNumberLength - i + 2) % 4 == 0) {
                // 如果在（圆，万，亿，万）位上的四个数都为零,如果标志为未读零，则只读零，如果标志为已读零，则略过这四位
                if (i - 4 >= 0 && "0000".equals(tempNumber.substring(i - 4, i))) {
                    if (!preReadZero) {
                        chineseNumber.insert(0, "零");
                        preReadZero = true;
                    }
                    i -= 3; // 下面还有一个i--
                    continue;
                }
                // 如果当前位在（圆，万，亿，万）位上，则设置标志为已读零（即重置读零标志）
                preReadZero = true;
            }
            int digit = Integer.parseInt(tempNumber.substring(i - 1, i));
            if (digit == 0) {
                // 如果当前位是零并且标志为未读零，则读零，并设置标志为已读零
                if (!preReadZero) {
                    chineseNumber.insert(0, "零");
                    preReadZero = true;
                }
                // 如果当前位是零并且在（圆，万，亿，万）位上，则读出（圆，万，亿，万）
                if ((tempNumberLength - i + 2) % 4 == 0) {
                    chineseNumber.insert(0, unit[tempNumberLength - i]);
                }
            }
            // 如果当前位不为零，则读出此位，并且设置标志为未读零
            else {
                chineseNumber.insert(0, num[digit] + unit[tempNumberLength - i]);
                preReadZero = false;
            }
        }
        // 如果分角两位上的值都为零，则添加一个“整”字
        if (tempNumberLength - 2 >= 0 && "00".equals(tempNumber.substring(tempNumberLength - 2, tempNumberLength))) {
            chineseNumber.append("整");
        }
        return chineseNumber.toString();
    }


    /**
     * 拼接IN参数
     *
     * @param params
     * @return
     */
    public static String getInStr(String[] params) {
        return getInStr(Arrays.asList(params));
    }

    /**
     * 拼接IN参数
     *
     * @param params
     * @return
     */
    public static String getInStr(List<String> params) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.size(); i++) {
            String param = params.get(i);
            if (i != 0)
                sb.append(",");
            sb.append("'" + param + "'");
        }
        return sb.toString();
    }

    /**
     * 逗号拼接
     * @param s
     * @return
     */
    public static String join(String[] s) {
        return join(s, ",");
    }

    /**
     * 按照字符拼接字符串
     * @param s
     * @param j
     * @return
     */
    public static String join(String[] s, String j) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length; i++) {
            String s1 = s[i];
            if (i != 0)
                sb.append(j);
            sb.append(s1);
        }

        return sb.toString();
    }


    /**
     * 替换字符串中出现的html字符代码
     * @param str
     * @return
     */
    public static String replaceHtmlSpecial(String str) {
        str = str.replaceAll("&forall;", "∀");
        str = str.replaceAll("&part;", "∂");
        str = str.replaceAll("&exists;", "∃");
        str = str.replaceAll("&empty;", "∅");
        str = str.replaceAll("&nabla;", "∇");
        str = str.replaceAll("&isin;", "∈");
        str = str.replaceAll("&notin;", "∉");
        str = str.replaceAll("&ni;", "∋");
        str = str.replaceAll("&prod;", "∏");
        str = str.replaceAll("&sum;", "∑");
        str = str.replaceAll("&minus;", "−");
        str = str.replaceAll("&lowast;", "∗");
        str = str.replaceAll("&radic;", "√");
        str = str.replaceAll("&prop;", "∝");
        str = str.replaceAll("&infin;", "∞");
        str = str.replaceAll("&ang;", "∠");
        str = str.replaceAll("&and;", "∧");
        str = str.replaceAll("&or;", "∨");
        str = str.replaceAll("&cap;", "∩");
        str = str.replaceAll("&cup;", "∪");
        str = str.replaceAll("&int;", "∫");
        str = str.replaceAll("&there4;", "∴");
        str = str.replaceAll("&sim;", "∼");
        str = str.replaceAll("&cong;", "≅");
        str = str.replaceAll("&asymp;", "≈");
        str = str.replaceAll("&ne;", "≠");
        str = str.replaceAll("&equiv;", "≡");
        str = str.replaceAll("&le;", "≤");
        str = str.replaceAll("&ge;", "≥");
        str = str.replaceAll("&sub;", "⊂");
        str = str.replaceAll("&sup;", "⊃");
        str = str.replaceAll("&nsub;", "⊄");
        str = str.replaceAll("&sube;", "⊆");
        str = str.replaceAll("&supe;", "⊇");
        str = str.replaceAll("&oplus;", "⊕");
        str = str.replaceAll("&otimes;", "⊗");
        str = str.replaceAll("&perp;", "⊥");
        str = str.replaceAll("&sdot;", "⋅");
        str = str.replaceAll("&Alpha;", "Α");
        str = str.replaceAll("&Beta;", "Β");
        str = str.replaceAll("&Gamma;", "Γ");
        str = str.replaceAll("&Delta;", "Δ");
        str = str.replaceAll("&Epsilon;", "Ε");
        str = str.replaceAll("&Zeta;", "Ζ");
        str = str.replaceAll("&Eta;", "Η");
        str = str.replaceAll("&Theta;", "Θ");
        str = str.replaceAll("&Iota;", "Ι");
        str = str.replaceAll("&Kappa;", "Κ");
        str = str.replaceAll("&Lambda;", "Λ");
        str = str.replaceAll("&Mu;", "Μ");
        str = str.replaceAll("&Nu;", "Ν");
        str = str.replaceAll("&Xi;", "Ξ");
        str = str.replaceAll("&Omicron;", "Π");
        str = str.replaceAll("&Rho;", "Ρ");
        str = str.replaceAll("&Sigma;", "Σ");
        str = str.replaceAll("&Tau;", "Τ");
        str = str.replaceAll("&Upsilon;", "Υ");
        str = str.replaceAll("&Phi;", "Φ");
        str = str.replaceAll("&Chi;", "Χ");
        str = str.replaceAll("&Psi;", "Ψ");
        str = str.replaceAll("&Omega;", "Ω");
        str = str.replaceAll("&alpha;", "α");
        str = str.replaceAll("&beta;", "β");
        str = str.replaceAll("&gamma;", "γ");
        str = str.replaceAll("&delta;", "δ");
        str = str.replaceAll("&epsilon;", "ε");
        str = str.replaceAll("&zeta;", "ζ");
        str = str.replaceAll("&eta;", "η");
        str = str.replaceAll("&theta;", "θ");
        str = str.replaceAll("&iota;", "ι");
        str = str.replaceAll("&kappa;", "κ");
        str = str.replaceAll("&lambda;", "λ");
        str = str.replaceAll("&mu;", "μ");
        str = str.replaceAll("&nu;", "ν");
        str = str.replaceAll("&xi;", "ξ");
        str = str.replaceAll("&omicron;", "ο");
        str = str.replaceAll("&pi;", "π");
        str = str.replaceAll("&rho;", "ρ");
        str = str.replaceAll("&sigmaf;", "ς");
        str = str.replaceAll("&sigma;", "σ");
        str = str.replaceAll("&tau;", "τ");
        str = str.replaceAll("&upsilon;", "υ");
        str = str.replaceAll("&phi;", "φ");
        str = str.replaceAll("&chi;", "X");
        str = str.replaceAll("&psi;", "ψ");
        str = str.replaceAll("&omega;", "ω");
        str = str.replaceAll("&thetasym;", "ϑ");
        str = str.replaceAll("&upsih;", "ϒ");
        str = str.replaceAll("&piv;", "ϖ");
        str = str.replaceAll("&OElig;", "Œ");
        str = str.replaceAll("&oelig;", "œ");
        str = str.replaceAll("&Scaron;", "Š");
        str = str.replaceAll("&scaron;", "š");
        str = str.replaceAll("&Yuml;", "Ÿ");
        str = str.replaceAll("&fnof;", "ƒ");
        str = str.replaceAll("&circ;", "ˆ");
        str = str.replaceAll("&tilde;", "˜˜˜˜˜˜~");
        str = str.replaceAll("&ensp;", "");
        str = str.replaceAll("&emsp;", "");
        str = str.replaceAll("&thinsp;", "");
        str = str.replaceAll("&zwnj;", "");
        str = str.replaceAll("&zwj;", "");
        str = str.replaceAll("&lrm;", "");
        str = str.replaceAll("&rlm;", "");
        str = str.replaceAll("&ndash;", "–");
        str = str.replaceAll("&mdash;", "—");
        str = str.replaceAll("&lsquo;", "‘");
        str = str.replaceAll("&rsquo;", "’");
        str = str.replaceAll("&sbquo;", "‚");
        str = str.replaceAll("&ldquo;", "“");
        str = str.replaceAll("&rdquo;", "”");
        str = str.replaceAll("&bdquo;", "„");
        str = str.replaceAll("&dagger;", "†");
        str = str.replaceAll("&Dagger;", "‡");
        str = str.replaceAll("&bull;", "•");
        str = str.replaceAll("&hellip;", "…");
        str = str.replaceAll("&permil;", "‰");
        str = str.replaceAll("&prime;", "′");
        str = str.replaceAll("&Prime;", "″");
        str = str.replaceAll("&lsaquo;", "‹");
        str = str.replaceAll("&rsaquo;", "›");
        str = str.replaceAll("&oline;", "‾");
        str = str.replaceAll("&euro;", "€");
        str = str.replaceAll("&trade;", "™");
        str = str.replaceAll("&larr;", "←");
        str = str.replaceAll("&uarr;", "↑");
        str = str.replaceAll("&rarr;", "→");
        str = str.replaceAll("&darr;", "↓");
        str = str.replaceAll("&harr;", "↔");
        str = str.replaceAll("&crarr;", "↵");
        str = str.replaceAll("&lceil;", " ");
        str = str.replaceAll("&rceil;", " ");
        str = str.replaceAll("&rceil;", "");
        str = str.replaceAll("&lfloor;", "");
        str = str.replaceAll("&rfloor;", "");
        str = str.replaceAll("&loz;", "◊");
        str = str.replaceAll("&spades;", "♠");
        str = str.replaceAll("&clubs;", "♣");
        str = str.replaceAll("&hearts;", "♥");
        str = str.replaceAll("&diams;", "♦");
        str = str.replaceAll("&times;", "×");
        str = str.replaceAll("&divide;", "÷");
        str = str.replaceAll("&plusmn;", "±");
        str = str.replaceAll("&sup2;", "2");
        str = str.replaceAll("&sup1;", "1");
        str = str.replaceAll("&nbsp;", " ");
        str = str.replaceAll("&times;", "X");
        str = str.replaceAll("&gt;", " ");
        str = str.replaceAll("&quot;", " ");
        str = str.replaceAll("&amp;", " ");
        str = str.replaceAll("&rdquo;", "”");
        str = str.replaceAll("&mdash;", "-");
        str = str.replaceAll("&ldquo;", "“");
        str = str.replaceAll("&;", "");
        str = str.replaceAll("　", " ");
        str = str.replaceAll("&middot;", "•");
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\r\n", "");
        return str;
    }


    /**
     * 将map转换为存储过程参数
     * @param param
     * @return
     */
    public static String getFilterStr(Map<String, String> param) {
        StringBuffer sb = new StringBuffer("");
        Iterator<Map.Entry<String, String>> paramIt = param.entrySet().iterator();
        int size = 0;

        while (paramIt.hasNext()) {
            Map.Entry<String, String> entry = paramIt.next();
            if (entry.getValue() != null && !"".equals(entry.getValue())) {
                sb.append("^");
                sb.append(entry.getKey());
                sb.append("|");
                sb.append(entry.getValue());
                size++;
            }
        }
        sb.append("^");
        sb.append(size);
        return sb.toString();
    }

    /**
     * 转换为String
     * @param val
     * @return
     */
    public static String toString(Object val) {
        String v = null;
        if (val != null)
            v = val.toString();
        return v;
    }

    /**
     * 如果为null则为"" 否转换为String
     * @param val
     * @return
     */
    public static String toStringEmpty(Object val) {
        String v = "";
        if (val != null)
            v = val.toString();
        return v;
    }

    /**
     * int类型转换为String
     * @param val
     * @return
     */
    public static String toString(int val) {
        return toString(new Integer(val));
    }


    /**
     * 判断对象中所有的属性值是否都为NULL或空
     *
     * @param obj
     * @return
     */
    public static boolean isObjNullOrEmpty(Object obj) {
        boolean res = true;
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(obj) != null && !"".equals(f.get(obj))) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                    res = false;
                    //System.out.println("对象值：" + f.getName() + "-" + f.get(obj));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 去除字符串中的各种换行
     */
    public static String replaceEnter(String s) {
        return s == null ? s : s.replace("\r", "").replace("\n", "").replace("\r\n", "");
    }


    /**
     * 根据正则表达式截取字符串
     * @param regular
     * @param str
     * @return
     */
    public static String findStrByRegular(String regular, String str) {
        String res = "";
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(str);
        if(m.find()) {
            res = m.group();
        }
        return res;
    }


    /**
     * 同步产生UUID
     * @return
     */
    public synchronized static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
