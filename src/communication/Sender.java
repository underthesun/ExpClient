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
 * 底层数据发送类，根据上层传递的数据和地址信息发送数据包
 *
 * @see DatagramSocket
 * @see DatagramPacket
 * @author b1106
 */
public class Sender {

    private DatagramSocket dataSocket;

    /**
     * 构造Sender
     *
     * @param ds UDP套接字实例
     */
    public Sender(DatagramSocket ds) {
        dataSocket = ds;
    }

    /**
     * 数据发送接口
     *
     * @param ip 数据目的地址
     * @param port 数据目的端口号
     * @param message 数据
     */
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
