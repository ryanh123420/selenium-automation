package com.ryanh.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Factory class responsible for setting up WebDrivers
 */
public class DriverFactory {
    public enum BrowserTypes {
        CHROME,
        FIREFOX,
        EDGE,
    }

    /**
     * Points the ad hosts the site uses at localhost so they never resolve. Ads render as absolutely positioned
     * overlays that intercept clicks on the boss cards, which makes the layout non deterministic between runs.
     */
    private static final String AD_BLOCK_RULE = "--host-resolver-rules="
            + "MAP pagead2.googlesyndication.com 127.0.0.1,"
            + "MAP googleads.g.doubleclick.net 127.0.0.1,"
            + "MAP ep1.adtrafficquality.google 127.0.0.1,"
            + "MAP ep2.adtrafficquality.google 127.0.0.1";

    /**
     * Whether to run the browser without a UI. Set with -Dheadless=true or the HEADLESS environment variable, so a
     * run can be switched to headless without touching the code.
     * @return true if the run should be headless
     */
    private static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", System.getenv("HEADLESS")));
    }

    public static WebDriver createDriver(BrowserTypes browserType){
        WebDriver driver = null;

        switch (browserType){
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(AD_BLOCK_RULE);
                if (isHeadless()) {
                    //--start-maximized is ignored without a UI, so set the window size explicitly to keep the
                    //layout of the page consistent between headed and headless runs.
                    chromeOptions.addArguments("--headless=new", "--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported Browser type: " + browserType);
        }

        return driver;
    }
}