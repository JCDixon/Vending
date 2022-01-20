/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.service;

/**Exception used in the case that a Vending Item has invalid data
 *
 * @author john
 */
public class VendingDataValidationException extends Exception {

    public VendingDataValidationException(String message) {
        super(message);
    }

    public VendingDataValidationException(String message,
            Throwable cause) {
        super(message, cause);
    }
}
