package com.led.home.ledcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.led.home.db.Color;
import com.led.home.db.ColorReaderDbHelper;
import com.led.home.exceptions.DBException;
import com.led.home.exceptions.DisconnectedClientException;
import com.led.home.exceptions.MQTTConnectException;
import com.led.home.exceptions.PublishException;
import com.led.home.mq.MessageQueueClient;

import java.util.ArrayList;
import java.util.List;

public class LedControllerActivity extends BaseActivity {

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

    @Override
    protected void onResume() {

        super.onResume();
        final GridView gridview = (GridView) findViewById(R.id.fullscreen_content_controls);

        ColorReaderDbHelper db = ColorReaderDbHelper.getInstance(this);

        List<Color> colors = new ArrayList<Color>();

        final ColorAdapter colorAdapter = new ColorAdapter(this);
        colorAdapter.init();

        gridview.setAdapter(colorAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Color color = (Color)parent.getItemAtPosition(position);
                setColor(color.getColorRepresentation());
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Color color = (Color)parent.getItemAtPosition(position);
                    if(!color.getIsDefaultColor()) {
                        ColorReaderDbHelper.getInstance(getApplicationContext()).deleteColor(color.getId());
                        colorAdapter.init();
                        colorAdapter.notifyDataSetChanged();
                        gridview.setAdapter(colorAdapter);
                    }
                } catch (DBException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
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

    public void startCustomColorActivity(View v) {
        Intent intent = new Intent(this, CustomColorActivity.class);
        startActivity(intent);
    }
}
