package com.qa.base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

public abstract class BaseTest {
    protected static final String BASE_URL =
            System.getProperty("baseUrl", "https://www.saucedemo.com");
    private static final boolean HEADLESS =
            Boolean.parseBoolean(System.getProperty("headless", "true"));

    private Playwright playwright;
    private Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();
        page.navigate(BASE_URL);
    }

    @AfterEach
    void tearDown(TestInfo info) {
        String name = info.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_");
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("target/traces/" + name + ".zip")));
        browser.close();
        playwright.close();
    }
}
