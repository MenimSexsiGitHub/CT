package com.sn.qa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class DateUtill {

    private static final String[] formats ={
            "yyyy-MM-dd HH:mm:ss",
            "M/dd/yyyy",
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "yyyyMMdd", "MM/dd/yyyy", "dd/MM/yyyy", "mm/dd/yyyy",
    };

    public static String getFormattedDatetime(String sFormat, Date date){
        String sFormatted;
        SimpleDateFormat sdf =  new SimpleDateFormat(sFormat);
        sFormatted = sdf.format(date);
        return sFormatted;
    }

    public static Date getModifiedDateTime(String sDatePart, int num){
        Calendar cal = Calendar.getInstance();
        switch (sDatePart.toUpperCase()){
            case "DAY":
                cal.add(Calendar.DATE, num);
                return cal.getTime();
            case "MONTH":
                cal.add(Calendar.MONTH, num);
                return cal.getTime();
            case "YEAR":
                cal.add(Calendar.YEAR, num);
                return cal.getTime();
            case "HOUR":
                cal.add(Calendar.HOUR, num);
                return cal.getTime();
            case "MINUTE":
                cal.add(Calendar.MINUTE, num);
                return cal.getTime();
            case "SECOND":
                cal.add(Calendar.SECOND, num);
                return cal.getTime();
            default:
                return cal.getTime();
        }
    }

    public static String parseDateFormat(String date){
        if (date !=null){
            for (String parse:formats){
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(date);
                    System.out.println(" printing the date value: " +parse);
                    return parse;
                }catch (ParseException e){
                }

            }
        }
        return null;
    }

    public static String formattDate(String date, String format){
        String sFormatted = null;
        DateFormat expDate = new SimpleDateFormat(format);
        try {
            Date fDate = expDate.parse(date);
            sFormatted = expDate.format(fDate);
            System.out.println("formatted date: " + sFormatted);
        }catch (ParseException e){
        }
        return sFormatted;
    }

    public static String formatDate(String date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        DateFormat targetFormat = new SimpleDateFormat(format);
        String expDate = null;
        try {
            Date ExpDate = sdf.parse(date);
            expDate = targetFormat.format(ExpDate);
    System.out.println("formatted date: " + expDate);
        }catch (ParseException e){}
        return expDate;
    }

    public static Date getDateFormatObject(String sDate){
        DateFormat formatter = null;
        Date date = null;
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = formatter.parse(sDate);
        }catch (ParseException e){e.printStackTrace();}
        return date;
    }

    public static String returnCurrentMonth(){
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        String curMonth = currentMonth.toString();
        return curMonth;
    }

    public static Integer returnCurrentDay(){
        LocalDate currentDate = LocalDate.now();
        Integer currentDay = currentDate.getDayOfMonth();
        return currentDay;
    }

    public static String returnNextDay(String addDay){
        long add = Long.parseLong(addDay);
        LocalDate currentDate = LocalDate.now();
        String nextDay = currentDate.plusDays(add).toString();
        System.out.println(nextDay);
        return nextDay;
    }

    public static String returnLastDayOfTheMonth(){
        Calendar calendar = Calendar.getInstance();
        int res = calendar.getActualMaximum(Calendar.DATE);
        String lastDayOfMonth = String.valueOf(res);
        formatDate(lastDayOfMonth,"MM/dd/yyyy");
        return lastDayOfMonth;
    }

    public static String  returnLastWorkingDayOfTheMonth(){
        LocalDate lastWorkingDayOfMonth;
        LocalDate lastDayOfMonth = LocalDate.now();
        switch (DayOfWeek.of(lastDayOfMonth.get(ChronoField.DAY_OF_WEEK))){
            case SATURDAY:
                lastWorkingDayOfMonth = lastDayOfMonth.minusDays(1);
                break;
            case SUNDAY:
                lastWorkingDayOfMonth = lastDayOfMonth.minusDays(2);
                break;
            default:
                lastWorkingDayOfMonth = lastDayOfMonth;
        }
        String lastWorkingDayOfCurrentMonth = lastWorkingDayOfMonth.toString();
         return lastWorkingDayOfCurrentMonth;

    }

    public static String returnLastWorkDayOfTheMonth(String format){
        Date today = new Date();
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(today);
       calendar.add(Calendar.MONTH,1);
       calendar.set(Calendar.DAY_OF_MONTH,1);
       do {
           calendar.add(Calendar.DATE, -1);
       }while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
        Date lastDayOfMonth = calendar.getTime();
        DateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(lastDayOfMonth);
    }








}
