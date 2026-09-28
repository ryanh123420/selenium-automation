package com.ryanh.components;

import com.ryanh.base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * UI component representing an individual note tile on a BossCard.
 * Scoped to a single tile's root WebElement.
 */
public class NoteTile extends BasePage {
    private final WebElement root;
    private final By copyNoteButton = By.cssSelector("div.grid div.flex button[title*='Copy this note']");
    private final By deleteNoteButton = By.cssSelector("div.grid div.flex button[title*='Delete note']");
    private final By deleteAlertWindow = By.cssSelector("div[role='alertdialog']");
    private final By deleteAlertButton = By.cssSelector("div[role='alertdialog'] button.bg-destructive");
    private final By noteLink = By.cssSelector("div.grid div.flex a[href*=\"/viserio-cooldowns/raid/\"]");

    private final By toastNotification = By.cssSelector("section ol li");

    //Menu that asks which list to copy into. It is portalled to the body, so it is not scoped to the tile root.
    private final By copyDestinationMenu = By.cssSelector("div[role='menu']");
    private final By copyToPersonalNotes = By.xpath("//div[@role='menuitem'][contains(., 'Personal Notes')]");

    public NoteTile(WebDriver driver, WebElement root) {
        super(driver);
        this.root = root;
    }

    /**
     * Copies this note into the personal notes. Clicking copy opens a menu asking which list to copy into, so a
     * destination has to be chosen before the copy happens and the toast appears.
     */
    public void copy() {
        click(root, copyNoteButton);
        waitUntilVisible(copyDestinationMenu);
        click(copyToPersonalNotes);
        waitUntilExists(toastNotification);
    }

    /**
     * Deletes this note. Confirms in the alert dialog.
     */
    public void delete() {
        wait.until(ExpectedConditions.presenceOfElementLocated(deleteNoteButton));
        wait.until(ExpectedConditions.elementToBeClickable(deleteNoteButton));
        root.findElement(deleteNoteButton).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(deleteAlertWindow));
        driver.findElement(deleteAlertButton).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(toastNotification));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(toastNotification));
    }

    /**
     * Gets the name of this note from its link text.
     * @return - Name of the note.
     */
    public String getName() {
        waitUntilExists(noteLink);
        return root.findElement(noteLink).getText();
    }

    /**
     * Opens this note by clicking its link.
     */
    public void open() {
        click(noteLink);
    }
}
