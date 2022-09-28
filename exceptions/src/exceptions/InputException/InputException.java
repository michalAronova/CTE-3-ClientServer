package exceptions.InputException;

public class InputException extends RuntimeException {
    private final String message;

    public InputException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString(){
        return message;
    }
}
