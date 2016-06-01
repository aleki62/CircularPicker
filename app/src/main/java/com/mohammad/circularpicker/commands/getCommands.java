package com.mohammad.circularpicker.commands;

/**
 * Created by mohammad on 5/31/2016.
 */

import com.mohammad.circularpicker.newTcpClient;

import java.io.IOException;


public class getCommands extends newTcpClient{
    public getCommands(String ip, int port, OnMessageReceived listener) {
        super(ip, port, listener);
    }

    public void getPoint(){
        run();
        sendMessage("*SETPOINT*?#");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSPCOOLAWAY(){
        run();
        sendMessage("*SPCOOLAWAY*?#");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSPHEATAWAY(){
        run();
        sendMessage("*SPHEATAWAY*?#");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getONOFF(){
        run();
        sendMessage("*ONOFF*?#");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTemp(){
        run();
        sendMessage("*TEMP*?#");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFanSpeed(){
        run();
        sendMessage("*FAN*?#");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAll(){
        run();
        sendMessage("*GETALL*?#");
//        try {
//            stopClient();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
