package exceptions.XMLException;

public class InvalidXMLException extends RuntimeException {
    private XMLExceptionMsg reason;
    private String message;
    public InvalidXMLException(XMLExceptionMsg reason, String message){
        this.reason = reason;
        this.message = message;
    }

    public String getMessage() {
        return reason.toString() + message;
    }
}
