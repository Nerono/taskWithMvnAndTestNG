package testprog.helpstuff;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger implements AutoCloseable{
    BufferedWriter bw;
    StringWriter sw;
    PrintWriter pw;
    String stackTrace;
    private static String path ="D:\\TestLog\\log.txt";

    public static String getPath(){return path;}

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public Logger(){
        try {
            // bw = new BufferedWriter(new FileWriter(path,true));
            new File("D:\\TestLog\\").mkdirs();
            bw = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(path,true), StandardCharsets.UTF_8));
            sw = new StringWriter();
            pw = new PrintWriter(sw);

        }catch (IOException e){e.printStackTrace();}
    }

    public void startLog()throws IOException{
        bw.newLine();
        bw.newLine();
        bw.newLine();
        bw.write("######## Start test " + Logger.getCurrentTimeStamp()+" ########");
        bw.newLine();
    }
    public void logLine(String s) throws IOException{
        bw.newLine();
        bw.write(s);
    }
    public void logAndPrintLine(String s) throws IOException{
        bw.newLine();
        bw.write(s);
        System.out.println(s);
    }
    public void logAndPrintExcLine(Exception e)throws IOException{
        e.printStackTrace(pw);
        stackTrace = sw.toString();
        pw.flush();
        sw.getBuffer().setLength(0);
        logAndPrintLine(stackTrace);
    }
    public void logSucces()throws IOException{
        bw.newLine();
        bw.write("#Succes");
    }
    public void logFailure()throws IOException{
        bw.newLine();
        bw.write("#Failure");
    }
    public void finishLog() throws IOException{
        bw.newLine();
        bw.write("######## FINISH ########");
        bw.close();
        pw.close();
        sw.close();
    }
    public void finishWithoutLog() throws IOException{
        bw.close();
        pw.close();
        sw.close();
    }

    @Override
    public void close()  {
        try {
            finishLog();
        }catch (IOException e){e.printStackTrace();}
    }
}