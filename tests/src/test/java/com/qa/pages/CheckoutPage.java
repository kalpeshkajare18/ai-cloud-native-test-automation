package com.qa.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {
    private final Page page;

    public CheckoutPage(Page page) { this.page = page; }

    public void startCheckout() { page.click("#checkout"); }

    public void fillDetails(String first, String last, String zip) {
        page.fill("#first-name", first);
        page.fill("#last-name", last);
        page.fill("#postal-code", zip);
        page.click("#continue");
    }

    public void finish() { page.click("#finish"); }

    public String confirmation() {
        return page.locator(".complete-header").textContent();
    }
}
