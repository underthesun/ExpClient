/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import main.MidClient;

/**
 *
 * @author b1106
 */
public class ClientTest {

    static MidClient client;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(args[0]);
        client = new MidClient(args[0]);
//        client = new MidClient();
//        initIntegrity();
    }

   
}
