# Selenium Automation Framework

A Java Selenium test automation framework for the WoWUtils raid management web application, built with Java, TestNG, and Maven using the Page Object Model design pattern.

## Project Purpose
1. Demonstrate proficiency in test automation framework design and implementation
2. Practice Selenium WebDriver desgin patterns
3. Automate testing workflows for the WoWUtils raid roster management tool

## Tech Stack
* **Test Framework:** TestNG
* **Build Tool:** Maven
* **Web Automation:** Selenium WebDriver
* **Design Patterns:** Page Object Model, Component Object Pattern, Factory Pattern

## Project Structure
```
  src/
  ├── main/java/com/ryanh/
  │   ├── base/
  │   │   └── BasePage.java                     # Base class with reusable wait strategies
  │   ├── components/
  │   │   ├── BossCard.java                     # Reusable component for boss encounter cards
  │   │   └── NoteTile.java                     # Component for individual note tiles on a BossCard
  │   ├── helper/
  │   │   └── RunWithoutTests.java              # Helper class for debugging
  │   ├── pages/
  │   │   ├── HomePage.java                     # Main navigation page
  │   │   ├── LoginPage.java                    # Authentication page
  │   │   └── PlanningHubPage.java              # Raid setup and notes planning page
  │   └── utils/
  │       └── DriverFactory.java                # WebDriver factory with browser support
  └── test/java/
      ├── com/ryanh/
      │   ├── base/
      │   │   └── BaseTest.java                 # Test setup and teardown
      │   └── tests/
      │       ├── HomeTests.java                
      │       ├── PlanningHubTests.java         # Data-driven tests with TestNG
      │       └── data/
      │           └── BossDataProviders.java    # DataProviders for test case parameters
      └── testng.xml
```
