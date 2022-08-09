package com.sn.core.util;

import com.sn.core.constant.CoreEnums;
import joptsimple.internal.Strings;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {
    private static Double maxWaitTimeinMin;
    private static Double maxPollTimeinMin;
    private static String SYS_MAX_WAIT_TIME;
    private static String SYS_MAX_POLL_TIME;
    public static Logger logger = Logger.getLogger(Utilities.class.getName());

    private Utilities(){}

    public static void waitTime(long seconds){
        try {
            Thread.sleep(seconds = 1000L);
        }catch (InterruptedException var3){
            logger.error(var3.getStackTrace());
            Thread.currentThread().interrupt();;
        }
    }

    public static boolean isNullOrEmptyOrNullString(String evalString){
        return Strings.isNullOrEmpty(evalString) || evalString.equalsIgnoreCase("null");
    }

    public static String getNodeValueFromStringXML(String xml, String nodeName) throws Exception{
        DocumentBuilderFactory dbf =  DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(xml));
        Document document = db.parse(src);
        return document.getElementsByTagName(nodeName).item(0).getTextContent();
    }

    public static String getDateFromString(String strDate){
        String[] strDateSplit = null;
        String daysStr = "0";
        logger.info("Initilized days to "+ daysStr);
        if (strDate.contains("-")){
            strDateSplit = strDate.split("-");
            daysStr = "-" +strDateSplit[1];
        }else if (strDate.contains("+")){
            strDateSplit = strDate.split("\\+");
            strDate = strDateSplit[1];
        }else {
            if (!strDate.contains("Today")){
                return strDate;
            }
            daysStr ="0";
        }
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentdate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentdate);
        c.add(5, Integer.parseInt(daysStr));
        Date calculatedDate = c.getTime();
        return dateFormat.format(calculatedDate);
    }

    public static String getDateFromString_ddMMyyyy(String strDate){
        String[] strDateSplit = null;
        String daysStr = "0";
        logger.info("Initilized days to "+ daysStr);
        if (strDate.contains("-")){
            strDateSplit = strDate.split("-");
            daysStr = "-" +strDateSplit[1];
        }else if (strDate.contains("+")){
            strDateSplit = strDate.split("\\+");
            strDate = strDateSplit[1];
        }else {
            if (!strDate.contains("Today")){
                return strDate;
            }
            daysStr ="0";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentdate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentdate);
        c.add(5, Integer.parseInt(daysStr));
        Date calculatedDate = c.getTime();
        return dateFormat.format(calculatedDate);
    }

    public static String getConnectedDeviceName(){
        try {
            Process p = Runtime.getRuntime().exec("cmd /c adb devices");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()){
                System.out.println("---------------: " +line);
                if (line.contains("\td")){
                    String deviceName = line.split("\td")[0];
                    return deviceName;
                }
            }
        }catch (Exception var4){
            logger.info(var4.getStackTrace());
        }
        return null;
    }

    public static Double getMaxWaitTimeInMin(){
        if (maxWaitTimeinMin == null){
            maxWaitTimeinMin = 30.0D;
        }
        return maxWaitTimeinMin;
    }

    public static Double getMaxPollTimeInMin(){
        if (maxPollTimeinMin == null){
            maxPollTimeinMin = 0.5D;
        }
        return maxPollTimeinMin;
    }

    public static Double stringToDouble(String s){
        if (s !=null && !s.isEmpty()){
            try {
                return Double.valueOf(s);
            }catch (NumberFormatException var2){
                logger.info("NumberFormatException: provided string can't be converted to double");
            }
        }
        return null;
    }

    public static Integer stringToInteger(String s){
        if (s !=null && !s.isEmpty()){
            try {
                return Integer.valueOf(s);
            }catch (NumberFormatException var2){
                logger.info("NumberFormatException: provided string can't be converted to integer");
            }
        }
        return null;
    }

    public static boolean stringToBoolen(String s){
        return Boolean.parseBoolean(s);
    }

    public static void downloadReport(String fileNemeStartWith){
        String home = null;
        File file = new File(home + "/downloads/");
        File[] files = null;
        try {
            files = file.listFiles((f) ->{
                return f.getName().startsWith(fileNemeStartWith);
            });
        }catch (Exception var10){
            logger.info("Exception from downloading file");
        }
        File[] var4 = files;
        int var5 = files.length;

        for (int var6 = 0; var6 <var5; ++var6){
            File filePath = var4[var6];
            logger.info("File path: " + filePath.getAbsoluteFile());
            try {
                if (filePath.exists()){
                    logger.info("File exists");
                    boolean result = filePath.delete();
                    logger.info("File deletion : "+ result);
                }else {
                    logger.info("File does not exist");
                }
            }catch (Exception var9){
                logger.info("File does not exist");
            }
        }
    }

    public static String generateString(Integer charCount){
        return RandomStringUtils.randomAlphabetic(charCount);
    }

    public static Logger setLogLevel(Logger log, String logLevel){
        Locale.setDefault(Locale.ENGLISH);
        CoreEnums.LogLevels logName = CoreEnums.LogLevels.valueOf(logLevel.toUpperCase(Locale.ROOT));
        switch (logName){
            case ALL:
                log.setLevel(Level.ALL);
                break;
            case TRACE:
                log.setLevel(Level.TRACE);
                break;
            case DEBUG:
                log.setLevel(Level.DEBUG);
                break;
            case WARN:
                log.setLevel(Level.WARN);
                break;
            case ERROR:
                log.setLevel(Level.ERROR);
                break;
            case FATAL:
                log.setLevel(Level.FATAL);
                break;
            case OFF:
                log.setLevel(Level.OFF);
                break;
            case INFO:
                log.setLevel(Level.INFO);
                break;
        }
        logger.setLevel(log.getLevel());
        logger.trace("logger lever is set to : " + logger.getLevel());
        return log;
    }

    static {
        logger.setLevel(Level.INFO);
    }









}
