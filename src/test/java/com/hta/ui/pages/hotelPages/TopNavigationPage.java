package com.hta.ui.pages.hotelPages;

import com.hta.base.TestBase;
import com.hta.utils.logging.ErrorHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TopNavigationPage extends TestBase {

    private final WebDriver driver;

    By welcomeText = By.xpath("//td[@class='welcome_menu' and contains(text(),'Welcome to Adactin Group of Hotels')]");
    By welcomeUserText = By.cssSelector("#username_show");
    By searchHotelLink = By.xpath("//a[contains(text(), 'Search Hotel')]");
    By bookedItineraryLink = By.xpath("//a[contains(text(), 'Booked Itinerary')]");
    By changePasswordLink = By.xpath("//a[contains(text(), 'Change Password')]");
    By logoutLink = By.xpath("//a[contains(text(), 'Logout')]");

    public TopNavigationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyWelcomeTextDisplayed() {
        try{
            waitForElementToBeVisible(driver.findElement(welcomeText), "Welcome Text");
        } catch (Exception error){
            ErrorHandler.logError(error, "isWelcomeTextDisplayed", "Failed to find welcome text displayed");
            throw error;
        }
    }

    public void verifyWelcomeTextMessage(String expectedText){
        try{
            doesElementContainText(driver.findElement(welcomeText), expectedText, "Welcome Text");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyWelcomeTextMessage", "Failed to find welcome text displayed");
            throw error;
        }
    }

    public void verifyWelcomeUserTextDisplayed() {
        try{
            waitForElementToBeVisible(driver.findElement(welcomeUserText), "Welcome User");
        } catch (Exception error){
            ErrorHandler.logError(error, "isWelcomeUserTextDisplayed", "Failed to find welcome text displayed");
            throw error;
        }
    }

    public void verifySearchHotelLinkDisplayed() {
        try{
            waitForElementToBeVisible(driver.findElement(searchHotelLink), "Search Hotel");
        } catch (Exception error){
            ErrorHandler.logError(error, "isSearchHotelLinkDisplayed", "Failed to find search hotel link");
            throw error;
        }
    }

    public void verifyBookedItineraryLinkDisplayed() {
        try{
            waitForElementToBeVisible(driver.findElement(bookedItineraryLink), "Booked Itinerary");
        } catch (Exception error){
            ErrorHandler.logError(error, "isBookedItineraryLinkDisplayed", "Failed to find booked itinerary link");
            throw error;
        }
    }

    public void verifyChangePasswordLinkDisplayed() {
        try{
            waitForElementToBeVisible(driver.findElement(changePasswordLink), "Change Password");
        } catch (Exception error){
            ErrorHandler.logError(error, "isChangePasswordLinkDisplayed", "Failed to find change password link");
            throw error;
        }
    }

    public void verifyLogoutLinkDisplayed() {
        try{
            waitForElementToBeVisible(driver.findElement(logoutLink), "Logout");
        } catch (Exception error){
            ErrorHandler.logError(error, "isLogoutLinkDisplayed", "Failed to find logout link");
            throw error;
        }
    }

    public void verifyAllNavigationsMenusAreDisplayed(String expectedText) {
        try{
            verifyWelcomeTextDisplayed();
            verifyWelcomeTextMessage(expectedText);
            verifyWelcomeUserTextDisplayed();
            verifySearchHotelLinkDisplayed();
            verifyBookedItineraryLinkDisplayed();
            verifyChangePasswordLinkDisplayed();
            verifyLogoutLinkDisplayed();
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyAllNavigationsMenusAreDisplayed", "Failed to find all navigations");
            throw error;
        }
    }
}
