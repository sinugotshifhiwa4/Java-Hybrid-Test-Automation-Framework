package com.hta.tests.ui.hotelTests;

import com.hta.base.TestBase;
import com.hta.config.retry.TestRetryAnalyzer;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class LoginTests extends TestBase {

    // To Run Uat Sanity tests: mvn clean test -Denv=uat -Dgroups=sanity

    private static final Logger logger = LoggerUtils.getLogger(LoginTests.class);
    private static final String INVALID_USERNAME = "User8958";
    private static final String INVALID_PASSWORD = "password123";

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void loginWithValidCredentials() {
        try {
            loginPage.loginToPortal(decryptCredentials().get(0), decryptCredentials().get(1));
            loginPage.verifyLoginErrorMessageNotVisible();
            loginPage.captureScreenshot("ValidLogin");
            logger.info("Login successful");
        } catch (Exception error) {
            ErrorHandler.logError(error, "loginWithValidCredentials", "Failed to login to portal");
            throw error;
        }
    }

    @Test(groups = {"sanity"}, retryAnalyzer = TestRetryAnalyzer.class)
    public void loginWithInvalidCredentials() {
        try {
            loginPage.loginToPortal(INVALID_USERNAME, INVALID_PASSWORD);
            loginPage.isLoginErrorMessageVisible();
            loginPage.captureScreenshot("InvalidLogin");
            logger.info("Login Failed as expected");
        } catch (Exception error) {
            ErrorHandler.logError(error, "loginWithInvalidCredentials", "Failed to login to portal");
            throw error;
        }
    }
}
