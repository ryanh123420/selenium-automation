package com.ryanh.helper;

import com.ryanh.pages.HomePage;
import com.ryanh.pages.LoginPage;
import com.ryanh.pages.PlanningHubPage;
import com.ryanh.utils.DriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * Logs me into my test account for debug and manual set up so I don't have to log out of my main account.
 */
public class RunWithoutTests {
    public static void main(String[] args) {
        WebDriver driver = DriverFactory.createDriver(DriverFactory.BrowserTypes.CHROME);

        driver.get("https://wowutils.com/viserio-cooldowns");

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.navigateToLogin();
        loginPage.login(System.getenv("BATTLENET_EMAIL_TEST"), System.getenv("BATTLENET_PASSWORD_TEST"));

        PlanningHubPage planningHubPage = new PlanningHubPage(driver);

        driver.get("https://wowutils.com/viserio-cooldowns/planning");
    }
}
