package com.hta.tests;

import com.hta.dataProviders.excel.BookingTestDataProvider;
import org.testng.annotations.Test;

public class DemoTests {

    @Test(dataProvider = "YearTestDataByIndex", dataProviderClass = BookingTestDataProvider.class)
    public void getExpiryYear(int expiryYear) {
        System.out.println("Test: " + expiryYear);
    }

    @Test(dataProvider = "RoomTypeTestData", dataProviderClass = BookingTestDataProvider.class)
    public void getRoomType(String roomType) {
        System.out.println("Test: " + roomType);
    }

    @Test(dataProvider = "LocationTestData", dataProviderClass = BookingTestDataProvider.class)
    public void getLocation(String location) {
        System.out.println(location);
    }

    @Test(dataProvider = "BookingDetails", dataProviderClass = BookingTestDataProvider.class)
    public void bookingDetails(String  location, String  hotels, String roomTypes){
        System.out.println(location);
        System.out.println(hotels);
        System.out.println(roomTypes);
    }

}
