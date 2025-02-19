package com.hta.tests.ui.hotelTests;

import com.hta.base.TestBase;
import com.hta.config.retry.TestRetryAnalyzer;
import com.hta.dataProviders.excel.BookingTestDataProvider;
import com.hta.utils.DateGeneratorUtils;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.List;

public class SearchHotelTests extends TestBase {

    private static final Logger logger = LoggerUtils.getLogger(SearchHotelTests.class);
    private static final String LOCATION_SECTION = "Locations";
    private static final String HOTEL_SECTION = "Hotels";
    private static final String ROOM_TYPE_SECTION = "RoomTypes";
    private static final String NUMBER_OF_ROOMS_SECTION = "NumberOfRooms";
    private static final String ADULTS_PER_ROOM_ROOM_SECTION = "AdultsPerRoom";
    private static final String CHILDREN_PER_ROOM_SECTION = "ChildrenPerRoom";

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void searchHotel(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.searchForHotel(
                    bookingDataReader.getStringByIndex(LOCATION_SECTION, 4),
                    bookingDataReader.getStringByIndex(HOTEL_SECTION, 1),
                    bookingDataReader.getStringByIndex(ROOM_TYPE_SECTION, 2),
                    bookingDataReader.getStringByIndex(NUMBER_OF_ROOMS_SECTION, 5),
                    DateGeneratorUtils.getCurrentDate(),
                    DateGeneratorUtils.getDateAfterTenDays(),
                    bookingDataReader.getStringByIndex(ADULTS_PER_ROOM_ROOM_SECTION, 1),
                    bookingDataReader.getStringByIndex(CHILDREN_PER_ROOM_SECTION, 1)
            );
            captureScreenshot("Hotel-Search-Filled-Data");
            searchHotelPage.clickSearchButton();
            searchHotelPage.verifyLocationErrorMessageNotDisplayed();
            searchHotelPage.verifyCheckInDateErrorMessageNotDisplayed();
            searchHotelPage.verifyCheckOutDateErrorMessageNotDisplayed();
            selectHotelPage.verifyHotelSelectionRadioButtonZeroDisplayed();
            logger.info("Hotel search completed");
        } catch (Exception error){
            ErrorHandler.logError(error, "searchHotel","Failed to search for hotel");
            throw error;
        }
    }


    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifySearchFailsWithoutHotelSelection(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.verifySearchHotelHeaderDisplayed();
            searchHotelPage.clickSearchButton();
            searchHotelPage.verifyLocationErrorMessageDisplayed();
            logger.info("Search Hotel fails without selecting location");
        } catch (Exception error){
            ErrorHandler.logError(error, "verifySearchFailsWithoutHotelSelection","Failed to validate required element");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class, dataProvider = "LocationData", dataProviderClass = BookingTestDataProvider.class)
    public void verifyHotelSearchDisplaysResultsForLocation(String location) {
        try {
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.selectLocationDropdown(location);
            searchHotelPage.clickSearchButton();
            searchHotelPage.verifyLocationErrorMessageNotDisplayed();
            selectHotelPage.areAllRadioButtonsDisplayed();
            logger.info("Search Hotel successfully completed for location: {}", location);
        } catch (Exception error) {
            ErrorHandler.logError(error, "verifyHotelSearchDisplaysResultsForLocation", "Failed to search for hotel in location: " + location);
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class, dataProvider = "HotelData", dataProviderClass = BookingTestDataProvider.class)
    public void verifyHotelSelection(String hotel){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.selectHotelsDropdown(hotel);
            logger.info("Search Hotel successfully completed for hotel: {}", hotel);

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyHotelSelection","Failed to search for hotels");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class, dataProvider = "RoomTypeData", dataProviderClass = BookingTestDataProvider.class)
    public void verifyRoomTypeSelection(String roomType){
        try{
            // Login2
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.selectRoomTypeDropdown(roomType);
            logger.info("Search Hotel successfully completed for room type: {}", roomType);

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyRoomTypeSelection","Failed to search for room type");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, dataProvider = "NumberOfRoomsData", dataProviderClass = BookingTestDataProvider.class)
    public void verifyNumberOfRoomsSelection(String numberOfRooms) {
        try {
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.selectNumberOfRooms(numberOfRooms);
            logger.info("Search Hotel successfully completed for number of rooms: {}", numberOfRooms);

        } catch (Exception error) {
            ErrorHandler.logError(error, "verifyNumberOfRoomsSelection", "Failed to search for number of rooms");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifyUserCanFIllCheckInDate(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.fillCheckInDateInput(DateGeneratorUtils.getCurrentDate());
            logger.info("Check In Date filled successfully");

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyUserCanFIllCheckInDate", "Failed to fill check in date");
            throw error;
        }

    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifyCheckInDateCannotBeInPast(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.fillCheckInDateInput(DateGeneratorUtils.getTwoDaysBeforeCurrentDate());
            DateGeneratorUtils.validateCheckInDate(DateGeneratorUtils.getTwoDaysBeforeCurrentDate(), DateGeneratorUtils.getCurrentDate());
            logger.info("Check In Date validated  successfully");

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyCheckInDateCannotBeInPast", "Failed to validate date cant be in past");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifyUserCanFIllCheckOutDate(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.fillCheckOutDateInput(DateGeneratorUtils.getDateAfterSixDays());
            logger.info("Check Out Date filled successfully");

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyUserCanFIllCheckInDate", "Failed to fill check in date");
            throw error;
        }

    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifyCheckOutDateCannotBeBeforeCheckInDate(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.fillCheckInDateInput(DateGeneratorUtils.getCurrentDate());
            searchHotelPage.fillCheckOutDateInput(DateGeneratorUtils.getTwoDaysBeforeCurrentDate());
            searchHotelPage.clickSearchButton();
            searchHotelPage.verifyCheckInDateErrorMessageDisplayed();
            searchHotelPage.verifyCheckOutDateErrorMessageIsDisplayed();
            logger.info("Check In Date and Check Out Date validated successfully");

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyUserCanFIllCheckInDate", "Failed to fill check in date");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class, dataProvider = "AdultsPerRoomData", dataProviderClass = BookingTestDataProvider.class)
    public void verifyAdultsPerRoomSelection(String adultsPerRoom){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.selectAdultsDropdown(adultsPerRoom);
            logger.info("Search Hotel successfully completed selection  for adults per room: {}", adultsPerRoom);

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyAdultsPerRoomSelection","Failed to search for adults per room");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class, dataProvider = "ChildrenPerRoomData", dataProviderClass = BookingTestDataProvider.class)
    public void verifyChildrenPerRoomSelection(String childrenPerRoom){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.selectChildrenDropdown(childrenPerRoom);
            logger.info("Search Hotel successfully completed selection  for children per room: {}", childrenPerRoom);

        } catch (Exception error){
            ErrorHandler.logError(error, "verifyChildrenPerRoomSelection","Failed to search for children per room");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifyHotelSearchFormCanBeFilledAndReset(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.searchForHotel(
                    bookingDataReader.getStringByIndex(LOCATION_SECTION, 4),
                    bookingDataReader.getStringByIndex(HOTEL_SECTION, 1),
                    bookingDataReader.getStringByIndex(ROOM_TYPE_SECTION, 2),
                    bookingDataReader.getStringByIndex(NUMBER_OF_ROOMS_SECTION, 5),
                    DateGeneratorUtils.getCurrentDate(),
                    DateGeneratorUtils.getDateAfterTenDays(),
                    bookingDataReader.getStringByIndex(ADULTS_PER_ROOM_ROOM_SECTION, 1),
                    bookingDataReader.getStringByIndex(CHILDREN_PER_ROOM_SECTION, 1)
            );
            searchHotelPage.clickResetButton();

            // Verify no dropdown option is selected
            List<String> locations = bookingDataReader.getAllStrings(LOCATION_SECTION);
            List<String> hotels = bookingDataReader.getAllStrings(HOTEL_SECTION);
            List<String> roomTypes = bookingDataReader.getAllStrings(ROOM_TYPE_SECTION);

            searchHotelPage.verifyLocationsDropdownOptionIsNotSelected(locations);
            searchHotelPage.verifyHotelsDropdownOptionIsNotSelected(hotels);
            searchHotelPage.verifyRoomTypesDropdownOptionIsNotSelected(roomTypes);

            captureScreenshot("Hotel-Search-Reset-Data");

            logger.info("Hotel Reset Data completed");
        } catch (Exception error){
            ErrorHandler.logError(error, "searchHotel","Failed to search for hotel");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void verifyCancellationOfSearchHotel(){
        try{
            // Login
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();

            // Search Hotel
            searchHotelPage.searchForHotel(
                    bookingDataReader.getStringByIndex(LOCATION_SECTION, 4),
                    bookingDataReader.getStringByIndex(HOTEL_SECTION, 1),
                    bookingDataReader.getStringByIndex(ROOM_TYPE_SECTION, 2),
                    bookingDataReader.getStringByIndex(NUMBER_OF_ROOMS_SECTION, 5),
                    DateGeneratorUtils.getCurrentDate(),
                    DateGeneratorUtils.getDateAfterTenDays(),
                    bookingDataReader.getStringByIndex(ADULTS_PER_ROOM_ROOM_SECTION, 1),
                    bookingDataReader.getStringByIndex(CHILDREN_PER_ROOM_SECTION, 1)
            );
            captureScreenshot("Hotel-Search-Filled-Data");
            searchHotelPage.clickSearchButton();
            searchHotelPage.verifyLocationErrorMessageNotDisplayed();
            searchHotelPage.verifyCheckInDateErrorMessageNotDisplayed();
            searchHotelPage.verifyCheckOutDateErrorMessageNotDisplayed();
            selectHotelPage.verifyHotelSelectionRadioButtonZeroDisplayed();
            selectHotelPage.clickCancelButton();
            searchHotelPage.verifySearchHotelHeaderDisplayed();
            logger.info("Hotel search cancellation validation completed");
        } catch (Exception error){
            ErrorHandler.logError(error, "searchHotel","Failed to search for hotel");
            throw error;
        }
    }
}
