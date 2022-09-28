package DTO.codeObj;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CodeObj implements Serializable {
    private List<Pair<Integer, Character>> ID2PositionList;
    private Map<Integer, Integer> notchRelativeLocation;
    private String reflectorID;
    private List<Pair<Character, Character>> plugs;
    private boolean updatedRotorIds = false;
    private boolean updatedRotorPos = false;
    private boolean updatedReflector = false;
    private boolean updatedPlugs = false;

    public CodeObj() {
        ID2PositionList = new ArrayList<>();
    }
    public CodeObj(List<Pair<Integer,Character>> ID2PositionList, CodeObj currentCode,
                   Map<Integer, Integer> relativeNotchesLocation){
        this.ID2PositionList = ID2PositionList;
        notchRelativeLocation = relativeNotchesLocation;
        reflectorID = currentCode.reflectorID;
        plugs = currentCode.plugs;
        updatedRotorIds = currentCode.updatedRotorIds;
        updatedRotorPos = currentCode.updatedRotorPos;
        updatedReflector = currentCode.updatedReflector;
        updatedPlugs = currentCode.updatedPlugs;
    }
    public CodeObj(List<Pair<Integer, Character>> ID2PositionList,
                   String reflectorID, List<Pair<Character, Character>> plugs){
        this.ID2PositionList = ID2PositionList;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        updatedRotorIds = true;
        updatedRotorPos = true;
        updatedReflector = true;
        updatedPlugs = true;
    }

    public CodeObj(List<Pair<Integer, Character>> ID2PositionList,
                   String reflectorID, List<Pair<Character, Character>> plugs,
                   Map<Integer, Integer> relativeNotchesLocation){
        this.ID2PositionList = ID2PositionList;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        this.notchRelativeLocation = relativeNotchesLocation;
        updatedRotorIds = true;
        updatedRotorPos = true;
        updatedReflector = true;
        updatedPlugs = true;
    }

    public List<Pair<Integer, Character>> getID2PositionList() {
        return ID2PositionList;
    }

    public String getReflectorID() {
        return reflectorID;
    }

    public Map<Integer, Integer> getNotchRelativeLocations(){ return notchRelativeLocation; }

    public void setNotchRelativeLocation(Map<Integer, Integer> notchRelativeLocation){
        this.notchRelativeLocation = notchRelativeLocation;
    }

    public List<Pair<Character, Character>> getPlugs() {
        return plugs;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        for(int i = ID2PositionList.size() - 1; i >= 0; i--){
            sb.append(ID2PositionList.get(i).getKey());
            sb.append(",");
        }
        sb.replace(sb.length() - 1, sb.length(),">");
        sb.append("<");
        for(int i = ID2PositionList.size() - 1; i >= 0; i--){
            sb.append(ID2PositionList.get(i).getValue());
            sb.append(String.format("(%d)", notchRelativeLocation.get(ID2PositionList.get(i).getKey())));
        }
        sb.append("><");
        sb.append(reflectorID+">");
        if(plugs.size() > 0){
            sb.append("<");
            plugs.forEach(p -> sb.append(p.getKey()+"|"+p.getValue()+","));
            sb.replace(sb.length() - 1, sb.length(),">");
        }
        return sb.toString();
    }

    public void setID2PositionList(List<Pair<Integer, Character>> ID2PositionList){
        this.ID2PositionList = ID2PositionList;
    }

    public void setRotorIDs(List<Integer> rotorIDs){
        if(!updatedRotorPos){
            for (Integer id: rotorIDs) {
                ID2PositionList.add(new Pair<>(id, null));
            }
        }
        else{
            List<Pair<Integer, Character>> updated = new ArrayList<>();
            for (int i = 0; i < rotorIDs.size() ; i++) {
                updated.add(new Pair<>(rotorIDs.get(i), ID2PositionList.get(i).getValue()));
            }
            ID2PositionList = updated;
        }
        updatedRotorIds = true;
    }

    public void setRotorPos(List<Character> rotorPositions){
        if(!updatedRotorIds){
            for (Character pos: rotorPositions) {
                ID2PositionList.add(new Pair<>(null, pos));
            }
        }
        else{
            List<Pair<Integer, Character>> updated = new ArrayList<>();
            for (int i = 0; i < rotorPositions.size() ; i++) {
                updated.add(new Pair<>(ID2PositionList.get(i).getKey(), rotorPositions.get(i)));
            }
            ID2PositionList = updated;
        }
        updatedRotorPos = true;
    }

    public void setReflectorID(String reflectorID){
        updatedReflector = true;
        this.reflectorID = reflectorID;
    }

    public void setPlugs(List<Pair<Character, Character>> plugs){
        updatedPlugs = true;
        this.plugs = plugs;
    }

    public boolean isValidCode(){
        return updatedRotorIds && updatedRotorPos && updatedReflector && updatedPlugs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeObj codeObj = (CodeObj) o;
        return updatedRotorIds == codeObj.updatedRotorIds && updatedRotorPos == codeObj.updatedRotorPos && updatedReflector == codeObj.updatedReflector && updatedPlugs == codeObj.updatedPlugs && Objects.equals(ID2PositionList, codeObj.ID2PositionList) && Objects.equals(notchRelativeLocation, codeObj.notchRelativeLocation) && Objects.equals(reflectorID, codeObj.reflectorID) && Objects.equals(plugs, codeObj.plugs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID2PositionList, notchRelativeLocation, reflectorID, plugs, updatedRotorIds, updatedRotorPos, updatedReflector, updatedPlugs);
    }
}
