package it.unife.cavicchidome.CircoloCulturale.exceptions;

public class ValidationException extends Exception{

    public ValidationException(String reason){
        super(reason);
    }

    public ValidationException() {
        super();
    }
}
