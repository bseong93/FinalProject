package com.example.com.iotapp3;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;


public class Fragment2 extends Fragment{

    private LinearLayout layoutFanOn, fanOn1, fanOn2;
    private LinearLayout layoutFanOff, fanOff1, fanOff2;
    private LinearLayout layoutLightOn, lightOn1, lightOn2;
    private LinearLayout layoutLightOff, lightOff1, lightOff2;

    private final int UPDATE = -1, FAN_ON = 0, FAN_OFF = 1, LIGHT_ON = 2, LIGHT_OFF = 3;
    private boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        // Fan 관련 변수 초기화
        layoutFanOn = (LinearLayout) view.findViewById(R.id.fanOn);
        fanOn1 = (LinearLayout) view.findViewById(R.id.fanOn1);
        fanOn2 = (LinearLayout) view.findViewById(R.id.fanOn2);

        layoutFanOff = (LinearLayout) view.findViewById(R.id.fanOff);
        fanOff1 = (LinearLayout) view.findViewById(R.id.fanOff1);
        fanOff2 = (LinearLayout) view.findViewById(R.id.fanOff2);

        // Light 관련 변수 초기화
        layoutLightOn = (LinearLayout) view.findViewById(R.id.lightOn);
        lightOn1 = (LinearLayout) view.findViewById(R.id.lightOn1);
        lightOn2 = (LinearLayout) view.findViewById(R.id.lightOn2);

        layoutLightOff = (LinearLayout) view.findViewById(R.id.lightOff);
        lightOff1 = (LinearLayout) view.findViewById(R.id.lightOff1);
        lightOff2 = (LinearLayout) view.findViewById(R.id.lightOff2);

        // Fan관련 이벤트 리스너 등록
        layoutFanOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fanOn();
                new HttpThread(FAN_ON).start();
            }
        });

        layoutFanOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fanOff();
                new HttpThread(FAN_OFF).start();
            }
        });

        // Light 관련 이벤트 리스너 등록
        layoutLightOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lightOn();
                new HttpThread(LIGHT_ON).start();
            }
        });

        layoutLightOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lightOff();
                new HttpThread(LIGHT_OFF).start();
            }
        });

        // update 스레드 동작
        UpdateThread th = new UpdateThread();
        th.start();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        System.out.println("==================onDestory()=====================");
    }

    /* ----------------- Handler & Data Load Thread ------------------ */

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch ( msg.what){
                case FAN_ON:
                    fanOn();
                    break;
                case FAN_OFF:
                    fanOff();
                    break;
                case LIGHT_ON:
                    lightOn();
                    break;
                case LIGHT_OFF:
                    lightOff();
                    break;
            }
        }
    };

    class UpdateThread extends Thread{
        private String fanUrl = "http://api.thingspeak.com/channels/257667/fields/1/last.txt?api_key=I07Y6U9TP4JMYL3P";
        private String lightUrl = "http://api.thingspeak.com/channels/257666/fields/1/last.txt?api_key=09NTO8QMWG48LM2M";

        public void run(){
            while(flag){
                try {
                    int fanData = Integer.parseInt(sendGet(fanUrl));
                    int lightData = Integer.parseInt(sendGet(lightUrl));

                    if(fanData == 0)
                        handler.sendEmptyMessage(FAN_OFF);
                    else if(fanData == 1)
                        handler.sendEmptyMessage(FAN_ON);

                    if(lightData == 0)
                        handler.sendEmptyMessage(LIGHT_OFF);
                    else if(lightData == 1)
                        handler.sendEmptyMessage(LIGHT_ON);

                    sleep(1000);
                }
                catch(Exception e){}
            }
        }
    }


    class HttpThread extends Thread{
        private String url;
        private int command;

        public HttpThread(int command){
            this.command = command;
        }
        public void run(){
            switch(command){
                case FAN_ON:
                    url = "http://api.thingspeak.com/update?api_key=I07Y6U9TP4JMYL3P&field1=1";
                    break;
                case FAN_OFF:
                    url = "http://api.thingspeak.com/update?api_key=I07Y6U9TP4JMYL3P&field1=0";
                    break;
                case LIGHT_ON:
                    url = "http://api.thingspeak.com/update?api_key=09NTO8QMWG48LM2M&field1=1";
                    break;
                case LIGHT_OFF:
                    url = "http://api.thingspeak.com/update?api_key=09NTO8QMWG48LM2M&field1=0";
                    break;
            }
            try {
                sendGet(url);
            }catch(Exception e){}
        }
    }


    /* ----------------- Layout Functions ------------------ */
    public void fanOn(){
        fanOn1.setBackgroundColor(Color.rgb(68,191,92));
        fanOn2.setBackgroundColor(Color.rgb(49,142,48));

        fanOff1.setBackgroundColor(Color.rgb(95,110,120 ));
        fanOff2.setBackgroundColor(Color.rgb(56,64,70));
    }

    public void fanOff(){
        fanOff1.setBackgroundColor(Color.rgb(68,191,92));
        fanOff2.setBackgroundColor(Color.rgb(49,142,48));

        fanOn1.setBackgroundColor(Color.rgb(95,110,120 ));
        fanOn2.setBackgroundColor(Color.rgb(56,64,70));
    }

    public void lightOn(){
        lightOn1.setBackgroundColor(Color.rgb(68,191,92));
        lightOn2.setBackgroundColor(Color.rgb(49,142,48));

        lightOff1.setBackgroundColor(Color.rgb(95,110,120 ));
        lightOff2.setBackgroundColor(Color.rgb(56,64,70));
    }

    public void lightOff(){
        lightOff1.setBackgroundColor(Color.rgb(68,191,92));
        lightOff2.setBackgroundColor(Color.rgb(49,142,48));

        lightOn1.setBackgroundColor(Color.rgb(95,110,120 ));
        lightOn2.setBackgroundColor(Color.rgb(56,64,70));
    }


    /* ----------------- HTTP GET request ------------------ */
    private String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // add request header
        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // return result
        return response.toString();

    }


}
//Toast.makeText(getActivity(), "Test String", Toast.LENGTH_SHORT).show();
