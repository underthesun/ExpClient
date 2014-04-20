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
 *
 * @author b1106
 */
public class MidClient {

    private ProcessManager processManager;
    private Configuration configuration;
    private CommunicationTool communicationTool;
    private IntegrityValidator integrityValidator;
    private Gson gson;

    public MidClient(String confFile) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        configuration = Configurator.parseConfiguration(new File(confFile));
        communicationTool = new CommunicationTool(this, configuration.CLIENT_PORT);

        processManager = new ProcessManager(this, configuration);
        integrityValidator = new IntegrityValidator(this);
    }

    public MidClient() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        configuration = Configurator.parseConfiguration(new File("client_conf.json"));
        communicationTool = new CommunicationTool(this, configuration.CLIENT_PORT);

        processManager = new ProcessManager(this, configuration);
        integrityValidator = new IntegrityValidator(this);
        
        processManager.register();
    }

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

    public void sendMessage(String ip, int port, String data) {
        communicationTool.sendMessage(ip, port, data);
    }

    public void sendMessageToUI(String data) {
        communicationTool.sendMessage(configuration.UI_ADDR, configuration.UI_PORT, data);
    }

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
