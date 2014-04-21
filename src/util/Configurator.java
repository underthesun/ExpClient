/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Configuration;

/**
 * 配置信息获取类。 解析配置文件，获取节点配置信息。
 *
 * @author b1106
 * @see Configuration
 */
public class Configurator {

    /**
     * 配置文件解析接口
     * @param file 文件
     * @return 配置信息
     */
    public static Configuration parseConfiguration(File file) {
        InputStreamReader inputStreamReader = null;
        Configuration configuration = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            inputStreamReader = new InputStreamReader(new FileInputStream(file));
            configuration = gson.fromJson(inputStreamReader, Configuration.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configurator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStreamReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Configurator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return configuration;
    }
//    public static void main(String[] args) {
//        parseConfiguration();
//    }
}
