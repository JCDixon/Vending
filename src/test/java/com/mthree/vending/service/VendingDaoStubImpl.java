/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.service;

import com.mthree.vending.dao.VendingPersistenceException;
import com.mthree.vending.dto.Coin;
import com.mthree.vending.dto.VendingItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import com.mthree.vending.dao.VendingDao;
import org.springframework.stereotype.Component;

/**Dao stub for testing purposes
 *
 * @author john
 */
@Component
public class VendingDaoStubImpl implements VendingDao {

    //Stub has only one item
    public VendingItem onlyItem;
    //Stub still keeps track of change
    public BigDecimal CurrentChange = BigDecimal.ZERO;

    
    //Alternative constructor that takes in a preinitialized VendingItem
    public VendingDaoStubImpl(VendingItem onlyItem) {
        this.onlyItem = onlyItem;
    }

    //Default constructor initializes a VendingItem
    public VendingDaoStubImpl() {
        onlyItem = new VendingItem();
        onlyItem.setItemNumber(1);
        onlyItem.setName("Snickers");
        onlyItem.setPrice("1.25");
        onlyItem.setQuantity(10);
    }

    //Stub get  item by index
    @Override
    public VendingItem getItemByIndex(int index) throws VendingPersistenceException {
        if (onlyItem.getItemNumber() == index) {
            return onlyItem;
        } else {
            return null;
        }
    }
    
    //Stub that adds onlyItem to list and returns it
    @Override
    public List<VendingItem> getInventory() throws VendingPersistenceException {
        List<VendingItem> itemList = new ArrayList<>();
        itemList.add(onlyItem);
        return itemList;
    }

    //decrements quantity of an item in inventory 
    @Override
    public int decrementInventory(int index) throws VendingPersistenceException {
        if (onlyItem.getItemNumber() == index) {
            onlyItem.setQuantity(onlyItem.getQuantity() - 1);
            return onlyItem.getQuantity();
        } else {
            return -1;
        }
    }

    //Add coin value to our current total funds
    @Override
    public void insertCoin(Coin currency) {
        CurrentChange = CurrentChange.add(Coin.getCoinValueInBigDecimal(currency));
    }

    //Return our current funds
    @Override
    public BigDecimal getCurrentFunds() {
        return CurrentChange;
    }

    //Set out current funds
    @Override
    public void setCurrentFunds(BigDecimal newValue) {
        CurrentChange = newValue;
    }
}
