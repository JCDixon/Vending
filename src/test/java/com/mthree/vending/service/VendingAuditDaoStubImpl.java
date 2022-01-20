/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mthree.vending.service;

import com.mthree.vending.dao.VendingAuditDao;
import com.mthree.vending.dao.VendingPersistenceException;
import org.springframework.stereotype.Component;

/**Stub for the service layer to write to
 *
 * @author john
 */
@Component
public class VendingAuditDaoStubImpl implements VendingAuditDao {

    @Override
    public void writeAuditEntry(String entry) throws VendingPersistenceException {
        //Does nothing
    }

}
