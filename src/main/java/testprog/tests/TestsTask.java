package testprog.tests;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprog.helpstuff.BasicPage;
import testprog.helpstuff.Logger;
import testprog.helpstuff.TestsBasic;
import testprog.realpages.PODressSearch;
import testprog.realpages.POMainPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestsTask extends TestsBasic {
    Logger logger;
    POMainPage mp;
    PODressSearch dsp;

    //Try open main page (task 1)
    public boolean openMainPageCheck() throws IOException{
        logger.logLine("Trying open main page");
        try{
            mp = openMainPage();
            logger.logSucces();
            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    //Testing currency list from the top bar (task 2,3)
    public boolean currencyConformityMCheck() throws IOException{
        logger.logLine("Testing currency list from the top bar. Perfomance of each item and the correspondence " +
                "of currencies in the products...");
        BasicPage po;
        try{
            po = mp.setNewCurrency(BasicPage.EUR);
            compareCurrAndProdCurr(mp, logger);
            logger.logAndPrintLine("Next currency");
            if(po!=null) {
                po = mp.setNewCurrency(BasicPage.UAH);
                compareCurrAndProdCurr(mp, logger);
                logger.logAndPrintLine("Next currency");
            }
            if(po!=null){
                mp.setNewCurrency(BasicPage.USD);
                compareCurrAndProdCurr(mp, logger);
                logger.logSucces();
            }

            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    //Searching "dress" in the search line (task 4 and checking that the current page is the search results page
    //and include "Товаров:" line)
    public boolean dressSearchCheck() throws IOException{
        logger.logLine("Searching \"dress\" in the search line and checking the opened page(include searching \"Товаров:\")...");
        try{
            dsp = mp.dressSearch();
            mp=null;
            logger.logSucces();
            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;}
    }

    //Comparing number of products (task 5)
    public boolean numberOfProductsChecking() throws IOException{
        logger.logLine("Comparing of the number in the \"Товаров:\" line with the number of displayed products...");
        try{
            compareTitleNumWithPage(dsp, logger);
            logger.logSucces();
            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    //Checking products currency is $ (task 6)
    public boolean currencyConformityDSCheck() throws IOException{
        logger.logLine("Checking products currency is $...");
        try{
            compareCurrAndProdCurr(dsp,logger);
            logger.logSucces();
            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    //Change sorting (task 7)
    public boolean changeSortingCheck() throws IOException{
        logger.logLine("Change sorting to \"Price: from high to low\"...");
        try{
            dsp.setNewSorting(dsp.priceHToL);
            logger.logSucces();
            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    //Checking the result of a sort change (task 8)
    public boolean resultOfSortingCheck() throws IOException{
        logger.logAndPrintLine("Checking the result of a sort change...");
        try{
            (new WebDriverWait(dsp.getDriver(), 10)).until(ExpectedConditions.
                    attributeToBe(dsp.itemPriceHToLLocator, "class", "select-list current js-search-link"));
            checkSortingAccuracy(dsp, logger);
            logger.logSucces();
            return true;
        }catch (Exception e){
            //!!!!System.out.println(dsp.getDriver().findElement(dsp.itemPriceHToLLocator).getAttribute("class"));
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    //Checking fields that form the price of product (task 9,10)
    public boolean priceFieldsCheck()throws IOException{
        logger.logLine("Validation of data in the fields that form the price...");
        try{
            compareAllPrices(dsp,logger);
            logger.logSucces();
            return true;
        }catch (Exception e){
            logger.logFailure();
            logger.logAndPrintExcLine(e);
            return false;
        }
    }

    public void fullTest() throws IOException{
        logger = new Logger();
        logger.startLog();
        try {
            logger.logAndPrintLine("Logs saved in " + Logger.getPath());
            openMainPageCheck();
            if (mp instanceof POMainPage) {
                currencyConformityMCheck();
                dressSearchCheck();
            }else{logger.logAndPrintLine("Main page didn't open");}
            if(dsp instanceof PODressSearch){
                numberOfProductsChecking();
                currencyConformityDSCheck();
                if (changeSortingCheck()){
                    resultOfSortingCheck();
                }
                priceFieldsCheck();
            }else{logger.logAndPrintLine("Main page didn't open");}
        }finally {
            if(dsp!=null) dsp.getDriver().close();
            logger.close();
        }

    }





}