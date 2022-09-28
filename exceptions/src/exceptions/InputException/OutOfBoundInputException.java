package exceptions.InputException;

public class OutOfBoundInputException extends InputException {
    private final int minimum;
    private final int maximum;

    public OutOfBoundInputException(String message, int minimum, int maximum){
        super(message);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public String toString(){
        return super.toString() + String.format(" out of bound, value must be between %d - %d", minimum, maximum);
    }

    @Override
    public String getMessage(){
        return this.toString();
    }
}
