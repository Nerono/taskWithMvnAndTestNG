package testprog.helpstuff;


//import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprog.realpages.PODressSearch;
import testprog.realpages.POMainPage;

import java.io.IOException;
import java.util.List;

public class TestsBasic {

    //Сравнивает валюту товаров в переданном списке с текущей валютой, выбранной в верхней панели
    public void compareCurrAndProdCurr(BasicPage bp, Logger l) throws IOException {
        List<WebElement> currentProductPrices = bp.getCurrentProductPrices();
        char currentCurrency = bp.getLastChar(bp.getDriver().findElement(bp.currentCurrencyLocator));
        (new WebDriverWait(bp.getDriver(),10)).until(ExpectedConditions.presenceOfElementLocated(bp.productPriceLocator));
        int i = 1;
        for(WebElement element : currentProductPrices){
            if(bp.getLastChar(element) == currentCurrency){
                l.logAndPrintLine("Валюта продукта #"+i+" совпадает с выбранной валютой - "+currentCurrency);
            }else{
                l.logAndPrintLine("Валюта продукта #"+i+" НЕ совпадает с выбранной валютой - "+currentCurrency);
            }
            i++;
        }
    }

    //Сравнивает число товаров, отображаемых на странице с числом товаров указанным в "Товаров: x"
    public void compareTitleNumWithPage(PODressSearch po, Logger l)throws IOException{
        if(po.getProductNumber() == po.getCurrentProductPrices().size()){
            l.logAndPrintLine("Количество товаров в заголовке \"Товаров: x.\" совпадает с числом товаров на странице");
        }else{
            l.logAndPrintLine("Количество товаров в заголовке \"Товаров: x.\" НЕ совпадает с числом товаров на странице");
            l.logAndPrintLine("Товаров на странице: "+po.getCurrentProductPrices().size()+
                    " Количество товаров указанное в заголовке: " + po.getProductNumber());
        }
    }

    //Проверяет, размещены ли товары на странице согласно выбранной сортировке(цена по убыванию)
    public void checkSortingAccuracy(PODressSearch po, Logger l) throws IOException{
        boolean checker = true;
        double currVal;
        double pastVal=0;
        int i = 1;
        for(WebElement element : po.getCurrentProducts()){
            try {
                currVal = po.getPriceNumbers(element.findElement(po.regularPriceLocator));
            }catch (NoSuchElementException e){
                currVal = po.getPriceNumbers(element.findElement(po.finalPriceLocator));
            }
            if(currVal>pastVal && i!=1){
                checker = false;
                l.logAndPrintLine("Ошибка при размещении товаров согласно сортировке \n" +
                        "Ошибка на товаре #"+i+" "+currVal + " "+pastVal);
            }
            pastVal = currVal;
            i++;
        }
        if(checker){l.logAndPrintLine("Все товары размещены согласно указанной сортировке");}

    }

    //Проверяет, что в поле показывающем скидку скидка указана в процентах и поля показывающие скидку, цену до скидки и
    //цену после скидки хранят значения типа double
    public void checkDiscountFields (PODressSearch po, WebElement element,int i,Logger l) throws NoSuchElementException,IOException{
        try{
            if ('%' == po.getLastChar(element.findElement(po.discountPercentageLocator))){
                po.getPriceNumbers(element.findElement(po.discountPercentageLocator));
                po.getPriceNumbers(element.findElement(po.regularPriceLocator));
                po.getPriceNumbers(element.findElement(po.finalPriceLocator));
                l.logAndPrintLine("Товар #"+i+": поля для скидки, цены до и цены после есть, формат значений полей верный");
            }

        }catch (NoSuchElementException e){throw e;}
        catch (NumberFormatException e){l.logAndPrintLine("Неправильное значение в одном из полей товара #"+i);}
    }

    //Проверяет совпадение скидки с ценами до и после нее. Также каждый продукт проверяется методом checkDiscountFields
    public void compareAllPrices(PODressSearch po, Logger l) throws IOException{
        int i =1;
        for(WebElement element: po.getDriver().findElements(po.productLocator)){
            try{
                double myDiscPrice = (1-po.getPercentageFraction(element.findElement(po.discountPercentageLocator)))*
                        po.getPriceNumbers(element.findElement(po.regularPriceLocator));
                double discPrice = po.getPriceNumbers(element.findElement(po.finalPriceLocator));
                checkDiscountFields(po,element,i,l);
                if(Math.abs(myDiscPrice-discPrice)<0.01){
                    System.out.println("Товар #"+i+": цена до и после скидки совпадает с размером скидки");
                }else {
                    l.logAndPrintLine("Товар #"+i+": имеет скидку. Цена до и после скидки НЕ совпадает с размером скидки. Правильная цена: "
                            + myDiscPrice+" Указанная цена: "+discPrice); }
            }catch (NoSuchElementException e){l.logAndPrintLine("Товар #"+i+": НЕ имеет скидку.");}
            finally { i++;}
        }
    }

    public POMainPage openMainPage(){

        /*WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();*/

        WebDriver driver;
        try{
            driver = new ChromeDriver();
        }catch (IllegalStateException e){
            System.setProperty("webdriver.chrome.driver","D:\\TestTools\\chromedriver\\chromedriver.exe");
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        return new POMainPage(driver);
    }
}