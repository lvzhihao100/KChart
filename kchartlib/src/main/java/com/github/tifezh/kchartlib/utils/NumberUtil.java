package com.github.tifezh.kchartlib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lv on 18-6-5.
 */

public class NumberUtil {
    /**
     * 保留几位小数
     * @param number 原数据
     * @param keepNum 小数位数
     * @param isMax 是否最多保留几位
     * @return 处理后数据
     */
    public static String keep(String number, int keepNum,boolean isMax) {
        if (TextUtils.isEmpty(number)) {
            return 0 + "";
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(keepNum);
        if (!isMax){
            nf.setMinimumFractionDigits(keepNum);
        }
        nf.setGroupingUsed(false);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return nf.format(new BigDecimal(number).doubleValue());
    }

    /**
     * 去除小数点最后的零
     *
     * @param number 原数据
     * @return 处理后数据
     */
    public static String keepNoZero(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0 + "";
        }
        if (number.indexOf(".") > 0) {
            number = number.replaceAll("0+?$", "");//去掉多余的0
            number = number.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return number;
    }

    public static String keepMax(String number, int keepNum) {
        return keep(number,keepNum,true);
    }
    public static String keepMax(double number, int keepNum) {
        return keep(number+"",keepNum,true);
    }
    public static String keep(String number, int keepNum) {
        return keep(number,keepNum,false);
    }
    public static String keepMax4(String number) {
        return keep(number,4,true);
    }
    public static String keepMax4(double number) {
        return keep(number+"",4,true);
    }
    public static String keep4(double number) {
        return keep(number+"",4,false);
    }
    public static String keep4(String number) {
        return keep(number,4,false);
    }
    public static String keepMax2(String number) {
        return keep(number,2,true);
    }
    public static String keepMax2(double number) {
        return keep(number+"",2,true);
    }
    public static String keep2(double number) {
        return keep(number+"",2,false);
    }
    public static String keep2(String number) {
        return keep(number,2,false);
    }

    public static String keepNoZoreMax4(String number) {
        return keepMax4(keepNoZero(number));
    }

    public static String keepNoZoreMax4(double number) {
        return keepMax4(keepNoZero(number));
    }

    public static String keepNoZoreMax2(String number) {
        return keepMax2(keepNoZero(number));
    }


    public static String keepNoZero(double number) {
        return keepNoZero(number + "");
    }

    private static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }

    public static boolean isWholeNumber(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    public static boolean isPositiveDecimal(String orginal) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    public static boolean isNegativeDecimal(String orginal) {
        return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
    }

    public static boolean isDecimal(String orginal) {
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    /**
     * 判断是否是整数或小数
     *
     * @param s s
     * @return 为空返回false
     */
    public static boolean isRealNumber(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        return isWholeNumber(s) || isDecimal(s);
    }





    public static String keepMax8(String number) {
        return keepMax(number,8);
    }
    public static String keepMax8(double number) {
        return keepMax(number,8);
    }

    public static String keepNoZeroMax4(String number) {
        return keepMax4(keepNoZero(number));
    }
    public static String keepNoZeroMax4(double number) {
        return keepMax4(keepNoZero(number));
    }


}
