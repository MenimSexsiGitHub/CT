package com.sn.core.util;

import org.apache.log4j.Logger;

public class CommonUtilities {
    protected static Logger logger = Logger.getLogger("Core.Driver");

    public CommonUtilities(){}

    public static void waitTime(int seconds){
        try {
            Thread.sleep((long) seconds * 1000);
        }catch (Exception var2){
            logger.error("excepton in common utilities waitTime method");
        }
    }



}
