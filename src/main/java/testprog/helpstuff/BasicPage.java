package testprog.helpstuff;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

//Шаблон для базовой страницы, которая отображает список продуктов и имеет верхнюю панель для выбора валюты, языка и тд
//
//
public class BasicPage {

    private WebDriver driver;

    public static final int USD = 0;
    public static final int UAH = 1;
    public static final int EUR = 2;

    public final By productLocator = By.xpath("//div[@class='product-price-and-shipping']");
    public final By productPriceLocator =  By.xpath("//span[@class = 'price']");
    public final By finalPriceLocator = By.xpath("span[@class='price']");
    public final By regularPriceLocator = By.xpath("span[@class='regular-price']");
    public final By discountPercentageLocator = By.xpath("span[@class='discount-percentage']");

    public final By searchLocator = By.xpath("//input[@name='s']");
    public final By currentCurrencyLocator = By.xpath("//span[@class='expand-more _gray-darker hidden-sm-down']");
    public final By itemUSDLocator =By.xpath("//a[@title='Доллар США']");
    public final By itemUAHLocator =By.xpath("//a[@title='Украинская гривна']");
    public final By itemEURLocator = By.xpath("//a[@title='Евро']");

    /*public final By itemUSDLocator =By.xpath("//a[contains(text(),'$')]");
    public final By itemUAHLocator =By.xpath("//a[contains(text(),'₴')]");
    public final By itemEURLocator = By.xpath("//a[contains(text(),'€')]");*/

    public BasicPage(WebDriver driver){
        setDriver(driver);
    }

    //Возвращает WebDriver этой страницы
    public WebDriver getDriver(){return driver;}

    //Устанавливает WebDriver этой страницы
    public void setDriver(WebDriver driver){this.driver = driver;}

    //Возвращает список WebElement содержащих текущую цену для каждого товара на странице
    public List<WebElement> getCurrentProductPrices(){
        return getDriver().findElements(productPriceLocator);
    }

    //Возвращает список WebElement являющихся всеми продуктами на странице
    public List<WebElement> getCurrentProducts(){return getDriver().findElements(productLocator);}

    //Возвращает последний знак из текстового поля(процент, валюта)
    public char getLastChar(WebElement element){
        return element.getText().charAt(element.getText().length()-1);
    }

    //Переводит текстовое значение скидки //span[@class='discount-percentage'] в double дробь <1 удобную для вычислений
    public double getPercentageFraction(WebElement element){
        return Double.parseDouble(element.getText().substring(1,element.getText().length()-1).replace(',','.'))/100;
    }

    //Переводит текстовое значение стоимости в double
    public double getPriceNumbers(WebElement element){
        return Double.parseDouble(element.getText().substring(0,element.getText().length()-1).replace(',','.'));

    }

    //Выбирает новую валюту в верхней панели
    public BasicPage setNewCurrency(int a) throws NoSuchElementException{
        //Отслеживает прогрузилась ли верхняя панель. Метод вернет null если нет
        try{
            getDriver().findElement(currentCurrencyLocator).click();}
        catch (NoSuchElementException e){
            System.out.println("Change currency Error. The top panel did not appear, change of currency to failed");
            return null;}
        switch (a){
            case BasicPage.USD:
                //driver.findElement(currentCurrencyLocator);
                (new WebDriverWait(getDriver(),10)).until(ExpectedConditions.visibilityOfElementLocated(itemUSDLocator)).click();
                break;
            case BasicPage.UAH:
                //driver.findElement(currentCurrencyLocator);
                (new WebDriverWait(getDriver(),10)).until(ExpectedConditions.visibilityOfElementLocated(itemUAHLocator)).click();
                break;
            case BasicPage.EUR:
                //driver.findElement(currentCurrencyLocator);
                (new WebDriverWait(getDriver(),10)).until(ExpectedConditions.visibilityOfElementLocated(itemEURLocator)).click();
                break;
        }
        return this;
    }

}