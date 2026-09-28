package com.ryanh.components;

import com.ryanh.base.BasePage;
import org.openqa.selenium.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * UI component on the Planning Hub page that allows creation of cooldown notes and assignments for a specific boss.
 * TODO Add functionality for some of the less essential actions on a card.
 */
public class BossCard extends BasePage {
    private final WebElement root;

    /**
     * Selectors for elements on a boss card that are always available regardless on if there are any notes created.
     */
    private final By addNoteButton = By.cssSelector("button[title*='Create a CD Plan']");
    private final By expandViewButton = By.cssSelector("div.grid div.flex button[title='Open full view']");
    //Read the name from the card header rather than the guide link, since bosses without a published guide have no
    //guide link at all.
    private final By bossName = By.cssSelector("span.font-cal");
    private final By bossGuideLink = By.cssSelector("div.grid div.flex a[href*='/viserio-cooldowns/guides']");
    //Lives in a dialog portalled to the body, not inside the card, so this one is deliberately not root scoped.
    private final By cdPlanButton = By.xpath("//button[.//p[normalize-space(text())='CD Plan']]");

    //Only available when no notes are created
    //Matches on the button text rather than a title attribute, since the icon button in the card header uses
    //"Create a CD Plan or template" as its title and would otherwise collide.
    private final By createANoteButton = By.xpath(".//button[contains(., 'Create a CD Plan')]");

    //Locator for individual note tiles on this card
    private final By noteTile = By.cssSelector("div.grid div.box-border:not(.animate-pulse)");

    /**
     * When a BossCard is created, we set the root element so we can differentiate between different BossCards on the
     * Planning Hub page.
     *
     * @param driver - WebDriver
     * @param root   - Root element on the Planning Hub page
     */
    public BossCard(WebDriver driver, WebElement root) {
        super(driver);
        this.root = root;
    }

    /**
     * Returns the name of a boss by checking the text of the boss guide link for that boss.
     *
     * @return - Text wrapped around the link href
     */
    public String getBossName() {
        return root.findElement(bossName).getText();
    }

    /**
     * Add a note by clicking the add button, when a note is added the page automatically navigates to that
     * notes editing page. Wait for the locator reference to become stale since we navigate to a different page.
     */
    public void addNote() {
        WebElement addButton = waitUntilClickable(root, addNoteButton);
        addButton.click();
        click(cdPlanButton);
        waitForStaleElement(addButton);
        driver.navigate().back();
        waitUntilVisible(addNoteButton);
        //The navigation rebuilds the page, which brings the ad slots back over the cards.
        hideAdSlots();
    }

    /**
     * Similar functionality to addNote but has to be handled differently due to the button only appearing when
     * there are no notes.
     */
    public void createNote() {
        WebElement createButton = waitUntilClickable(root, createANoteButton);
        createButton.click();
        click(cdPlanButton);
        waitForStaleElement(createButton);
        driver.navigate().back();
        waitUntilExists(bossName);
        //The navigation rebuilds the page, which brings the ad slots back over the cards.
        hideAdSlots();
    }

    /**
     * Whether this boss has a published guide. Not every boss has one, so anything that follows the guide link needs
     * to check first.
     *
     * @return - True if the card has a guide link, false if the boss has no guide yet.
     */
    public boolean hasGuide() {
        return !root.findElements(bossGuideLink).isEmpty();
    }

    public void openBossGuide() {
        root.findElement(bossGuideLink).click();
        waitForPageURL(getGuideURL());
    }

    /**
     * Checks that the note table contains a note for a BossCard.
     *
     * @return - True if a note exists in the table, false if there are none.
     */
    public boolean isTilePresent() {
        try {
            waitUntilVisible(root, noteTile);
            return true;
        } catch (TimeoutException e) {
            //No tile showed up on this card within the wait, which is an answer rather than an error.
            return false;
        }
    }

    /**
     * Get the number of note tiles on a card.
     *
     * @return - The number of tiles.
     */
    public int getNumberOfTiles() {
        return root.findElements(noteTile).size();
    }

    /**
     * Returns all NoteTile components on this card.
     *
     * @return - List of NoteTile instances scoped to each tile element.
     */
    public List<NoteTile> getNoteTiles() {
        waitUntilVisible(root, noteTile);
        return root.findElements(noteTile)
                .stream()
                .map(el -> new NoteTile(driver, el))
                .toList();
    }

    /**
     * Returns the first NoteTile on this card.
     *
     * @return - The first NoteTile.
     * @throws NoSuchElementException if no tiles exist.
     */
    public NoteTile getFirstNoteTile() {
        List<NoteTile> tiles = getNoteTiles();
        if (tiles.isEmpty()) {
            throw new NoSuchElementException("No note tiles found on this boss card.");
        }
        return tiles.getFirst();
    }

    /**
     * Clears all notes on a card by deleting each tile.
     * TODO - Make this faster, have to wait for toastNotification to disappear
     */
    public void clearNotes() {
        while (!root.findElements(noteTile).isEmpty()) {
            getFirstNoteTile().delete();
        }
    }

    public String getGuideURL() {
        return root.findElement(bossGuideLink).getAttribute("href");
    }
}
