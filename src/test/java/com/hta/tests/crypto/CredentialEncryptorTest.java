package com.hta.tests.crypto;

import com.hta.base.TestBase;
import com.hta.config.environments.EnvironmentFileAlias;
import com.hta.config.environments.EnvironmentFilePaths;
import com.hta.config.environments.EnvironmentSecretKey;
import com.hta.crypto.services.CryptoOperationsManager;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.CryptoException;
import org.testng.annotations.Test;

public class CredentialEncryptorTest extends TestBase {

    private static final Logger logger = LoggerUtils.getLogger(CredentialEncryptorTest.class);
    private static final String USERNAME = "PORTAL_USERNAME";
    private static final String PASSWORD = "PORTAL_PASSWORD";

    @Test(groups = {"encryption"}, priority = 2)
    public void encryptCredentials() throws CryptoException {
        try{
            // Run Encryption
            CryptoOperationsManager.encryptEnvironmentVariables(
                    EnvironmentFilePaths.UAT.getEnvironmentFileFullPath(),
                    EnvironmentFileAlias.UAT.getEnvironmentAlias(),
                    EnvironmentSecretKey.UAT.getKeyName(),
                    USERNAME, PASSWORD
            );
            logger.info("Secret key generation process completed");
        } catch (Exception error){
            ErrorHandler.logError(error, "encryptCredentials", "Failed to encrypt credentials");
            throw error;
        }
    }
}
