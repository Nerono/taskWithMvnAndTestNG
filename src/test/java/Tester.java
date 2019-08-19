import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import testprog.helpstuff.BasicPage;
import testprog.helpstuff.TestsBasic;
import testprog.realpages.PODressSearch;
import testprog.realpages.POMainPage;
import testprog.tests.TestsTask;

import java.util.List;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;

public class Tester {
    WebDriver driver;
    String adressM;
    String adressD;
    char currentCurrency;
    POMainPage mp;
    PODressSearch dp;
    List<WebElement> currentProductPrices;
    String s;


    /*@BeforeSuite
    public void preparing() throws Exception{
        tb = new TestsBasic();
        s = "http://prestashop-automation.qatestlab.com.ua/ru/";
        mp = tb.openMainPage();
    }*/
    @BeforeTest
    public void preparing() throws Exception{
       // System.setProperty("webdriver.chrome.driver","D:\\TestTools\\chromedriver\\chromedriver.exe");
        WebDriverManager.getInstance(CHROME).setup();
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/opt/google/chrome"); //googlechrome in this directory in ubuntu
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }
    @AfterTest
    public void closing() throws Exception{
        driver.close();
    }


    @Test(testName = "Opening main page test")
    public void testOpenMain() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";
        driver.get(adressM);
        mp = new POMainPage(driver);
        Assert.assertEquals(adressM,mp.getDriver().getCurrentUrl());
    }
    @Test
    public void testCurrencyConformityMEUR() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";
        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.EUR);
        currentCurrency = mp.getLastChar(mp.getDriver().findElement(mp.currentCurrencyLocator));
        currentProductPrices = mp.getCurrentProductPrices();

        for(WebElement element : currentProductPrices){
            Assert.assertEquals(currentCurrency,mp.getLastChar(element));
        }
    }

    @Test
    public void testCurrencyConformityMUAH() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";
        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.UAH);
        currentCurrency = mp.getLastChar(mp.getDriver().findElement(mp.currentCurrencyLocator));
        currentProductPrices = mp.getCurrentProductPrices();

        for(WebElement element : currentProductPrices){
            Assert.assertEquals(currentCurrency,mp.getLastChar(element));
        }
    }

    @Test
    public void testCurrencyConformityMUSD() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";
        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        currentCurrency = mp.getLastChar(mp.getDriver().findElement(mp.currentCurrencyLocator));
        currentProductPrices = mp.getCurrentProductPrices();

        for(WebElement element : currentProductPrices){
            Assert.assertEquals(currentCurrency,mp.getLastChar(element));
        }
    }

    @Test
    public void testDressSearch() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";

        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        dp = mp.dressSearch();

        String title = "Товаров: "+dp.getCurrentProducts().size()+".";
        Assert.assertEquals(title,dp.getSearchTitle());
    }

    @Test
    public void testDressPageCurrency() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";

        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        dp = mp.dressSearch();
        currentCurrency = dp.getLastChar(dp.getDriver().findElement(mp.currentCurrencyLocator));
        currentProductPrices = dp.getCurrentProductPrices();

        for(WebElement element : currentProductPrices){
            Assert.assertEquals(currentCurrency,dp.getLastChar(element));
        }
    }

    @Test
    public void testSortingChangeHToL() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";

        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        dp = mp.dressSearch();
        dp.setNewSorting(PODressSearch.priceHToL);
        String newTitle = "Цене: от высокой к низкой";
        (new WebDriverWait(dp.getDriver(), 10)).until(ExpectedConditions.
                attributeToBe(dp.itemPriceHToLLocator, "class", "select-list current js-search-link"));

        Assert.assertEquals(true, dp.getDriver().findElement(dp.sortingDropDownLocator).getText().contains(newTitle));
    }


    @Test
    public void testSortingAccuracy() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";

        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        dp = mp.dressSearch();
        dp.setNewSorting(PODressSearch.priceHToL);
        (new WebDriverWait(dp.getDriver(), 10)).until(ExpectedConditions.
                attributeToBe(dp.itemPriceHToLLocator, "class", "select-list current js-search-link"));

        double currVal;
        double pastVal=0;
        //int i = 1;
        List<WebElement> e = dp.getCurrentProducts();

        for(int i = e.size()-1;i>=0;i--){
            WebElement element = e.get(i);
            try {
                currVal = dp.getPriceNumbers(element.findElement(dp.regularPriceLocator));
            }catch (NoSuchElementException exc){
                currVal = dp.getPriceNumbers(element.findElement(dp.finalPriceLocator));
            }
            Assert.assertTrue(currVal>= pastVal,"currVal= "+currVal+" pastVal= " + pastVal);
            pastVal = currVal;
        }
    }

    @Test
    public void testDiscountFields() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";

        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        dp = mp.dressSearch();

        for(WebElement element : dp.getCurrentProducts()){
            try{
                if ('%' == dp.getLastChar(element.findElement(dp.discountPercentageLocator))){
                    dp.getPriceNumbers(element.findElement(dp.discountPercentageLocator));
                    dp.getPriceNumbers(element.findElement(dp.regularPriceLocator));
                    dp.getPriceNumbers(element.findElement(dp.finalPriceLocator));
                }
            }catch (NoSuchElementException e){}

        }
    }


    @Test
    public void testDiscountAccuracy() throws Exception{
        adressM = "http://prestashop-automation.qatestlab.com.ua/ru/";

        driver.get(adressM);
        mp = new POMainPage(driver);
        mp.setNewCurrency(BasicPage.USD);
        dp = mp.dressSearch();

        for(WebElement element : dp.getCurrentProducts()){
            try{
                if ('%' == dp.getLastChar(element.findElement(dp.discountPercentageLocator))){
                    double myDiscPrice = (1-dp.getPercentageFraction(element.findElement(dp.discountPercentageLocator)))*
                            dp.getPriceNumbers(element.findElement(dp.regularPriceLocator));

                    Assert.assertTrue(Math.abs(myDiscPrice-dp.getPriceNumbers(element.findElement(dp.finalPriceLocator)))<0.01);
                }
            }catch (NoSuchElementException e){}

        }

    }



   /* @Test
    public void testCurrencies() throws Exception{
        List<WebElement> currentProductPrices = mp.getCurrentProductPrices();
        char currentCurrency = mp.getLastChar(mp.getDriver().findElement(mp.currentCurrencyLocator));

        for(WebElement element : currentProductPrices){
            Assert.assertEquals(currentCurrency,mp.getLastChar(element));
        }
    }*/
}
