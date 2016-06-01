package com.mohammad.circularpicker;

import com.mohammad.circularpicker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class getResponse  {

    private Socket socket;

    public String run() {

        try {
            InetAddress serverAddr = InetAddress.getByName("192.168.56.1");
            socket = new Socket(serverAddr, 8000);

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {


            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine ()) != null) {
                System.out.println("the response is : *** "+line);
            }

//            TextView message = (TextView) findViewById(R.id.received);

            //in.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    return null;
    }

}