package com.ryanh.base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * A BasePage class for other Page classes to extend. Contains methods such as clicking buttons
 * and typing characters that are wrapped with waiting strategies.
 * TODO Add more complex actions with Actions API
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    /**
     * Waits until the element is visible on the page.
     * @param element By locator for an element
     * @return Wait strategy for the element to be visible
     */
    protected WebElement waitUntilVisible(By element) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    /**
     * Waits until the element is clickable on the page.
     * @param element By locator for an element
     * @return Wait strategy for the element to be clickable
     */
    protected WebElement waitUntilClickable(By element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the element exists in the DoM.
     * @param element By locator for an element
     */
    protected void waitUntilExists (By element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    /**
     * Waits until the element is visible inside a component root, rather than anywhere on the page. Use this from
     * component classes so the wait and the following action resolve the same element.
     * @param root Root element of the component to search within
     * @param element By locator for an element, relative to the root
     * @return Wait strategy for the element to be visible within the root
     */
    protected WebElement waitUntilVisible(WebElement root, By element) {
        return wait.withMessage("visibility of " + element + " within the component root")
                .until(driver -> {
                    WebElement found = root.findElement(element);
                    return found.isDisplayed() ? found : null;
                });
    }

    /**
     * Waits until the element is clickable inside a component root, rather than anywhere on the page.
     * @param root Root element of the component to search within
     * @param element By locator for an element, relative to the root
     * @return Wait strategy for the element to be clickable within the root
     */
    protected WebElement waitUntilClickable(WebElement root, By element) {
        return wait.withMessage("clickability of " + element + " within the component root")
                .until(driver -> {
                    WebElement found = root.findElement(element);
                    return found.isDisplayed() && found.isEnabled() ? found : null;
                });
    }

    /**
     * Waits until the element exists in the DoM inside a component root, rather than anywhere on the page.
     * @param root Root element of the component to search within
     * @param element By locator for an element, relative to the root
     */
    protected void waitUntilExists(WebElement root, By element) {
        wait.withMessage("presence of " + element + " within the component root")
                .until(driver -> root.findElement(element));
    }

    /**
     * Wait for the page URL to be the parameter String
     * @param url String URL
     */
    protected void waitForPageURL(String url) {
        wait.until(ExpectedConditions.urlContains(url));
    }

    /**
     * Waits for an element to become stale, useful for forced page navigation
     * @param element By locator for an element
     */
    protected void waitForStaleElement(WebElement element) {
        wait.until(ExpectedConditions.stalenessOf(element));
    }

    /**
     * Calls the WebElement.click() method, wrapped with a waiting strategy
     * @param element By locator for an element
     */
    protected void click(By element) {
        waitUntilClickable(element).click();
    }

    /**
     * Calls the WebElement.sendKeys() method, wrapped with a waiting strategy
     * @param element By locator for an element
     */
    protected void type(By element, String text) {
        waitUntilVisible(element).sendKeys(text);
    }

    /**
     * Calls the WebElement.click() method on an element inside a component root, wrapped with a waiting strategy.
     * @param root Root element of the component to search within
     * @param element By locator for an element, relative to the root
     */
    protected void click(WebElement root, By element) {
        waitUntilClickable(root, element).click();
    }

    /**
     * Calls the WebElement.sendKeys() method on an element inside a component root, wrapped with a waiting strategy.
     * @param root Root element of the component to search within
     * @param element By locator for an element, relative to the root
     */
    protected void type(WebElement root, By element, String text) {
        waitUntilVisible(root, element).sendKeys(text);
    }

    /**
     * Hides the ad slots that overlay the page. There are two: a fixed position rail pinned to the side of the
     * viewport at large widths, and an AdSense ins element sized to its container. Both sit over the rightmost
     * column of boss cards and intercept clicks on the buttons underneath them. The rail is position fixed and the
     * ins element sizes to a reserved slot, so hiding them does not reflow the card grid.
     */
    protected void hideAdSlots() {
        ((JavascriptExecutor) driver).executeScript(
                "if (!document.getElementById('test-hide-ads')) {"
                        + "  const s = document.createElement('style');"
                        + "  s.id = 'test-hide-ads';"
                        + "  s.textContent = 'ins.adsbygoogle, div[class*=\"lg:fixed\"] { display: none !important; }';"
                        + "  document.head.appendChild(s);"
                        + "}");
    }
}
