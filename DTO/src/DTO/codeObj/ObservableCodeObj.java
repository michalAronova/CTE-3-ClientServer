package DTO.codeObj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Pair;

public class ObservableCodeObj {

    private ObservableList<Pair<Integer, Character>> ID2PositionList;
    private ObservableMap<Integer, Integer> notchRelativeLocation;
    private StringProperty reflectorID;
    private ObservableList<Pair<Character, Character>> plugs;

    public ObservableCodeObj(CodeObj codeObj){
        reflectorID = new SimpleStringProperty();
        update(codeObj);
    }

    public void update(CodeObj codeObj){
        ID2PositionList = FXCollections.observableArrayList(codeObj.getID2PositionList());
        notchRelativeLocation = FXCollections.observableMap(notchRelativeLocation);
        reflectorID.setValue(codeObj.getReflectorID());
        plugs = FXCollections.observableArrayList(codeObj.getPlugs());
    }
}
