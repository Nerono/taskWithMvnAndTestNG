import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import testprog.helpstuff.Logger;

import java.io.IOException;


public class CustomListener extends TestListenerAdapter {
Logger logger;
    @Override
    public void onStart(ITestContext tc) {
        logger = new Logger();
        try {
            logger.startLog();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext tc){
        logger.close();
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        try {
            logger.logAndPrintLine(tr.getName() + "--Test method failed");
            logger.logAndPrintExcLine((Exception) tr.getThrowable());
        }catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        try {
            logger.logAndPrintLine(tr.getName() + "--Test method skipped");
        }catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        try {
            logger.logAndPrintLine(tr.getName() + "--Test method success");
        }catch (IOException e){e.printStackTrace();}
    }
}
