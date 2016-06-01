package com.mohammad.circularpicker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by mohammad on 5/18/2016.
 */
public class newTcpClient {
    private boolean busy=false;
    private String serverMessage;



    public boolean isConnected() {
        return mRun;
    }
    public static String SERVERIP;// = "192.168.4.1"; //your computer IP address
    public static int SERVERPORT = 3344;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    PrintWriter out;
    BufferedReader in;

    public newTcpClient(String ip, int port, OnMessageReceived listener) {
        mMessageListener = listener;
        SERVERIP= ip;
        SERVERPORT=port;
    }

    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.e("TCP Client", "C: Sent:" + message);
        }
        else {
            Log.e(MainActivity.Tag, "send message error...");
            mRun=false;
        }
    }

    public void stopClient() throws IOException {
        mRun = false;
        out.flush();
        out.close();
        Log.e(MainActivity.Tag, "اتصال به مبدل قطع شد!!!");
    }


    public void run() {
        busy= true;
        try {
            Log.e(MainActivity.Tag+" TCP Client", "C: Connecting...");
            Socket socket = new Socket(SERVERIP, SERVERPORT);

            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                socket.setSoTimeout(1000);
                InputStream inputStream=socket.getInputStream();
                StringBuilder stringBuilder=new StringBuilder();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"ASCII");
                for (int c=inputStreamReader.read();c!=-1;c=inputStreamReader.read()){
                    stringBuilder.append((char) c);
                }
                Log.e(MainActivity.Tag+ " RESPONSE SERVER", "S: Received Message: '" + serverMessage + "'" + " پایان کلاینت ");

            } catch (Exception e) {

//                Log.e(MainActivity.Tag+" TCP", "S: Error", e);
                mRun=false;
                busy=false;
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                mRun=false;
                busy= false;
                out.flush();
                out.close();
                in.close();
                socket.close();
            }

        } catch (Exception e) {

            mRun=false;
            busy= false;
            Log.e(MainActivity.Tag+ " TCP", "C: Error", e);
        }

    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
