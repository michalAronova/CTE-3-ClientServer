package exceptions.InputException;

import java.util.ArrayList;
import java.util.List;

public class ObjectInputException extends InputException {
    private final List<Object> validValues;
    private List<Object> invalidValues;
    public ObjectInputException(String message, int value){
        super(message);
        this.validValues = new ArrayList<>();
        validValues.add(value);
    }

    public ObjectInputException(String message, List<Object> validValues, List<Object> invalidValues){
        this(message, validValues);
        this.invalidValues = invalidValues;
    }

    public ObjectInputException(String message, List<Object> validValues){
        super(message);
        this.validValues = validValues;
    }

    @Override
    public String toString(){
        String str = super.toString();
        if(invalidValues != null){
            str = str + String.format(" | Invalid values found: %s", invalidValues);
        }
        return str + String.format("%nThe valid values are: %s", validValues);
    }
    @Override
    public String getMessage(){
        return this.toString();
    }

    public Object getValidValuesList(){ return validValues; }
}
