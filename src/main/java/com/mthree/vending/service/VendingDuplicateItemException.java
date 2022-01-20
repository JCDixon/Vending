/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.service;

/**Used in the case that their is a duplicate item
 * or duplicate item code
 *
 * @author john
 */
public class VendingDuplicateItemException extends Exception {

    public VendingDuplicateItemException(String message) {
        super(message);
    }

    public VendingDuplicateItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
