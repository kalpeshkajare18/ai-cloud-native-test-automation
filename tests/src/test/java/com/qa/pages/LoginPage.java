package com.qa.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) { this.page = page; }

    public void login(String user, String password) {
        page.fill("#user-name", user);
        page.fill("#password", password);
        page.click("#login-button");
    }

    public String errorMessage() {
        return page.locator("[data-test='error']").textContent();
    }
}
