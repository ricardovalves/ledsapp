package com.led.home.ledcontroller;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.led.home.exceptions.DisconnectedClientException;
import com.led.home.exceptions.PublishException;

public class CustomColorActivity extends BaseActivity {

    private SeekBar hueControl;
    private SeekBar saturationControl;
    private SeekBar valueControl;

    private int hueValue = 0;
    private int saturationValue = 255;
    private int valueValue = 255;

    private LinearLayout finalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_color);

        hueControl = (SeekBar)findViewById(R.id.hue);
        saturationControl = (SeekBar)findViewById(R.id.saturation);
        valueControl = (SeekBar)findViewById(R.id.value);

        finalColor = (LinearLayout)findViewById(R.id.finalcolor);

        setHueListener();
        setSaturationListener();
        setValueListener();

        // we instantiate with a default color (red)
        setColorValue(hueValue, saturationValue, valueValue);
    }

    private void setHueListener() {
        hueControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hueValue = progress;
                setColorValue(hueValue, saturationValue, valueValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setSaturationListener() {
        saturationControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saturationValue = progress;
                setColorValue(hueValue, saturationValue, valueValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setValueListener() {
        valueControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueValue = progress;
                setColorValue(hueValue, saturationValue, valueValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setColorValue(int hue, int saturation, int value) {

        float seekBarHue = hue * 360 / 255;
        float seekBarSaturation = saturation * 100 / 255;
        float seekBarValue = value * 100 / 255;

        float[] hsvColor = new float[]{
            seekBarHue,
            seekBarSaturation/100,
            seekBarValue/100
        };
        finalColor.setBackgroundColor(Color.HSVToColor(hsvColor));
        if(client == null) {
            this.startClient();
        }

        // define hexadecimal colors
        String fastLedHue = Integer.toHexString(hue);
        String fastLedSaturation = Integer.toHexString(saturation);
        String fastLedValue = Integer.toHexString(value);

        if(client != null) {
            try {
                client.publish(fastLedHue + fastLedSaturation + fastLedValue);
            } catch (DisconnectedClientException e) {
                Toast.makeText(getApplicationContext(), "The MQTT client is not connected to the broker",
                        Toast.LENGTH_LONG).show();
            } catch (PublishException e) {
                Toast.makeText(getApplicationContext(), "There was an issue while trying to publish",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "There seems to be an issue with your MQTT connection",
                    Toast.LENGTH_LONG).show();
        }
    }
}
