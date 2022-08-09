package com.sn.qa.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public  class PropertyFileReader {


    Properties reader ;
    FileInputStream fis;

//    public PropertyFileReader(String file) {
//        try {
//            this.reader = new Properties();
//            this.fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config/"+file);
//            reader.load(fis);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public PropertyFileReader(String file, String location) {
        try {
            this.reader = new Properties();
            this.fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/" + location +file);
            reader.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  String get(String name){
        return this.reader.getProperty(name);
    }

}