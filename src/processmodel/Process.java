/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processmodel;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author b1106
 */
public class Process {

    private int id;
    private String addr;
    private int port;
    private long lastBeat;
    private boolean isOnline;
    private Set<Process> preProcessSet;
    private Set<Process> postProcessSet;

    public Process(int id) {
        this.id = id;
        this.addr = "";
        this.port = 6666;
        this.lastBeat = -1;
        this.isOnline = false;
        this.preProcessSet = new HashSet<Process>();
        this.postProcessSet = new HashSet<Process>();
    }

    public void addPreProcess(Process p) {
        preProcessSet.add(p);
    }

    public Process removePreProcess(Process p) {
        Process tmp = null;
        for (Process preP : preProcessSet) {
            if (p.id == preP.id) {
                tmp = preP;
            }
        }
        return tmp;
    }

    public void addPostProcess(Process p) {
        postProcessSet.add(p);
    }

    public Process removePostProcess(Process p) {
        Process tmp = null;
        for (Process postP : postProcessSet) {
            if (p.id == postP.id) {
                tmp = postP;
            }
        }
        return tmp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getLastBeat() {
        return lastBeat;
    }

    public void setLastBeat(long lastBeat) {
        this.lastBeat = lastBeat;
    }

    public boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Set<Process> getPreProcessSet() {
        return preProcessSet;
    }

    public void setPreProcessSet(Set<Process> preProcessSet) {
        this.preProcessSet = preProcessSet;
    }

    public Set<Process> getPostProcessSet() {
        return postProcessSet;
    }

    public void setPostProcessSet(Set<Process> postProcessSet) {
        this.postProcessSet = postProcessSet;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    
}
