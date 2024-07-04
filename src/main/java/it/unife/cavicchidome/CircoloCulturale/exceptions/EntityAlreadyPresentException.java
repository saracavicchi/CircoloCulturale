package it.unife.cavicchidome.CircoloCulturale.exceptions;

public class EntityAlreadyPresentException extends Exception{

        Object entity;

        public <T> T getEntity() {
            try {
                return (T) this.entity;
            } catch (ClassCastException cce) {
                System.err.println(cce.getMessage());
            }
            return null;
        }

        public EntityAlreadyPresentException(Object entity){
            super();
            this.entity = entity;
        }

        public EntityAlreadyPresentException() {
            super();
        }
}
