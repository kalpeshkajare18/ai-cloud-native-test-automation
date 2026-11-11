package com.qa.pages;

import com.microsoft.playwright.Page;

public class InventoryPage {
    private final Page page;

    public InventoryPage(Page page) { this.page = page; }

    public String title() { return page.locator(".title").textContent(); }

    public int productCount() { return page.locator(".inventory_item").count(); }

    public void addFirstProductToCart() {
        page.locator("button[data-test^='add-to-cart']").first().click();
    }

    public String cartBadgeCount() {
        return page.locator(".shopping_cart_badge").textContent();
    }

    public void openCart() { page.click(".shopping_cart_link"); }
}
