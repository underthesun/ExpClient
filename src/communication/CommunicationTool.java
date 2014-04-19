/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.MidClient;

/**
 *
 * @author b1106
 */
public class CommunicationTool {

    private int clientPort;
    private MidClient client;
    private DatagramSocket dataSocket;
    private Receiver receiver;
    private Sender sender;

    public CommunicationTool(MidClient client, int port) {
        this.client = client;
        this.clientPort = port;
        try {
            if (clientPort != -1) {
                dataSocket = new DatagramSocket(clientPort);
            } else {
                dataSocket = new DatagramSocket();
                client.setPort(dataSocket.getLocalPort());
            }
        } catch (SocketException ex) {
            Logger.getLogger(CommunicationTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.receiver = new Receiver(this, dataSocket);
        new Thread(receiver).start();
        this.sender = new Sender(dataSocket);
    }

    public void parseData(String ip, int port, String data) {
        client.parseData(ip, port, data);
    }

    public void sendMessage(String ip, int port, String data) {
        sender.sendMessage(ip, port, data);
    }
}
