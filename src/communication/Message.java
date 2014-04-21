/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import main.MidClient;

/**
 * 过程节点用到的消息类，字段包括消息类型type,消息发出端标识id，消息内容content. 各项静态字段定义了需要用到各种消息类型
 *
 * @author b1106
 * @see MidClient
 */
public class Message {

    public static String REGISTER = "reg";
    public static String REGISTER_ACK = "regAck";
    public static String HEARTBEAT = "hb";
    public static String HEARTBEAT_ACK = "hbAck";
    //
    public static String PROCESS_PRE_KEY = "pre";
    public static String PROCESS_POST_KEY = "post";
    //
    public static String ORDER = "order";
    public static String DATA = "data";
    public static String INT_ERROR_QTY = "qty";
    public static String INT_ERROR_UNI = "unq";
    public static String INT_ERROR_CTM = "ctm";
    //  
    public static String INT_START = "intStart";
    public static String INT_REPORT = "intReport";
    public static String INT_ECHO = "intEcho";
    public static String PROCESS_ONLINE = "proOn";
    public static String KILL = "kill";
    //
    private String type;
    private int id;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
