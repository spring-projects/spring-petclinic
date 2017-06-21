package org.springframework.samples.petclinic.system;

/**
 * @author Martijn
 * @since 21-6-2017.
 */
public class ExampleException extends RuntimeException {
    public ExampleException(String message) {
        super(ExampleException.class.getSimpleName() + ": " + message);
    }

    protected ExampleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(ExampleException.class.getSimpleName() + ": " + message, cause, enableSuppression, writableStackTrace);
    }
}
