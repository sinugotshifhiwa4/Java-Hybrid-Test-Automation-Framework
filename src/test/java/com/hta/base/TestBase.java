package com.hta.base;

import com.hta.config.environments.EnvironmentFileAlias;
import com.hta.config.environments.EnvironmentSecretKey;
import com.hta.config.paths.JsonDataFilePaths;
import com.hta.config.properties.PropertiesConfigManager;
import com.hta.config.properties.PropertiesFileAlias;
import com.hta.crypto.services.CryptoOperationsManager;
import com.hta.drivers.BrowserFactory;
import com.hta.drivers.DriverFactory;
import com.hta.testDataStorage.TestContextIds;
import com.hta.ui.pages.hotelPages.LoginPage;
import com.hta.ui.pages.hotelPages.SearchHotelPage;
import com.hta.ui.pages.hotelPages.SelectHotelPage;
import com.hta.ui.pages.hotelPages.TopNavigationPage;
import com.hta.utils.jacksonUtils.JsonDataReader;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.List;

import static com.hta.base.TestConfigInitializer.*;

public class TestBase extends BasePage {

    private static final Logger logger = LoggerUtils.getLogger(TestBase.class);

    // Global config parameters
    private static final String BROWSER = "CHROME_BROWSER";
    private static final String URL = "PORTAL_BASE_URL";

    // Running Mode
    private static final String HEADLESS_MODE = "--headless";

    // Test Data Ids
    private static final TestContextIds BOOKING_ID_ONE = TestContextIds.BOOKING_TEST_ID_ONE;

    // Get Driver Instance
    private final DriverFactory driverFactory = DriverFactory.getInstance();
    protected BrowserFactory browserFactory;

    // Json Test Data initialization variables
    protected JsonDataReader bookingDataReader;
    protected JsonDataReader paymentDataReader;

    // Pages
    protected LoginPage loginPage;
    protected TopNavigationPage topNavigationPage;
    protected SearchHotelPage searchHotelPage;
    protected SelectHotelPage selectHotelPage;
//    protected BookHotelPage bookHotelPage;
//    protected BookingConfirmationPage bookingConfirmationPage;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        try {
            initializeConfigurations();
            initializeJsonReaders();
            TestConfigInitializer.initializeTestContexts(BOOKING_ID_ONE);
            logger.info("Global setup completed successfully.");
        } catch (Exception error) {
            ErrorHandler.logError(error, "globalSetup", "Failed to initialize global setup");
            throw error;
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        try {
            // Skip browser initialization when running crypto operations
            skipBrowserInitializationIfNeeded();

            logger.info("Setup configured successfully");
        } catch (Exception error) {
            ErrorHandler.logError(error, "setup", "Failed to initialize setup");
            throw error;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            cleanUpTestContexts(BOOKING_ID_ONE);
            logger.info("Test tear-down completed successfully.");
        } catch (Exception error) {
            ErrorHandler.logError(error, "tearDown", "Failed to tear down");
            throw error;
        } finally {
            driverFactory.quitDriver();
        }
    }

    private void initializeJsonReaders() {
        bookingDataReader = createJsonReader(JsonDataFilePaths.BOOKING);
        paymentDataReader = createJsonReader(JsonDataFilePaths.PAYMENTS);
    }

    private void skipBrowserInitializationIfNeeded() {
        if (!Boolean.getBoolean("skipBrowserInitialization")) {
            initializeBrowserComponents();
        } else {
            logger.info("Skipping browser initialization for encryption tests.");
        }
    }

    private void initializePages(WebDriver driver) {
        loginPage = new LoginPage(driver);
        topNavigationPage = new TopNavigationPage(driver);
        searchHotelPage = new SearchHotelPage(driver);
        selectHotelPage = new SelectHotelPage(driver);
//        bookHotelPage = new BookHotelPage(driver);
//        bookingConfirmationPage = new BookingConfirmationPage(driver);
    }

    private void initializeBrowserComponents() {
        try {
            browserFactory = new BrowserFactory();

            String browser = PropertiesConfigManager.getPropertyKeyFromCache(
                    PropertiesFileAlias.GLOBAL.getConfigurationAlias(),
                    BROWSER);

            browserFactory.initializeBrowser(browser, HEADLESS_MODE);

            if (!driverFactory.hasDriver()) {
                String errorMessage = "WebDriver initialization failed for thread: " + Thread.currentThread().threadId();
                logger.error(errorMessage);
                throw new IllegalStateException(errorMessage);
            }

            initializePages(driverFactory.getDriver());

            String url = PropertiesConfigManager.getPropertyKeyFromCache(PropertiesFileAlias.UAT.getConfigurationAlias(), URL);

            driverFactory.navigateToUrl(url);
            validateLoginPage();
        } catch (Exception error) {
            ErrorHandler.logError(error, "initializeBrowserComponents", "Failed to initialize browser components");
            throw error;
        }
    }

    private void validateLoginPage() {
        loginPage.validateHeroImageLoadTime();
        loginPage.isCompanyLogoPresent();
    }

    public List<String> decryptCredentials() {
        try {
            return CryptoOperationsManager.decryptEnvironmentVariables(
                    EnvironmentFileAlias.UAT.getEnvironmentAlias(),
                    EnvironmentSecretKey.UAT.getKeyName(),
                    "PORTAL_USERNAME", "PORTAL_PASSWORD"
            );
        } catch (Exception error) {
            ErrorHandler.logError(error, "decryptCredentials", "Failed to decrypt credentials");
            throw error;
        }
    }
}
