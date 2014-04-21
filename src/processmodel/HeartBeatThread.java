/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processmodel;

import java.util.TimerTask;

/**
 * 心跳线程类。周期性的发服务器端发送心跳信息
 *
 * @author b1106
 * @see ProcessManager
 */
public class HeartBeatThread extends TimerTask {

    private ProcessManager manager;

    /**
     * 构造心跳线程实例
     * @param manager 过程管理实例
     */
    public HeartBeatThread(ProcessManager manager) {
        this.manager = manager;
    }

    /**
     * 通过过程管理实例发送心跳信息
     */
    @Override
    public void run() {
        manager.heartbeat();
    }
}
