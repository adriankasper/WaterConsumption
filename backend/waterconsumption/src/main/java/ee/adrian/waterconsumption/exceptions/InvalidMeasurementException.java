package ee.adrian.waterconsumption.exceptions;

public class InvalidMeasurementException extends RuntimeException {

    public InvalidMeasurementException(String message) {
        super(message);
    }
}