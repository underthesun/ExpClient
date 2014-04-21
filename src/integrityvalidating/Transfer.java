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
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Configuration;
import main.MidClient;
import util.ProcessSim;

/**
 * 数据完整性验证依赖的工具类，对收到的数据进行进行回馈，报告验证结果，产生随机错误，并将订单转发至后向过程。
 *
 * @see MidClient
 * @see Message
 * @see Gson
 * @author b1106
 */
public class Transfer {

    private static int cnt = 0;
    private MidClient client;
    private Gson gson;

    /**
     * 构造Transfer实例
     *
     * @param client 过程节点实例
     */
    public Transfer(MidClient client) {
        this.client = client;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * 将数据转发至后向过程。 通过Gson将数据序列化，并封装在Message中进行转发，随机的选择后向过程作为目的过程。 整个过程分为两步：
     * 1.根据数据生成新的订单信息，包含收到的完整数据信息，将信息转发至后向过程； 2.对数据产生随机错误，将错误化的数据转发至对应的后向过程
     *
     * @param order 收到的数据
     * @see MidClient#sendMessageToClient(util.ProcessSim, java.lang.String)
     *
     */
    public void transfer(Order order) {
        String orderId = "Process" + client.getConfiguration().ID + "_" + cnt++;
        order.setOrderId(orderId);

        Message message = new Message();
        message.setType(Message.ORDER);
        message.setId(client.getProcessManager().getProcessSim().getId());
        message.setContent(gson.toJson(order, Order.class));

        Set<ProcessSim> postProcess = client.getProcessManager().getPostProcessSims();
        if (!postProcess.isEmpty()) {//termination process
            List<ProcessSim> processSims = new ArrayList<ProcessSim>(postProcess);
            final ProcessSim ps = processSims.get(new Random().nextInt(processSims.size()));
            //send order information to a random post process
            if (ps.isIsOnline()) {
                client.sendMessageToClient(ps, gson.toJson(message, Message.class));

                final Message messageData = new Message();
                messageData.setType(Message.DATA);
                messageData.setId(client.getConfiguration().ID);
                messageData.setContent(gson.toJson(errGen(order), Order.class));//generate random error
                //send order data after 1000 millisecond
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    client.sendMessageToClient(ps, gson.toJson(messageData, Message.class));

                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Transfer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }).start();
            } else {
            }
        }
    }

    /**
     * 产生随机的完整性错误。 根据Configuration中的错误概率ERR_RATIOC产生三种类型的错误:
     * 1.通过移除物品队列中的物品产生数目错误； 2.通过设置错误的物品标识产生唯一性错误； 3.通过交换两个物品包包含的物品信息产生包含关系错误
     *
     * @param order 收到的数据
     * @return 错误化的数据
     * @see Configuration#ERR_RATIO
     * @see Random
     */
    public Order errGen(Order order) {
        double ratio = client.getConfiguration().ERR_RATIO;
        double x = new Random().nextDouble();
        System.out.println(ratio + " : " + x);
        ArrayList<Item> items = order.getItems();
        ArrayList<Package> packages = order.getPackages();
        if (x < ratio) {
            int errType = new Random().nextInt(3);
            System.out.println("errtype: " + errType);
            switch (errType) {
                case 0:// qty error
                    items.remove(items.size() - 1);
                    break;
                case 1:// unq error
                    items.get(0).setId(-1);
                    break;
                case 2:// ctm error
                    if (packages.size() > 1) {
                        ArrayList<Item> tmp = packages.get(0).getItems();
                        packages.get(0).setItems(packages.get(1).getItems());
                        packages.get(1).setItems(tmp);
                    }
                    break;
            }
        }
        return order;
    }

    /**
     * 错误报告接口。
     * 将检测到的错误信息报告至监控端
     *
     * @param errType 错误类型
     * @see Message#INT_REPORT
     */
    public void errReport(String errType) {
        Message m = new Message();
        m.setType(Message.INT_REPORT);
        m.setId(client.getConfiguration().ID);
        m.setContent(errType);
        client.sendMessageToUI(gson.toJson(m, Message.class));
    }

    /**
     * 收到数据进行反馈的接口。
     * 将收到的数据反馈至监控端
     * 
     * @param message 收到的数据
     * @see Message#INT_ECHO
     */
    public void intEcho(Message message) {
        message.setType(Message.INT_ECHO);
        message.setId(client.getConfiguration().ID);
        client.sendMessageToUI(gson.toJson(message, Message.class));
    }
}
