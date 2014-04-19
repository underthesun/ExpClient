/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package processmodel;

import java.util.TimerTask;

/**
 * 
 * @author b1106
 */
public class HeartBeatThread extends TimerTask {
    private ProcessManager manager;
    
    public HeartBeatThread (ProcessManager manager){
        this.manager = manager;
    }
    
    @Override
    public void run() {
        manager.heartbeat();
    }

}
