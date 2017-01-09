package com.led.home.exceptions;

public class DisconnectedClientException extends Exception {

    public DisconnectedClientException() {
        super();
    }

    public DisconnectedClientException(String message) {
        super(message);
    }

    public DisconnectedClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectedClientException(Throwable cause) {
        super(cause);
    }
}
