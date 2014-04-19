/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author b1106
 */
public class Receiver implements Runnable {

    private DatagramSocket dataSocket;
    private int bytelen = 10240;
    private byte[] buf = new byte[bytelen];
    private DatagramPacket dataReceived;
    private CommunicationTool communicationTool;

    public Receiver(CommunicationTool ct, DatagramSocket ds) {
        this.communicationTool = ct;
        this.dataSocket = ds;
        dataReceived = new DatagramPacket(buf, bytelen);
    }

    @Override
    public void run() {
        while (true) {
            try {            
                dataSocket.receive(dataReceived);
                String ip = dataReceived.getAddress().getHostAddress();
                int port = dataReceived.getPort();
                String data = new String(buf, 0, dataReceived.getLength());
                communicationTool.parseData(ip, port, data);
            } catch (IOException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
