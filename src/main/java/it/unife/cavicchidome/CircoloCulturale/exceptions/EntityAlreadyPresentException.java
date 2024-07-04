package it.unife.cavicchidome.CircoloCulturale.exceptions;

public class EntityAlreadyPresentException extends Exception{

        public EntityAlreadyPresentException(String reason){
            super(reason);
        }

        public EntityAlreadyPresentException() {
            super();
        }
}
