/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Configuration;

/**
 *
 * @author b1106
 */
public class Test {

    public static void main(String[] args) {
        configToJson();
    }

    public static void configToJson() {
        Configuration configuration = new Configuration();
        configuration.setCLIENT_PORT(6667);
        configuration.setHEARTBEAT_THRESHOLD(3000);
        configuration.setID(1);
        configuration.setNAME("");
        configuration.setPERIOD(1000);
        configuration.setSERVER_ADDR("127.0.0.1");
        configuration.setSERVER_PORT(6666);
        configuration.setERR_RATIO(0.1);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("client_conf.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(configuration, Configuration.class, bw);
            
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
        }


    }
}
