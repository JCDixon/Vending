package com.mthree.vending.dao;

import com.mthree.vending.dto.Coin;
import com.mthree.vending.dto.VendingItem;
import java.math.BigDecimal;
import java.util.List;

/**
 *Interface for our Vending Data Access Object
 * @author john
 */
public interface VendingDao {

    VendingItem getItemByIndex(int index) throws VendingPersistenceException;

    List<VendingItem> getInventory() throws VendingPersistenceException;

    int decrementInventory(int index) throws VendingPersistenceException;

    void insertCoin(Coin currency);

    BigDecimal getCurrentFunds();

    void setCurrentFunds(BigDecimal newValue);
}
