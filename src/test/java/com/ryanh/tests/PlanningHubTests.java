package com.ryanh.tests;

import com.ryanh.base.BaseTest;
import com.ryanh.components.BossCard;
import com.ryanh.pages.PlanningHubPage;
import com.ryanh.tests.data.BossDataProviders;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test scripts for the webapp raid planning hub page: https://wowutils.com/viserio-cooldowns/planning
 * TODO: Add tests for non-BossCard related actions on the Planning Hub page.
 */
public class PlanningHubTests extends BaseTest {
    //The tab parameter matters, the Planning Hub renders no boss cards without it.
    private static final String PLANNING_HUB = "https://wowutils.com/viserio-cooldowns/planning?tab=notes";

    private PlanningHubPage planningHubPage;
    private final String currentSeason = "MidnightSeason2";

    @BeforeMethod
    public void planningHubSetup() {
        planningHubPage = new PlanningHubPage(driver);
        openPlanningHub();
        Assert.assertEquals(planningHubPage.getSelectedGroupOption(), "Personal");
    }

    /**
     * Loads the Planning Hub and puts it into the state the tests expect, hiding the ad slots that would otherwise
     * sit over the boss cards and intercept clicks.
     */
    private void openPlanningHub() {
        driver.get(PLANNING_HUB);
        planningHubPage.hideAds();
        planningHubPage.showNotesTab();
        planningHubPage.showPersonalNotes();
    }

    /**
     * Deletes any notes left on the boss card a test used. This runs even when a test fails part way through, so a
     * failure cannot leak notes into the tests that follow. Cleanup problems are reported but not rethrown, since
     * rethrowing here would mask the real failure.
     *
     * @param result - TestNG result, used to read the boss name the test ran against
     */
    @AfterMethod(alwaysRun = true)
    public void cleanNotes(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length == 0 || !(parameters[0] instanceof String bossName)) {
            //Tests without a boss parameter create no notes, so there is nothing to clean up.
            return;
        }

        try {
            openPlanningHub();
            BossCard boss = planningHubPage.getBossByName(bossName);
            //isTilePresent waits for the tiles to render. clearNotes on its own checks without waiting, so straight
            //after a page load it would find nothing and quietly leave the notes behind.
            if (boss.isTilePresent()) {
                boss.clearNotes();
            }
        } catch (RuntimeException e) {
            System.out.println("Cleanup failed for " + bossName + ": " + e.getMessage());
        }
    }

    /**
     * Adds one note to every boss card on the Planning Hub page.
     * @param bossName - Name of a specific boss
     */
    @Test(dataProvider = currentSeason, dataProviderClass = BossDataProviders.class)
    public void addNotes(String bossName) {
        planningHubPage.waitForCards();
        BossCard boss = planningHubPage.getBossByName(bossName);
        boss.addNote();

        //Refresh the boss list due to the navigation to avoid stale elements
        BossCard refreshedBoss = planningHubPage.getBossByName(bossName);
        Assert.assertTrue(refreshedBoss.isTilePresent());

        refreshedBoss.getFirstNoteTile().delete();
    }

    /**
     * Adds one note to every boss card on the Planning Hub page via the "Create Note" button.
     * @param bossName - Name of a specific boss
     */
    @Test(dataProvider = currentSeason, dataProviderClass = BossDataProviders.class)
    public void createNotes(String bossName) {
        BossCard boss = planningHubPage.getBossByName(bossName);
        boss.createNote();

        //Refresh the boss list due to the navigation to avoid stale elements
        BossCard refreshedBoss = planningHubPage.getBossByName(bossName);
        Assert.assertTrue(refreshedBoss.isTilePresent());

        refreshedBoss.getFirstNoteTile().delete();
    }

    /**
     * Copy the first note on each boss card.
     * @param bossName - Name of a specific boss
     */
    @Test(dataProvider = currentSeason, dataProviderClass = BossDataProviders.class)
    public void copyNotes(String bossName) {
        BossCard boss = planningHubPage.getBossByName(bossName);
        int tileAmount = boss.getNumberOfTiles();

        boss.addNote();
        BossCard refreshedBoss = planningHubPage.getBossByName(bossName);

        refreshedBoss.getFirstNoteTile().copy();
        Assert.assertTrue(refreshedBoss.getNumberOfTiles() > tileAmount);

        refreshedBoss.clearNotes();
    }

    /**
     * Opens the guide for each boss card. Bosses without a published guide have no guide link, so those rows are
     * skipped rather than failed. They start running again on their own once a guide is published.
     * @param bossName - Name of a specific boss
     */
    @Test(dataProvider = currentSeason, dataProviderClass = BossDataProviders.class)
    public void guideLinkNavigation(String bossName) {
        BossCard boss = planningHubPage.getBossByName(bossName);

        if (!boss.hasGuide()) {
            throw new SkipException("No guide published for " + bossName);
        }

        String guideURL = boss.getGuideURL();
        boss.openBossGuide();
        Assert.assertEquals(guideURL, driver.getCurrentUrl());
    }

    @Test
    public void dropdownTest() {
        planningHubPage.clickSortDropdown("Last Updated");
        Assert.assertEquals(planningHubPage.getSelectedSortOption(), "Last Updated");
        planningHubPage.clickSortDropdown("Creation Date");
        Assert.assertEquals(planningHubPage.getSelectedSortOption(), "Creation Date");
    }
}

