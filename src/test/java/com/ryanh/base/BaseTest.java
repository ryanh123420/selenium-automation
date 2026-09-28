package com.ryanh.base;

import com.ryanh.pages.HomePage;
import com.ryanh.pages.LoginPage;
import com.ryanh.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

/**
 * Class that contains the setup and teardown methods for the WebDriver that test classes will extend from.
 */
public abstract class BaseTest {
    private static final String WEBSITE = "https://wowutils.com/viserio-cooldowns";
    protected WebDriver driver;

    /**
     * Setup method for the WebDriver.
     * TODO: Remove the LoginPage flow and replace with cookie injection, need to figure out the best solution with
     *  OAuth2 Battle.net login.
     */
    @BeforeClass
    public void setup() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserTypes.CHROME);
        driver.get(WEBSITE);
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.navigateToLogin();
        //Battle.net credentials are saved as environment variables
        loginPage.login(System.getenv("BATTLENET_EMAIL_TEST"), System.getenv("BATTLENET_PASSWORD_TEST"));
    }

    /**
     * Teardown method for when a test finishes, and we want to close the WebDriver.
     */
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}