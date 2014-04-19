/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author b1106
 */
public class Sender {

    private DatagramSocket dataSocket;
    
    public Sender(DatagramSocket ds){
        dataSocket = ds;
    }

    public void sendMessage(String ip, int port, String message) {
        try {
            byte[] bytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), port);
            dataSocket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
