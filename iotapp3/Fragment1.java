package com.example.com.iotapp3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static java.lang.Thread.sleep;


public class Fragment1 extends Fragment{
    private boolean flag = true;
    private String url1 = "http://api.thingspeak.com/channels/247820/fields/1/last.txt?api_key=HC39Y2NQKXUV0HWW";
    private String url2 = "http://api.thingspeak.com/channels/247820/fields/2/last.txt?api_key=HC39Y2NQKXUV0HWW";
    private String url3 = "http://api.thingspeak.com/channels/247820/fields/3/last.txt?api_key=HC39Y2NQKXUV0HWW";
    private String url4 = "http://api.thingspeak.com/channels/247820/fields/4/last.txt?api_key=HC39Y2NQKXUV0HWW";

    private String temperature[] = {"", ""};
    private String humidity[] = {"", ""};
    private String dust[] = {"", ""};
    private String gas[] = {"", ""};

    private TextView tempText1, tempText2;
    private TextView humidText1, humidText2;
    private TextView dustText1, dustText2;
    private TextView gasText1, gasText2;

    /* ----------------- onCreateView & onDestroy ------------------ */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1, container, false);

        // Get TextView
        tempText1 = (TextView) view.findViewById(R.id.tempText1);
        tempText2 = (TextView) view.findViewById(R.id.tempText2);
        humidText1 = (TextView) view.findViewById(R.id.humidText1);
        humidText2 = (TextView) view.findViewById(R.id.humidText2);
        dustText1 = (TextView) view.findViewById(R.id.dustText1);
        dustText2 = (TextView) view.findViewById(R.id.dustText2);
        gasText1 = (TextView) view.findViewById(R.id.gasText1);
        gasText2 = (TextView) view.findViewById(R.id.gasText2);

        MyThread th = new MyThread();
        th.setDaemon(true);
        th.start();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        System.out.println("================== onDestry() =======================");
    }

    /* ----------------- Handler & Data Load Thread ------------------ */

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                tempText1.setText(temperature[0]);
                tempText2.setText(temperature[1]);

                humidText1.setText(humidity[0]);
                humidText2.setText(humidity[1]);

                dustText1.setText(dust[0]);
                dustText2.setText(dust[1]);

                gasText1.setText(gas[0]);
                gasText2.setText(gas[1]);
            }
        }
    };



    class MyThread extends Thread{
        public void run(){
            while(flag){
                try {
                    setTemp();
                    setHumid();
                    setDust();
                    setGas();
                    sleep(1000);
                }
                catch(Exception e){}
                handler.sendEmptyMessage(0);
            }
        }
    }



    /* ----------------- Set Data & TextView Update ------------------ */
    private void setTemp() throws Exception{
        float data = Float.parseFloat(sendGet(url1));
        if(data < 18){
            temperature[0] = Float.toString(data) + "℃";
            temperature[1] = "Temperature: Low";
        }else if(data < 25){
            temperature[0] = Float.toString(data) + "℃";
            temperature[1] = "Temperature: Good";
        }else{
            temperature[0] = Float.toString(data) + "℃";
            temperature[1] = "Temperature: High";
        }

    }
    private void setHumid() throws Exception{
        float data = Float.parseFloat(sendGet(url2));
        if(data < 50){
            humidity[0] = Float.toString(data) + "%";
            humidity[1] = "Humidity: Low";
        }else if(data < 65){
            humidity[0] = Float.toString(data) + "%";
            humidity[1] = "Humidity: Good";
        }else{
            humidity[0] = Float.toString(data) + "%";
            humidity[1] = "Humidity: High";
        }

    }
    private void setDust() throws Exception{
        float data = Float.parseFloat(sendGet(url4));
        if(data < 30){
            dust[0] = Float.toString(data) + "um";
            dust[1] = "Dust: Good";
        }else if(data < 80){
            dust[0] = Float.toString(data) + "um";
            dust[1] = "Dust: Normal";
        }else{
            dust[0] = Float.toString(data) + "um";
            dust[1] = "Dust: Bad";
        }

    }
    private void setGas() throws Exception{
        float data = Float.parseFloat(sendGet(url3));
        if(data < 0.1){
            gas[0] = Float.toString(data) + "ppm";
            gas[1] = "Gas: Good";
        }else{
            gas[0] = Float.toString(data) + "ppm";
            gas[1] = "Gas: Danger";
        }

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