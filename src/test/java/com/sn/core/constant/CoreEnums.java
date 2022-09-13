package com.sn.core.constant;

public class CoreEnums {
    public CoreEnums(){}


    public static enum DriverType{
        CHROME,
        FIREFOX,
        SAFARI,
        IE,
        IOS,
        ANDROID,
        ANDROIDCHROME,
        IOSCHROME;

        private DriverType(){}
    }

    public static enum  EmailFolder{
        INBOX,
        SPAM;

        private EmailFolder(){}
    }

    public static enum ERROR{
        ELEMENT_ERROR,
        DRIVER_ERROR,
        ALERT_ERROR,
        GENERAL_ERROR;

        private ERROR(){}
    }

    public static enum LogLevels{
        ALL,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,
        OFF;

        private LogLevels(){}
    }

    public static enum USERCONDITION{
        SCRIPT_CONTINUE,
        SCRIPT_STOP;

        private USERCONDITION(){}
    }

    public static enum OSNames{
        WINDOWS,
        MAC,
        LINUX;

        private OSNames(){}
    }

    public static enum BrowserNames{
        IOS,
        IOSCHROME,
        ANDROID,
        ANDROIDCHROME,
        CHROME,FIREFOX,
        SAFARI,
        IE;

        private BrowserNames(){}
    }

    public static enum RunType{
        LOCAL,
        CLOUD,
        GRID,
        DEVICE_GRID,
        THIRD_PARTY_GRID;

        private RunType(){}
    }









}
