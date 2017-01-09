package com.led.home.exceptions;

public class MQTTConnectException extends Exception {

    public MQTTConnectException() {
        super();
    }

    public MQTTConnectException(String message) {
        super(message);
    }

    public MQTTConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQTTConnectException(Throwable cause) {
        super(cause);
    }
}
