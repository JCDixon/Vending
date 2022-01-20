package com.mthree.vending.service;

import com.mthree.vending.dao.VendingPersistenceException;
import com.mthree.vending.dto.Coin;
import com.mthree.vending.dto.VendingItem;
import java.math.BigDecimal;
import java.util.List;

/**Interface for our service layer
 *
 * @author john
 */
public interface VendingServiceLayer {
    
    VendingItem purchaseItem(int index) throws VendingNoItemInventoryException, VendingPersistenceException, VendingInsufficientFundsException;

    VendingItem getItemByIndex(int index) throws VendingPersistenceException;

    List<VendingItem> getInventory() throws VendingPersistenceException;

    void insertCoin(Coin currency) throws VendingPersistenceException;
    
    BigDecimal getCurrentFunds();
    
    String depositChange();
}
