/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import communication.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import main.Configuration;
import main.MidClient;
import util.ProcessSim;

/**
 *
 * @author b1106
 */
public class ProcessManager {

    private MidClient client;
    private HeartBeatThread heartBeatThread;
    private Configuration configuration;
    private Gson gson;
    private ProcessSim processSim;
    private HashSet<ProcessSim> preProcessSims;
    private HashSet<ProcessSim> postProcessSims;

    public ProcessManager(MidClient client, Configuration configuration) {
        this.client = client;
        this.configuration = configuration;
        processSim = new ProcessSim();
        processSim.setId(configuration.ID);
        processSim.setAddr(null);
        processSim.setIsOnline(false);
        processSim.setLastBeat(-1);
        processSim.setPort(configuration.CLIENT_PORT);

        gson = new GsonBuilder().setPrettyPrinting().create();

        register();
        heartBeatThread = new HeartBeatThread(this);
        Timer timer = new Timer();
        timer.schedule(heartBeatThread, 1000, configuration.PERIOD);
    }

    public void heartbeat() {
        if (!processSim.isIsOnline()) {
            register();
        } else {
            if (System.currentTimeMillis() - processSim.getLastBeat()
                    > configuration.HEARTBEAT_THRESHOLD) {
                processTimeout();
            } else {
                Message message = new Message();
                message.setType(Message.HEARTBEAT);
                message.setId(configuration.ID);
                client.sendMessage(configuration.SERVER_ADDR,
                        configuration.SERVER_PORT,
                        gson.toJson(message, Message.class));
            }
        }
    }

    public void register() {
        Message message = new Message();
        message.setType(Message.REGISTER);
        message.setId(configuration.ID);
        client.sendMessage(configuration.SERVER_ADDR,
                configuration.SERVER_PORT,
                gson.toJson(message, Message.class));
    }

    public void registerConfirm(Message m) {
        processSim.setIsOnline(true);
        processSim.setLastBeat(System.currentTimeMillis());

        HashMap<String, HashSet<ProcessSim>> linkedProcess = gson.fromJson(m.getContent(), new TypeToken<HashMap<String, HashSet<ProcessSim>>>() {
        }.getType());
        preProcessSims = linkedProcess.get(Message.PROCESS_PRE_KEY);
        postProcessSims = linkedProcess.get(Message.PROCESS_POST_KEY);
    }

    public void heartbeatConfirm(Message m) {
        processSim.setLastBeat(System.currentTimeMillis());
        HashMap<String, HashSet<ProcessSim>> linkedProcess = gson.fromJson(m.getContent(), new TypeToken<HashMap<String, HashSet<ProcessSim>>>() {
        }.getType());
        preProcessSims = linkedProcess.get(Message.PROCESS_PRE_KEY);
        postProcessSims = linkedProcess.get(Message.PROCESS_POST_KEY);
    }

    public void processTimeout() {
        processSim.setIsOnline(false);

//        System.out.println("client fucking timeout");
    }

    public ProcessSim getProcessSim() {
        return processSim;
    }

    public void setProcessSim(ProcessSim processSim) {
        this.processSim = processSim;
    }

    public HashSet<ProcessSim> getPreProcessSims() {
        return preProcessSims;
    }

    public void setPreProcessSims(HashSet<ProcessSim> preProcessSims) {
        this.preProcessSims = preProcessSims;
    }

    public HashSet<ProcessSim> getPostProcessSims() {
        return postProcessSims;
    }

    public void setPostProcessSims(HashSet<ProcessSim> postProcessSims) {
        this.postProcessSims = postProcessSims;
    }
    
    
}
