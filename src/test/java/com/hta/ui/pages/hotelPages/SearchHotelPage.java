package com.hta.ui.pages.hotelPages;

import com.hta.base.TestBase;
import com.hta.config.DropdownSelector;
import com.hta.utils.logging.ErrorHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class SearchHotelPage extends TestBase {

    private final WebDriver driver;

    By searchHotelHeader = By.xpath("//td[@class='login_title' and contains(.,'Search Hotel ')]");
    By locationDropdown = By.cssSelector("#location");
    By locationErrorMessage = By.cssSelector("#location_span");
    By hotelsDropdown = By.cssSelector("#hotels");
    By roomTypeDropdown = By.cssSelector("#room_type");
    By numberOfRoomsDropdown = By.cssSelector("#room_nos");
    By checkInDateInput = By.cssSelector("#datepick_in");
    By checkOutDateInput = By.cssSelector("#datepick_out");
    By checkInDateErrorMessage = By.cssSelector("#checkin_span");
    By checkOutDateErrorMessage = By.cssSelector("#checkout_span");
    By adultsPerRoomDropdown = By.cssSelector("#adult_room");
    By childrenPerRoomDropdown = By.cssSelector("#child_room");
    By searchButton = By.cssSelector("#Submit");
    By resetButton = By.cssSelector("#Reset");

    public SearchHotelPage(WebDriver driver) {
        this.driver = driver;
    }

    public void verifySearchHotelHeaderDisplayed(){
        try{
            waitForElementToBeVisible(driver.findElement(searchHotelHeader), "Search Hotel Header");
        } catch (Exception error){
            ErrorHandler.logError(error, "isSearchHotelHeaderDisplayed", "Failed to find Search Hotel Header");
            throw error;
        }
    }

    public void selectLocationDropdown(String location){
        try{
            selectDropdownElement(driver.findElement(locationDropdown), DropdownSelector.VISIBLE_TEXT.getSelector(), location, "Location");
        } catch (Exception error){
            ErrorHandler.logError(error, "selectLocationDropdown", "Failed to select Location Dropdown");
            throw error;
        }
    }

    public void verifyLocationErrorMessageDisplayed(){
        try{
            waitForElementToBeVisible(driver.findElement(locationErrorMessage), "Location Error Message");
        } catch (Exception error){
            ErrorHandler.logError(error, "isLocationErrorMessageDisplayed", "Failed to find Location Error Message");
            throw error;
        }
    }

    public void verifyLocationErrorMessageNotDisplayed(){
        try{
            waitForElementNotVisible(locationErrorMessage, "Location Error Message");
        } catch (Exception error){
            ErrorHandler.logError(error, "isLocationErrorMessageNotDisplayed", "Failed to validate absence of error message");
            throw error;
        }
    }

    public void selectHotelsDropdown(String hotel){
        try {
            selectDropdownElement(driver.findElement(hotelsDropdown), DropdownSelector.VISIBLE_TEXT.getSelector(), hotel, "Hotel");
        } catch (Exception error){
            ErrorHandler.logError(error, "selectHotelsDropdown", "Failed to select Hotel Dropdown");
            throw error;
        }
    }

    public void selectRoomTypeDropdown(String roomType){
        try {
            selectDropdownElement(driver.findElement(roomTypeDropdown), DropdownSelector.VISIBLE_TEXT.getSelector(), roomType, "Room Type");
        } catch (Exception error){
            ErrorHandler.logError(error, "selectRoomTypeDropdown", "Failed to select Room Type Dropdown");
            throw error;
        }
    }

    public void selectNumberOfRooms(String numberOfRooms){
        try {
            selectDropdownElement(driver.findElement(numberOfRoomsDropdown), DropdownSelector.VISIBLE_TEXT.getSelector(), numberOfRooms, "Number of Rooms");
        } catch (Exception error){
            ErrorHandler.logError(error, "selectNumberOfRooms", "Failed to select Number of Rooms");
            throw error;
        }
    }

    public void fillCheckInDateInput(String checkInDate){
        try {
            clearElement(driver.findElement(checkInDateInput), "Check In Date");
            sendKeys(driver.findElement(checkInDateInput), checkInDate, "Check In Date");
        } catch (Exception error){
            ErrorHandler.logError(error, "fillCheckInDateInput", "Failed to fill Check In Date");
            throw error;
        }
    }

    public void verifyCheckInDateErrorMessageDisplayed(){
        try {
            waitForElementToBeVisible(driver.findElement(checkInDateErrorMessage), "Check In Date Error Message");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyCheckInDateErrorMessageDisplayed", "Failed to find Check In Date ErrorMessage");
            throw error;
        }
    }

    public void verifyCheckInDateErrorMessageNotDisplayed(){
        try {
           waitForElementNotVisible(checkInDateErrorMessage, "Check In Date Error");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyCheckInDateErrorMessageNotDisplayed", "Failed to validate absence of error message");
            throw error;
        }
    }

    public void fillCheckOutDateInput(String checkOutDate){
        try {
            clearElement(driver.findElement(checkOutDateInput), "Check Out Date");
            sendKeys(driver.findElement(checkOutDateInput), checkOutDate, "Check Out Date");
        } catch (Exception error){
            ErrorHandler.logError(error, "fillCheckOutDateInput", "Failed to fill Check Out Date");
            throw error;
        }
    }

    public void verifyCheckOutDateErrorMessageIsDisplayed(){
        try {
            waitForElementToBeVisible(driver.findElement(checkOutDateErrorMessage), "Check Out Date Error Message");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyCheckOutDateErrorMessageIsDisplayed", "Failed to find Check Out Date ErrorMessage");
            throw error;
        }
    }

    public void verifyCheckOutDateErrorMessageNotDisplayed(){
        try {
            waitForElementNotVisible(checkOutDateErrorMessage, "Check Out Date Error");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyCheckOutDateErrorMessageNotDisplayed", "Failed to validate absence of error message");
            throw error;
        }
    }

    public void selectAdultsDropdown(String adults){
        try {
            selectDropdownElement(driver.findElement(adultsPerRoomDropdown), DropdownSelector.VISIBLE_TEXT.getSelector(), adults, "Adults Per Room");
        } catch (Exception error){
            ErrorHandler.logError(error, "selectAdultsDropdown", "Failed to select Adults Dropdown");
            throw error;
        }
    }

    public void selectChildrenDropdown(String children){
        try {
            selectDropdownElement(driver.findElement(childrenPerRoomDropdown), DropdownSelector.VISIBLE_TEXT.getSelector(), children, "Children Per Room");
        } catch (Exception error){
            ErrorHandler.logError(error, "selectChildrenDropdown", "Failed to select Children Dropdown");
            throw error;
        }
    }

    public void verifyLocationsDropdownOptionIsNotSelected(List<String> predefinedOptions){
        try {
            verifyNoDropdownOptionSelected(driver.findElement(locationDropdown), predefinedOptions, "Locations");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyLocationDropdownOptionIsNotSelected", "Failed to find Locations Dropdown Option");
            throw error;
        }
    }

    public void verifyHotelsDropdownOptionIsNotSelected(List<String> predefinedOptions){
        try {
            verifyNoDropdownOptionSelected(driver.findElement(hotelsDropdown), predefinedOptions, "Hotels");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyHotelDropdownOptionIsNotSelected", "Failed to find Hotels Dropdown Option");
            throw error;
        }
    }

    public void verifyRoomTypesDropdownOptionIsNotSelected(List<String> predefinedOptions){
        try {
            verifyNoDropdownOptionSelected(driver.findElement(roomTypeDropdown), predefinedOptions, "Room Types");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifyRoomTypeDropdownOptionIsNotSelected", "Failed to find Room Types Dropdown Option");
            throw error;
        }
    }

    public void clickSearchButton(){
        try{
            clickElement(driver.findElement(searchButton), "Search Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickSearchButton", "Failed to click search button");
            throw error;
        }
    }

    public void clickResetButton(){
        try {
            clickElement(driver.findElement(resetButton), "Reset Button");
        } catch (Exception error) {
            ErrorHandler.logError(error, "clickResetButton", "Failed to click reset Button");
            throw error;
        }
    }

    public void searchForHotel(
            String location,
            String hotel,
            String roomType,
            String numberOfRooms,
            String checkInDate,
            String checkOutDate,
            String adultsPerRoom,
            String childrenPerRoom
    ){
        try {
            selectLocationDropdown(location);
            selectHotelsDropdown(hotel);
            selectRoomTypeDropdown(roomType);
            selectNumberOfRooms(numberOfRooms);
            fillCheckInDateInput(checkInDate);
            fillCheckOutDateInput(checkOutDate);
            selectAdultsDropdown(adultsPerRoom);
            selectChildrenDropdown(childrenPerRoom);
        } catch (Exception error){
            ErrorHandler.logError(error, "searchForHotel", "Failed to search for Hotel");
            throw error;
        }
    }
}
