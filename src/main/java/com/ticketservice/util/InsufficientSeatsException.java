package com.ticketservice.util;


public class InsufficientSeatsException extends  Exception {
    private static final long serialVersionUID = 1L;
    private String message = null;
    public InsufficientSeatsException() { super(); }
    public InsufficientSeatsException(String message) {
        super(message);
        this.message = message;
    }
    public InsufficientSeatsException(Throwable cause)
    {
        super(cause);
    }
    @Override
    public String toString() {
        return message;
    }
}
