package com.sn.qa.util;


import com.relevantcodes.extentreports.*;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;




public class CucumberCliMain {

    public static List<String> failedScenariosList;
    private static String outputDirectory;
    private static File htmlReportDir;
    private static ExtentReports extent;
//    private static ExtentReports extent;

    public CucumberCliMain(){}

    public static void main(String[] argv) throws IOException {
        System.out.println("Starting test");
        byte exitstatus = run(argv, Thread.currentThread().getContextClassLoader());
    }
    public static byte run(String[] argv, ClassLoader classLoader) throws IOException {

            RuntimeOptions runtimeOptions = new RuntimeOptions(new ArrayList(Arrays.asList(argv)));
            MultiLoader resourceLoader = new MultiLoader(classLoader);
            ResourceLoaderClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
            Runtime runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
            runtime.run();
            return runtime.exitStatus();
    }
    public static void insertExecutionData() throws IOException{
        System.out.println("------------Post Test Execution Cucumber Data Insertion Plugin --------------");
        try {
            String jenkinsProps = System.getenv("BUILD_NUMBER");
            if (jenkinsProps !=null && !jenkinsProps.isEmpty()){
                System.setProperty("enable.cucumber.data.dumping", "true");
//                new CLIRunner().run;
            }
            System.out.println("------------ Cucumber Data Insertion Successful --------------");
        }catch (Throwable e){
//            Utility.printOutput(getStackTraceAsString);
            System.out.println("Data insertion failed");
        }
    }
    public static String getStackTraceAsString(Exception e) throws IOException{
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        sw.close();
        String trace = sw.toString();
        return trace;
    }

    public static void getScenarioNameList() throws Throwable{
        System.out.println("---------------Getting scenarios names ---------------");
        List<String> defaultArguments = new LinkedList<String>();
        defaultArguments.add("--plugin");
        defaultArguments.add("html:target/report/not-to-use");
        defaultArguments.add("--glue");
        defaultArguments.add("com.sn.qa.steps");
        defaultArguments.add("resources/features");
        List<String> allArguments = new LinkedList<String>();
        allArguments.addAll(defaultArguments);
        allArguments.addAll(getJenkinsTagList());
        String allArgsArray[] = new String[allArguments.size()];
        for (int i = 0; i<allArguments.size(); i++){
            allArgsArray[i] = allArguments.get(i);
        }
        try {
            CucumberCliMain.main(allArgsArray);
        }catch (Throwable e){}
        System.out.println("Scenario names list fetched");
    }

    public static List<String> getJenkinsTagList(){
        List<String> list = new LinkedList<>();
        String args = System.getProperty("cucumber.options");
        System.out.println("cucumber options ="+args);
        String tags[] = args.split("--tags");
        System.out.println("split array size ="+tags.length);
        for (String tag : tags){
           if(tag != null && !tag.trim().isEmpty()){
               list.add("--tags ");
               list.add(tag.trim());
            }
        }
//        printList(list);
        return list;
    }
    private static void printList(List<String> list){
        for (String item : list){System.out.println(item);}
    }

    public static List<String> getFailedScenariosList(){
        if (failedScenariosList !=null){
            return failedScenariosList;
        } return new LinkedList<>();
    }

    //if CucumberUpload flag passes as true in command line, then this method will upload cucumber result
    public static void saveCucumberReport(){
        String cucumberUpLoadString = System.getProperty("cucumberUpLoad");
        if (cucumberUpLoadString ==null || (cucumberUpLoadString!=null && cucumberUpLoadString.equalsIgnoreCase("false"))){
            System.out.println("Cucumber result upload flag is false.");
            return;
        }
        System.out.println("Cucumber result upload flag is true.");
        HttpURLConnection connection = null;
        try {
            System.out.println(System.getProperty("user.dir") + "/target/cucumber.json");
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/target/cucumber.json");
            String cucumberJsonFile = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
            //install all trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            //create all trusting host name
            HostnameVerifier allHostValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            //install all trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostValid);

            URL jiraURL = new URL("");
            byte[] dataBytes = cucumberJsonFile.getBytes("UTF-8");
            String login = "";
            final byte[] authBytes = login.getBytes(StandardCharsets.UTF_8);
            String encoded = Base64.getEncoder().withoutPadding().encodeToString(authBytes);

            connection = (HttpsURLConnection) jiraURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "applocation/json");
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(dataBytes);
            wr.flush();
            wr.close();

            Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            for (int c; (c = in.read()) >= 0; System.out.println((char) c)) ;

        }catch (MalformedURLException e){System.err.println("MalformedURLException: "+e.getMessage());
        }catch (IOException e){
            System.err.println("IOException: "+e.getMessage());
            if (connection !=null){
                try {
                    System.out.println(connection.getErrorStream());
                    InputStream errorStream = connection.getErrorStream();
                    if(errorStream !=null){
                        Reader in = new BufferedReader(new InputStreamReader(errorStream));
                        for (int c; (c=in.read()) >= 0; System.out.println((char) c));
                    }
                }catch (IOException e2){}
            }
        }catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }
    }












    }
