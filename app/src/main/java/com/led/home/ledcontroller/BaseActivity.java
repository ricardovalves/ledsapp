package com.led.home.ledcontroller;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.led.home.exceptions.MQTTConnectException;
import com.led.home.mq.MessageQueueClient;


public abstract class BaseActivity  extends AppCompatActivity {

    protected MessageQueueClient client;

    @Override
    protected void onPause() {
        super.onPause();
        disconnectClient();
    }

    @Override
    protected void onStart(){
        super.onStart();
        startClient();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        startClient();
    }

    @Override
    protected void onResume(){
        super.onResume();
        startClient();
    }

    protected void startClient() {
        try {
            client = MessageQueueClient.getInstance();
        } catch (MQTTConnectException e) {
            Toast.makeText(getApplicationContext(), "There was an issue while setting up the connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void disconnectClient() {
        if(client != null) {
            try {
                client.disconnect();
            } catch (MQTTConnectException e) {
                e.printStackTrace();
            }
        }
    }

}
