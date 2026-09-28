package com.ryanh.pages;

import com.ryanh.base.BasePage;
import com.ryanh.components.BossCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Page where users can see an overview of all created cooldown and assignment notes.
 * TODO Add additional functionality and locators for changing difficulty, season, personal vs group, etc.
 */
public class PlanningHubPage extends BasePage {
    //Root element for "BossCard" components found on the Planning Hub page.
    private final By bossCards = By.cssSelector("div.flex div.grid div.border:not(.items-center)");
    private final By sortingDropdown = By.cssSelector("div.rounded-sm button[role='combobox']");
    private final By sortingDropdownTable = By.cssSelector("div[role='presentation']");
    private final By personalNotes = By.xpath("//button[contains(text(), 'Personal')]");
    private final By groupNotes = By.xpath("//button[contains(text(), 'Group')]");
    private final By cdNotes = By.xpath("//button[contains(text(), 'CD Notes')]");


    public PlanningHubPage(WebDriver driver){
        super(driver);
    }

    /**
     * Get all BossCard objects on the Planning Hub page. Find all the root elements for each BossCard, then map those
     * roots to new BossCard objects, then convert to a List of BossCards.
     * @return - List of BossCard objects.
     */
    public List<BossCard> getBossCards() {
        return driver.findElements(bossCards).stream()
                .map(root -> new BossCard(driver,root))
                .toList();
    }

    /**
     * Find a specific BossCard from a list of BossCards. Use a stream to filter a specific boss from the list, then
     * find the first card since there should never be a duplicate. If no cards are found, throw an error.
     * @param bossName - The name of a specific boss.
     * @return - A specific BossCard.
     */
    public BossCard getBossByName(String bossName) {
        return getBossCards().stream()
                .filter(b -> b.getBossName().equals(bossName))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException("Card not found: " + bossName)
                );
    }

    /**
     * Returns the sorting option selected, either 
     * @return
     */
    public String getSelectedSortOption() {
        return driver.findElement(sortingDropdown).getText();
    }

    public void clickSortDropdown(String option) {
        waitUntilExists(sortingDropdown);
        actions.moveToElement(driver.findElement(sortingDropdown))
                .click()
                .perform();
        waitUntilExists(sortingDropdownTable);
        actions.moveToElement(driver.findElement(By.xpath("//div[@role='option']//span[text()='" + option + "']")))
                .click()
                .perform();
    }

    public void showNotesTab() {
        click(cdNotes);
    }

    public void showPersonalNotes() {
        click(personalNotes);
    }

    public void waitForCards() {
        waitUntilExists(bossCards);
    }

    /**
     * Hides the ad rail that overlaps the rightmost column of boss cards, so clicks on those cards are not
     * intercepted. Call this after each navigation to the Planning Hub.
     */
    public void hideAds() {
        hideAdSlots();
    }

    public void showGroupNotes() {
        click(groupNotes);
    }

    public String getSelectedGroupOption() {
        String personalNotesState = driver.findElement(personalNotes).getAttribute("data-state");
        String groupNotesState = driver.findElement(groupNotes).getAttribute("data-state");

        if(personalNotesState.equals("active")){
            return "Personal";
        }
        else if(groupNotesState.equals("active")){
            return "Group";
        }
        return "none";
    }
}
