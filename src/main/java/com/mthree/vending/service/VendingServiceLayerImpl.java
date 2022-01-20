package com.mthree.vending.service;

import com.mthree.vending.dao.VendingPersistenceException;
import com.mthree.vending.dto.Coin;
import com.mthree.vending.dto.VendingItem;
import java.util.List;
import com.mthree.vending.dao.VendingAuditDao;
import com.mthree.vending.dao.VendingDao;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**Implementation of service layer ensuring that all business logic
 * is followed and that Exceptions are thrown when  they aren't
 *
 * @author john
 */
@Component
public class VendingServiceLayerImpl implements VendingServiceLayer {

    private VendingDao dao;
    private VendingAuditDao auditDao;

    @Autowired
    public VendingServiceLayerImpl(VendingDao dao, VendingAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    /**
     * Method that contains the logic to purchase an item if
     * the user has entered the necessary funds and the item is in stock.
     * Exceptions are thrown if the user has insufficient funds or item is out of stock
     *  
     * @param index - the index of the item to be purchased
     * @return - the purchased item if all conditions are satisfied
     * @throws VendingNoItemInventoryException - if not enough stock
     * @throws VendingPersistenceException - if there is an error querying our persistent storage
     * @throws VendingInsufficientFundsException  - If user has insufficient funds
     */
    @Override
    public VendingItem purchaseItem(int index)
            throws VendingNoItemInventoryException, VendingPersistenceException, VendingInsufficientFundsException {

        //If the user hasn't inserted any funds, throw an exception
        if ((dao.getCurrentFunds().compareTo(BigDecimal.ZERO)) == 0) {
            throw new VendingInsufficientFundsException("Insert funds before selecting");
        }

        //If item is out of stock, throw an exception
        if (getItemByIndex(index).getQuantity() == 0) {
            throw new VendingNoItemInventoryException("Sold out");
        }
        
        //Use the item's price to compare to our current funds
        BigDecimal itemPrice = new BigDecimal(dao.getItemByIndex(index).getPrice());
        //int res holds the result of the comparison
        int res = dao.getCurrentFunds().compareTo(itemPrice);
        //If the result of the comparison is:
        switch (res) {
            case 0:  // 0, the current funds are equal to the item price
                dao.setCurrentFunds(BigDecimal.ZERO); //Set current funds to zero
                break;
            case 1: //1, the user had more than enough funds. Subtract itemPrice from current funds
                dao.setCurrentFunds(dao.getCurrentFunds().subtract(itemPrice));
                break;
            case -1: //-1, the user has insufficient funds, throw an exception
                auditDao.writeAuditEntry("Insufficient funds to purchase " + dao.getItemByIndex(index).getName());
                throw new VendingInsufficientFundsException("Insufficient funds\n"
                        + dao.getItemByIndex(index).getName() + " costs: $" + dao.getItemByIndex(index).getPrice()
                        + "\nCurrent funds: $" + dao.getCurrentFunds());
        }
        
        //At this point, if no exception was thrown, only case 0 or 1 should have occured
        //Deposit the item purchased by the user
        VendingItem depositedItem = dao.getItemByIndex(index);
        auditDao.writeAuditEntry("Deposited a " + dao.getItemByIndex(index).getName());
        //Decrement the inventory for that item
        dao.decrementInventory(index);
        auditDao.writeAuditEntry(dao.getItemByIndex(index).getName() + " inventory decremented to "
                                                  + dao.getItemByIndex(index).getQuantity());
        return depositedItem;
    }

    /**
     * Get an item by its index value
     * @param index
     * @return the VendingItem
     * @throws VendingPersistenceException - In case there was an error reading from persistent storage
     */
    @Override
    public VendingItem getItemByIndex(int index) throws VendingPersistenceException {
        return dao.getItemByIndex(index);
    }

    /**
     * Returns a list of all VendingItems
     * @return the list of all items
     * @throws VendingPersistenceException  - In case there was an error reading from persistent storage
     */
    @Override
    public List<VendingItem> getInventory() throws VendingPersistenceException {
        return dao.getInventory();
    }

    /**
     * Method to take in a Coin and tell the Dao to add it to the current funds
     * @param currency Coin to be added
     * @throws VendingPersistenceException - if there is an error writing to the audit file
     */
    @Override
    public void insertCoin(Coin currency) throws VendingPersistenceException {
        dao.insertCoin(currency);
        auditDao.writeAuditEntry("Inserted a " + currency.getCoinValue());
    }

    /**
     * Returns our current total funds
     * @return 
     */
    @Override
    public BigDecimal getCurrentFunds() {
        return dao.getCurrentFunds();
    }

    /**
     * Converts the total current funds into
     * quarters, dimes, nickels, and pennies
     * @return 
     */
    @Override
    public String depositChange() {
        String change = Coin.getChangeDue(dao.getCurrentFunds());
        dao.setCurrentFunds(BigDecimal.ZERO);
        return change;
    }
}
