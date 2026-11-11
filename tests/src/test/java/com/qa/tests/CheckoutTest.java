package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.CheckoutPage;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest extends BaseTest {

    @Test
    @Tag("smoke")
    void addToCartUpdatesBadge() {
        new LoginPage(page).login("standard_user", "secret_sauce");
        InventoryPage inventory = new InventoryPage(page);
        inventory.addFirstProductToCart();
        assertEquals("1", inventory.cartBadgeCount());
    }

    @Test
    @Tag("regression")
    void userCanCompleteCheckout() {
        new LoginPage(page).login("standard_user", "secret_sauce");
        InventoryPage inventory = new InventoryPage(page);
        inventory.addFirstProductToCart();
        inventory.openCart();

        CheckoutPage checkout = new CheckoutPage(page);
        checkout.startCheckout();
        checkout.fillDetails("Test", "User", "12345");
        checkout.finish();
        assertEquals("Thank you for your order!", checkout.confirmation());
    }
}
