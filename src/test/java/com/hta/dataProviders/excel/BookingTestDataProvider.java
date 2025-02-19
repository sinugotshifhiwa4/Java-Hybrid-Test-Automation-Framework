package com.hta.dataProviders.excel;

import com.hta.config.paths.ExcelDataFilePaths;
import org.testng.annotations.DataProvider;

import java.util.Iterator;

public class BookingTestDataProvider {

    private static final String BOOKING_DATA_PATH = ExcelDataFilePaths.BOOKING.getFullPath();
    private static final String BOOKING_SHEET = "Booking";
    private static final String PAYMENTS_SHEETS = "Payments";


    /**
     * Provides booking details test data.
     *
     * @return Iterator of objects containing booking details: location, hotels, and room types
     */
    @DataProvider(name = "BookingDetails")
    public static Iterator<Object[]> getBookingDetails() {
        return ExcelTestDataProvider.getMultiColumnData(BOOKING_DATA_PATH, BOOKING_SHEET,
                "Location", "Hotels", "RoomTypes");
    }

    /**
     * Provides location test data.
     *
     * @return Iterator of objects containing hotel locations
     */
    @DataProvider(name = "LocationData")
    public static Iterator<Object[]> getLocationData() {
        return ExcelTestDataProvider.getColumnData(BOOKING_DATA_PATH, BOOKING_SHEET, "Location");
    }

    @DataProvider(name = "HotelData")
    public static Iterator<Object[]> getHotelsData() {
        return ExcelTestDataProvider.getColumnData(BOOKING_DATA_PATH, BOOKING_SHEET, "Hotels");
    }

    /**
     * Provides room type test data.
     *
     * @return Iterator of objects containing room types
     */
    @DataProvider(name = "RoomTypeData")
    public static Iterator<Object[]> getRoomTypeData() {
        return ExcelTestDataProvider.getColumnData(BOOKING_DATA_PATH, BOOKING_SHEET, "RoomTypes");
    }

    @DataProvider(name = "NumberOfRoomsData")
    public static Iterator<Object[]> getNumberOfRoomsData() {
        return ExcelTestDataProvider.getColumnData(BOOKING_DATA_PATH, BOOKING_SHEET, "NumberOfRooms");
    }

    @DataProvider(name = "AdultsPerRoomData")
    public static Iterator<Object[]> getAdultsPerRoomData() {
        return ExcelTestDataProvider.getColumnData(BOOKING_DATA_PATH, BOOKING_SHEET, "ChildrenPerRoom");
    }

    @DataProvider(name = "ChildrenPerRoomData")
    public static Iterator<Object[]> getChildrenPerRoomData() {
        return ExcelTestDataProvider.getColumnData(BOOKING_DATA_PATH, BOOKING_SHEET, "ChildrenPerRoom");
    }


    /**
     * Provides ExpiryYear test data for a specific index.
     *
     * @return Object array containing ExpiryYear data for the specified index
     */
    @DataProvider(name = "YearTestDataByIndex")
    public static Object[][] getPaymentsDataByIndex() {
    return ExcelTestDataProvider.getValueByIndex(BOOKING_DATA_PATH, PAYMENTS_SHEETS, "ExpiryYear", 7);

    }
}
