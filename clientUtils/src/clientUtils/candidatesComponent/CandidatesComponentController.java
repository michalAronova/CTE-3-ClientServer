package clientUtils.candidatesComponent;

import DTO.missionResult.AlliesCandidates;
import DTO.missionResult.Candidate;
import clientUtils.MainAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;

public class CandidatesComponentController {
    @FXML public VBox rootVBox;
    @FXML private TableView<Candidate> candidatesTableView;

    @FXML private TableColumn<Candidate, String> allyColumn;
    @FXML private TableColumn<Candidate, String> candidateColumn;
    @FXML private TableColumn<Candidate, String> codeColumn;

    private final ObservableList<Candidate> dataList = FXCollections.observableArrayList();

    private MainAppController mainApplicationController;

    @FXML
    public void initialize(){
        allyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAllyName()));
        candidateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCandidate()));
        codeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCode().toString()));

        candidatesTableView.setItems(dataList);
    }

    public void setMainApplicationController(MainAppController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void addToTable(AlliesCandidates alliesCandidates){
        List<Candidate> candidates = Candidate.getListFromAlliesCandidates(alliesCandidates);
        dataList.addAll(candidates);
    }

    public void hideAllyColumn(){
        allyColumn.setVisible(false);
    }
}
