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
import main.MidClient;
import util.ProcessSim;

/**
 *
 * @author b1106
 */
public class Transfer {

    private static int cnt = 0;
    private MidClient client;
    private Gson gson;

    public Transfer(MidClient client) {
        this.client = client;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

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

    public void errReport(String errType) {
        Message m = new Message();
        m.setType(Message.INT_REPORT);
        m.setId(client.getConfiguration().ID);
        m.setContent(errType);
        client.sendMessageToUI(gson.toJson(m, Message.class));
    }

    public void intEcho(Message message) {
        message.setType(Message.INT_ECHO);
        message.setId(client.getConfiguration().ID);
        client.sendMessageToUI(gson.toJson(message, Message.class));
    }
}
