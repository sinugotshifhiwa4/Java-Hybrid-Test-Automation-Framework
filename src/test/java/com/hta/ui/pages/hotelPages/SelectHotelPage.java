package com.hta.ui.pages.hotelPages;

import com.hta.base.TestBase;
import com.hta.utils.logging.ErrorHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SelectHotelPage extends TestBase {

    private final WebDriver driver;

    By selectHotelHeader = By.xpath("//td[@class='login_title' and contains(.,'Select Hotel ')]");
    By hotelSelectionRadioButtonZero = By.cssSelector("#radiobutton_0");
    By hotelSelectionRadioButtonFirst = By.cssSelector("#radiobutton_1");
    By hotelSelectionRadioButtonSecond = By.cssSelector("#radiobutton_2");
    By hotelSelectionRadioButtonThird = By.cssSelector("#radiobutton_3");
    By hotelSelectionRadioButtonFourth = By.cssSelector("#radiobutton_4");
    By continueButton = By.cssSelector("#continue");
    By cancelButton = By.cssSelector("#cancel");

    public SelectHotelPage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifySelectHotelHeaderDisplayed() {
        try {
            waitForElementToBeVisible(driver.findElement(selectHotelHeader), "Select Hotel Header");
        } catch (Exception error) {
            ErrorHandler.logError(error, "isSelectHotelHeaderDisplayed", "Failed to wait for Hotel Header to be visible");
            throw error;
        }
    }

    public void verifyHotelSelectionRadioButtonZeroDisplayed(){
        try {
            waitForElementToBeVisible(driver.findElement(hotelSelectionRadioButtonZero), "Select Hotel Header");
        } catch (Exception error) {
            ErrorHandler.logError(error, "verifyHotelSelectionRadioButtonZeroDisplayed", "Failed to wait for Hotel Header to be visible");
            throw error;
        }
    }

    public void clickHotelSelectionRadioButtonZero(){
        try {
            clickElement(driver.findElement(hotelSelectionRadioButtonZero), "Select Hotel Zero Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickHotelSelectionRadioButtonZero", "Failed to wait for Hotel Header to be visible");
            throw error;
        }
    }

    public void verifyHotelSelectionRadioButtonFirstDisplayed() {
        try {
            waitForElementToBeVisible(driver.findElement(hotelSelectionRadioButtonFirst), "Select Hotel First Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "isHotelSelectionRadioButtonFirstDisplayed", "Failed to wait for Hotel Selection Radio Button to be visible");
            throw error;
        }
    }

    public void clickHotelSelectionRadioButtonFirst() {
        try {
            clickElement(driver.findElement(hotelSelectionRadioButtonFirst), "Select Hotel First Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickHotelSelectionRadioButtonFirst", "Failed to click Hotel First Radio Button");
            throw error;
        }
    }

    public void verifyHotelSelectionRadioButtonSecondDisplayed() {
        try {
            waitForElementToBeVisible(driver.findElement(hotelSelectionRadioButtonSecond), "Select Hotel Second Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "isHotelSelectionRadioButtonSecondDisplayed", "Failed to wait for Hotel Selection Second Radio Button to be visible");
            throw error;
        }
    }

    public void clickHotelSelectionRadioButtonSecond() {
        try {
            clickElement(driver.findElement(hotelSelectionRadioButtonSecond), "Select Hotel Second Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickHotelSelectionRadioButtonSecond", "Failed to click Hotel Second Radio Button");
            throw error;
        }
    }

    public void verifyHotelSelectionRadioButtonThirdDisplayed() {
        try {
            waitForElementToBeVisible(driver.findElement(hotelSelectionRadioButtonThird), "Select Hotel Third Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "isHotelSelectionRadioButtonThirdDisplayed", "Failed to wait for Hotel Selection Third Radio Button to be visible");
            throw error;
        }
    }

    public void clickHotelSelectionRadioButtonThird() {
        try {
            clickElement(driver.findElement(hotelSelectionRadioButtonThird), "Select Hotel Third Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickHotelSelectionRadioButtonThird", "Failed to click Hotel Third Radio Button");
            throw error;
        }
    }

    public void verifyHotelSelectionRadioButtonFourthDisplayed() {
        try {
            waitForElementToBeVisible(driver.findElement(hotelSelectionRadioButtonFourth), "Select Hotel Fourth Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "isHotelSelectionRadioButtonFourthDisplayed", "Failed to wait for Hotel Selection Fourth Radio Button to be visible");
            throw error;
        }
    }

    public void clickHotelSelectionRadioButtonFourth() {
        try {
            clickElement(driver.findElement(hotelSelectionRadioButtonFourth), "Select Hotel Fourth Radio Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickHotelSelectionRadioButtonFourth", "Failed to click Hotel Fourth Radio Button");
            throw error;
        }
    }

    public void areAllRadioButtonsDisplayed(){
        verifyHotelSelectionRadioButtonFirstDisplayed();
        verifyHotelSelectionRadioButtonSecondDisplayed();
        verifyHotelSelectionRadioButtonThirdDisplayed();
        verifyHotelSelectionRadioButtonFourthDisplayed();
    }

    public void clickContinueButton(){
        try {
            clickElement(driver.findElement(continueButton), "Continue Button");
        } catch (Exception error){
            ErrorHandler.logError(error, "clickContinueButton", "Failed to click Continue Button");
            throw error;
        }
    }

    public void clickCancelButton(){
        try {
            clickElement(driver.findElement(cancelButton), "Cancel Button");
        } catch (Exception error){
            ErrorHandler.logError(error, "clickCancelButton", "Failed to click Cancel Button");
            throw error;
        }
    }

}
