package com.mohammad.circularpicker.commands;

import com.mohammad.circularpicker.newTcpClient;

import java.io.IOException;
import java.util.InputMismatchException;

/**
 * Created by mohammad on 5/31/2016.
 */
public class setCommands extends newTcpClient {

    public setCommands(String ip, int port, OnMessageReceived listener) {
        super(ip, port, listener);
    }

    public void setPoint(int temperature){
        run();
        if (temperature<=360 && temperature>=100 && temperature%5==0){
            sendMessage("*SETPOINT*"+String.valueOf(temperature)+"#");
        }
        else{
            try {
                stopClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new InputMismatchException("The input should be between 10 and 36");

        }

    }

    public void configuration(int cooling_temp, int Heating_temp){
        run();
        if (cooling_temp<=220 && cooling_temp>=140 && cooling_temp%5==0){
            sendMessage("*SPCOOLAWAY*"+String.valueOf(cooling_temp)+"#");
        }
        else{
            try {
                stopClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new InputMismatchException("The input should be between 14 and 22");
        }

        if (Heating_temp<=300 && Heating_temp>=220 && Heating_temp%5==0){
            sendMessage("*SPHEATAWAY*"+String.valueOf(Heating_temp)+"#");
        }
        else{
            try {
                stopClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new InputMismatchException("The input should be between 22 and 30");
        }
    }

    public void setON_OFF(int mode){
        run();
        if (mode==1)
            sendMessage("*ONOFF*1#");
        else if (mode==0)
            sendMessage("*ONOFF*0#");
        else
            throw new InputMismatchException("State Should be 0 or 1 for on or off");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAway(int mode){

        run();
        if (mode==1)
            sendMessage("*MODE*1#");
        else if (mode==0)
            sendMessage("*MODE*0#");
        else
            throw new InputMismatchException("Away Mode Should be 0 or 1 for on or off");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setMode(int mode){
        run();
        if (mode==1)
            sendMessage("*MODE*1#");
        else if (mode==0)
            sendMessage("*MODE*0#");
        else
            throw new InputMismatchException("Mode Should be 0 or 1 for HEATING or COOLING");
        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFanSpeed(int fanSpeed){

        run();
        if (fanSpeed==0)
            sendMessage("*FAN*0#");
        else if (fanSpeed==1)
            sendMessage("*FAN*1#");
        else if (fanSpeed==2)
            sendMessage("*FAN*2#");
        else if (fanSpeed==3)
            sendMessage("*FAN*3#");
        else
            throw new InputMismatchException("Fan Speed should be between 0 and 3");

        try {
            stopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


