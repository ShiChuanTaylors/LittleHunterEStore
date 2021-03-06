package cart;

import java.util.*;
import database.ShirtDetails;

public class ShoppingCart {

    private HashMap items = null;
    private String moneySymbol = "MYR";
    private float currencyExRate = 1;
    //private GstClient gc; 
    public ShoppingCart() {
        items = new HashMap();
        //this.gc = new GstClient();
        //this.transactionDate = new DateClient().getRMIdate();
    }

    public synchronized void add(String bookId, ShirtDetails book) {
        if (items.containsKey(bookId)) {
            ShoppingCartItem scitem = (ShoppingCartItem) items.get(bookId);
            scitem.incrementQuantity();
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem(book);
            items.put(bookId, newItem);
        }
    }

    public synchronized void remove(String bookId) {
        if (items.containsKey(bookId)) {
            ShoppingCartItem scitem = (ShoppingCartItem) items.get(bookId);
            scitem.decrementQuantity();
            if (scitem.getQuantity() <= 0) {
                items.remove(bookId);
            }
        }
    }

    public synchronized List getItems() {
        List results = new ArrayList();
        Iterator i = items.values().iterator();
        while (i.hasNext()) {
            results.add(i.next());
        }
        return (results);
    }

    protected void finalize() throws Throwable {
        items.clear();
    }

    public synchronized int getNumberOfItems() {
        int numberOfItems = 0;
        for (Iterator i = getItems().iterator(); i.hasNext();) {
            ShoppingCartItem item = (ShoppingCartItem) i.next();
            numberOfItems += item.getQuantity();
        }
        return numberOfItems;
    }

    public synchronized double getTotal() {
        double amount = 0.0;
        for (Iterator i = getItems().iterator(); i.hasNext();) {
            ShoppingCartItem item = (ShoppingCartItem) i.next();
            ShirtDetails ShirtDetails = (ShirtDetails) item.getItem();
            amount += (item.getQuantity() * ShirtDetails.getPrice());
        }
        return roundOff(amount);
    }

    static private double roundOff(double x) {
        return Math.round(x * 100) / 100.0;
    }

    public synchronized void clear() {
        items.clear();
    }
    
    public void setCurrencyExRate(float value) {
        this.currencyExRate = value;
    }
    
    public float getCurrencyExRate() {
        return this.currencyExRate;
    }
    
    public void setMoneySymbol(String countryName){
        this.moneySymbol = countryName;
    }
    
    public String getMoneySymbol(){
        return this.moneySymbol;
    }
    
    /*public GstClient getRMIGstClient() {
        return this.gc;
    }*/

}

