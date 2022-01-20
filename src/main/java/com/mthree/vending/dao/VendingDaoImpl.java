package com.mthree.vending.dao;

import com.mthree.vending.dto.Coin;
import com.mthree.vending.dto.VendingItem;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Implementation of all filesystem and memory transactions for the vending program
 *
 * @author john
 */
@Component
public class VendingDaoImpl implements VendingDao {

    //Permanent Inventory file name
    private final String INVENTORY_FILE;
    //Permanent delimiter for separating fields in library file
    public final String DELIMITER = "::";

    //List in memory of Vending items
    private Map<Integer, VendingItem> Inventory = new HashMap<>();
    //Value in memory of current funds
    private BigDecimal CurrentChange = BigDecimal.ZERO;

    //Default constructor initializes inventory file 
    public VendingDaoImpl() {
        INVENTORY_FILE = "inventory.txt";
    }

    //Alternate constructor sets the inventory file name
    public VendingDaoImpl(String inventoryTextFile) {
        INVENTORY_FILE = inventoryTextFile;
    }

    /**
     * Gets an item at its item/index number, which is its unique key to get from the HashMap
     * @param index
     * @return the value at that index/key
     * @throws VendingPersistenceException in case there are issues reading from persistent storage
     */
    @Override
    public VendingItem getItemByIndex(int index) throws VendingPersistenceException {
        loadInventory();
        return Inventory.get(index);
    }

    /**
     * Returns full inventory as a String list ordered from least index to greatest index
     * @return
     * @throws VendingPersistenceException 
     */
    @Override
    public List<VendingItem> getInventory() throws VendingPersistenceException {
        loadInventory();
        //As HashMap cannot be guaranteed to be sorted
        List<VendingItem> sortedItems = new ArrayList<>(
                Inventory.values()
                        .stream()
                        .sorted(Comparator
                                .comparing(entry -> entry.getItemNumber()))
                        .collect(Collectors.toList())
        );

        return sortedItems;
    }

    /**
     * Method that decrements 1 from an items quantity 
     * Only used after an item is purchased
     * @param index
     * @return
     * @throws VendingPersistenceException 
     */
    @Override
    public int decrementInventory(int index) throws VendingPersistenceException {
        loadInventory();
        VendingItem item = Inventory.get(index);
        item.setQuantity(item.getQuantity() - 1);
        Inventory.put(index, item);
        writeInventory();
        return Inventory.get(index).getQuantity();
    }

    /**
     * Takes in a Coin, converts it to a BigDecimal, then adds it to the total change
     * @param currency the Coin that is added
     */
    @Override
    public void insertCoin(Coin currency) {
        CurrentChange = CurrentChange.add(Coin.getCoinValueInBigDecimal(currency));
    }

    /**
     * Returns the current funds
     * @return 
     */
    @Override
    public BigDecimal getCurrentFunds() {
        return CurrentChange;
    }

    /**
     * This method is used to set our current funds after
     * using up an amount to purchase an item
     * @param newValue 
     */
    @Override
    public void setCurrentFunds(BigDecimal newValue) {
        CurrentChange = newValue;
    }

    //Convert from a series of String values to a VendingItem object
    private VendingItem unmarshallInventory(String itemAsText) throws VendingPersistenceException {
        String[] itemTokens = itemAsText.split(DELIMITER, -1);

        //Item needs to contain exactly 4 values
        if (itemTokens.length != 4
                || itemTokens[0].equals("")
                || itemTokens[1].equals("")) {
            return null;
        }

        VendingItem itemFromFile = new VendingItem();

        itemFromFile.setItemNumber(Integer.parseInt(itemTokens[0]));

        itemFromFile.setName(itemTokens[1]);

        itemFromFile.setPrice(itemTokens[2]);

        itemFromFile.setQuantity(Integer.parseInt(itemTokens[3]));

        //Return newly created VendingItem from file
        return itemFromFile;
    }

    //Load our inventory from a file
    private void loadInventory() throws VendingPersistenceException {
        Scanner scanner;

        try {
            // Instantiate Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(INVENTORY_FILE)));
        } catch (FileNotFoundException e) {
            throw new VendingPersistenceException(
                    "-_- Could not load library data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;

        VendingItem currentItem;

        while (scanner.hasNextLine()) {

            // Get the next line in the file
            currentLine = scanner.nextLine();

            // Unmarshall the line into a VendingItem
            currentItem = unmarshallInventory(currentLine);
            try {
                //If unmarshallInventory() returns a null object then there was improperly formatted data
                if (currentItem == null) //Throw an exception to handle this error
                {

                    throw new VendingPersistenceException("Improperly formatted data encountered");
                }
                //If the VendingItem already exists in the library skip adding the duplicate to the inventory
                if (Inventory.containsKey(currentItem.getItemNumber())) {
                    throw new VendingPersistenceException("Duplicate file already exists in the library");
                }

                //If the VendingItem was properly formatted add it to the Map
                // Use the ItemNumber as the map key for our VendingItem object.
                // Put currentItem into the map using its ItemNumber as the key
                Inventory.put(currentItem.getItemNumber(), currentItem);
                //Inventory.put(currentItem.getItemNumber(), currentItem);

            } catch (VendingPersistenceException e) {
                //Move past the improperly formatted data or duplicate DVD and continue unmarshalling data
            }
        }
        // close scanner
        scanner.close();
    }

    private String marshallItem(VendingItem anItem) {
        // We need to turn a VendorItem object into a line of text for our file.
        // For example, we need an in memory object to end up like this:
        // Drive1234::Drive::Sept 2011::R::Nicolas Refn::Film District::Note

        //Firstly add the ItemNumber
        String itemAsText = anItem.getItemNumber() + DELIMITER;

        // add the rest of the properties in the correct order:
        // Name
        itemAsText += anItem.getName() + DELIMITER;

        // Price
        itemAsText += anItem.getPrice() + DELIMITER;

        // Quantity
        itemAsText += anItem.getQuantity();

        // We have now turned a DVD to text! Return it!
        return itemAsText;
    }

    /**
     * Writes all DVDs in the library out to a LIBRARY_FILE. See loadLibrary for
     * file format.
     *
     * @throws VendingPersistenceException if an error occurs writing to the
     * file
     */
    private void writeInventory() throws VendingPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(INVENTORY_FILE));
        } // Translate IOException into an application specific exception 
        // then throw it back to the code that called us.  
        catch (IOException e) {
            throw new VendingPersistenceException(
                    "Could not save inventory data.", e);
        }

        // Write out the VendingItem objects to the library file.
        String itemAsText;
        List<VendingItem> itemList = this.getInventory();
        for (VendingItem currentItem : itemList) {
            // turn a VendingItem into a String
            itemAsText = marshallItem(currentItem);
            // write the VendingItem object to the file
            out.println(itemAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
}
