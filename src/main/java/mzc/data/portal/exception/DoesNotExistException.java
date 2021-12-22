package mzc.data.portal.exception;

public class DoesNotExistException extends IllegalArgumentException {
    public DoesNotExistException(String s) {
        super(s);
    }
}