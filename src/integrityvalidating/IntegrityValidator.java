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
 * 完整性验证类，实现了数量验证接口，唯一性验证接口，包含关系验证接口。 通过数量验证-唯一性验证-包含关系验证的顺序进行完整性验证
 *
 * @see Validator
 * @see QuantityValidator
 * @see UnicityValidator
 * @see ContainmentValidator
 * @see Order
 * @see Transfer
 * @author b1106
 */
public class IntegrityValidator implements QuantityValidator, UnicityValidator, ContainmentValidator {

    private List<Order> orders;
    private MidClient client;
    private Gson gson;
    private Transfer transfer;

    /**
     * 构造完整性验证实例，同时构造Gson的实例用以对数据进行序列化，Transfer实例用以转发订单数据和消息
     *
     * @param client 过程节点实例
     */
    public IntegrityValidator(MidClient client) {
        this.client = client;

        orders = new ArrayList<Order>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        transfer = new Transfer(client);
    }

    /**
     * 进行数量验证，比较订单信息中的物品数量和实际收到的物品数量
     *
     * @param order 收到的订单信息
     * @param orderRecv 收到的数据
     * @return 验证结果
     */
    @Override
    public boolean qtyValidate(Order order, Order orderRecv) {
        boolean flag = true;
        if ((order.getItems().size() != orderRecv.getItems().size())
                || order.getPackages().size() != orderRecv.getPackages().size()) {
            flag = false;
        }
        return flag;
    }

    /**
     * 进行完整性验证，按数量验证-唯一性验证-包含关系验证的顺序进行完整性验证，通过Transfer报告每一步验证的结果。
     * 若验证正确，则通过Transfer转发订单，并移除订单队列中对应的待验证订单
     *
     * @param order 收到的订单信息
     * @param orderRecv 收到的数据
     * @return 验证结果
     * @see Transfer#errReport(java.lang.String)
     * @see Transfer#transfer(integrityvalidating.Order)
     */
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

    /**
     * 进行唯一性验证，订单信息中包含的物品都必须出现在收到的物品中
     *
     * @param order 收到的订单信息
     * @param orderRecv 收到的数据
     * @return 验证结果
     */
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

    /**
     * 进行包含关系验证，收到物品的包含关系应该与订单信息中的包含关系一致
     *
     * @param order 收到的订单信息
     * @param orderRecv 收到的数据
     * @return 验证结果
     */
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

    /**
     * 通过Gson反序列化获取订单信息，将收到的订单添加至待验证订单队列。 通过Transfer反馈收到的订单信息
     *
     * @param m 收到的消息
     * @see Gson#fromJson(com.google.gson.JsonElement, java.lang.Class)
     * @see Transfer#intEcho(communication.Message)
     */
    public void orderRecv(Message m) {
        Order order = gson.fromJson(m.getContent(), Order.class);
        if (!orders.contains(order)) {
            orders.add(order);
        }
        transfer.intEcho(m);
//        System.out.println("client " + client.getProcessManager().getProcessSim().getId() + "  recvorder " + order.getOrderId());
//        System.out.println(gson.toJson(order, Order.class));
    }

    /**
     * 对外提供的完整性验证接口。 通过Gson反序列化获取收到的数据信息，并调用实际的完整性验证接口进行验证。
     *
     * @param m 收到的消息
     */
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
