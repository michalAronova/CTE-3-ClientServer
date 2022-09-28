package DTO.techSpecs;

import DTO.codeObj.CodeObj;

import java.util.Objects;

public class TechSpecs {

    private final int totalRotors;
    private final int rotorsInUse;
    private final int reflectorsCount;
    private final int processedMsg;
    private final CodeObj currentCode;
    private final CodeObj updatedCode;

    //mimi
    public TechSpecs(int totalRotors, int rotorsInUse, int reflectorsCount,
                     int processedMsg, CodeObj currentCode, CodeObj updatedCode) {
        this.totalRotors = totalRotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectorsCount = reflectorsCount;
        this.processedMsg = processedMsg;
        this.currentCode = currentCode;
        this.updatedCode = updatedCode;
    }

    public int getTotalRotors() {
        return totalRotors;
    }

    public int getRotorsInUse() {
        return rotorsInUse;
    }

    public int getReflectorsCount() {
        return reflectorsCount;
    }

    public int getProcessedMsg() {
        return processedMsg;
    }

    public CodeObj getCurrentCode() {
        return currentCode;
    }

    public CodeObj getUpdatedCode() { return updatedCode; }

    @Override
    public String toString() {
        String ret = String.format("Rotors in use/Total rotors: %d/%d%nReflector count: %d%n" +
                        "Total processed messages: %d%n",
                        rotorsInUse, totalRotors, reflectorsCount, processedMsg);
        if(currentCode != null){
            ret = ret.concat(String.format("Initial code: %s%nCurrent code: %s", currentCode, updatedCode));
        }
        else{
            ret = ret.concat(String.format("Initial code: No code yet %nCurrent code: No code yet"));
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechSpecs techSpecs = (TechSpecs) o;
        return totalRotors == techSpecs.totalRotors && rotorsInUse == techSpecs.rotorsInUse && reflectorsCount == techSpecs.reflectorsCount && processedMsg == techSpecs.processedMsg && Objects.equals(currentCode, techSpecs.currentCode) && Objects.equals(updatedCode, techSpecs.updatedCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalRotors, rotorsInUse, reflectorsCount, processedMsg, currentCode, updatedCode);
    }
}
