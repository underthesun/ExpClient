/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import communication.CommunicationTool;
import communication.Message;
import integrityvalidating.IntegrityValidator;
import java.io.File;
import processmodel.ProcessManager;
import util.Configurator;
import util.ProcessSim;

/**
 * 过程节点类，包括了过程建模，数据完整性验证和分布式查询处理等各项功能
 *
 * @author b1106
 *
 * @see ProcessManager
 * @see CommunicationTool
 * @see Configuration
 * @see IntegrityValidator
 * @see Gson
 */
public class MidClient {

    private ProcessManager processManager;
    private Configuration configuration;
    private CommunicationTool communicationTool;
    private IntegrityValidator integrityValidator;
    private Gson gson;

    /**
     * 构造过程节点端实例
     *
     * @param confFile 配置文件
     */
    public MidClient(String confFile) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        configuration = Configurator.parseConfiguration(new File(confFile));
        communicationTool = new CommunicationTool(this, configuration.CLIENT_PORT);

        processManager = new ProcessManager(this, configuration);
        integrityValidator = new IntegrityValidator(this);
    }

    /**
     * 构造过程节点端实例
     */
    public MidClient() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        configuration = Configurator.parseConfiguration(new File("client_conf.json"));
        communicationTool = new CommunicationTool(this, configuration.CLIENT_PORT);

        processManager = new ProcessManager(this, configuration);
        integrityValidator = new IntegrityValidator(this);

        processManager.register();
    }

    /**
     * 数据解析接口。 根据数据的类型，调用不同的组件进行处理。 包括了节点注册反馈，心跳反馈，订单信息，数据信息，退出信息等
     *
     * @param ip 数据地址
     * @param port 数据端口
     * @param data 数据
     * @see Message
     */
    public void parseData(String ip, int port, String data) {
        Message message = gson.fromJson(data, Message.class);
        String mType = message.getType();
        if (mType.equals(Message.REGISTER_ACK)) {
            processManager.registerConfirm(message);
        } else if (mType.equals(Message.HEARTBEAT_ACK)) {
            processManager.heartbeatConfirm(message);
        } else if (mType.equals(Message.ORDER)) {
            integrityValidator.orderRecv(message);
        } else if (mType.equals(Message.DATA)) {
            integrityValidator.validate(message);
        } else if (mType.equals(Message.KILL)) {
            System.out.println("kill");
            System.exit(0);
        }
    }

    /**
     * 数据发送接口。通过CommunicationTool提供的数据发送接口发送数据
     *
     * @param ip 数据地址
     * @param port 数据端口
     * @param data 数据
     * @see CommunicationTool#sendMessage(java.lang.String, int,
     * java.lang.String)
     */
    public void sendMessage(String ip, int port, String data) {
        communicationTool.sendMessage(ip, port, data);
    }

    /**
     * 数据反馈接口。通过CommunicationTool提供的数据发送接口发送数据，而数据的地址和端口信息通过配置文件获取
     *
     * @param data data
     * @see CommunicationTool#sendMessage(java.lang.String, int,
     * java.lang.String)
     * @see Configuration#UI_ADDR
     * @see Configuration#UI_PORT
     */
    public void sendMessageToUI(String data) {
        communicationTool.sendMessage(configuration.UI_ADDR, configuration.UI_PORT, data);
    }

    /**
     * 数据发送接口。通过CommunicationTool提供的数据发送接口发送数据。
     *
     * @param p 过程节点
     * @param data 数据
     * @see ProcessSim
     */
    public void sendMessageToClient(ProcessSim p, String data) {
        communicationTool.sendMessage(p.getAddr(), p.getPort(), data);
    }

    public void setPort(int port) {
        configuration.setCLIENT_PORT(port);
    }

    public ProcessManager getProcessManager() {
        return processManager;
    }

    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
