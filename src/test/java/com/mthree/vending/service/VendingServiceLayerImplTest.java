/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mthree.vending.service;

import com.mthree.vending.dao.VendingAuditDao;
import com.mthree.vending.dao.VendingDao;
import com.mthree.vending.dao.VendingPersistenceException;
import com.mthree.vending.dto.Coin;
import com.mthree.vending.dto.VendingItem;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class VendingServiceLayerImplTest {

    public VendingServiceLayerImpl service;

    @Autowired
    public VendingServiceLayerImplTest() {

        VendingDao dao = new VendingDaoStubImpl();
        VendingAuditDao auditDao = new VendingAuditDaoStubImpl();

        //Instance of service layer for testing
        service = new VendingServiceLayerImpl(dao, auditDao);
    }

    /**
     * Test when purchasing with exact funds
     */
    @Test
    public void testPurchaseWithExactFunds() {
        try {
            //ARRANGE
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            //ACT
            service.purchaseItem(1);
        } catch (VendingPersistenceException
                | VendingNoItemInventoryException
                | VendingInsufficientFundsException e) {
            //ASSERT
            fail("Purchased with exact change. No exception should have been thrown.");
        }
    }

    /**
     * Test when purchasing with not enough funds
     */
    @Test
    public void testPurchaseInsufficientFunds() {
        try {
            //ARRANGE
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            //ACT
            service.purchaseItem(1);
            fail("Purchased with insufficient change. Item should not have been able to be purchased.");
        } catch (VendingPersistenceException | VendingNoItemInventoryException e) {
            fail("Wrong exception.");
        } catch (VendingInsufficientFundsException e) {
            //ASSERT
            return; //Proper Exception was thrown
        }
    }

    /**
     * Test getting an item using a known correct index
     */
    @Test
    public void testGetItemCorrectIndex() {
        try {
            VendingItem anItem = service.getItemByIndex(1);
            if (anItem == null) {
                fail("Item was in the correct index. Item shoud not be null");
            }
        } catch (VendingPersistenceException e) {
            fail("Item exists at this index. Test shouldn't have failed");
        }
    }

    /**
     * Test getting an item using a known incorrect index
     */
    @Test
    public void testGetItemIncorrectIndex() {
        try {
            VendingItem anItem = service.getItemByIndex(9);
            if (anItem != null) {
                fail("Item was not in the correct index. Item should be null");
            }
        } catch (VendingPersistenceException e) {
            fail("Wrong Exception");
        }
    }

    /**
     * Test getting the inventory and testing the known fields of that object
     */
    @Test
    public void testGetInventory() {
        VendingItem testItem1 = new VendingItem();
        testItem1.setItemNumber(1);
        testItem1.setName("Snickers");
        testItem1.setPrice("1.25");
        testItem1.setQuantity(2);
        try {
            List<VendingItem> inventory = service.getInventory();

            assertNotNull(inventory, "The inventory list must not be null");
            assertEquals(1, inventory.size(), "List of VendingItems should be 1");

            assertEquals(testItem1.getItemNumber(),
                    1,
                    "Checking Item Id");
            assertEquals(testItem1.getName(),
                    "Snickers",
                    "Checking Item Name");
            assertEquals(testItem1.getPrice(),
                    "1.25",
                    "Checking Item Price");
            assertEquals(testItem1.getQuantity(),
                    2,
                    "Checking Item Quantity");
        } catch (VendingPersistenceException e) {
            fail("Test should not be throwing this error");
        }
    }

    /**
     * Test that inserting coins works correctly,
     * that coins add up to a known correct amount,
     * and that coins are deposited correctly
     */
    @Test
    public void testInsertCoin() {
        try {
            service.insertCoin(Coin.PENNY);
            service.insertCoin(Coin.DIME);
            service.insertCoin(Coin.NICKEL);
            service.insertCoin(Coin.QUARTER);
            assertEquals(service.getCurrentFunds().toString(), "0.41", "Adding one of every coin should add to 0.41");
            service.insertCoin(Coin.DIME);
            service.insertCoin(Coin.DIME);
            service.insertCoin(Coin.DIME);
            service.insertCoin(Coin.PENNY);
            service.insertCoin(Coin.PENNY);
            service.insertCoin(Coin.PENNY);
            service.insertCoin(Coin.PENNY);
            service.insertCoin(Coin.QUARTER);
            service.insertCoin(Coin.QUARTER);
            //Purchase with exact change
            service.purchaseItem(1);
            assertEquals(service.getCurrentFunds().toString(), "0", "Current funds should be zero");
            service.insertCoin(Coin.PENNY);
            service.insertCoin(Coin.DIME);
            service.insertCoin(Coin.NICKEL);
            service.insertCoin(Coin.NICKEL);
            String correctChange = "Change to return::" + "Quarters: 0" + "::" + "Dimes: 2" + "::" + "Nickels: 0" + "::" + "Pennies: 1";
            assertEquals(service.depositChange(), correctChange, "Correct change should equal "
                    + "Change to return::" + "Quarters: 0" + "::" + "Dimes: 2" + "::" + "Nickels: 0" + "::" + "Pennies: 1");
        } catch (VendingPersistenceException
                | VendingNoItemInventoryException
                | VendingInsufficientFundsException e) {
            fail("Wrong Error");
        }
    }
}
