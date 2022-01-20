/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mthree.vending.dao;

/**
 *Interface for audit file
 * @author john
 */
public interface VendingAuditDao {

    void writeAuditEntry(String entry) throws VendingPersistenceException;
}
