package com.ryanh.pages;

import com.ryanh.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By assignmentsPageButton = By.xpath("//a[contains(text(), 'Open the Planning Hub')]");
    private final By battleNetLogin = By.xpath("//img[@alt='Battle.net']/ancestor::button");
    private final String pageURL = "https://wowutils.com/viserio-cooldowns";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToPlanningHub() {
        click(assignmentsPageButton);
        waitForPageURL("https://wowutils.com/viserio-cooldowns/planning");
    }

    public void navigateToLogin() {
        click(battleNetLogin);
    }
}
