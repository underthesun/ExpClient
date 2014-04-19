/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrityvalidating;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import communication.Message;
import java.util.ArrayList;
import java.util.List;
import main.MidClient;

/**
 *
 * @author b1106
 */
public class IntegrityValidator implements QuantityValidator, UnicityValidator, ContainmentValidator {

    private List<Order> orders;
    private MidClient client;
    private Gson gson;
    private Transfer transfer;

    public IntegrityValidator(MidClient client) {
        this.client = client;

        orders = new ArrayList<Order>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        transfer = new Transfer(client);
    }

    @Override
    public boolean qtyValidate(Order order, Order orderRecv) {
        boolean flag = true;
        if ((order.getItems().size() != orderRecv.getItems().size())
                || order.getPackages().size() != orderRecv.getPackages().size()) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean validate(Order order, Order orderRecv) {
        boolean flag = qtyValidate(order, orderRecv);
        if (!flag) {
            transfer.errReport(Message.INT_ERROR_QTY);
        } else {
            flag = unqValidate(order, orderRecv);
            if (!flag) {
                transfer.errReport(Message.INT_ERROR_UNI);
            } else {
                flag = ctmValidate(order, orderRecv);
                if (!flag) {
                    transfer.errReport(Message.INT_ERROR_CTM);
                } else {
                    //transfer to post nodes                    
                    transfer.transfer(orderRecv);
                }
            }
        }
        orders.remove(order);
        return flag;
    }

    @Override
    public boolean unqValidate(Order order, Order orderRecv) {
        boolean flag = true;
        ArrayList<Item> items = order.getItems();
        ArrayList<Item> itemsRecv = orderRecv.getItems();
        for (Item item : items) {
            if (!itemsRecv.contains(item)) {
                flag = false;
                break;
            }
        }
        ArrayList<Package> packs = order.getPackages();
        ArrayList<Package> packsRecv = orderRecv.getPackages();
        for (Package pack : packs) {
            if (!packsRecv.contains(pack)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean ctmValidate(Order order, Order orderRecv) {
        boolean flag = true;
        ArrayList<Package> packs = order.getPackages();
        ArrayList<Package> packsRecv = orderRecv.getPackages();
        for (Package pack : packs) {
            ArrayList<Item> items = pack.getItems();
            Package packRecv = null;
            for (Package p : packsRecv) {
                if (p.getId() == pack.getId()) {
                    packRecv = p;
                    break;
                }
            }

            ArrayList<Item> itemsRecv = packRecv.getItems();
            for (Item item : items) {
                if (!itemsRecv.contains(item)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public void orderRecv(Message m) {
        Order order = gson.fromJson(m.getContent(), Order.class);
        if (!orders.contains(order)) {
            orders.add(order);
        }
        transfer.intEcho(m);
//        System.out.println("client " + client.getProcessManager().getProcessSim().getId() + "  recvorder " + order.getOrderId());
//        System.out.println(gson.toJson(order, Order.class));
    }

    public void validate(Message m) {
        System.out.println("validate");
        Order orderRecv = gson.fromJson(m.getContent(), Order.class);
        Order order = null;
        for (Order o : orders) {
            if (o.getOrderId().equals(orderRecv.getOrderId())) {
                order = o;
                break;
            }
        }
        validate(order, orderRecv);
//        System.out.println("client " + client.getProcessManager().getProcessSim().getId() + "  recv data " + order.getOrderId());
    }
}
