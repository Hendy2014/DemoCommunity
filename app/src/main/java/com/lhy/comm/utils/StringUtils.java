package com.lhy.comm.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Random;

/**
 * @author wlj
 * @date 2017/3/29
 * @email wanglijundev@gmail.com
 * @packagename wanglijun.vip.androidutils.utils
 * @desc: 字符串操作
 */

public class StringUtils {
    /**
     * Judge whether a string is whitespace, empty ("") or null.
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0 || str.equalsIgnoreCase("null")) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        return TextUtils.equals(a, b);
    }

    /**
     * Judge whether a string is number.
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Encode a string
     *
     * @param str
     * @return
     */
    public static String encodeString(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * Decode a string
     *
     * @param str
     * @return
     */
    public static String decodeString(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * Converts this string to lower case, using the rules of {@code locale}.
     *
     * @param s
     * @return
     */
    public static String toLowerCase(String s) {
        return s.toLowerCase(Locale.getDefault());
    }

    /**
     * Converts this this string to upper case, using the rules of {@code locale}.
     *
     * @param s
     * @return
     */
    public static String toUpperCase(String s) {
        return s.toUpperCase(Locale.getDefault());
    }

    /**
     * 字符串数组转化为字符串
     * 中间以,逗号隔开
     *
     * @param str 待转化字符串
     */
    public static String convertStringArrayToString(String[] str) {
        if (str == null) {
            return null;
        }

        int length = str.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("\"")
                    .append(str[i])
                    .append("\"");
            if (i == length - 1) {
                continue;
            }
            sb.append(",");
        }
        return sb.toString();
    }


    /**
     * 生成指定长度的随机字符串，字符是数字或大小写字母，可用于随机密钥生成等
     */
    public static String getRamdonString(int length) {
        String sources = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        int len = sources.length() - 1;
        StringBuilder flag = new StringBuilder();
        for (int j = 0; j < length; j++) {
            flag.append(sources.charAt(rand.nextInt(len))).append("");
        }
        return flag.toString();
    }

    /**
     * 生成指定长度的随机字符串，字符是数字
     */
    public static String getRamdonNumber(int length) {
        String sources = "0123456789";
        Random rand = new Random();
        int len = sources.length() - 1;
        StringBuilder flag = new StringBuilder();
        for (int j = 0; j < length; j++) {
            flag.append(sources.charAt(rand.nextInt(len))).append("");
        }
        return flag.toString();
    }
}
