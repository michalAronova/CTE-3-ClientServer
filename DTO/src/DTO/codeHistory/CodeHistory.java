package DTO.codeHistory;
import DTO.codeObj.CodeObj;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CodeHistory implements Serializable {
    private final CodeObj code;
    private final List<Translation> inputOutput;

    public CodeHistory(CodeObj code){
        inputOutput = new LinkedList<>();
        this.code = code;
    }

    public void addTranslation(String input, String output, long time){
        inputOutput.add(new Translation(input, output, time));
    }

    public void addTranslation(Translation translation){
        inputOutput.add(translation);
    }

    public CodeObj getCode(){
        return code;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Code: %s %n", code));
        if(inputOutput.isEmpty()){
            sb.append(String.format("      No translations processed with this code.%n"));
            return sb.toString();
        }
        for (int i = 0; i < inputOutput.size(); i++) {
            sb.append(String.format("      %d. %s%n", i+1, inputOutput.get(i).toString()));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeHistory that = (CodeHistory) o;
        return Objects.equals(code, that.code) && Objects.equals(inputOutput, that.inputOutput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, inputOutput);
    }
}
