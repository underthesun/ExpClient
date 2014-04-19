/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

/**
 *
 * @author shuai
 */
public class Item implements Cloneable {

    private int id;
    private int packageId = -1;
//    private int dstNode = -1;
    private long lastTimeChecked;

    public Item(int id) {
        this.id = id;
    }
//
//    @Override
//    public Object clone() {
//        Item ic = null;
//        try {
//            ic = (Item)super.clone();            
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        return ic;
//    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
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
        final Item other = (Item) obj;
        if (id != other.getId()) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public long getLastTimeChecked() {
        return lastTimeChecked;
    }

    public void setLastTimeChecked(long lastTimeChecked) {
        this.lastTimeChecked = lastTimeChecked;
    }

//    public int getDstNode() {
//        return dstNode;
//    }
//
//    public void setDstNode(int dstNode) {
//        this.dstNode = dstNode;
//    }

//    public static void main(String[] args) {
//        HashSet<Item> items = new HashSet<Item>();
//        Item item1 = new Item(1);
//        Item item2 = new Item(2);
//        item1.setPackageId(1);
//        item2.setPackageId(2);
//        items.add(item1);
//        items.add(item2);
//
//        HashSet<Item> items2 = new HashSet<Item>();
//        for(Item i:items){
//            items2.add((Item)i.clone());
//        }
//            
//            
//        for (Item i : items2) {
//            i.setPackageId(5);
//            System.out.println(i.getPackageId()+"::"+i);
//        }
//
//        for (Item i : items) {
//            System.out.println(i.getPackageId()+"::"+i);
//        } 
//
//        HashSet<Item> items3 = new HashSet<Item>();
//        items3.add(item1);items3.add(item2);items3.add(new Item(4));
//        System.out.println(items3.containsAll(items2));
//        
//        items2.remove(item2);
//        for (Item i : items2) {
//            System.out.println(i.getPackageId()+"::"+i);
//        }
//        for (Item i : items) {
//            System.out.println(i.getPackageId()+"::"+i);
//        } 
//    }
}
