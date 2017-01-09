package com.led.home.mq;


import com.led.home.exceptions.DisconnectedClientException;
import com.led.home.exceptions.MQTTConnectException;
import com.led.home.exceptions.PublishException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class based on the Paho Android Client
 * https://eclipse.org/paho/clients/android/
 * http://www.hivemq.com/blog/mqtt-client-library-encyclopedia-eclipse-paho-java
 */

public class MessageQueueClient {

    private static final String serverEndPoint = "tcp://192.168.1.80:1883";

    private static final List<String> topicList =
            new ArrayList<>(Arrays.asList(
                    "devices/24859-cef/ledstrip/color/set",
                    "devices/248d4aef-cef/ledstrip/color/set"
            ));

    private static MessageQueueClient instance = null;

    private static MqttClient client;

    private MessageQueueClient() throws MQTTConnectException {
        try {
            client = new MqttClient( serverEndPoint,
                    MqttClient.generateClientId(), new MemoryPersistence());
            client.connect();
        } catch (MqttException | IllegalArgumentException e) {
            throw new MQTTConnectException("Could not connect to MQTT server at: " +
                    serverEndPoint);
        }
    }

    public static MessageQueueClient getInstance() throws MQTTConnectException {
        if(instance == null) {
            instance = new MessageQueueClient();
        }
        return instance;
    }

    public void disconnect() throws MQTTConnectException {
        try {
            if(client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            throw new MQTTConnectException("Could not disconnect from MQTT server");
        }
        client = null;
        instance = null;
    }

    public void publish(String payload) throws DisconnectedClientException, PublishException {
        try {
            // run through topic list
            for(String topic:topicList) {
                client.publish(topic, payload.getBytes(), 2, false);
            }
        }catch (MqttException e) {
            throw new PublishException("Could not publish content");
        }
    }
}
