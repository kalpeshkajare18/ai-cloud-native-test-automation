package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.InventoryPage;
import com.qa.pages.LoginPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest extends BaseTest {

    @Test
    @Tag("smoke")
    void validUserCanLogin() {
        new LoginPage(page).login("standard_user", "secret_sauce");
        InventoryPage inventory = new InventoryPage(page);
        assertEquals("Products", inventory.title());
        assertEquals(6, inventory.productCount());
    }

    @Test
    @Tag("regression")
    void invalidPasswordShowsError() {
        LoginPage login = new LoginPage(page);
        login.login("standard_user", "wrong_password");
        assertTrue(login.errorMessage().contains("do not match"));
    }

    @Test
    @Tag("regression")
    void lockedOutUserIsBlocked() {
        LoginPage login = new LoginPage(page);
        login.login("locked_out_user", "secret_sauce");
        assertTrue(login.errorMessage().contains("locked out"));
    }
}
