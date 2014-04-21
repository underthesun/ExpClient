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
 * 过程管理类。 对过程节点端的过程图构建和维护，并提供访问关联过程信息的接口
 *
 * @author b1106
 * @see MidClient
 * @see HeartBeatThread
 * @see Configuration
 * @see ProcessSim
 * @see Gson
 */
public class ProcessManager {

    private MidClient client;
    private HeartBeatThread heartBeatThread;
    private Configuration configuration;
    private Gson gson;
    private ProcessSim processSim;
    private HashSet<ProcessSim> preProcessSims;
    private HashSet<ProcessSim> postProcessSims;

    /**
     * 构造过程管理类实例。 发起心跳线程，每个1000毫秒发送心跳信息
     *
     * @param client 过程节点类实例
     * @param configuration 过程节点配置实例
     */
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

        heartBeatThread = new HeartBeatThread(this);
        Timer timer = new Timer();
        timer.schedule(heartBeatThread, 1000, configuration.PERIOD);
    }

    /**
     * 发送心跳信息接口。 根据节点当前的状态，若还未注册成功，则调用注册接口发送注册信息； 若已注册成功，则发送心跳信息。
     * 若节点已经超时，则调用超时处理接口进行超时处理
     *
     * @see Message#HEARTBEAT
     * @see MidClient#sendMessage(java.lang.String, int, java.lang.String) 
     */
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

    /**
     * 注册接口。 在节点启动时发送注册信息
     * @see Message#REGISTER
     * @see MidClient#sendMessage(java.lang.String, int, java.lang.String) 
     */
    public void register() {
        Message message = new Message();
        message.setType(Message.REGISTER);
        message.setId(configuration.ID);
        client.sendMessage(configuration.SERVER_ADDR,
                configuration.SERVER_PORT,
                gson.toJson(message, Message.class));
    }

    /**
     * 注册回馈接口。处理服务器端的注册回馈信息
     * @param m 注册回馈数据
     * @see ProcessSim
     */
    public void registerConfirm(Message m) {
        processSim.setIsOnline(true);
        long t = System.currentTimeMillis();
        processSim.setRegTime(t);
        processSim.setLastBeat(t);

        HashMap<String, HashSet<ProcessSim>> linkedProcess = gson.fromJson(m.getContent(), new TypeToken<HashMap<String, HashSet<ProcessSim>>>() {
        }.getType());
        preProcessSims = linkedProcess.get(Message.PROCESS_PRE_KEY);
        postProcessSims = linkedProcess.get(Message.PROCESS_POST_KEY);
    }

    /**
     * 心跳回馈接口。处理服务器端的心跳回馈信息
     * @param m 心跳回馈数据
     * @see ProcessSim
     */
    public void heartbeatConfirm(Message m) {
        processSim.setLastBeat(System.currentTimeMillis());
        HashMap<String, HashSet<ProcessSim>> linkedProcess = gson.fromJson(m.getContent(), new TypeToken<HashMap<String, HashSet<ProcessSim>>>() {
        }.getType());
        preProcessSims = linkedProcess.get(Message.PROCESS_PRE_KEY);
        postProcessSims = linkedProcess.get(Message.PROCESS_POST_KEY);
    }

    /**
     * 超时处理接口。
     */
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
