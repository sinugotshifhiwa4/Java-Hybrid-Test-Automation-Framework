package com.hta.dataProviders.json;

import com.hta.utils.jacksonUtils.JsonDataReader;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;

public class BookingJsonData {

    private static final Logger logger = LoggerUtils.getLogger(BookingJsonData.class);
    private final JsonDataReader bookingDataReader;

    // Constructor-based dependency injection
    public BookingJsonData(JsonDataReader reader) {
        this.bookingDataReader = reader;
    }

    @DataProvider(name = "locations")
    public Iterator<Object[]> provideHotelLocations() {
        return getDataList("Locations");
    }

    @DataProvider(name = "hotels")
    public Iterator<Object[]> provideHotels() {
        return getDataList("Hotels");
    }

    @DataProvider(name = "roomTypes")
    public Iterator<Object[]> provideRoomTypes() {
        return getDataList("RoomTypes");
    }

    @DataProvider(name = "numberOfRooms")
    public Iterator<Object[]> provideNumberOfRooms() {
        return getDataList("NumberOfRooms");
    }

    @DataProvider(name = "adultsPerRoom")
    public Iterator<Object[]> provideAdultsPerRoom() {
        return getDataList("AdultsPerRoom");
    }

    @DataProvider(name = "childrenPerRoom")
    public Iterator<Object[]> provideChildrenPerRoom() {
        return getDataList("ChildrenPerRoom");
    }

    private Iterator<Object[]> getDataList(String section) {
        if (bookingDataReader == null) {
            logger.error("bookingDataReader is not initialized. Ensure TestBase is properly set up.");
            throw new IllegalStateException("bookingDataReader is not initialized. Ensure TestBase is properly set up.");
        }
        List<String> dataList = bookingDataReader.getAllStrings(section);
        return dataList.stream().map(data -> new Object[]{data}).iterator();
    }
}
