package com.egorkivilev.util.paystub;

public class PayException extends Exception {
    String message;

    public PayException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return Util.COLOUR_RED + message + Util.COLOUR_RESET;
    }
}
