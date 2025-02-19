package com.hta.drivers;

import com.hta.config.properties.PropertiesConfigManager;
import com.hta.config.properties.PropertiesFileAlias;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.hta.drivers.BrowserOptionsUtils.*;


public class SeleniumGridFactory {

    private static final Logger logger = LoggerUtils.getLogger(SeleniumGridFactory.class);
    private static final String SELENIUM_GRID_URL = "SELENIUM_GRID_URL";

    String GRID_URL = PropertiesConfigManager.getPropertyKeyFromCache(
            PropertiesFileAlias.GLOBAL.getConfigurationAlias(),
            SELENIUM_GRID_URL);

    public void initializeRemoteBrowser(String browserName, String... arguments) {
        try {
            WebDriver driver = createRemoteDriver(browserName, arguments);
            DriverFactory driverFactory = DriverFactory.getInstance();

            if (driverFactory != null) {
                driverFactory.setDriver(driver);
            } else {
                logger.warn("DriverFactory instance is null. WebDriver will not be stored.");
            }

            logger.info("Initialized remote {} browser on Selenium Grid", browserName);
        } catch (IllegalArgumentException e) {
            logger.warn("Unsupported browser requested: {}", browserName);
            throw new RuntimeException("Unsupported browser: " + browserName, e);
        } catch (Exception error) {
            ErrorHandler.logError(error, "initializeRemoteBrowser", "Initialization failed");
            throw new RuntimeException("Remote browser initialization failed", error);
        }
    }

    private WebDriver createRemoteDriver(String browserName, String... arguments) {
        try {
            URL gridUrl = URI.create(GRID_URL).toURL();
            DesiredCapabilities capabilities = new DesiredCapabilities();

            switch (browserName.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = getChromeOptions(arguments);
                    capabilities.merge(chromeOptions);
                    return new RemoteWebDriver(gridUrl, capabilities);
                case "firefox":
                    FirefoxOptions firefoxOptions = getFirefoxOptions(arguments);
                    capabilities.merge(firefoxOptions);
                    return new RemoteWebDriver(gridUrl, capabilities);
                case "edge":
                    EdgeOptions edgeOptions = getEdgeOptions(arguments);
                    capabilities.merge(edgeOptions);
                    return new RemoteWebDriver(gridUrl, capabilities);
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Grid URL: {}", GRID_URL, e);
            throw new RuntimeException("Invalid Selenium Grid URL", e);
        } catch (Exception error) {
            ErrorHandler.logError(error, "createRemoteDriver", "Initialization failed");
            throw new RuntimeException("Remote browser initialization failed", error);
        }
    }
}
