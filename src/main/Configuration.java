/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 * 过程节点端所需的配置类，用以启动时存储配置文件中获取到的配置信息
 *
 * @author b1106
 */
public class Configuration {

    public int ID;
    public String NAME;
    public int CLIENT_PORT;
    public int PERIOD;
    public int HEARTBEAT_THRESHOLD;
    public double ERR_RATIO;
    public int SERVER_PORT;
    public String SERVER_ADDR;
    public String UI_ADDR;
    public int UI_PORT;

    public double getERR_RATIO() {
        return ERR_RATIO;
    }

    public void setERR_RATIO(double ERR_RATIO) {
        this.ERR_RATIO = ERR_RATIO;
    }

    public int getHEARTBEAT_THRESHOLD() {
        return HEARTBEAT_THRESHOLD;
    }

    public void setHEARTBEAT_THRESHOLD(int HEARTBEAT_THRESHOLD) {
        this.HEARTBEAT_THRESHOLD = HEARTBEAT_THRESHOLD;
    }

    public int getSERVER_PORT() {
        return SERVER_PORT;
    }

    public void setSERVER_PORT(int SERVER_PORT) {
        this.SERVER_PORT = SERVER_PORT;
    }

    public int getID() {
        return ID;
    }

    public int getCLIENT_PORT() {
        return CLIENT_PORT;
    }

    public void setCLIENT_PORT(int CLIENT_PORT) {
        this.CLIENT_PORT = CLIENT_PORT;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getPERIOD() {
        return PERIOD;
    }

    public void setPERIOD(int PERIOD) {
        this.PERIOD = PERIOD;
    }

    public String getSERVER_ADDR() {
        return SERVER_ADDR;
    }

    public void setSERVER_ADDR(String SERVER_ADDR) {
        this.SERVER_ADDR = SERVER_ADDR;
    }
}
