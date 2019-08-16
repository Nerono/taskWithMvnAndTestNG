package testprog.realpages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprog.helpstuff.Logger;

import java.io.IOException;

public class PODressSearch extends testprog.helpstuff.BasicPage {
    public static final int priceHToL=0;

    public final By productNumberLocator = By.xpath("//section[@id='products']//p");
    public final By sortingDropDownLocator = By.xpath("//a[@class='select-title']");
    public final By itemPriceHToLLocator = By.xpath("//div[@class='dropdown-menu']/a[contains(.,'от высокой')]");

    public PODressSearch(WebDriver driver){
        super(driver);
        if (!((driver.findElement(searchLocator).getAttribute("value")).equals("dress")) &&
                !driver.findElement(productNumberLocator).getText().contains("Товаров:")) {
            throw new IllegalStateException("This is not the \"dress\" search page");
        }
    }

    //Устанавливает сортировку от большей цены к меньшей
    public PODressSearch setNewSorting(int i){
        getDriver().findElement(sortingDropDownLocator).click();
        switch (i){
            case 0:
                (new WebDriverWait(getDriver(),10)).until(ExpectedConditions.visibilityOfElementLocated(itemPriceHToLLocator)).click();
                break;
        }
        return this;
    }

    //Возвращает число товаров указанное в строке "Товаров: x"
    public int getProductNumber(){
        return Integer.parseInt(getDriver().findElement(productNumberLocator).getText().substring("Товаров: ".length(),
                getDriver().findElement(productNumberLocator).getText().length()-1));
    }

    //Возвращает текст заголовка под полем "РЕЗУЛЬТАТЫ ПОИСКА"
    public String getSearchTitle(){
        return getDriver().findElement(productNumberLocator).getText();
    }

}