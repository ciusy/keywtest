/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.text.DecimalFormat;

/**
 *
 * @author ruojun
 */
public class StringUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public static String formatFloat(float f) {
        return new DecimalFormat("###,###,###.###").format(f);
    }

    /**
     * 返回四个空格
     * @return
     */
    public static String fourBlanks() {
        return "    ";
    }

    /**
     *
     * @param origianArray  
     * @param type
     * @return 将一段时间分成许多小段
     */
    public static List<String[]> parseDateRangeToSmallRange(String[] origianArray, String type) {
        try {
            String fromMMDDYYYY = origianArray[0];
            String toMMDDYYYY = origianArray[1];
            Date from = sdf.parse(fromMMDDYYYY);
            Date to = sdf.parse(toMMDDYYYY);

            Calendar begin = Calendar.getInstance();
            begin.setTime(from);
            Calendar end = Calendar.getInstance();
            end.setTime(to);
            List<String[]> dateList = new ArrayList<String[]>();
            if (type.equals("月")) {
                while (begin.compareTo(end) <= 0) {
                    String tbegin = sdf.format(begin.getTime());
                    int year = begin.get(Calendar.YEAR);
                    int month = begin.get(Calendar.MONTH);
                    int date = begin.get(Calendar.DATE);
                    begin.set(Calendar.DATE, 1);
                    begin.add(Calendar.MONTH, 1);//下个月的第一天
                    begin.add(Calendar.DATE, -1);//这个月的最后一天
                    if (begin.compareTo(end) <= 0) {
                        dateList.add(new String[]{tbegin, sdf.format(begin.getTime())});
                        begin.add(Calendar.DATE, 1);
                    } else {
                        dateList.add(new String[]{tbegin, sdf.format(end.getTime())});
                    }
                }
                return dateList;
            }
            if (type.equals("周")) {
                while (begin.compareTo(end) <= 0) {
                    String tbegin = sdf.format(begin.getTime());
                    begin.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    begin.add(Calendar.WEEK_OF_MONTH, 1);//下个星期的第一天
                    begin.add(Calendar.DATE, -1);//这个星期的最后一天
                    if (begin.compareTo(end) <= 0) {
                        dateList.add(new String[]{tbegin, sdf.format(begin.getTime())});
                        begin.add(Calendar.DATE, 1);
                    } else {
                        dateList.add(new String[]{tbegin, sdf.format(end.getTime())});
                    }
                }
                return dateList;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static void testParseDateRangeToSmallRange(String[] args) {
        String fromMMDDYYYY = "01/31/2008";
        String toMMDDYYYY = "11/01/2009";
        List<String[]> dateList = new ArrayList<String[]>();
        //dateList = StringUtil.parseDateRangeToSmallRange(new String[]{fromMMDDYYYY, toMMDDYYYY}, "月");
        dateList = StringUtil.parseDateRangeToSmallRange(new String[]{fromMMDDYYYY, toMMDDYYYY}, "周");
        if (dateList != null) {
            String[] temp = null;
            for (int i = 0; i < dateList.size(); i++) {
                temp = dateList.get(i);
                System.out.println(temp[0] + "-" + temp[1]);
            }
        }
    }

    public static void main(String[] args) {
        String fromMMDDYYYY = "01/31/2008";
        String toMMDDYYYY = "11/01/2009";
        List<String[]> dateList = new ArrayList<String[]>();
        //dateList = StringUtil.parseDateRangeToSmallRange(new String[]{fromMMDDYYYY, toMMDDYYYY}, "月");
        dateList = StringUtil.parseDateRangeToSmallRange(new String[]{fromMMDDYYYY, toMMDDYYYY}, "周");
        if (dateList != null) {
            String[] temp = null;
            for (int i = 0; i < dateList.size(); i++) {
                temp = dateList.get(i);
                System.out.println(temp[0] + "-" + temp[1]);
            }
        }
    }
}
