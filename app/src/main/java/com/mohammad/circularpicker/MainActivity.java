package com.mohammad.circularpicker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mohammad.circularpicker.commands.ParseResponse;
import com.mohammad.circularpicker.commands.getCommands;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.pushlink.android.PushLink;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import me.angrybyte.numberpicker.listener.OnValueChangeListener;
import me.angrybyte.numberpicker.view.ActualNumberPicker;


public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, OnValueChangeListener {
    public static String Tag;
    public static int TimeConnectRFCenter;
    public static int RegCenterLevel;
//    public static TCPClient mTcpClient;
    static WifiManager wifiManager;

    public static int mode = 1;
    TextView textView4;
    TextView textView;
    TextView textView2;
    TextView textView3;
    public static int SETPOINT;
    public static int SPCOOLAWAY;
    public static int SPHEATAWAY;
    public static int ONOFF;
    public static int AWAY;
    public static int temp;
    public static int Heating, Cooling, fanSpeed;
    public static String commands=null;
    private SharedPreferences sharedpreferences;

    String ip="192.168.56.1";
    int port=8000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(0);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });


        textView3 = (TextView) findViewById(R.id.textView3);
        textView2 = (TextView) findViewById(R.id.textView2);
        INIT();

        final Dialog mdialog = new Dialog(this);
        mdialog.setContentView(R.layout.percentage);
        mdialog.setTitle("تنظیم درجه حرارت");
//        PushLink.start(this, R.mipmap.ic_launcher, "e18ts0p51hih6paa", "jkhgfyf48664");

        mdialog.setContentView(R.layout.percentage);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        SubActionButton.Builder itemBuilder2 = new SubActionButton.Builder(this);
        SubActionButton.Builder itemBuilder3 = new SubActionButton.Builder(this);
        SubActionButton.Builder itemBuilder4 = new SubActionButton.Builder(this);
        SubActionButton.Builder itemBuilder5 = new SubActionButton.Builder(this);
        SubActionButton.Builder itemBuilder6 = new SubActionButton.Builder(this);
        SubActionButton.Builder itemBuilder7 = new SubActionButton.Builder(this);

        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.drawable.about);
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.ac_sleep_moder_cool);
        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.settings);

        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.drawable.btn_close_normal);


        ImageView itemIcon5 = new ImageView(this);
        itemIcon5.setImageResource(R.drawable.gallery2_wind_velocity);


        ImageView itemIcon6 = new ImageView(this);
        itemIcon6.setImageResource(R.drawable.gallery2_mode_pre);


        ImageView itemIcon7 = new ImageView(this);
        itemIcon7.setImageResource(R.drawable.exit);


        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        SubActionButton button2 = itemBuilder2.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder3.setContentView(itemIcon3).build();
        SubActionButton button4 = itemBuilder4.setContentView(itemIcon4).build();
        SubActionButton button5 = itemBuilder5.setContentView(itemIcon5).build();
        SubActionButton button6 = itemBuilder6.setContentView(itemIcon6).build();
        SubActionButton button7 = itemBuilder7.setContentView(itemIcon7).build();

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .addSubActionView(button5)
                .addSubActionView(button6)
                .addSubActionView(button7)
                .attachTo(fab)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
                Intent intent = new Intent(MainActivity.this, about.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "about us", Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
                showCooler();
                Button b = (Button) mdialog.findViewById(R.id.b1);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        temp = actualNumberPicker.getValue();
                        setTemp(SETPOINT);
                        Cooling = SETPOINT;
                        WritesharedPrefrence("Cooling",SETPOINT);
                        Toast.makeText(MainActivity.this, "سرمایش با دمای" + temp + " درجه تنظیم شد", Toast.LENGTH_SHORT).show();
                        mdialog.cancel();
                        System.out.println("the temperature is :" + SETPOINT);


                    }
                });
                Toast.makeText(MainActivity.this, "cold selected", Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
//                showHeater();
//                Button b = (Button) mdialog.findViewById(R.id.b1);
//                b.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setTemp(temp);
//                        Heating=temp;
//                        Toast.makeText(MainActivity.this, "گرمایش با دمای " + temp+ " درجه تنظیم شد", Toast.LENGTH_SHORT).show();
//                        mdialog.cancel();
//                        WritesharedPrefrence("Heating", temp);
//                    }
//                });
//                Toast.makeText(MainActivity.this, "hot selected", Toast.LENGTH_SHORT).show();
                showSettings();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
                Toast.makeText(MainActivity.this, "turn off", Toast.LENGTH_SHORT).show();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
                FanToggle();
                Toast.makeText(MainActivity.this, "fan adjustment", Toast.LENGTH_SHORT).show();
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
                if (mode == 0) {
                    mode = 1;
                    setMode();
                } else {
                    mode = 0;
                    setMode();
                }
                Toast.makeText(MainActivity.this, "Toggle mode", Toast.LENGTH_SHORT).show();
            }
        });

        button1.setLongClickable(true);
        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "در باره ما", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        button2.setLongClickable(true);
        button2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"تنظیم دمای فعلی",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        button3.setLongClickable(true);
        button3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"تنظیم دمای مد خروج",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        button4.setLongClickable(true);
        button4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"خاموش کردن سیستم",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        button5.setLongClickable(true);
        button5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"تنظیم دور موتور فن",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        button6.setLongClickable(true);
        button6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"تغییر مد کاری",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        button7.setLongClickable(true);
        button7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"خروج از منزل",Toast.LENGTH_SHORT).show();
                return false;
            }
        });




//        new TCPClient("192.168.56.1",8000).run();
            MyTask myTask=new MyTask();
            myTask.execute();


    }

    public static void connecttossid(String ssid, String pass) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", pass);

//        Log.d("alhabib", " شد اصال");
//remember id
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int currentApiVersion = 0;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void showSettings() {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Settings");
        d.setContentView(R.layout.settings);
        Button b1 = (Button) d.findViewById(R.id.setSettings);
        final ActualNumberPicker np = (ActualNumberPicker) d.findViewById(R.id.setHeater);
        np.setListener(this);
        final ActualNumberPicker np1 = (ActualNumberPicker) d.findViewById(R.id.setCooler);
        np1.setListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WritesharedPrefrence("Cooling", np.getValue()); //set the value to textview
                WritesharedPrefrence("Heating", np1.getValue());
                Toast.makeText(MainActivity.this, "ذخیره شد", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        d.show();

    }

    public static void setFanSpeed(int number) {
    }

    public void setTemp(int current) {
        textView2.setText(current + "");
    }

    public static void setCurrent(int temp) {

    }

    public void setMode() {


    }

    public void WritesharedPrefrence(String key, int val) {
        sharedpreferences = getSharedPreferences("fancoel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, val + "");
        editor.commit();
    }

    public String ReadSharedPrefrence(String key) {
        sharedpreferences = getSharedPreferences("fancoel", Context.MODE_PRIVATE);
        String a = sharedpreferences.getString(key, "");
        return a;
    }

    public void INIT() {
//        final String[] commands = new String[10];
//        getCommands getCommands1=new getCommands("192.168.1.56", 3334, new newTcpClient.OnMessageReceived() {
//            @Override
//            public void messageReceived(String message) {
//                ParseResponse parseResponse=new ParseResponse();
//                commands[0] =parseResponse.ParseResponse(message);
//                commands=message;

//            }
//        });
//        getCommands1.getAll();

        MyClientTask myClientTask = new MyClientTask("192.168.4.1",3344,"*GETALL*?#");
        myClientTask.execute();
//        System.err.println(commands);
//        if(commands.length()>2){
//            commands=commands.replace(',',' ');
//        commands="*5*1*2*3*4*5*6*7#";


        }
//        else {
//            do nothing
//        }
//    }


    public void showHeater() {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.percentage);
        Button b1 = (Button) d.findViewById(R.id.b1);
        final ActualNumberPicker np = (ActualNumberPicker) d.findViewById(R.id.actual_picker);
        np.setListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        d.show();
    }


    public void showCooler() {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.percentage);
        Button b1 = (Button) d.findViewById(R.id.b1);
        final ActualNumberPicker np = (ActualNumberPicker) d.findViewById(R.id.actual_picker);
        np.setListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        d.show();
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);

    }


    public void FanToggle() {
        if (fanSpeed == 1) {
            fanSpeed = 2;
            WritesharedPrefrence("fanSpeed", 2);
            textView3.setText("2x");
        } else if (fanSpeed == 2) {
            fanSpeed = 3;
            WritesharedPrefrence("fanSpeed", 3);
            textView3.setText("3x");
        } else if (fanSpeed == 3) {
            fanSpeed = 1;
            WritesharedPrefrence("fanSpeed", 1);
            textView3.setText("1x");
        }
    }

    @Override
    public void onValueChanged(int oldValue, int newValue) {
        Log.i("value is ", newValue + "");
    }

    protected class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {
            final String[] content = new String[1];

            newTcpClient nTcpClient=new newTcpClient("192.168.56.1",80, new newTcpClient.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    System.out.println("new message has been sent");
                    content[0] =message;
                }
            });
            nTcpClient.run();
            try {
                nTcpClient.sendMessage("salam");
            }catch (Exception e){
                Log.e(Tag,e.toString());
            }

            return content[0];
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(),"the request has been sent",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.contains("error")) {
//                    updateDisplay("problem on connecting to service provider");
//                        Crouton.makeText(this,"error in :",Style.ALERT);
                }
            } catch (Exception e) {
                System.out.println("error occured in " + e.toString());
            }
        }
    }


    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer ="*SETPOINT*17#";
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            System.out.println("***");
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if(msgToServer != null) {
                    dataOutputStream.writeBytes(msgToServer);
                }
                Byte b=new Byte(response);
                response = dataInputStream.readLine();
                System.out.println("****"+response);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           commands =response;
            commands=commands.replace('*',' ');
            commands=commands.replace('#',' ');
            System.err.println("*** the response is :*** "+response);
            String [] request=commands.split("\\s+");
            System.out.println("request is ***"+request[2]);
            SETPOINT= Integer.parseInt(request[1]);
            SPCOOLAWAY= Integer.parseInt(request[2]);
            SPHEATAWAY= Integer.parseInt(request[3]);
            ONOFF= Integer.parseInt(request[4]);
            AWAY= Integer.parseInt(request[5]);
            mode= Integer.parseInt(request[6]);
            fanSpeed= Integer.parseInt(request[7]);
            temp= Integer.parseInt(request[8]);
            MainActivity.setFanSpeed(fanSpeed);
            MainActivity.setCurrent(temp);

            textView4 = (TextView) findViewById(R.id.textView4);
            View fr=findViewById(R.id.frameLayout);
            if (mode == 1) {
                textView4.setText("Cooling");
                fr.setBackgroundResource(R.drawable.circular_bg);
                textView4.setTextColor(Color.BLUE);
                textView2.setText(Cooling + "");
                Toast.makeText(MainActivity.this, Cooling + "", Toast.LENGTH_SHORT).show();
            } else {
                textView4.setText("Heating");
                textView4.setTextColor(Color.YELLOW);
                fr.setBackgroundResource(R.drawable.circular_bg_cool);
                textView2.setText(Heating + "");
                Toast.makeText(MainActivity.this, Heating + "", Toast.LENGTH_SHORT).show();
            }

            System.out.println("Mode is : **** " + mode);

            textView = (TextView) findViewById(R.id.textView);
            textView.setText(temp + "");
            textView3.setText(fanSpeed + "x");

            super.onPostExecute(result);
        }

    }
}
