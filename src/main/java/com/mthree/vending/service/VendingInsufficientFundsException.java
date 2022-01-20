/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.service;

/**Exception used in the case that the user attempts
 * to purchase an item without the necessary funds
 *
 * @author john
 */
public class VendingInsufficientFundsException extends Exception {
    
    public VendingInsufficientFundsException(String message) {
        super(message);
    }

    public VendingInsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
