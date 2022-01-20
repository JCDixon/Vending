# Vending
Command-line vending machine project to demonstrate MVC design pattern with full unit testing.

-Utilizes Spring Framework for dependency injection with annotation-based configuration.

-Employed Maven for build automation and to manage Spring libraries required for dependency injection

-Follows the MVC pattern consisting of an App class, Controller, View, Service Layer, and DAO.

-Application-specific exceptions 
     
       InsufficientFundsException - One that is thrown when the user tries to purchase an item but doesn't deposit enough money.
       
       NoItemInventoryException - One that is thrown when the user tries to purchase an item that has zero inventory.
       
-DAO and Service Layer functions are fully tested with JUnit 5 tests.

-Use of BigDecimal for all monetary calculations.

-Uses enums to represent coins and their values.

-Includes audit functionality which logs all interactions with the Vending Machine using the Java DateTime API for timestamps.
