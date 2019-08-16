package testprog.realpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import testprog.helpstuff.BasicPage;

public class POMainPage extends BasicPage{
    private  String quote = "dress";

    final String mainPageUrl = "http://prestashop-automation.qatestlab.com.ua/ru/";


    public POMainPage(WebDriver driver){
        super(driver);
        if (!driver.getCurrentUrl().equals(mainPageUrl)) {
            throw new IllegalStateException("This is not the main page");
        }

    }

    //Осуществляет поиск слова "dress" в поисковом окне.
    public PODressSearch dressSearch(){
        getDriver().findElement(searchLocator).sendKeys(quote);
        getDriver().findElement(searchLocator).submit();
        return new PODressSearch(getDriver());
    }


}