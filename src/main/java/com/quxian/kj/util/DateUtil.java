package com.quxian.kj.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DateUtil {

    /**
     * 返回日期串 例：20191106
     * @return
     */
    public static String getDate(){
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
    }

    /**
     * 返回时间串 例：121030
     * @return
     */
    public static String getTime(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")).toString();
    }

}
