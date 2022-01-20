package com.mthree.vending.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Coin enum that is passed around the program layers representing the change
 * that the user inserts into the Vending Machine
 *
 * @author john
 */
public enum Coin {
    //Constant values for each coin type
    PENNY("0.01"), NICKEL("0.05"), DIME("0.10"), QUARTER("0.25");
    private String value;

    private Coin(String value) {
        this.value = value;
    }

    /**
     * Example: Coin.QUARTER.getCoinValue() return "0.25"
     *
     * @return Returns the value of a coin type
     */
    public String getCoinValue() {
        return value;
    }

    /**
     * Takes a BigDecimal and turns it into a number of Coins
     *
     * @param change - the change to be converted
     * @return - returns a String with the number of individuals coins
     * representing the original amount passed into this method
     */
    public static String getChangeDue(BigDecimal change) {
        
        //Don't bother converting change if change is zero
        if (change.equals(BigDecimal.ZERO) ) {return "No change returned";}
        
        BigDecimal quarter = new BigDecimal(Coin.QUARTER.getCoinValue());
        BigDecimal dime = new BigDecimal(Coin.DIME.getCoinValue());
        BigDecimal nickel = new BigDecimal(Coin.NICKEL.getCoinValue());
        BigDecimal penny = new BigDecimal(Coin.PENNY.getCoinValue());

        //Take the amount passed into this method and divide it by 0.25
        //to get the number of QUARTER Coins
        BigDecimal quartersDue = change.divide(quarter, 0, RoundingMode.FLOOR);
        //The remainder after dividing out 0.25 from the original amount
        //becomes the new amount to be passed into dimes
        change = change.remainder(quarter);

        //Take the remaining change and divide it by 0.10
        //to get the number of DIME Coins
        BigDecimal dimesDue = change.divide(dime, 0, RoundingMode.FLOOR);
        //The remainder after dividing out 0.10 from the original amount
        //becomes the new amount to be passed into nickels
        change = change.remainder(dime);

        //Take the remaining change and divide it by 0.05
        //to get the number of NICKEL Coins
        BigDecimal nickelsDue = change.divide(nickel, 0, RoundingMode.FLOOR);
        change = change.remainder(nickel);

        //Take the remaining change and divide it by 0.01
        //to get the number of PENNY Coins
        BigDecimal penniesDue = change.divide(penny, 0, RoundingMode.FLOOR);

        //Convert all values into Strings for presenting to the User
        String ChangeDue = "Change to return::";
        ChangeDue += "Quarters: " + quartersDue.toString() + "::";
        ChangeDue += "Dimes: " + dimesDue.toString() + "::";
        ChangeDue += "Nickels: " + nickelsDue.toString() + "::";
        ChangeDue += "Pennies: " + penniesDue.toString();

        return ChangeDue;
        
    }

    /**
     * Takes in a list of Coins and sums the value of all Coins in the list
     * @param coins - List to be iterated through
     * @return String sum of all Coin values
     */
    public static String getTotal(List<Coin> coins) {
        BigDecimal total = BigDecimal.ZERO;

        BigDecimal quarter = new BigDecimal(Coin.QUARTER.getCoinValue());
        BigDecimal dime = new BigDecimal(Coin.DIME.getCoinValue());
        BigDecimal nickel = new BigDecimal(Coin.NICKEL.getCoinValue());
        BigDecimal penny = new BigDecimal(Coin.PENNY.getCoinValue());

        //For each Coin, add its value to the total
        for (Coin current : coins) {
            switch (current) {
                case QUARTER:
                    total = total.add(quarter);
                    break;
                case DIME:
                    total = total.add(dime);
                    break;
                case NICKEL:
                    total = total.add(nickel);
                    break;
                case PENNY:
                    total = total.add(penny);
                    break;
            }
        }
        //Return the total
        return total.toString();
    }

    /**
     * Turns the String value of a Coin into a BigDecimal
     * @param coin - to be converted
     * @return  - BigDecimal of that coin's value
     */
    public static BigDecimal getCoinValueInBigDecimal(Coin coin) {
        BigDecimal coinValueInBD = new BigDecimal(coin.getCoinValue());
        return coinValueInBD;
    }
}
