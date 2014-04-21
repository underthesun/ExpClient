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
 * 过程节点端对底层通信进行封装的工具类
 *
 * @author b1106
 */
public class CommunicationTool {

    private int clientPort;
    private MidClient client;
    private DatagramSocket dataSocket;
    private Receiver receiver;
    private Sender sender;

    /**
     * 构造通信工具实例，构造UDP套接字。
     * 套接字端口号由MidClient传递过来，如果为-1则表示使用随机的端口号，在获得端口号之后更改MidClient中的端口号；
     * 否则使用给定的端口号创建套接字，并构造Receiver和Sender的实例用以收发数据包
     *
     * @param client 过程节点实例
     * @param port UDP套接字端口号
     * @see Receiver
     * @see Sender
     */
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

    /**
     * 通信工具类数据解析接口，通过调用MidClient的数据解析方法parseData对数据进行解析
     *
     * @param ip 数据包地址
     * @param port 数据包端口号
     * @param data 数据
     * @see MidClient#parseData(java.lang.String, int, java.lang.String)
     */
    public void parseData(String ip, int port, String data) {
        client.parseData(ip, port, data);
    }

    /**
     * 通信工具数据发送接口，通过调用Sender的信息发送方法sendMessage发送数据
     *
     * @param ip 数据包地址
     * @param port 数据包端口号
     * @param data 数据
     * @see Sender#sendMessage(java.lang.String, int, java.lang.String)
     */
    public void sendMessage(String ip, int port, String data) {
        sender.sendMessage(ip, port, data);
    }
}
