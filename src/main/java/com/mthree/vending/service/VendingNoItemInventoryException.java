/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.service;

/**Exception used in the case that an item is sold out
 *
 * @author john
 */
public class VendingNoItemInventoryException extends Exception {

    public VendingNoItemInventoryException(String message) {
        super(message);
    }

    public VendingNoItemInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
