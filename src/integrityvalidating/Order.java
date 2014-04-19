/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

import java.util.ArrayList;

/**
 *
 * @author b1106
 */
public class Order {

    private String orderId;
    private ArrayList<Item> items;
    private ArrayList<Package> packages;

    public Order(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Package> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }   

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.orderId != null ? this.orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if ((this.orderId == null) ? (other.orderId != null) : !this.orderId.equals(other.orderId)) {
            return false;
        }
        return true;
    }
    
    
}
