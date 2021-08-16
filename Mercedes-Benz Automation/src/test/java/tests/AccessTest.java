package tests;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/* To run the test you must: download the ChromeDriver from https://chromedriver.chromium.org/
* Use an IDE that you can add the selenium library
* In this project I used IntelliJ as IDE
* Add the chromeDriver folder (replace the one that is showed bellow)
* Replace all the directories showed bellow
* Add Selenium and Junit libraries
* To run the script you must click on "Run Access Test" (green button on your left)
* If you need to add a new library the IDE (IntelliJ) will add automatically
*
* Hope you enjoy
* */

public class AccessTest {

    String fileDir = "C:\\Users\\kamis\\Desktop\\Mercedes-Benz Automation\\";
    String driverDir = "C:\\Users\\kamis\\Desktop\\New_ChromeDriver\\";

    @Test
    public void testAccessWebSite() throws Exception {
        System.setProperty("webdriver.chrome.driver",driverDir + "chromedriver.exe");

        //Open Chrome Browser, maximized, pointing to the desired page
        WebDriver navegator = new ChromeDriver();
        navegator.manage().window().maximize();
        navegator.get("https://www.mercedes-benz.co.uk");
        Thread.sleep(5000);

        //Accepting the cookies, "Accept All"
        By cookies_accept = By.xpath("//div[@class='toggle-buttons-wrapper']//button[@type='button'][normalize-space()='Agree to all']");
        WebDriverWait wait = new WebDriverWait(navegator,10);
        wait.until(ExpectedConditions.elementToBeClickable(cookies_accept)).click();

        //Select "Our Cars" to show the next option we desire to select
        By our_cars_access = By.xpath("//a[normalize-space()='Our Cars']");
        WebDriverWait waitOurAccess = new WebDriverWait(navegator,10);
        wait.until(ExpectedConditions.elementToBeClickable(our_cars_access)).click();

        //Click on the "All Cars" option
        By all_cars_access = By.xpath("//div[@class='aem--headerMainNavigation__navLevel aem--headerMainNavigation__navSecond aem--ext-show aem--ext-active']//a[@class='aem--headerMainNavigation__navEntryLink'][normalize-space()='All cars']");
        WebDriverWait waitAllAccess = new WebDriverWait(navegator,10);
        wait.until(ExpectedConditions.elementToBeClickable(all_cars_access)).click();
        Thread.sleep(5000);

        //Hard coded link, due to not being able to "click" on the element for the Hatchbacks. Tried to troubleshoot with no success. Hard coded to be able to test the following steps
        navegator.navigate().to("https://www.mercedes-benz.co.uk/passengercars.html?group=all&subgroup=all.BODYTYPE.hatchback&view=BODYTYPE#modeltabs");
        Thread.sleep(5000);
        navegator.navigate().to("https://www.mercedes-benz.co.uk/passengercars/mercedes-benz-cars/car-configurator.html/motorization/CCci/GB/en/A-KLASSE/KOMPAKT-LIMOUSINE");
        Thread.sleep(5000);


        //scroll down so the location where the information required next is visible
        JavascriptExecutor jsScroll = (JavascriptExecutor) navegator;
        jsScroll.executeScript("window.scrollBy(0,1000)");

        Thread.sleep(5000);

        //check the Diesel checkbox
        WebElement checkBoxDiesel = navegator.findElement(By.xpath("//label[normalize-space()='Diesel']"));
        checkBoxDiesel.click();

        Thread.sleep(5000);

        //take the desired snapshot
        takeSnapShot(navegator,fileDir + "SC_Mercedes.png");

        Thread.sleep(5000);

        //collect the information from the first listed (cheapest)
        String lowestPrice = navegator.findElement(By.xpath("//body/main[@role='main']/div/div[@id='owcc-cont']/div[@class='owcc']/owcc[@app-instance-id='owcc']/cc-app-root[@dir='ltr']/div[@class='cc-app']/cc-app-container/div/div[@class='cc-app-container__main']/div[@class='cc-app-container__main-frame cc-grid-container']/div[@class='cc-grid-container ng-star-inserted']/div[@class='cc-app-container__content cc-grid-container']/div[@class='cc-app-container__content-selectables-container']/cc-motorization[@class='ng-star-inserted']/cc-motorization-comparison/div[@class='cc-motorization-comparison ng-star-inserted']/cc-slave-slider[@class='cc-slider--use-grid']/div[@class='cc-slider']/div[@class='cc-slider__viewport']/div[@class='cc-slider__slides']/cc-slave-slide[1]/div[1]/cc-motorization-header[1]/div[1]/div[1]")).getText();

        //shift page view to get the last listed var information
        By get_last_car = By.xpath("//div[@class='cc-slider-buttons cc-slider-buttons--position-crossing cc-slider-buttons--size-large cc-slider-buttons--design-circle']//button[@class='cc-slider-buttons__button--left ng-star-inserted']//div[@class='cc-slider-buttons__icon']");
        WebDriverWait waitForLastCar = new WebDriverWait(navegator,10);
        wait.until(ExpectedConditions.elementToBeClickable(get_last_car)).click();

        Thread.sleep(5000);

        //collect the information from the last listed (highest)
        String highestPrice = navegator.findElement(By.xpath("//body/main[@role='main']/div/div[@id='owcc-cont']/div[@class='owcc']/owcc[@app-instance-id='owcc']/cc-app-root[@dir='ltr']/div[@class='cc-app']/cc-app-container/div/div[@class='cc-app-container__main']/div[@class='cc-app-container__main-frame cc-grid-container']/div[@class='cc-grid-container ng-star-inserted']/div[@class='cc-app-container__content cc-grid-container']/div[@class='cc-app-container__content-selectables-container']/cc-motorization[@class='ng-star-inserted']/cc-motorization-comparison/div[@class='cc-motorization-comparison ng-star-inserted']/cc-slave-slider[@class='cc-slider--use-grid']/div[@class='cc-slider']/div[@class='cc-slider__viewport']/div[@class='cc-slider__slides']/cc-slave-slide[7]/div[1]/cc-motorization-header[1]/div[1]/div[1]")).getText();

        //write the collect prices, and the associated car names, in a file
        writeStrings(lowestPrice, highestPrice);

        Thread.sleep(5000);

        //close navigator
        navegator.close();

    }

    private void writeStrings(String str, String str2) throws IOException {
        File prices = new File(fileDir + "Prices.txt");

        FileWriter fw = new FileWriter(prices);
        fw.write(str);
        fw.write("\n");
        fw.write(str2);

        fw.close();
    }

    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception {
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(SrcFile, DestFile);
    }



}
