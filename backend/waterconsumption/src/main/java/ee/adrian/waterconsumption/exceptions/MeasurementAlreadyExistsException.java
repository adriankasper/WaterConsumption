package ee.adrian.waterconsumption.exceptions;

public class MeasurementAlreadyExistsException extends RuntimeException {

    public MeasurementAlreadyExistsException(String message) {
        super(message);
    }
}