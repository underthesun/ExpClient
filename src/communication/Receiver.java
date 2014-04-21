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
 * 底层数据接收类，监听给定的套接字并接收数据，将数据传递至CommunicationTool进行解析
 *
 * @author b1106
 * @see DatagramSocket
 * @see DatagramPacket
 * @see CommunicationTool
 */
public class Receiver implements Runnable {

    private DatagramSocket dataSocket;
    private int bytelen = 10240;
    private byte[] buf = new byte[bytelen];
    private DatagramPacket dataReceived;
    private CommunicationTool communicationTool;

    /**
     * 构造Receiver实例。
     *
     * @param ct 通信工具实例
     * @param ds UDP套接字实例
     * @see CommunicationTool
     * @see DatagramSocket
     */
    public Receiver(CommunicationTool ct, DatagramSocket ds) {
        this.communicationTool = ct;
        this.dataSocket = ds;
        dataReceived = new DatagramPacket(buf, bytelen);
    }

    /**
     * 独立的线程监听给定套接字接收数据，获取数据包的地址，端口号，数据等信息, 并将数据传递至CommunicationTool进行解析
     *
     * @see CommunicationTool#parseData(java.lang.String, int, java.lang.String)
     */
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
