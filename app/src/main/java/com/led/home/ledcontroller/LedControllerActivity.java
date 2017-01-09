package com.led.home.ledcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.led.home.exceptions.DisconnectedClientException;
import com.led.home.exceptions.MQTTConnectException;
import com.led.home.exceptions.PublishException;
import com.led.home.mq.MessageQueueClient;

public class LedControllerActivity extends BaseActivity {

    // color definition
    private static final String red = "00bdf5";
    private static final String green = "5ac7bf";
    private static final String blue = "81c7bf";
    private static final String pink = "d7c7bf";
    private static final String purple = "c166f2";
    private static final String yellow = "2cddd6";
    private static final String orange = "19e8f4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_controller);

        // force initialization
        try {
            MessageQueueClient.getInstance();
        } catch (MQTTConnectException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setColor(View v) {
        switch(v.getId()) {
            case R.id.red_button:
                setColor(red);
                break;
            case R.id.blue_button:
                setColor(blue);
                break;
            case R.id.yellow_button:
                setColor(yellow);
                break;
            case R.id.green_button:
                setColor(green);
                break;
            case R.id.orange_button:
                setColor(orange);
                break;
            case R.id.purple_button:
                setColor(purple);
                break;
            case R.id.pink_button:
                setColor(pink);
                break;
            default:
                startCustomColorActivity();
                break;
        }
    }

    public void setColor(String color) {

        if(client == null) {
            this.startClient();
        }
        if (client != null) {
            try {
                // color is already in hexadecimal format
                client.publish(color);
            } catch (PublishException | DisconnectedClientException e) {
                Toast.makeText(getApplicationContext(), "There was an issue while setting up the color",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startCustomColorActivity() {
        Intent intent = new Intent(this, CustomColorActivity.class);
        startActivity(intent);
    }
}
