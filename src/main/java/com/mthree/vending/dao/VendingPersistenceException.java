/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.dao;

/**Exception used in the case that there is an error reading
 * or writing to a file
 *
 * @author john
 */
public class VendingPersistenceException extends Exception{
        public VendingPersistenceException(String message) {
        super(message);
    }
    
    public VendingPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
