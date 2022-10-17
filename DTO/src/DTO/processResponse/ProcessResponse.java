package DTO.processResponse;

import DTO.codeObj.CodeObj;

import java.io.Serializable;

public class ProcessResponse implements Serializable {
    private final String output;
    private final CodeObj newCode;

    public ProcessResponse(String output, CodeObj newCode) {
        this.output = output;
        this.newCode = newCode;
    }

    public String getOutput() {
        return output;
    }

    public CodeObj getNewCode() {
        return newCode;
    }
}
