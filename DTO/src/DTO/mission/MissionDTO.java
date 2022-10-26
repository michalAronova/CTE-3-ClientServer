package DTO.mission;

import DTO.codeObj.CodeObj;
import DTO.missionResult.MissionResult;
import javafx.beans.property.BooleanProperty;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;

public class MissionDTO implements Serializable {
    private final String machineEncoded;
    private final List<Character> startPositions;
    private final double missionSize;
    private final String toDecrypt;

    public MissionDTO(String machineEncoded, List<Character> startPositions,
                      double missionSize, String toDecrypt) {
        this.machineEncoded = machineEncoded;
        this.startPositions = startPositions;
        this.missionSize = missionSize;
        this.toDecrypt = toDecrypt;
    }

    public String getMachineEncoded() {
        return machineEncoded;
    }

    public List<Character> getStartPositions() {
        return startPositions;
    }

    public double getMissionSize() {
        return missionSize;
    }

    public String getToDecrypt() {
        return toDecrypt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Encoding: ").append(machineEncoded);
        sb.append(System.lineSeparator());
        sb.append("Positions: ").append(startPositions);
        sb.append(System.lineSeparator());
        sb.append("Size: ").append(missionSize);
        sb.append(System.lineSeparator());
        sb.append("Encryption: ").append(toDecrypt);
        return sb.toString();
    }
}
