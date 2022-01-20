package com.mthree.vending;

import com.mthree.vending.controller.VendingController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author john
 */
public class App {

    public static void main(String[] args) {
        /*  *****Old implementation using manual dependency injection*****
        UserIO myIO = new UserIOConsoleImpl();
        VendingView myView = new VendingView(myIO);

        VendingDao myDao = new VendingDaoImpl();
        VendingAuditDao myAudit = new VendingAuditDaoImpl();
        VendingServiceLayer myService = new VendingServiceLayerImpl(myDao, myAudit);

        VendingController myController = new VendingController(myService, myView);
        myController.run();
          */
        
        //Spring boot dependency injection using Annotations 
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.mthree.vending");
        appContext.refresh();

        VendingController controller = appContext.getBean("vendingController", VendingController.class);
        controller.run();
    }
}
