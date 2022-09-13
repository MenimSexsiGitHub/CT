package com.sn.core.exceptionhandling;

import com.sn.core.constant.CoreEnums;
import com.sn.core.util.Utilities;
import cucumber.api.Scenario;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.HashMap;

public class ExceptionHandling {
    private static Logger logger = Logger.getLogger(ExceptionHandling.class.getName());
    public static boolean driverError = false;
    public static boolean apiError = false;
    public static String driverException = "";
    public static String errorRows = "";
    protected static HashMap<String,String> saveExecutionData = new HashMap();

    public ExceptionHandling(){}


    public static void displayCustomErrorMessage(String message, String webElement, String action, long startTime, CoreEnums.ERROR error) {
        try {
            saveExecutionData.put("e_message", String.valueOf(message));
            saveExecutionData.put("uielement", String.valueOf(webElement));
            saveExecutionData.put("action", String.valueOf(action));
            saveExecutionData.put("respondtime", String.valueOf(System.currentTimeMillis() - startTime / 1000L));
            logger.debug("execution data -> " + saveExecutionData);
            String err_message = "";
            switch (error){
                case ELEMENT_ERROR:
                    logger.trace("Processing object identification related error");
                    err_message = "Unable to locate the element "+ webElement;
                    logger.info("Error message: "+ err_message);
                    Assert.assertTrue(err_message, false);
                    break;
                case ALERT_ERROR:
                    logger.trace("Processing alert related error");
                    err_message = "Check automation step for alert ";
                    logger.info("Error message: "+ err_message);
                    Assert.assertTrue(err_message, false);
                    break;
                case GENERAL_ERROR:
                    logger.trace("Processing general error");
                    logger.info("Error message: "+ webElement);
                    Assert.assertTrue(err_message, false);
                    break;
                case DRIVER_ERROR:
                    logger.trace("Processing driver related error");
                    err_message ="driver exception";
                    logger.info("Error message: "+ err_message);
                    Assert.assertTrue(err_message, false);
                    break;
                default:
                    logger.error("error while executing method");
                    Assert.assertTrue("error while executing method",false);
            }
        }catch (Exception var7){
            logger.error("Exception details: \n" + var7.getStackTrace());
            Assert.assertTrue("error while executing method",false);
        }
    }

    public static void displayCustomErrorMessage(String message, String isDisplayed, long startTime, CoreEnums.ERROR error) {

    }

    public static String getErrorinAfter() {
        String errHtml ="";
        return errHtml;
    }

    


    public static String getAPIErrorDetails(Scenario scenario){
        String errorHtml = "";
        try {
            errorHtml = errorHtml + "";
            errorHtml = errorHtml + "";
            errorHtml = errorHtml + "";
        }catch (Exception var3){
            errorHtml = errorHtml + "" +var3.getMessage() +"";
        }
        return errorHtml + "";
    }


    static {
        logger.setLevel(Utilities.logger.getLevel());
    }














}
