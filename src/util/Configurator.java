/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Configuration;

/**
 *
 * @author b1106
 */
public class Configurator {

    public static Configuration parseConfiguration(File file) {
        BufferedReader br = null;
        Configuration configuration = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            br = new BufferedReader(new FileReader(file));
            configuration = gson.fromJson(br, Configuration.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configurator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
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
